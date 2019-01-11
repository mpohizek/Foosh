const functions = require('firebase-functions');
const admin = require("firebase-admin");
const cors = require("cors")
const express = require("express")

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
      contact : ['']
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

app.get("", (req, res) => {
  res.send("GET funkcija")
})
app.post("/mainfeed", (req, res) => {
  res.send("POST funkcija mainfeed")
})

module.exports = {
  api
}