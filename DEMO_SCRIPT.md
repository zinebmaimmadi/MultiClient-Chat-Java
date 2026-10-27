# 3-Minute Demo Script
**0:00-0:30** - Show the two Maven projects, Java 21 configuration, MVC-style packages, `server.properties`, and CSS/GridPane views.

**0:30-0:55** - Open `ChatServer.acceptLoop()` and point out `serverSocket.accept()` followed by `Thread.startVirtualThread(() -> serve(socket))`.

**0:55-1:20** - Run TCPServer. Show Online status, "Server Started", "Waiting for Client", and the connected-user ListView.

**1:20-2:05** - Run two TCPClient instances with usernames. Send messages with SEND and Enter. Show timestamps/usernames and server logs.

**2:05-2:30** - Connect a third client with a blank username. Show READ-ONLY MODE and that sending is disabled.

**2:30-2:45** - Type `allUsers` from one named client and show that the active-user list returns to that client.

**2:45-3:00** - Type `bye` or click LEAVE. Show disconnection and immediate server user-list/log update. End on the UML diagrams/README.
