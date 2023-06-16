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
Part of machine learning in this application is detecting items and detecting color. We utilize a dataset of nearby objects to build a model that can detect items. And developed it in TensorFlow-based machine learning solution. We use the MobileNetV2 type to modeling. For the color detection, we build a model that can detect a color. We developed the model using the CNN Model. 

### Step in Deploy the Machine Learning 
<details>
<summary>Object detection </summary>

![Diagram Tanpa Judul drawio (5)](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/271b0f38-a9a0-4d8d-a10f-4d384ba66fed)

</details>

<details>
<summary>Color Detection </summary>

 ![Diagram Tanpa Judul drawio (4)](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/28422eb6-d688-47a0-94aa-1795a9b22025)

</details>

### Dataset and Data Preparation 
The dataset we use for this object detection model is "Common Objects" dataset taken in 2014. The dataset can be download in [here](https://cocodataset.org/#download).  And we do a rescale and augmentation on the images. 
For the color detection, we use "bing image" pyhton library to download bulk of images form Bing.com.dataset and make it as a dataset for the color. The dataset can be download in <here>. For modeling, we use 2135 images. We split the dataset into training set of 1472 images and testing set of 663 images. 

<details>
<summary>Dataset</summary>
You can take a look at our filtered dataset for the object detection here: https://drive.google.com/drive/folders/16VpKYq1d1T67tmIX2h-GHPJzYjYUAb9w?usp=sharing .
And for the dataset of color objection, you can take a look in here : <>
</details>

### Model of Object Detection
Our model in this application is using MobileNetV2. We did some modification and adjustable to make the best model possible.
<details>
<summary>Model Summary</summary>

![WhatsApp Image 2023-06-15 at 18 56 25](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/ae1f8798-1516-429a-80c4-9f63f24a443b)

</details>



<details>
<summary>Accuracy</summary>

![1](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/76d6a24e-d28f-4f26-9863-62e5d5531dca)

</details>
<details>
<summary>Loss</summary>

![2](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/8908b114-f211-46d9-a021-61e738b1689b)

</details>


### Model of Color Detection 
Our model for the color detection in this application is using CNN Model . We did some modification and adjustable to make the best model possible.
<details>
<summary>Model Summary</summary>
 
<img width="411" alt="Screenshot 2023-06-15 002836" src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/9a9213dc-8c2f-43aa-a7de-1504a7499596">

</details>

<details>
<summary>Model Flowchart</summary>

![output (1)](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/614e9778-e3c8-4733-aa28-52390c60ca0a)

</details>

<details>
<summary>Accuracy</summary>

<img width="330" alt="Screenshot 2023-06-15 003126" src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/8ca0e720-43a5-4d48-9d1b-fe1f51b6120d">

</details>
<details>
<summary>Loss</summary>

<img width="342" alt="Screenshot 2023-06-15 003135" src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/ce077bf8-5825-4646-981b-7f157bddb70c">

</details>

## MOBILE DEVELOPMENT

### Overview
As the Mobile Development team, we use native Kotlin and Android Studio to create our Android app. Our responsibilities is setting up the Android app environment, designing the user interface through Figma, and implementing both object detection and color detection features for the app's functionalities. And once the application is complete, we monitor and fix any issues found to ensure the application runs as it should.

### Special Notes
Make sure to update the "default_web_client_id" values in the "strings.xml" file with your own client ID from the Google Cloud Platform. If you're unsure how to obtain it, you can refer to this  [tutorial](https://youtu.be/cN9ZV7_9FbE). Additionally, don't forget to enable the TalkBack accessibility feature on your device before running the application.





 ### App Documentation
<details>
<summary>Application UserFlow </summary>
 <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/ed2cdb9a-ffa6-4638-8a68-5ba1e9f5be09" width="100%"/>



</details>
 
<details>
<summary>Application Screenshots</summary>

<p float="left">
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/58e013f9-c4ab-4860-a022-bd9002285aba" width="24%" />
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/886c0599-8bbd-43ec-abd8-e6fd9717e928" width="24%" /> 
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/580517be-eb0f-44c0-a70f-32e9cd5fd9cb" width="24%" />
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/5620eaff-05fd-4dca-9c5d-2fc8d7e29d3a" width="24%" />
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/35958a2a-5de9-4847-86ef-328c9d86118c" width="24%" />
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/98f8e913-b86a-462f-bd2a-ac80a5cbea4f" width="24%" />
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/d23382d1-c9d6-433a-8e25-7751718808c3" width="24%" />
  <img src="https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/83630070/f2d02d3c-3966-4048-a3e2-441d5b429139" width="24%" />
</p>!

</details>


 
 <details>
<summary>Application Demo </summary>





https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71585270/d71059cf-6282-4bd3-b3aa-69f2eb58db4c




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
Then, to make it easier for users to login, we want to add Google Authentication. We use the Google Cloud Platform ( App Engine, Cloud Storage and SQL ) as the medium to deploy the project, and we may also use other third-party APIs as needed during development.
 
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
