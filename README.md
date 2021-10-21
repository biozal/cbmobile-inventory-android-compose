# Overview

The Inventory Demo App is a simple Android application written in Kotlin and uses Jetpack Compose to render the UI with Couchbase Lite as an embedded database that stores information in documents.

# Getting Started

For full docuemntation on this application, please see the repo [Wiki](https://github.com/biozal/cbmobile-inventory-android-compose/wiki).  

## Pre-requisites

- Android Studio 2020.3.1 (Arctic Fox) or higher
- Android SDK 29
- Android Build Tools 29
- JDK 8 (min)
- Android device or emulator (tested on Android 9.0 and higher)
- Docker Image of Couchbase Server and Sync Gateway (required for the replication screen to function properly)

# Build Instructions
- Clone repository
```sh
https://github.com/biozal/cbmobile-inventory-android-compose/
```
- Open the project's src folder in Android Studio.  
- Build from pull down menu and select Make Project or F9

# Couchbase Lite Documentation

Documenation on the Couchbase Lite API along with Sync Gateway can be found from the Couchbase website
- [Android Documentation](https://docs.couchbase.com/couchbase-lite/3.0/android/quickstart.html)
- [Sync Gateway](https://docs.couchbase.com/sync-gateway/current/introduction.html)

# Support

This project is provided AS-IS with no official support.  If you find a bug please file an issue.  For those interested in contributing to the project please follow the rules provided in the Wiki.
