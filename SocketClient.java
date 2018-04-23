// By Greg Ozbirn, University of Texas at Dallas
// Adapted from example at Sun website:
// http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
// 11/07/07

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketClient
{
   Socket socket = null;
   PrintWriter out = null;
   BufferedReader in = null;
   Scanner sc = new Scanner(System.in);
   String name;

   public void getName()
   {
      System.out.print("Enter your name: ");
      name = sc.nextLine();

      //Send data over socket
      out.println(name+"&1");

      //Receive text from server
      try
      {
         String line = in.readLine();
         System.out.println("\nWelcome " + line);
      }
      catch (IOException e)
      {
         System.out.println("Read failed");
         System.exit(1);
      }
   }

   public void interact(){

     String[] responses = {"a", "b", "c", "d", "e", "f", "g"};

     while(true){

       System.out.print("\nYour choice: ");
       String choice = sc.nextLine();

       /* ensure choice is an actual choice */
       if(!Arrays.asList(responses).contains(choice)){
         System.out.println("\""+choice+"\" is not a valid choice.\n");
         continue;
       }

       /* exit client if user chooses to quit */
       if (choice.equals("g") || choice.equals("G")) {
         out.println(name+"&g");
         System.out.println("\nGoodbye\n");
         System.exit(0);
       }

       //Send data over socket
       out.println(name+"&"+choice.toLowerCase());

       //Receive text from server
       String response = "";
       try
       {
          response = in.readLine();
          System.out.println("Server response: " + response);
       }
       catch (IOException e)
       {
          System.out.println("Read failed");
          System.exit(1);
       }

       /* Manipulate response based on choice */
       switch (choice.toLowerCase()){
          case "a":
              System.out.println("\nALL USERS\n---------");
              ArrayList<String> allUsers = new ArrayList<>(Arrays.asList(response.split("&")));

              for (int i = 1; i < allUsers.size(); i++){
                System.out.println(allUsers.get(i));
              }
              break;
          case "b":
              System.out.println("\nCONNECTED USERS\n_______________");
              ArrayList<String> allConnected = new ArrayList<>(Arrays.asList(response.split("&")));

              for (int i = 1; i < allConnected.size(); i++){
                System.out.println(allConnected.get(i));
              }
              break;
          case "c":
              break;
          case "d":
              break;
          case "e":
              break;
          case "f":
              break;
          default:
              System.out.println("\""+choice+"\" is not a valid choice.\n");
              break;
       }

     }
   }

   public void listenSocket(String host, int port)
   {
      //Create socket connection
      try
      {
      	 socket = new Socket(host, port);
      	 out = new PrintWriter(socket.getOutputStream(), true);
      	 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      }
      catch (UnknownHostException e)
      {
      	 System.out.println("Unknown host");
      	 System.exit(1);
      }
      catch (IOException e)
      {
      	 System.out.println("No I/O");
      	 System.exit(1);
      }
   }

   public static void main(String[] args)
   {
      if (args.length != 2)
      {
          System.out.println("Usage:  client hostname port");
	        System.exit(1);
      }

      SocketClient client = new SocketClient();

      String host = args[0];
      int port = Integer.valueOf(args[1]);
      client.listenSocket(host, port);
      System.out.println("\n------MESSAGING SERVICE------\n");
      client.getName();
      System.out.print("\nPlease select an option: \n\n"
        + "a. Display the names of all known users.\n"
        + "b. Display the names of all currently connected users.\n"
        + "c. Send a text message to a particular user.\n"
        + "d. Send a text message to a particular user.\n"
        + "e. Send a text message to all known users.\n"
        + "f. Get my messages.\n"
        + "g. Exit.\n"
        );
      client.interact();
   }
}
