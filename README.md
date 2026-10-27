# Multi-Client TCP Group Chat - Mini Project 1
**Student:** Zineb Maimmadi  
**ID:** 140962

## Overview
A Java 21 + JavaFX group-chat system using TCP sockets. A central server accepts multiple clients and starts a virtual thread for each connected client. The code separates networking/model logic from JavaFX views through controllers and event interfaces.

## Required features implemented
- Username identity; blank username enters READ-ONLY MODE.
- Real-time group messaging using SEND or Enter.
- `allUsers` returns the current active-user list only to the requesting client.
- `bye`, `end`, or LEAVE disconnects a client.
- Client Online/Offline indicator.
- Server accepts concurrent clients using virtual threads.
- Messages are timestamped and tagged with username.
- Chat messages are distributed to all *other* active clients.
- Server ListView shows connected usernames with random pastel backgrounds.
- Server activity log includes Server Started, Waiting for Client, and Welcome [User].
- Host/port loaded from `server.properties`.
- JavaFX uses GridPane and CSS.

## Requirements
- JDK 21 or later (virtual threads are required).
- Maven 3.9+.

## Build
From each project directory:
```bash
mvn clean package
```

## Run
Server:
```bash
java -jar target/tcpserver-1.0.0.jar
```
Client:
```bash
java -jar target/tcpclient-1.0.0.jar localhost 3000
```
The assignment's conceptual command is `java TCPClient <ServerIPAddress> <PortNumber>`; the packaged JAR form above is the practical Maven artifact command.

## Configuration
Edit `TCPServer/src/main/resources/server.properties` before building:
```properties
server.host=0.0.0.0
server.port=3000
```

## Architecture
PlantUML source for the class and deployment diagrams is in `uml/`.

## Demo checklist
1. Start the server and show Online status/logs.
2. Open two named clients and one blank-username client.
3. Show message exchange and timestamps.
4. Show that the blank user is read-only.
5. Send `allUsers` from one named client.
6. Disconnect with `bye` or LEAVE and show the server ListView update.
7. Briefly show `Thread.startVirtualThread(...)` in `ChatServer`.
