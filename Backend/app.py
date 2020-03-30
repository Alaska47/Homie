"""
Flask Documentation:     http://flask.pocoo.org/docs/
Jinja2 Documentation:    http://jinja.pocoo.org/2/documentation/
Werkzeug Documentation:  http://werkzeug.pocoo.org/documentation/

This file creates your application.
"""

import os
from flask import Flask, render_template, request, redirect, url_for, jsonify, request
from pymongo import MongoClient
from bson.json_util import loads, dumps
import dns # required for connecting with SRV
from datetime import datetime
import random

app = Flask(__name__)
client = MongoClient("mongodb+srv://root:aneeshisgay@cluster0-xubhe.mongodb.net/test?retryWrites=true&w=majority")  # host uri
db = client.HooHacks2020 # Select the database
homie_collection = db.Homie  # Select the collection name
updates_collection = db.Updates
donations_collection = db.Donations

@app.route('/api/getHomeless2', methods=['GET', 'POST']) #gets All the Homless
def get_Homeless2():
    video=random.choice(["2GRw9lG5H78","-mcAFzLwkos","j9z0FNGKng4","3NXJAYUwlC8","aJgCU-evt8c","0jJ6FTAKtTE","T_c5ff0EEcA","FdBbR2UNAEo"])
    homie_collection.update({"userName":request.args.get("username")},{"$set":{"goal":request.args.get("goal"), "moneyRaised": 0, "score": 100, "firstName":request.args.get("firstname"), "lastName":request.args.get("lastname"), "age":request.args.get("age"), "phoneNumber": request.args.get("phone"), "description": request.args.get("story"), "Gender": request.args.get("gender"), "video": video}})
    return video

#@app.route('/api/updateUser', method = ['GET', 'POST'])
#def updateUser():
#    #video = random.choice(["2GRw9lG5H78","-mcAFzLwkos","j9z0FNGKng4","3NXJAYUwlC8","aJgCU-evt8c","0jJ6FTAKtTE","T_c5ff0EEcA","FdBbR2UNAEo"])
#    return "WTF"
#    # homie_collection.update({"userName":request.args.get("username")},
#    #     {"$set":{"firstName":request.args.get("firstname"), "lastName":request.args.get("lastname"),
#    #     "age":request.args.get("age"),"phoneNumber": request.args.get("phone"), "description": request.args.get("story"),
#    #     "gender": request.args.get("gender"), "video": video,
#    #     "picture": request.args.get("picture")}})
#    return "updated"

@app.route('/api/like', methods=['GET', 'POST']) #upvote
def like():
    user = homie_collection.find({"userName": request.args.get("username")},{"_id":0, "numLikes":1, "score":1})
    # { $toInt: "$qty" },
    score = 0
    likes = 0
    for u in user:
        likes = u.get("numLikes")
        score = u.get("score")
        #likes = u.getInt("numLikes")
        #print(type(likes))
        # likes = (int)(u.get("numLikes"))
        likes = likes + 1
        # score = (int)(u.get("score"))
        score = score + 15
        print(likes)
        print(score)
    homie_collection.update({"userName":request.args.get("username")},{"$set":{"score":score, "numLikes": likes}})
    return "liked"

@app.route('/api/upvote', methods=['GET', 'POST']) #upvote
def upvote():
    user = homie_collection.find({"userName": request.args.get("username")},{"_id":0, "score":1})
    score = 0
    for u in user:
        score = (int)(u.get("score"))
        score = score + 10
    homie_collection.update({"userName":request.args.get("username")},{"$set":{"score":score}})
    return "upvoted"
    
@app.route('/api/downvote', methods=['GET', 'POST']) #upvote
def downvote():
    user = homie_collection.find({"userName": request.args.get("username")},{"_id":0, "score":1})
    score = 0
    for u in user:
        score = (int)(u.get("score"))
        score = score - 10
    homie_collection.update({"userName":request.args.get("username")},{"$set":{"score":score}})
    return "downvoted"

@app.route('/api/donate', methods=['GET', 'POST']) #Donate
def donate():
    now = datetime.now()
    donations_collection.insert({"description": request.args.get("description"), "time": now, "amount": (int)(request.args.get("amount")), "donatorId": request.args.get("donator"), "homelessId": request.args.get("homeless")})
    return "Donated"

@app.route('/api/createUpdate', methods=['GET', 'POST']) #create Update
def createUpdate():
    now = datetime.now()
    req = request.form
    print(req)
    updates_collection.insert({"title": request.args.get("title"), "time": now, "userName": request.args.get("username"), "updateDescription": request.args.get("description")})

    user = homie_collection.find({"userName": request.args.get("username")},{"_id":0, "numLikes":1, "score":1})
    # { $toInt: "$qty" },
    score = 0
    for u in user:
        score = u.get("score")
        score = score + 5
    homie_collection.update({"userName":request.args.get("username")},{"$set":{"score":score}})
    return "Created"

@app.route('/api/login', methods=['GET']) #login
def login():
    exists = homie_collection.find({'$and':[{"userName": request.args.get("username")}, {"password": request.args.get("password")}]},{"_id":0})
    task_list = []
    for task in exists:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
    return jsonify(task_list)

