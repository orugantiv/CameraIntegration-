# Camera Assignment App

This is an Android app created in Kotlin that allows users to take pictures and upload them to Firebase Storage. The app also includes functionality for retrieving the uploaded images and displaying them in an ImageView.

## Prerequisites
- Android Studio
- Firebase account
## Installation

- Clone the repository or download the ZIP file.
- Open Android Studio and select "Open an existing Android Studio project".
- Navigate to the directory where you cloned or unzipped the project and select the "CameraAssignment" directory.
- Android Studio should load the project automatically. If not, open the project by selecting "Open Project" from the "File" menu and navigating to the "CameraAssignment" directory.
- Connect your Android device to your computer using a USB cable.
- Click the "Run" button in Android Studio and select your device from the list of available devices.
- The app should now be installed on your device and ready to use.
## Usage
### Taking a picture
- Open the app on your device.
- Click the "Take Picture" button.
- If prompted, grant the app permission to use your device's camera.
- Take a picture using your device's camera app.
- The picture will be displayed in the ImageView.
## Uploading a picture
- Take a picture using the instructions above.
- Enter a name for the picture in the "File Name" field.
- Click the "Upload" button.
-The picture will be uploaded to Firebase Storage.
## Retrieving a picture
- Select the name of the picture from the dropdown list.
- Click the "Fetch" button.
- The picture will be retrieved from Firebase Storage and displayed in the ImageView.
## Firebase Setup
- Go to the Firebase console and create a new project.
- In the Firebase console, select the "Storage" tab and click "Get started".
- Follow the instructions to set up Firebase Storage for your project.
- In the Firebase console, select the "Realtime Database" tab and click "Create Database".
- Choose "Start in test mode" and click "Enable".
- Replace the contents of the "google-services.json" file in the "app" directory with the contents of the "google-services.json" file from your Firebase project.
- Run the app and make sure everything works as expected.
