from flask import Blueprint, request, session, Response, abort
from pymongo import MongoClient, ReturnDocument
import os

auth = Blueprint('auth', __name__, url_prefix='/auth')

# MongoDB --------------------------------------------------------------------------------------------------
client = MongoClient(os.getenv('DATABASE_URL'))
db = client.ibm_faf
userHandlesCollection = db.userHandles
# ----------------------------------------------------------------------------------------------------------

# Login Route
@auth.route('/login', methods=['POST'])
def login():
    """"Logins a user by creating a session based on the facebook userID provided
    """
    if(not request.json):
        abort(400)
    if('userID' not in request.json or 'facebookAccessToken' not in request.json):
        abort(400)

    userID = request.json['userID']
    facebookAccessToken = request.json['facebookAccessToken']
    # Upsert the UserID provided and get the corresponding document
    currentUserHandles = userHandlesCollection.find_one_and_update({'facebook': userID},
                                                                   {'$set': {
                                                                       'facebook': userID}},
                                                                       projection={'_id': False},
                                                                   return_document=ReturnDocument.AFTER,
                                                                   upsert=True)
    # Persist in Session and return to client
    session['userHandles'] = currentUserHandles
    session['facebookAccessToken'] = facebookAccessToken
    return currentUserHandles

# Logout Route
@auth.route('/logout', methods=['POST'])
def logout():
    """"Logs out a user by deleting their session object
    """
    # Remove userHandles from session
    session.pop('userHandles', None)
    session.pop('facebookAccessToken', None)
    return Response(status=200)


@auth.route('/whoami')
def whoami():
    """Returns their session data to identify whether logged in or not
    """
    return session['userHandles'] if session else {}

# Connect other social media
@auth.route('/connect', methods=['POST'])
def connect():
    """Provided a json of other social media IDs, update it to the Mongo collection
    """
    # TODO : Check request.json values to authorize only updation of valid social media profiles

    if(not session):
        abort(401)
    if(not request.json):
        abort(400)

    oldUserHandles = session['userHandles']
    newUserHandles = userHandlesCollection.find_one_and_update({'facebook': oldUserHandles['facebook']},
                                                               {'$set': request.json},
                                                               projection={'_id': False},
                                                               return_document=ReturnDocument.AFTER)
    # Update Session and return to client
    session['userHandles'] = newUserHandles
    return newUserHandles
