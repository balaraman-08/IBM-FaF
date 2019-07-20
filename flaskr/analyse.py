from flask import Blueprint, request, session, Response
from pymongo import MongoClient, ReturnDocument
import facebook
import tweepy
from ibm_watson import PersonalityInsightsV3
import os

analyse = Blueprint('analyse', __name__, url_prefix='/analyse')

# MongoDB --------------------------------------------------------------------------------------------------
client = MongoClient(os.getenv('DATABASE_URL'))
db = client.ibm_faf
userHandlesCollection = db.userHandles
analysedUserData = db.analysedUserData
# ----------------------------------------------------------------------------------------------------------

# Tweepy ---------------------------------------------------------------------------------------------------
auth = tweepy.OAuthHandler(
    os.getenv('TWITTER_CONSUMER_KEY'), os.getenv('TWITTER_CONSUMER_SECRET'))
auth.set_access_token(os.getenv('TWITTER_ACCESS_TOKEN'),
                      os.getenv('TWITTER_ACCESS_TOKEN_SECRET'))
api = tweepy.API(auth)
# ----------------------------------------------------------------------------------------------------------

# Personality Insights ----------------------------------------------------------------------------------------
personality_insights = PersonalityInsightsV3(
    version='2017-10-13',
    iam_apikey=os.getenv('PI_APIKEY'),
    url=os.getenv('PI_URL')
)
# ----------------------------------------------------------------------------------------------------------

# Analyse user's personality
@analyse.route('/me', methods=['GET'])
def analyse_me():
    userHandles = session['userHandles']

    # Create a file to save user posts
    f = open(
        'temp/{}.txt'.format(userHandles['facebook']), 'w+', encoding='utf-8')

    # Create graph object for Facebook API
    graph = facebook.GraphAPI(access_token=session['facebookAccessToken'])

    # Get user's facebook feed since 2017
    feed = graph.get_all_connections(
        id=userHandles['facebook'], connection_name='feed', since='2017-01-01T00:00:00+0000')
    for post in feed:
        f.write((post['message'] + '\n') if 'message' in post else '')

    # Get user's tweets, if twitter is connected
    if 'twitter' in userHandles:
        timeline = api.user_timeline(userHandles['twitter'], count=150)
        for tweet in timeline:
            f.write(tweet.text + '\n')

    # Move the file cursor to starting
    f.seek(0)

    # Send collected data to the Personality Insights API
    profile = personality_insights.profile(
        content=f.read(),
        accept='application/json',
        consumption_preferences=True
    ).get_result()  # and get the results

    # Close the user posts file and delete it
    f.close()
    os.remove('temp/{}.txt'.format(userHandles['facebook']))

    # Normalize the personality insights by removing unwanted data
    profile['consumption_preferences'] = [category for category in profile['consumption_preferences'] if category['consumption_preference_category_id']
                                          in ['consumption_preferences_movie', 'consumption_preferences_music', 'consumption_preferences_reading']]

    for category in profile['consumption_preferences']:
        category['consumption_preferences'] = [
            preference for preference in category['consumption_preferences'] if preference['score'] != 0]

    for personality in profile['personality']:
        del personality['children']

    analysedUserData.update({'facebook': userHandles['facebook']}, {
                            '$set': {'profile': profile}}, upsert=True)

    return profile


@analyse.route('/friends', methods=['GET'])
def analyse_friends():
    return 'Analyse Friends'
