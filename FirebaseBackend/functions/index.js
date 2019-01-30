const functions = require('firebase-functions');
const admin = require("firebase-admin");
const cors = require("cors")
const express = require("express")
const Fuse = require("fuse.js")
const bodyParser = require("body-parser");
var request = require('request');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions


var API_KEY = "AAAAh-OdgCM:APA91bH-RNBKfVErwazv5DjhNBv5qFVFaHGxkiXyk4h16tF8Aiegw_5HC18OXgVUK3dVLryU1cCPp1pf-ZEso9cHmOeGSlgHZRR46cbK1JY3mLZGQxqreP28wPzEqQyY8UVY-ZvzzAwG"; // Your Firebase Cloud Messaging Server API key
var serviceAccount = require("./fooshandroid-4f97debb2be3.json");
admin.initializeApp(
    {
        credential: admin.credential.cert(serviceAccount),
        databaseURL: "https://fooshandroid.firebaseio.com/"
    }
);
const database = admin.database();


const app = express();
const api = functions.https.onRequest(app);

const onListingsCreate = functions.database.ref('/listings/{pushId}')
    .onCreate((snapshot, context) => {
      var listing = snapshot.val();
      database.ref("listings/").orderByChild("orderNum").limitToLast(1).once('value').then(
        snap => {   
          var lastListing;    
          snap.forEach(el => {     
            lastListing = el.val();  
            //console.log(lastListing);
          })
          console.log("Last:" + lastListing.orderNum)
          var num = lastListing.orderNum + 1;
          console.log(num);
          var updates = { dateCreated: new Date().toUTCString(), orderNum: num}
          
          return snapshot.ref.update(updates);   
        }
      );
});

const createBlankUserDocument = functions.auth.user().onCreate( userCreate => {
  admin.auth().getUser(userCreate.uid).then(
    user => {
      console.log('User document creation begin for UID:' + user.uid
    + 'email:' + user.email
    + 'name:' + user.displayName
    + 'photo' + user.photoURL);
   
    var userDocument = {
      displayName: user.displayName,
      profileImgPath: user.photoURL || '',
      bio : '',
      updateFlag: false,
      email: user.email,
      contact : ''
    };
    const userDbRef = database.ref('users/'+ user.uid).set(userDocument).then(
      result => {     
        return true;
      }
    ); 
    }
  );
    
    return null;    
});

// Web API functions

var jsonParser = bodyParser.json()

app.get("", (req, res) => {
  res.send("GET funkcija")
})

app.post("/mainfeed", jsonParser, (req, res) => {  
  var ref = database.ref("listings");

    var orderBy = req.body.data.orderBy;
    var category = req.body.data.category;
    var priceLowBound = req.body.data.priceLowBound;
    var priceUpBound = req.body.data.priceUpBound;
    var location = req.body.data.location;
    var textSearch = req.body.data.textSearch;
    var skip = req.body.data.skip;
    var limit = req.body.data.limit;
    var hiring = req.body.data.hiring; 

    ref.orderByChild("orderNum").once("value").then(
        (snapshot) =>{           
            var listings = [];              
            snapshot.forEach(el => {
                listings.push(el.val())               
            });
            listings = listings.reverse();
            if(hiring != undefined){
                listings = listings.filter((el) => {
                    if(el.hiring == hiring){
                        return el;
                    }
                });      
            }                  
            listings = listings.filter((el) => {
                if(el.active == true){                 
                    return el;
                }
            })
            if(orderBy == "priceAsc"){
                listings = listings.sort((a,b) => {return a.price-b.price});
            }
            if(orderBy == "priceDec"){
                listings = listings.sort((a,b) => {return b.price-a.price});
            }
            if(category){
                listings = listings.filter((el) => {
                    if(el.category == category){                 
                        return el;
                    }
                })
            }
            if(priceLowBound || priceUpBound){
                priceLowBound = priceLowBound || 0;
                priceUpBound = priceUpBound || 9999999;

                listings = listings.filter((el) => {                  
                    if(el.price >= priceLowBound && el.price <= priceUpBound){
                        return el;
                    }
                })
            }
            if(location){
                listings = listings.filter((el) => {
                    if(el.location == location){
                        return el;
                    }
                })
            }

            if(textSearch){
                var options = {                   
                    threshold: 0.6,
                    location: 0,
                    distance: 100,
                    maxPatternLength: 32,
                    minMatchCharLength: 1,
                    keys: [
                      "description",
                      "category",
                      "title"
                  ]
                  };
                  var fuse = new Fuse(listings, options); 
                  var listings = fuse.search(textSearch);
            }

            if(skip && limit){
                listings = listings.slice(skip, skip + limit);
            }

            listings = listings.filter((el) => {
                el.images = [el.images[0]];
                return el;
            });

            res.send({data : listings});

        }
    ) 
})

app.post("/mylistings", jsonParser, (req, res) => {  
  var ref = database.ref("listings");
  var userRef = database.ref("users");

  var ownerId = req.body.data.ownerId;  
  var startAt = req.body.data.startAt;
  var limit = req.body.data.limit;
  var isOwner = req.body.data.isOwner;    

if(isOwner){
    ref.orderByChild("ownerId").equalTo(ownerId).once("value").then(
        (snapshot) =>{           
            var listings = []; 
                            
            snapshot.forEach(el => {
                listings.push(el.val())               
            });
                
            listings = listings.filter(el => el).sort( (a,b) => b.orderNum-a.orderNum).slice(startAt, startAt + limit);
            listings = listings.filter((el) => {
                el.images = [el.images[0]];
                return el;
            });   
            res.send({data : listings});
    
            
        }  
    
    )  
} else {
    ref.orderByChild("orderNum").once("value").then(
        (snapshot) =>{           
            var listings = [];              
            snapshot.forEach(el => {
                listings.push(el.val())               
            });
            console.log(ownerId);
            var userRef = database.ref("users");

            userRef.child(ownerId + "/applications").once('value').then(
                (snapshot) =>{           
                    var applications = [];
                   
                    snapshot.forEach(el => {
                        applications.push(el.val())               
                    });
                    console.log(applications);          
                    listings = listings.filter(
                        (el) => {
                            if(applications.includes(el.id)){
                                return el;
                            }
                        }
                    )
                    listings = listings.slice(startAt, startAt + limit);
                    listings = listings.filter((el) => {
                        el.images = [el.images[0]];
                        return el;
                    });           
                    res.send({data : listings});    
                }
              );

        }
    )
}

    
})

const onNotificationSend = functions.database.ref('/notificationRequests/{pushId}')
    .onCreate((requestSnapshot, context) => {
        var request = requestSnapshot.val();
        sendNotificationToUser(
          request.username, 
          request.message,
          function() {
            requestSnapshot.ref.remove();
          }
        );

});


function sendNotificationToUser(username, message, onSuccess) {
    request({
    url: 'https://fcm.googleapis.com/fcm/send',
    method: 'POST',
    headers: {
      'Content-Type' :' application/json',
      'Authorization': 'key='+API_KEY
    },
    body: JSON.stringify({
      notification: {
        title: message
      },
      to : '/topics/user_'+username
    })
  }, function(error, response, body) {
    if (error) { console.error(error); }
    else if (response.statusCode >= 400) { 
      console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
    }
    else {
      onSuccess();
    }
  });
}

module.exports = {
  api,
  onListingsCreate,
  onNotificationSend,
  createBlankUserDocument
}