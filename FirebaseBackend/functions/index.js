const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
const admin = require('firebase-admin');
admin.initializeApp();
const database = admin.database();



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
