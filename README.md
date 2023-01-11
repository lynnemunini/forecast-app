# WEATHER FORECAST APP
This is a weather forecast app that provides users with accurate and up-to-date weather information. The app retrieves data from OpenWeatherMap **(One Call API)** and allows users to search for the weather of a particular location, view the forecast report, utilize the current location to get weather forecast of that location, and save searched locations in local storage for later use.

## Technologies Used
* **Jetpack Compose:** A modern UI toolkit for building native Android apps
* **Jetpack Navigation:** A library for building navigation on Android
* **Room:** A library for providing local data storage on Android
* **Hilt:** A dependency injection library for Android
* **OpenWeatherMap API:** A weather data API (One Call API) to retrieve weather data for a particular location
* **geocoder:** A library for converting location coordinates to location names and vice versa
* **Google Play's Location Library:** A library for retrieving the current location of the device

## Permissions
The app requires the **"Access to Location"** permission in order to work. This permission is required to retrieve the current location of the device and get the weather forecast of that location.

<img src = "images/permissions.png" alt = "Permissions and Network image">

The app also requires an active **internet** connection to work and retrieve the weather data.

<img src = "images/network.png" alt = "Network image">


## Features
* Search for the weather for a particular location
* Utilize user's current location to get weather forecast of that location
  
   <img src = "images/weather.png" alt = "Current weather">

* View the forecast report for a location
* Save searched locations in local storage
  
  <img src = "images/search.png" alt = "Search and Forecast report image">

### Known Issues
If the user clicks "Deny & Don't Ask Again" when prompted for the "Access to Location" permission, they will not be able to proceed further in the app and will need to restart the app to grant the permission.