# Synchronized Clock Android App

## Overview

This Android application, "Synchronized Clock," is a simple clock that displays the current time based on either the device's system time or the Network Time Protocol (NTP) server time. It uses the Apache Commons Net library for NTP time synchronization.

## Features

- Display of the current time in "HH:mm:ss" format.
- Automatic synchronization with an NTP server if a network connection is available.
- Display's NTP time in blue and system time in red.
- Periodic updating of the displayed time every second.
- Handling of network connectivity checks.

## Installation

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Build and run the application on your Android device or emulator.

## Usage

Upon launching the app, you will see a digital clock display that updates every second. The label above the clock indicates whether the displayed time is based on the NTP server time (in blue) or the device's system time (in red).

- If your device has an active network connection, the app will attempt to synchronize with an NTP server and display the NTP time.
- If there's no network connection, it will display the device's system time.

## Dependencies

This project relies on the following dependencies:

- Apache Commons Net Library for NTP time synchronization.

Make sure to include these dependencies in your project build configuration.

## Implementation Details

- The app uses a `Handler` to update the displayed time every second.
- It uses the "HH:mm:ss" format for displaying time.
- Network connectivity is checked using the `ConnectivityManager` class.
- NTP time synchronization is handled using the `NTPUDPClient` class from the Apache Commons Net library.

## Contributing

Contributions to this project are welcome. Feel free to open issues, suggest improvements, or submit pull requests.

## License

This project is provided under the Nackademin course Datorkommunikation, nätverk och konnektivitet License.  ;)  
