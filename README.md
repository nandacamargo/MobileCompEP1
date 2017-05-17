# Mobile Computing Programming Exercise

### Group Members:

Eduardo Delgado Coloma Bier <br>
Fernanda de Camargo Magano  <br>

### Objective of this exercise
Develop an app using Android and another using ionic. <br>
Both have the same functionalities and the point is to compare both tecnologies. <br>
This app makes use of webservices (http://207.38.82.139/seminar/doc/) and  <br> 
access to devices resources, like camera and internet access.

### Pre-requisites
Android-studio 2.3 <br>
Android SDK >= 1.7 <br>
Ionic 2 <br>


### Presence Confirmation

One way to confirm a student presence is using QR Code. <br>
The other form of confirmation works like this: the student send a request to the server. <br>
This request stays pending until the teacher confirm.


### Android and Ionic app
We divided in two types of users: students and teachers. <br>
The students can confirm their presence on the seminars and change their account information. <br>
The teacher can do more: besides editing their profile, they can add/edit/delete seminars, <br>
send confirmation of the students presence on the seminars to the server. <br>
So, some functionalities of the system that teachers have access are hidden from the students. <br>


### Android Libraries

We are using to this project the zxing library (to do QR-Code readings) and <br>
volley (HTTP library to help us with the requests). <br>

<b> Zxing  </b> https://github.com/zxing/zxing <br>
<b> Volley </b> https://github.com/google/volley <br>


### Ionic Libraries

We are using to this project the BarCodeScanner library (to do the QR-Code confirmation) <br>
and Http (to make the requests)

<b> Http </b> from Angular
<b> BarCodeScanner </b> from Ionic


### Emulator test

We tested using these emulators: <br>
- Galaxy Nexus, Android version 4.1 <br>
- Galaxy Nexus, Android version 7.1.1 <br>
- Pixel, version 7.1.1 <br>


