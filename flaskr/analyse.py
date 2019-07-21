from flask import Blueprint, request, session, Response, abort
from pymongo import MongoClient, ReturnDocument
import facebook
import tweepy
from ibm_watson import PersonalityInsightsV3
import os
from operator import itemgetter

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
    """ Analyses my personality from social media posts using Watson Personality Insights
        and stores that into the database before returning
    """

    if(not session):
        abort(401)

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

    # Store in the MongoDB collection
    analysedUserData.update({'facebook': userHandles['facebook']}, {
                            '$set': {'profile': profile}}, upsert=True)

    return profile

# ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Analyse user's friends' personality
@analyse.route('/friends', methods=['GET'])
def analyse_friends():
    """ Compare my friends' personalities, retrieved from their own analysedUserData in MongoDB,
        with my own personality traits, in order to generate different rank lists based on that.
    """

    if(not session):
        abort(401)

    userHandles = session['userHandles']

    # ------------------------------------------------------------------------------------------------
    # Lists of top friends with these qualities percentile
    openness = []
    conscientiousness = []
    extraversion = []
    agreeableness = []
    neuroticism = []
    friendAffinity = []
    consumptionPreferences = {}
    
    # Get my personality profile
    myProfile = analysedUserData.find_one({'facebook': userHandles['facebook']}, {'_id': 0})['profile']
    myNeeds = myProfile['needs']
    myValues = myProfile['values']

    def classify(user_id, personality):
        """ Helper function to split the Big 5 percentile values of each friend
            into a separte list of their own
        """
        openness.append({'userID': user_id, 'percentile': personality[0]['percentile']})
        conscientiousness.append({'userID': user_id, 'percentile': personality[1]['percentile']})
        extraversion.append({'userID': user_id, 'percentile': personality[2]['percentile']})
        agreeableness.append({'userID': user_id, 'percentile': personality[3]['percentile']})
        neuroticism.append({'userID': user_id, 'percentile': personality[4]['percentile']})

    def map_nature(user_id, needs, values):
        """ Helper function to find the friend affinity driven by needs and values

            It finds the average absolute difference in needs and value percentiles of me and my friend,
            i.e, the percentile by which we differ in character,
            and complements it to get the percentile by which we match, i.e., our affinity.
            This value is put in a list along with friend's ID.
        """
        # Get 
        deltaSum = 0
        for i in range(12):
            deltaSum += abs(needs[i]['percentile'] - myNeeds[i]['percentile'])
        for i in range(5):
            deltaSum += abs(values[i]['percentile'] - myValues[i]['percentile'])
        friendAffinity.append({'userID': user_id, 'affinity': 1-(deltaSum/17)})
    # ------------------------------------------------------------------------------------------------

    # Create graph object for Facebook API
    graph = facebook.GraphAPI(access_token=session['facebookAccessToken'])
    
    friends_list_without_name = []
    friends_list_with_name = {}

    # Get all my friends who are using the application
    friends_list_generator = graph.get_all_connections(
        id=userHandles['facebook'], connection_name='friends')

    # Generate two lists of my friends    
    for friend in friends_list_generator:
        friends_list_without_name.append(friend['id'])
        friends_list_with_name[friend['id']] = friend['name']

    # MongoDB cursor for all my friends analysed data
    friends_data_cursor = analysedUserData.find(
        {'facebook': {'$in': friends_list_without_name}}, {'_id': 0})

    for data in friends_data_cursor:
        """For each personality data of each of my friend,
            use the helper functions to generate the unique lists
        """
        classify(data['facebook'], data['profile']['personality'])
        map_nature(data['facebook'], data['profile']['needs'], data['profile']['values'])
        consumptionPreferences[data['facebook']] = data['profile']['consumption_preferences']

    """ Finally generate the rank list for each of the trait
        and return along with other helper data
    """
    return {
        'opennessRank': sorted(openness, key=itemgetter('percentile'), reverse=True),
        'conscientiousnessRank': sorted(conscientiousness, key=itemgetter('percentile'), reverse=True),
        'extraversionRank': sorted(extraversion, key=itemgetter('percentile'), reverse=True),
        'agreeablenessRank': sorted(agreeableness, key=itemgetter('percentile'), reverse=True),
        'neuroticismRank': sorted(neuroticism, key=itemgetter('percentile'), reverse=True),
        'affinityRank': sorted(friendAffinity, key=itemgetter('affinity'), reversed=True),
        'consumption_preferences': consumptionPreferences,
        'friendsName': friends_list_with_name
    }