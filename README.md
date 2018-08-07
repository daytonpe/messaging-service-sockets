# Network Communication Using Sockets

## Overview
This project will utilize Sockets for communication between processes.  
Your task is to write a client and a server.

The client and server will demonstrate a message posting system.  The server will maintain messages posted by clients, which clients can retrieve and view.

## Compiling

```
// Step 1
$javac SocketThrdServer.java
$java SocketThrdServer 8080

// Step 2 (In another tab AFTER starting server):
$javac SocketClient.java
$java SocketClient [IP] [PORT]
```

**Note**: you can use any valid port number so long as client and server sides match
**Note**: you will need to input the IP address of your computer ex: 111.1.1.1

## Client Specifications
* Accept a machine name and port number to connect to as command line arguments.
* Connect to the server.
* Prompt for and send the user’s name.
* Present the following menu of choices to the user:
* Display the names of all known users.
* Display the names of all currently connected users.
* Send a text message to a particular user.
* Send a text message to all currently connected users.
* Send a text message to all known users.
* Get my messages.
* Exit.
* Interact with the server to support the menu choices.
* Ask the user for the next choice or exit.

## Server Specifications
* Accept a port number as a command line argument.
* Accept connections from clients.
* Create a new thread for each client.
* Store messages sent to each user.
* End by termination with control-C.


## Server Thread Specifications
* Accept and process requests from the client.
* Add the user’s name to the list of known users.
* Provide mutual exclusion protection for the data structure that stores the messages.
* Send only the minimal data needed to the client, not the menu or other UI text.

## Other Rules

* Each client transaction should interact with the server.  Clients will not communicate directly with each other.
* Configuration:   your server should support multiple different clients at the same time, but should not allow the same user name to have more than one connection at the same time.
* Authentication:   assume the client has privileges to use the system—do not require a password.
* Limits:  you can assume a maximum of 100 known users, and a maximum of 10 messages each, where each message is at most 80 characters long.
* Persistence:  when the server exits the messages it is storing are lost. They will not be saved to a file.  When a user gets their messages, those messages are removed from the server.
* Users:  a known user is any user who has connected during the server session, but may or may not be currently connected.  Also, a message sent to an unknown user makes them known.
* Errors:  obvious errors should be caught and reported.  For example, an invalid menu choice.
* Output:  your output should use the same wording and format as the sample output.

##Sample output (assumes Joe has already connected):

Client output for user Sue:

```
>client cs1 2005

Connecting to cs1:2005

Enter your name: Sue

1. Display the names of all known users.
2. Display the names of all currently connected users.
3. Send a text message to a particular user.
4. Send a text message to all currently connected users.
5. Send a text message to all known users.
6. Get my messages.
7. Exit.
Enter your choice: 1

Known users:
Sue
Joe


1. Display the names of all known users.
2. Display the names of all currently connected users.
3. Send a text message to a particular user.
4. Send a text message to all currently connected users.
5. Send a text message to all known users.
6. Get my messages.
7. Exit.
Enter your choice: 3

Enter recipient's name: Joe
Enter a message: Hello Joe

Message posted to Joe


1. Display the names of all known users.
2. Display the names of all currently connected users.
3. Send a text message to a particular user.
4. Send a text message to all currently connected users.
5. Send a text message to all known users.
6. Get my messages.
7. Exit.
Enter your choice: 6

Your messages:
From Joe, 04/11/13 03:14 PM, Hello Sue
From Joe, 04/11/13 03:15 PM, What’s new?


1. Display the names of all known users.
2. Display the names of all currently connected users.
3. Send a text message to a particular user.
4. Send a text message to all currently connected users.
5. Send a text message to all known users.
6. Get my messages.
7. Exit.
Enter your choice: 7
```

## Server Sample Output

```
>server 2005

Server is running on cslinux1.utdallas.edu:2005

04/11/13, 3:10 PM, Connection by known user Sue.
04/11/13, 3:12 PM, Connection by unknown user Joe.
04/11/13, 3:13 PM, Sue displays all known users.
04/11/13, 3:13 PM, Sue posts a message for Joe.
04/11/13, 3:13 PM, Joe displays all connected users.
04/11/13, 3:14 PM, Joe posts a message for Sue.
04/11/13, 3:15 PM, Joe posts a message for Sue.
04/11/13, 3:17 PM, Sue gets messages.
04/11/13, 3:20 PM, Sue exits
04/11/13, 3:21 PM, Joe exits
```
