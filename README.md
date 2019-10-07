# AndroidViewMode_BindService_Demo
Demo project to communicate with a bind service using ViewModel
### How to clone :
 git clone https://github.com/iampkh/AndroidViewMode_BindService_Demo

 GoTo AndroidStudio -> File --> Open --> Select this folder.
 

## Purpose : How to use Viewmode,LiveData for a service communication (Messenger)

## Classes and Purpose:
### MainActivity : 
Gateway for the application. After launching the activity, you can see a textview which explain code flow.
A button used for sending a message to service using messenger.
Also  it observes for change in "getResponse()" ViewModel .when any change in the string displayed using Toast.

### BinderViewModel
This is a ViewModel, which contains a Repository instance to control the Service.

### Repository
This has the implementation of binding the service, sending and receiving the message from service.
ViewModel uses this repository instance and communicates with Service.

### BindingService
Service which runs in the background(run in main thread) , receives the message from the MainActiivty and displays toast message.
After 3 second delay, return message is sent using messenger.
This response is received by Respository.java . and updated the LiveData<String> object.
Hence, the activity which observes the live data get notified with the new message.
