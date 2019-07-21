# Friend Affinity Finder
An application that lets a user find the affinity they have with their friends based on their comparative needs and values, which are derived from their respective digital presence. The application also classifies and ranks the user’s friends based on their Big 5 personality characteristics and gives fun insights on their friends' interests.

The project uses a Python Flask server, a MongoDB Cluster and an Android application user interface. Built for the **IBM Hack Challenge 2019** collectively by ***Team 128 Gigs***.

## The Team
128 Gigs is 4 member team consisting of:

 - [Balaraman M](https://github.com/balaraman-08) -- Team Lead
 - [Ganesh Ramkumar R](https://github.com/Science001) -- Back end Developer
 - Gowtham C -- Back end Developer
 - Rahul Raj K -- Front end Developer

## Running the server
Once you have cloned the project repository, install the following Python modules:

    python -m pip install flask
    python -m pip install pymongo
    python -m pip install facebook-sdk
    python -m pip install tweepy
    python -m pip install ibm-watson
    
Once the modules are installed, start the server by issuing the following command:

    python -m flask run

The server will be up and running and can be accessed at port 5000 of localhost, by default.

## Connecting application to server
