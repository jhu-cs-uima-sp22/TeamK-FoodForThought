# TeamK-FoodForThought
Repository for the Food For Thought App!


## App Instructions
1. Authenticating a user <br>

The first screen to appear will be a page prompting you to either login or sign up for a new account. Feel free to use the following login information on the login page for the app, or create a new account using the sign up button:
```
email: test@foodforthought.com
password: test123
```
2. Searching for food pantries <br> 

Upon authentication, the user is brought to a map. Using the search bar at the top of the map, the user should input the city in which they would like to search for food pantries. For the first sprint, we have pantries exclusively in Baltimore. After clicking the Search button next to the search bar, a series of pins will appear on the map denoting the locations of all food pantries in the database in the provided city. 

3. Selecting and viewing a food pantry <br>

Once pins appear on the map, clicking on a pin will reveal more information about a food pantry such as its name and address. To view more specific details about a pantry, click on the info window that appears after clicking the pin for a food pantry on the map. Clicking this info window will bring you to a detailed view of the food pantry, which includes two types of information:
- the resources the food pantry needs 
- the hours of volunteering at this food pantry

If a user would like to contribute to a food pantry (donate resources or sign up for volunteer hours), they can click the Contribute button at the bottom of the food pantry detail page. 

4. Contributing to a food pantry <br>

On the contribute page, the user can check a particuar resource to indicate they would like to donate some quantity of that resource and they can provide the amount of that resource in the input to the right. Alternatively, they can select the hours over which they would like to volunteer by selecting start and end times from the scroll menus next to the desired day(s). Once all this information is selected, the Submit button will send this information to the database. 

## Sprint 1 Deliverables and Features

1. Login, Signup pages and Authentication

We fully implemented the Login and Signup Activies, and we connected these activities to Firebase Authentication to automatically handle creating new accounts and validating user credentials via email/password. The Welcome Activity acts as the main landing page for the application, which is what initially prompts a user to either login or signup. We also included persistent authentication so that a user remains authenticated after logging in on a device (unless specifying they would like to sign out). The sign out functionality can be accessed from the navigation drawer in the Maps Activity.

2. Firebase Realtime Database Storage

We are using a Firebase Reatime Database to store data related to our users and pantries. There are a few types of data:
- UserInfo: user info such as name, email, and other information included in the SignUp page
- PantryInfo: includes data about a pantry such as its name, location, hours of operation, and resources needed
- UserContributions: stores data about a user's past contributions to food pantries, such as the hours they volunteered and resources donated to a particular pantry

In the first sprint, we have setup UserInfo and PantryInfo to interact with the application. This means that pantries are read directly from the Firebase database to populate the Maps Activity and PantryDetail Activity. The UserContributions aspect of the database is a main deliverable for Sprint 2, as well as changing the resources needed for a pantry when a user donates resources to a pantry. 

3. Maps Activity and Pantry Search

When entering a city to search for, please note that if you enter a city where no food pantries in our database are present,
no markers will show up, but it will center around the city. Please make sure to enter a city that actually exists.
For testing purposes, enter the city "Baltimore".

4. PantryDetail Activity and ContributeActivity

The PantryDetail allows a user to view what resources are needed by the pantry and the hours available for volunteering. If a user wishes to donate some resources or volunteer, they can click the Contribute button. This brings them to the ContributeActivity, in which users can select which items they would like to donate and how many, or they can select which hours on given days they would like to volunteer. Upon submitting, they reach a confirmation page. We note that updating the resources needed in Firebase after a user donates is a Sprint 2 deliverable, along with keeping a log for a user's past donations and volunteer hours for a given pantry.

## Challenges and Other Comments
Overall, we only experienced a few minor bugs that we were able to resolve relatively quickly. The biggest complications were caused by occasionally using the asynchronous Firebase methods in a synchronous manner. To prevent unwanted behavior with these asynchronous methods, we addded `onCompleteListeners` to our `get()` operations when accessing data from Firebase. 

Overall, we made all expected deliverables for Sprint 1.