@app.route('/api/registerDonator', methods=['GET', 'POST']) #register Donator
def registerDonator():
    exists = homie_collection.find({'$and':[{"userName": request.args.get("username")}, {"password": request.args.get("password")}]},{"_id":0})
    task_list = []
    for task in exists:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
        break
    print(task_list)
    if len(task_list) == 0:
        homie_collection.insert({"userName": request.args.get("username"), "password": request.args.get("password"), "gender": request.args.get("gender"),
            "firstName": request.args.get("firstName"), "lastName": request.args.get("lastName"), "phoneNumber": request.args.get("phoneNumber"), "userType": "Donator", "donations": []})
        return "Registered Donator"
    return "Fails"

@app.route('/api/registerHomeless', methods=['GET', 'POST']) #register homeless
def registerHomeless():
    exists = homie_collection.find({'$and':[{"userName": request.args.get("username")}, {"password": request.args.get("password")}]},{"_id":0})
    task_list = []
    for task in exists:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
        break
    print(task_list)
    if len(task_list) == 0:
        homie_collection.insert({"userName": request.args.get("username"), "password": request.args.get("password"), "gender": request.args.get("gender"),
            "firstName": request.args.get("firstName"), "lastName": request.args.get("lastName"), "phoneNumber": request.args.get("phoneNumber"), "userType": "Homeless", "Age": (int)(request.args.get("age")), "donations": [],
            "description": request.args.get("description"), "picture": request.args.get("picture"), "video": request.args.get("video"), "goal": request.args.get("goal"), "score": 100, "moneyRaised": 0, "numLikes":0})
        return "Registered Homeless"
    return "Fails"

@app.route('/api/register', methods=['GET', 'POST']) #register homeless
def register():
    exists = homie_collection.find({'$and':[{"userName": request.args.get("username")}, {"password": request.args.get("password")}]},{"_id":0})
    task_list = []
    for task in exists:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
        break
    print(task_list)
    if len(task_list) == 0:
        homie_collection.insert({"userName": request.args.get("username"), "password": request.args.get("password"), "userType": request.args.get("userType") })
        return "Registered"
    return "Fails"

@app.route('/api/getUpdateForUser', methods=['GET']) #gets the Cards for specified Homeless based on username
def get_Updates_for_User():
    all_tasks = updates_collection.find({"userName": request.args.get("username")},{"_id":0, "userName":0}).sort([("time", -1)])
    task_list = []
    for task in all_tasks:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
    return jsonify(task_list)

@app.route('/api/getDonations', methods=['GET']) #gets the Donations 
def get_Donations_for_User():
    all_tasks = donations_collection.find({'$or':[{"donatorId": request.args.get("username")}, {"homelessId": request.args.get("username")}]},{"_id":0}).sort([("time", -1)])
    #all_tasks = updates_collection.find({"homelessId": request.args.get("username")},{"_id":0})
    task_list = []
    for task in all_tasks:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
    return jsonify(task_list)


@app.route('/api/getHomelessCard', methods=['GET']) #gets the Cards for specified Homeless based on username
def get_HomelessCard():
    all_tasks = homie_collection.find({"userName": request.args.get("username")},{"_id":0})
    task_list = []
    for task in all_tasks:
        task_list.append({"name": task["firstName"]+task["lastName"], "description": task["description"], "moneyRaised": task["moneyRaised"],
                        "goal": task["goal"], "numLikes": task["numLikes"], "numKarma": task["score"], "picture": task["picture"]})
    return jsonify(task_list)

@app.route('/api/getUser', methods=['GET']) #gets a user based on username
def get_Homeless_Man():
    all_tasks = homie_collection.find({"userName": request.args.get("username")},{"_id":0})
    task_list = []
    for task in all_tasks:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
        print(json_str)
        print(record2)
    return jsonify(task_list)

@app.route('/api/getHomeless', methods=['GET']) #gets All the Homless
def get_Homeless():
    all_tasks = homie_collection.find({"userType": "Homeless"},{"_id":0}).sort([("score", -1)])
    task_list = []
    for task in all_tasks:
        json_str = dumps(task)
        record2 = loads(json_str)
        task_list.append(record2)
        print(json_str)
        print(record2)
    thing = jsonify(task_list)
    return thing


#@app.route('/api/getDonators', methods=['GET']) #get all donators on platform
#def get_Donators():
#    all_tasks = homie_collection.find({"userType": "Donator"},{"_id":0})
#    task_list = []
#    for task in all_tasks:
#        json_str = dumps(task)
#        record2 = loads(json_str)
#        task_list.append(record2)
#        print(json_str)
#        print(record2)
#    return jsonify(task_list)

@app.route('/')
def home():
    """Render website's home page."""
    return render_template('home.html')


@app.route('/about/')
def about():
    """Render the website's about page."""
    return render_template('about.html')


###
# The functions below should be applicable to all Flask apps.
###

@app.route('/<file_name>.txt')
def send_text_file(file_name):
    """Send your static text file."""
    file_dot_text = file_name + '.txt'
    return app.send_static_file(file_dot_text)


@app.after_request
def add_header(response):
    """
    Add headers to both force latest IE rendering engine or Chrome Frame,
    and also to cache the rendered page for 10 minutes.
    """
    response.headers['X-UA-Compatible'] = 'IE=Edge,chrome=1'
    response.headers['Cache-Control'] = 'public, max-age=600'
    return response


@app.errorhandler(404)
def page_not_found(error):
    """Custom 404 page."""
    return render_template('404.html'), 404


if __name__ == '__main__':
    app.run(debug=True)
