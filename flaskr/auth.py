from flask import Blueprint, request, session, Response
from pymongo import MongoClient, ReturnDocument
import os

auth = Blueprint('auth', __name__, url_prefix='/auth')

# Create a client for MongoDB
client = MongoClient(os.getenv('DATABASE_URL'))
db = client.ibm_faf

# Create object for userHandles Collection
userHandlesCollection = db.userHandles

# Login Route
@auth.route('/login', methods=['POST'])
def login():
    userID = request.json['userID']
    facebookAccessToken = request.json['facebookAccessToken']
    # Upsert the UserID provided and get the new document
    currentUserHandles = userHandlesCollection.find_one_and_update({'facebook': userID},
                                                                   {'$set': {
                                                                       'facebook': userID}},
                                                                       projection={'_id': False},
                                                                   return_document=ReturnDocument.AFTER,
                                                                   upsert=True)
    # Persist in Session, remove OID and return to client
    session['userHandles'] = currentUserHandles
    session['facebookAccessToken'] = facebookAccessToken
    return currentUserHandles

# Logout Route
@auth.route('/logout', methods=['POST'])
def logout():
    # Remove userHandles from session
    session.pop('userHandles', None)
    session.pop('facebookAccessToken', None)
    return Response(status=200)


@auth.route('/whoami')
def whoami():
    return session['userHandles']

# Connect other social media
@auth.route('/connect', methods=['POST'])
def connect():
    # TODO : Check request.json values to authorize only updation of valid social media profiles
    # Update the new media connections received
    oldUserHandles = session['userHandles']
    newUserHandles = userHandlesCollection.find_one_and_update({'facebook': oldUserHandles['facebook']},
                                                               {'$set': request.json},
                                                               projection={'_id': False},
                                                               return_document=ReturnDocument.AFTER)
    # Update Session, remove OID and return to client
    session['userHandles'] = newUserHandles
    return newUserHandles
