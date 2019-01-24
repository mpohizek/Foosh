const functions = require('firebase-functions');
const admin = require("firebase-admin");
const cors = require("cors")
const express = require("express")
const Fuse = require("fuse.js")
const bodyParser = require("body-parser");


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

admin.initializeApp();
const database = admin.database();


const app = express();
const api = functions.https.onRequest(app);

const onListingsCreate = functions.database.ref('/listings-test/{pushId}')
    .onCreate((snapshot, context) => {
      var listing = snapshot.val();
      database.ref("listings-test/").orderByChild("orderNum").limitToLast(1).once('value').then(
        snap => {   
          var lastListing;    
          snap.forEach(el => {     
            lastListing = el.val();  
            //console.log(lastListing);
          })
          console.log("Last:" + lastListing.orderNum)
          var num = lastListing.orderNum + 1;
          console.log(num);
          var updates = { dateCreated: new Date().toISOString(), orderNum: num}
          
          return snapshot.ref.update(updates);   
        }
      );
});

exports.createBlankUserDocument = functions.auth.user().onCreate( userCreate => {
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
  var ref = database.ref("listings-test");

    var orderBy = req.body.data.orderBy;
    var category = req.body.data.category;
    var priceLowBound = req.body.data.priceLowBound;
    var priceUpBound = req.body.data.priceUpBound;
    var location = req.body.data.location;
    var textSearch = req.body.data.textSearch;
    var skip = req.body.data.skip;
    var limit = req.body.data.limit;

    ref.orderByChild("orderNum").once("value").then(
        (snapshot) =>{           
            var listings = [];              
            snapshot.forEach(el => {
                listings.push(el.val())               
            });
            listings = listings.reverse();
            
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
                listings.slice(skip, skip + limit);
            }

            res.send({data : listings});

        }
    ) 
})

app.post("/mylistings", jsonParser, (req, res) => {  
  var ref = database.ref("listings-test");
  
  var ownerId = req.body.data.ownerId;    
  var hiring = req.body.data.hiring;
  var startAt = req.body.data.startAt;
  var limit = req.body.data.limit; 

  ref.orderByChild("ownerId").equalTo(ownerId).once("value").then(
      (snapshot) =>{           
          var listings = [];
          var finalRes = [];
          var hiringFalse = [];
          var hiringTrue = [];                
          snapshot.forEach(el => {
              listings.push(el.val())               
          });
          console.log(listings);
          if(hiring == true){
              hiringTrue = listings.map((listing)=>{                
                  if(listing.hiring) return listing
              })
              finalRes = hiringTrue;
          } else{
              hiringFalse = listings.map((listing)=>{
                  if(!listing.hiring) return listing
              })
              finalRes = hiringFalse;
          }
          finalRes = finalRes.filter(el => el).sort( (a,b) => b.orderNum-a.orderNum).slice(startAt, startAt + limit);        
          res.send({data : listings});
      }
  )      
})

module.exports = {
  api,
  onListingsCreate
}


