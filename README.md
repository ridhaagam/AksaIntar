# AksaIntar
Aksa Intar is an object detector and color detector application. This application is intended for people who have vision problems.

People who have deficiencies must have their own challenges. An example of this is the blind who need an object to identify objects around them. what should he see. Because of this, the blind will have difficulty in living their daily lives. With the difficulty of being blind, our team created a project that can recognize an object through the camera. With the application that we made, we hope that it will be easier for the blind to live their lives. The apps we build combine machine learning and object recognition technologies in android apps. When recognizing an object, the application will tell the user the name of the object via audio. The main topic of our research on capstones is the application of applications for those who need help identifying objects in real-time. Apart from identifying an object, we also designed this application to be able to identify a color. This makes our application also suitable for color blind people. The name of the application is "Aksa Intar". We take the name from Sanskrit which means sight.

### TEAM OF AKSA INTAR
|Name|Role|ID|
|:------|:------|:------|       
|Muh. Ridha Agam 	        |Machine Learning	|M251DSX3131|
|Putri Maharani Isnainiyah	|Machine Learning	|M251DSY0474|
|Jeremy Lewi Munthe         |Mobile Developer	|A243DSX2749|
|Kelvin Aprilio	            |Mobile Developer	|A251DSX2720|
|Cahyo Dwi Setiono	        |Cloud Computing	|C286DSX0832|
|Noah Kiano Napitupulu	    |Cloud Computing	|C243DSX0628|

## MACHINE LEARNING 

### Overview
Part of machine learning in this application is detecting items. We utilize a dataset of nearby objects to build a model that can detect items. And developed it in TensorFlow-based machine learning solution. We use the MobileNetV2 type to modeling. 

### Step in Deploy the Machine Learning 
<details>
<summary>Object detection </summary>

![flowchart of mobilenetv2 drawio](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/892a997e-b5a4-424d-bd61-999fe60a672e)

</details>

### Dataset and Data Preparation 
The dataset we use for this model is "Common Objects" dataset taken in 2014. The dataset can be download in [here](https://cocodataset.org/#download). This dataset contains (jumlah gambar dalam dataset) images. 
For modeling, we use 2000 images. We split the dataset into training set of 1600 images and testing set of 400 images. And we do a rescale and augmentation on the images. 

<details>
<summary>Dataset</summary>
You can take a look at our filtered dataset here: https://drive.google.com/drive/folders/16VpKYq1d1T67tmIX2h-GHPJzYjYUAb9w?usp=sharing
</details>

### Model
Our model in this application is using MobileNetV2. We did some modification and adjustable to make the best model possible.
<details>
<summary>Model Summary</summary>

<img width="517" alt="Screenshot 2023-05-31 043402" src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/db5ad9b0-eaf7-4ab2-a12e-4b24fd05db98">

</details>

<details>
<summary>Model Flowchart</summary>

<>

</details>

<details>
<summary>Accuracy</summary>

<img width="274" alt="Screenshot 2023-05-31 044543" src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/4a6f85ee-e826-4acb-9903-160a24a88a64">

</details>
<details>
<summary>Loss</summary>

<img width="261" alt="Screenshot 2023-05-31 044653" src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/4a6b5aa7-d8cd-4fd9-9b30-a7311094f0a5">

</details>

## MOBILE DEVELOPMENT

### Overview
As the Mobile Development team, we use native Kotlin and Android Studio to create our Android app. Our responsibilities is setting up the Android app environment, designing the user interface through Figma, and implementing both object detection and color detection features for the app's functionalities. And once the application is complete, we monitor and fix any issues found to ensure the application runs as it should.
 ### App Documentation
<details>
<summary>Application Flowchart </summary>



</details>
 
 <details>
<summary>Application Screen Detail </summary>



</details>
 
 <details>
<summary>Application Demo </summary>



</details>
 
 <details>
<summary>Figma Link </summary>

This is the link to access the [Figma design](https://www.figma.com/file/iiWVTmkrsJye1XFBoKl1NQ/Aksa-Intar-App?node-id=0%3A1&t=zTcY4mvBm9BMwuEE-1).

</details>

 ### Library
Below are several libraries that we use for application development:

 * [Glide](https://github.com/bumptech/glide)
 * [Dagger Hilt](https://dagger.dev/hilt/)
 * [Retrofit](https://github.com/square/retrofit)
 * [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
 * [Layouting](https://developer.android.com/jetpack/compose/lists?hl=id)
 * [Shared Preferences](https://developer.android.com/training/data-storage/shared-preferences?hl=id)
 
## CLOUD COMPUTING
### Overview
For the cloud computing path, we create an API upload image for the apps. 
Then, to make it easier for users to login, we want to add Google Authentication. We use the Google Cloud Platform ( Compute Engine or App Engine and Cloud Storage ) as the medium to deploy the project, and we may also use other third-party APIs as needed during development.
 
### Google Cloud Platform
<details>
<summary>Dashboard GCP </summary>

![image](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/78722645/0a2c6f16-ee94-4143-94c5-7bd6825d12dd)

</details>
 
<details>
<summary>API & Services </summary>

![Untitled1](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/78722645/d23390e6-747f-4ebd-b811-08add445c1c7)

</details>

<details>
<summary>Cloud Storage [Bucket] </summary>

![image](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/78722645/754e1a6d-a72e-4b15-8115-0406e9b7686d)

</details>
 
<details>
<summary>SQL </summary>

![Untitled2](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/78722645/b8e7b03a-67dd-40d7-9f92-c0e0f877bf53)

</details>
 
<details>
<summary>App Engine </summary>

![Untitled3](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/78722645/a81df9b3-a38e-4a61-86d6-2509cc0a51c4)

</details>

### Cloud Architecture
<details>
<summary>Architecture </summary>

![image](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/78722645/3548b0ac-e139-485e-bc00-93794771a67a)

</details>
 
### API Documentation
Check our API documentation [POSTMAN] from link [here](https://documenter.getpostman.com/view/18310989/2s93sf1qdh).
