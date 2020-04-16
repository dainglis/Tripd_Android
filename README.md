# PROG3150 Mobile Application Development - Group 3
This repository is for Group 3's MAD assignments

## Tripd
The **Tripd** app is a road-trip planner for Android. Users can create a trip by adding *start* and *end* dates, and add **events** to the days of their trip.

The **Tripd** app has four pages:
* Review Trips
* Trip & Event Info
* Add/Edit Trip
* Add/Edit Event

### Requirements for A03
- [ ] [Michel] Use at least one Service, which notifies the main application after a task
- [ ] [Michel] Implement broadcast receiver and use system broadcasts (for smooth operation)
- [x] [David] Wrap the database in a content provider
- [x] [David] Use at least one system content provider (Calendar ContentProvider)
- [ ] System Dialogs and Permissions for Android v.6+
- [ ] Make sure our application can run on Android v.6+ and the earlier version without recompile
- [ ] [Megan] Create an Application Widget with information pertinent to the application activities
- [ ] [Nick] Implement Maps API ** Look into free access to Google services
- [ ] [Everyone] Provide documentation (Extensive file header comment) with the names of classes responsible for each of the elements


#### Consideration for A03
With Igor's provided feedback from A02, we should ensure:
- [x] Class names START WITH UPPERCASE!!!
- [x] Check that event date is within the trip dates
- [ ] Increase volume of log messages
- [ ] Ensure all exception handling is implemented


### Features Added for A02
The following requirements were met for A02:
- [x] GUI with at least 6 widgets (Menu, DialogFragment, Button, Switch, EditText, Spinner)
- [x] List view that displays details when an item is selected
- [x] Download files from the internet (downloading image)
- [x] Working with files (populating the database test data from a file)
- [x] Have at least two chained asynchronous tasks (async downloading image)
- [x] Use of intents to start other activities and visit internet sites (Support phone number and Wikipedia browser links)
- [x] Work with a database
- [x] Separate GUI, background, and database code (MVC pattern)


### Group
* David Inglis
* Megan Bradshaw
* Nick Iden
* Michel Talisson Gomes Lima
* Steven Knapp


### Meeting Minutes - March 27th 2020
Igor changed the deadline to the April 17th instead of the 11th. 
Megan - App widget (in android homescreen). Cardview of next upcoming trip, click launches the app directly to trip breakdown for that page. 
Everyon e- Refactor your file so Objects use uppercase names
Michel - System broadcast when the time and date happens for a particular event 
Service: Brainstorm for ideas. 
Issues with the repo or building: Message David
Next Meeting: Check in on Thursday Next Week.(Apr 2, 12pm) 


### Rubric Notes
Use of Services and Notifications - 15% 
    Application uses at least one application level service with notifications properly set. 
    Application uses at least 2 system services
Use of broadcasts and recievers - 15%
    Application uses at least one application broadcase and at least one system broadcast. 
Use of content providers - 15% 
    Application uses application content provider and one system provider
Use of dialogs and permissions - 10%
    Application uses both custom dialogs. 
    Application uses Marshmallow style permission requests
Use of Application Widgets - 10%
    Application uses a properly formed application widget. 
    The communication between the widget and the application is clear and proper. 
    Widget leads to a proper place in the application. 
Use of Maps - 5%
    Map is properly set up. 
    It is meaningful and dispayed in the proper place and scale. 
Documentation of all required elements - 15%
    Documentation is clear and concise. 
    It describes location and use of every feature implemented in the application.
Coding practices (logs, exception handling, comments) - 15%
    All code is properly commented.
    The use of log and exception handling is widespread. 
    The code is easy to read and understand. 

From you I will need an archived project with all code. Please, unhook it from GIT and make sure that your own path names are no longer than 150 chars. We have 240 total and I have no intention to place your code at C:\ level!!!!! 

I will also need a description document. The groups not submitting such document will get 20% reduction in mark. If it is not in the description, I have a right of overlooking it!!!!!
