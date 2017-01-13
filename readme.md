#ArduinoMessengerClient

ArduinoMessengerClient is a library designed to simplify handling TCP/IP socket communication with Arduino board.

To parse your messages [ArduinoMessenger][am]

## Building the code
The application is coded in Java, using Maven to handle the project's configuration and tests.

### Prerequisites
The project has been tested on the following Java versions:
* JDK 7
* JDK 8
* OpenJDK 7

All other dependencies are handled through Maven, and noted in the included POM file.

### Installing
To install artifact in your local maven repository please follow these steps:

1. Download [ZIP](https://github.com/ktokarzewski/ArduinoMessengerClient/archive/master.zip)
2. Extract archive 
3. Open extracted project directory with command line and type `mvn package install`.

After that you would be able to add this artifact dependency to yours project pom.xml file:

```
<dependencies>
    <dependency>
        <groupId>pl.com.tokarzewski</groupId>
        <artifactId>arduino-messenger</artifactId>
        <version>0.1.2-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Quick start
This section describes basic
### Connection handling
To establish connection with your device you need to define `Connection` object.
```
String arduinoIP = "192.168.1.10";
int arduinoPort = 5544;
Connection connection = new ConnectionImpl(arduinoIP, arduinoPort);
```
And invoke `setupAndConnect()` method:
```
connection.setupAndConnect():
```
You can 
```
connection.tearDownAndDisconnect();
```

### Sending messages
```
Messenger messnger = new MessengerImpl(connection);
messenger.sendGetMessage("requested_resource");
```

```
messenger.sendPutMessage("resource_name", "resource_value");
```
### Message format

```
GET
{"request":"content"};
```

`messenger.sendPutMessage("resource_name","resource_value");`
```
PUT\n
{"resource":"resource_name","value":"resource_value"};
```


## Collaborate

The project is still under ongoing development, and any help will be well received.

There are two ways to help: reporting errors and asking for extensions through the issues management, or forking the repository and extending the project.

### Issues management
Issues are managed at the GitHub [project issues page][issues].

Everybody is allowed to report bugs or ask for features.

### Getting the code
The latest version of the code can be found at the [GitHub project page][scm].

Feel free to fork it, and share the changes.

## License
The project is released under the [MIT License][license].

[issues]: https://github.com/ktokarzewski/ArduinoMessengerClient/issues
[license]: http://www.opensource.org/licenses/mit-license.php
[scm]: https://github.com/ktokarzewski/ArduinoMessengerClient
[am]:https://github.com/ktokarzewski/ArduinoMessenger
