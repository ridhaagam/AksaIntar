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

### Dataset and Data Preparation 
The dataset we use for this model is "Common Objects" dataset taken in 2014. The dataset can be download in [here](https://cocodataset.org/#download). This dataset contains (jumlah gambar dalam dataset) images. 
For modeling, we use 2000 images. We split the dataset into training set of 1600 images and testing set of 400 images. And we do a rescale and augmentation on the images. 

### Modeling 
Our model in this application is using MobileNetV2. We did some modification and adjustable to make the best model possible.
<details>
<summary>Model Summary</summary>

```
![](https://github.com/ridhaagam/Capstone-Project-C23-PS361/assets/71591898/63e85928-37f4-4928-b1fc-12fb64f52070)
```
</details>
