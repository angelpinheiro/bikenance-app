# 🚴‍ Bikenance

Bikenance is an Android application that allows you to keep complete control over bike and component
maintenance. With Bikenance, you can register your bikes, manage components, track maintenance, and
receive reminders to keep your bikes in optimal condition. The app features seamless integration
with Strava, a popular platform for tracking cycling activities.

**This is an ongoing learning project** that I build to explore and enhance my programming skills
and try new stuff. There may be changes or updates made at any time without prior notice.

Below you can see some screenshots of some parts off the app that are currently being developed.

| <img src="assets/1.png" width="120"> | <img src="assets/2.png" width="120"> | <img src="assets/3.png" width="120"> | <img src="assets/4.png" width="120"> | <img src="assets/5.png" width="120"> | <img src="assets/6.png" width="120"> | <img src="assets/7.png" width="120"> | <img src="assets/7.png" width="120"> |
|:------------------------------------:|:------------------------------------:|:------------------------------------:|:------------------------------------:|:------------------------------------:|:------------------------------------:|:------------------------------------:|:------------------------------------:|

## Key Features

- **Strava Integration**: Bikenance integrates with Strava, allowing you to leverage your
  activity data for enhanced maintenance tracking.

- **Component Management**: Manage the components of your bikes, such as forks, wheels, brakes,
  chains,
  etc. Record details such as the brand, model, and installation date for each component, and
  associate
  them with your Strava-imported bikes.

- **Maintenance Tracking**: Activity data from Strava is used to update component mileage and usage
  hours.

- **Maintenance Reminders**: Receive notifications and reminders when maintenance or component
  replacement is due based on accumulated mileage and usage hours. Future smart calculations will
  take into account your Strava activity parameters like avg power, or climatological data.

- **Maintenance History**: Keep a maintenance history for each component. Record
  repairs, part replacements, adjustments, and other maintenance tasks performed.

## Roadmap

### MVP Version

The MVP version of Bikenance will focus on the following functionalities:

- Strava integration
    - Login with strava
    - Import user profile and bikes from Strava
    - Keep track of user rides
    - Display user rides
- Bike components registration.
- Basic maintenance tracking based on mileage and usage hours.
- Maintenance reminders based on accumulated mileage and usage hours.

### Future versions

- 📄 Invoice and Note Storage: Allow users to store invoices and notes related to repairs carried out
  at specialized stores.

- 📏 Biomechanical Measurements and Customizations: Provide a space to store biomechanical
  measurements, such as saddle height, handlebar distance, etc. This will facilitate bike
  adjustments in case of changes or necessary modifications.

- 🧠 Intelligent Wear Calculation
    - Utilize activity parameters from Strava to weigh component wear. These parameters may include
      terrain type, weather conditions, and activity intensity.
    - Dynamic Wear Thresholds: Over time, the app analyzes the user's maintenance history to
      determine personalized wear thresholds for each component. If a component consistently wears
      out before the initially suggested threshold, the app adjusts the threshold accordingly for
      future maintenance notifications.

## Architecture

Bikenance utilizes a client-server architecture, with a mobile app for Android and
a [backend server](https://github.com/angelpinheiro/bikenance-backend).

### Android App

The Android app follow an MVVM architecture.

- Jetpack Compose for modern UI development.
- Room, Hilt, ViewModel, Coroutines, and other Android Jetpack Libraries.
- Push Notifications: Firebase Cloud Messaging (FCM) for delivering push notifications.

### Backend Server

The Bikenance backend server comprises the following technologies:

- Language: Kotlin
- Framework: Ktor, a flexible and asynchronous web framework for building server applications.
- Database: MongoDB, a NoSQL database used to store user profiles, bike data, and activity
  information.
- API Integration: Utilizes the Strava API to authenticate users, retrieve bike data, and receive
  activity information.

## Artwork attribution

Icons used for bike components are from
the [Mountain Bike](https://thenounproject.com/timo40/collection/mountain-bike)
collection by [Timo Nagel](https://thenounproject.com/timo40/)
at [The Noun Project ](https://thenounproject.com)

## License

Bikenance is [licensed](LICENSE.md) under
the [CC BY-NC-SA 4.0 License](https://creativecommons.org/licenses/by-nc-sa/4.0/)



