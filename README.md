# Friend Affinity Finder
An application that lets a user find the affinity they have with their friends based on their comparative needs and values, which are derived from their respective digital presence. The application also classifies and ranks the user’s friends based on their Big 5 personality characteristics and gives fun insights on their friends' interests.

The project uses a Python Flask server, a MongoDB Cluster and an Android application user interface. Built for the **IBM Hack Challenge 2019** collectively by ***Team 128 Gigs***.

## The Team
128 Gigs is 4 member team consisting of:

 - [Balaraman M](https://github.com/balaraman-08) -- Team Lead
 - [Ganesh Ramkumar R](https://github.com/Science001) -- Back end Developer
 - [Gowtham C](https://github.com/littleboy1103) -- Back end Developer
 - [Rahul Raj K](https://github.com/rahulrajk) -- Front end Developer

## Running the server
Once you have cloned the project repository, install the following Python modules:

    python -m pip install flask
    python -m pip install pymongo
    python -m pip install facebook-sdk
    python -m pip install tweepy
    python -m pip install ibm-watson
    
Once the modules are installed, start the server by issuing the following command:

    python -m flask run -h 0.0.0.0

The server will be up and running and can be accessed at port 5000 of localhost, by default.

To change the port:

    python -m flask run -h 0.0.0.0 -p PORT_NUMBER

## Connecting application to server
To run the application, install [Android Studio](https://developer.android.com/studio)

If installed, open Android/FriendAffinityFinder in Android Studio

To connect your flask server, follow the steps below:


1. Navigate to `Android/FriendAffinityFinder/app/src/main/res/values/strings.xml`

2. Find IP address of the system running the server

3. Change `<string name="URL">http://192.168.43.245:5000</string>` to `<string name="URL"><YOUR_IP_ADDRESS>:5000</string>`. If you're running on custom port, change `5000` to your `PORT_NUMBER`
 
4. **Connect Test Device to same network as the server**

5. Run the application from Android Studio

## Test Users
Since Facebook API is in Development mode, regular users cannot log in. So use the following test users while testing

| Email | Password |
| --- | --- |
| vcqapfably_1563655783@tfbnw.net | sheismytestuser |
| uihomikxdx_1563655781@tfbnw.net | heismytestuser |
| igsayguwjq_1563351256@tfbnw.net | sheismytestuser |
| open_kfbmyjv_user@tfbnw.net | heismytestuser |
