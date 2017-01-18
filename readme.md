#ArduinoMessengerClient

ArduinoMessengerClient is a library designed to simplify handling TCP/IP socket communication with Arduino board. To build server sketch for Arduino use [ArduinoMessenger][am] library.

## Table of content
- [Building the code](#building)
- [Prerequisites](#prerequisites)
- [Installing](#installing)
- [Getting started](#start)
- [Connection handling](#connection)
- [Messages](#messages)
- [Message format](#message_format)
- [Collaborate](#collaborate)
- [Issue management](#issues)
- [License](#license)

##
## Building the code
<a name="building"></a>

The application is coded in Java, using Maven to handle the project's configuration and tests.

### Prerequisites
<a name="prerequisites"></a>

The project has been tested on the following Java versions:
* JDK 7
* JDK 8
* OpenJDK 7

All other dependencies are handled through Maven, and noted in the included POM file.

### Installing
<a name="installing"></a>

To install artifact in your local maven repository please follow these steps:

1. Download [ZIP](https://github.com/ktokarzewski/ArduinoMessengerClient/archive/master.zip)
2. Extract archive 
3. Open extracted project directory with command line and type `mvn package install`.

After that you would be able to add dependency on ArduinoMessengerClient to your project in maven POM file:

```
<dependencies>
    <dependency>
        <groupId>pl.com.tokarzewski</groupId>
        <artifactId>arduino-messenger</artifactId>
        <version>0.1.3-SNAPSHOT</version>
    </dependency>
</dependencies>
```
To add dependency using Gradle:
```
dependencies {
    compile 'pl.com.tokarzewski:arduino-messenger:0.1.3-SNAPSHOT'
}
```

## Getting started
<a name="start"></a>
This section describes basic
### Connection handling
<a name="connection"></a>

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
To finish connection you should use:
```
connection.tearDownAndDisconnect();
```
### Messages
<a name="messages"></a>

To send and receive messages you need to define `Messenger` object.

```
Messenger messenger = new MessengerImpl(connection);
```
There are two major types of messages: 
- `GetMessage` which may be useful when you want get some resource from server:
```
messenger.sendGetMessage("requested_resource");
```
- and `PutMessage` to send `(resource, value)` pair to server:
```
messenger.sendPutMessage("resource_name", "resource_value");
```
To receive incoming messages use `MessageListener` interface.
```
messenger.addIncomingMessageListener(new MessageListener(){
    @Override
    public void onNewMessage(Message newMessage){
        //...
    }
});
```

### Message format
<a name="message_format"></a>
Each message consist of two parts. First is message type terminated by line feed character. Second part defines message content which is a JSON string terminated by semicolon character. 
- `GetMessage`
```
GET
{"request":"requested_value",};
```
- `PutMessage`
```
PUT
{"resource":"resource_name","value":"resource_value"};
```

## Collaborate
<a name="collaborate"></a>

The project is still under ongoing development, and any help will be well received.

There are two ways to help: reporting errors and asking for extensions through the issues management, or forking the repository and extending the project.

### Issues management
<a name="issues"></a>

Issues are managed at the GitHub [project issues page][issues].

Everybody is allowed to report bugs or ask for features.

## License
<a name="license"></a>

The project is released under the [MIT License][license].

[issues]: https://github.com/ktokarzewski/ArduinoMessengerClient/issues
[license]: http://www.opensource.org/licenses/mit-license.php
[am]:https://github.com/ktokarzewski/ArduinoMessenger
