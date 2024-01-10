OPSC7312 POE
Team:
Lonwabo Gade
Koketso Motsikwe

Files
README.pdf
Demonstration Video 
Source Code Folder
Images Folder
Part 1 Planning and design

File Details
README.pdf: This document
Demonstration video: This video showcases the application's functionality and user interface, providing a visual guide to its operation.
Source Code Folder: This folder contains the complete source code for the application, including the code for the application's user interface, logic, and data access. It also includes the signed APK file, which is the packaged and ready-to-install version of the application.
Images Folder: The images folder contains all the image assets and app icons used in the application. This includes images for the user interface, icons for the app's features, and any other images that are used in the application.
Part 1 Planning and Design
Version 1.0
Features
User registration and login.
Settings management (toggle between metric and imperial system, set maximum travel distance).
View nearby birding hotspots on a map.
Display user's current position on the map.
Select a hotspot on the map to get directions.
Calculate and display the best route to the selected hotspot.
Save bird observations at the current location.
View all saved bird observations.

Known Issues
User information, settings, and data are not stored in an online, hosted service (fixed in Version 2.0)
Version 2.0
New Features
Registration information is stored in an online, hosted authentication service (Firebase Authentication)
Settings and user bird observations are stored in an online, hosted database (Cloud Firestore)
Bird observations are displayed on the map (using custom markers). If an observation was made at a hotspot, click on the marker to see the observation on the bottom sheet drawer.

Additional features (Own features)
Voiceover for navigation.
Map style customisation (Dark/Light).
Map visualisation of bird hotspots (hotspots as markers on the map)
Bug Fixes
Improved accuracy of route calculations
Addressed minor performance issues.

Cloud Services
Firebase Authentication is used for secure user login and signup.
Firebase Firestore is used to store user settings and observations.
eBird is used to retrieve bird hotspots.
Authentication Service
Firebase's authentication system acts as a trusted intermediary, validating user identities during login to maintain a secure user experience.
Data Storage
Data is now stored in a persistent cloud-based storage system, ensuring that any changes made are not lost when the application is closed.


