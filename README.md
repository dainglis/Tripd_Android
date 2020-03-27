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
- [ ][*Michel] Use at least one Service, which notifies the main application after a task
- [ ][Michel] Implement broadcast receiver and use system broadcasts (for smooth operation)
- [ ][David] Wrap the database in a content provider
- [ ][David] Use at least one system content provider (ie Contacts)
- [ ] System Dialogs and Permissions for Android v.6+
- [ ] Make sure our application can run on Android v.6+ and the earlier version without recompile
- [ ][Megan] Create an Application Widget with information pertinent to the application activities
- [ ][Nick] Implement Maps API ** Look into free access to Google services
- [ ][Everyone] Provide documentation (Extensive file header comment) with the names of classes responsible for each of the elements


#### Consideration for A03
With Igor's provided feedback from A02, we should ensure:
- [ ] "class names START WITH UPPERCASE!!!!!!!" I believe he means object names, since all of our classes actually do start with uppercase letters
- [ ] [Steve] Check that event date is within the trip dates
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