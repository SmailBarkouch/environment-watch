# environment-watch
A way to crowd-source populations of endanger animals in a mobile platform.

### Features
- login and register accounts
- maps to see the location of other users' spottings
- a photo gallery to view all the submitted photos
- a camera button to submit your own photo
- an alert system notifing users about spotting of an animal in their area
- ways to view only the last 24 hours worth of spottings in maps and gallery
- a like and dislike system in gallery. Users can dislike fake photos that passed the check, and if enough dislikes accumulate the photo will disappear from the gallery for all users
- a way submit a request for a new animal to be added to the system

### Implementation
Environement Watch was written with Kotlin using Android Studio. It utilizes 4 main technologies:
- Firebase is used to login, store photo and location data, and store likes and dislikes.
- Google maps API is used to give users an interactive way to view the location of the spotting. 
- Tensorflow is utilized to verify submissions. It will run through the pretrained network locally and comfirm whether the image is actually of a particular specieis.
- Android location, camera, notification, and gallery services used.
