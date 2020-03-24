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
- [ ] Use at least one Service, which notifies the main application after a task
- [ ] Implement broadcast receiver and use system broadcasts (for smooth operation)
- [ ] Wrap the database in a content provider
- [ ] Use at least one system content provider (ie Contacts)
- [ ] System Dialogs and Permissions for Android v.6+
- [ ] Make sure our application can run on Android v.6+ and the earlier version without recompile
- [ ] Create an Application Widget with information pertinent to the application activities
- [ ] Implement Maps API
- [ ] Provide documentation with the names of classes responsible for each of the elements


#### Consideration for A03
With Igor's provided feedback from A02, we should ensure:
- [ ] "class names START WITH UPPERCASE!!!!!!!" I believe he means object names, since all of our classes actually do start with uppercase letters
- [ ] "please, spend a couple of second and check that event date is within the trip dates :)"
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
