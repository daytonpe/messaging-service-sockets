// Patrick Dayton
// CS5348 - Ozbirn
// Due 4/23/18

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
      out.println(name+"&1&null&null");

      //Receive text from server
      try
      {
         String line = in.readLine();
         if(line.equals("User already online.")){
           System.out.println("\n"+line+"\n");
           getName();
         }
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
       String recipient = "";
       String message = "";
       String sender = "";
       ArrayList<String> allUsers;
       ArrayList<String> allConnected;
       ArrayList<String> singleMessage;

       /* ensure choice is an actual choice */
       if(!Arrays.asList(responses).contains(choice)){
         System.out.println("\""+choice+"\" is not a valid choice.\n");
         continue;
       }

       /* Manipulate response based on choice */
       switch (choice.toLowerCase()){
          case "a":
              out.println(name+"&"+choice.toLowerCase()+"&null&null");
              break;
          case "b":
              out.println(name+"&"+choice.toLowerCase()+"&null&null");
              break;
          case "c":
              System.out.print("\nMessage recipient: ");
              recipient = sc.nextLine();
              System.out.print("\nMessage: ");
              message = sc.nextLine();
              out.println(name+"&"+choice.toLowerCase()+"&"+recipient+"&"+message);
              break;
          case "d":
              System.out.print("\nMessage: ");
              message = sc.nextLine();
              out.println(name+"&"+choice.toLowerCase()+"&null&"+message);
              break;
          case "e":
              System.out.print("\nMessage: ");
              message = sc.nextLine();
              out.println(name+"&"+choice.toLowerCase()+"&null&"+message);
              break;
          case "f":
              out.println(name+"&"+choice.toLowerCase()+"&null&null");
              break;
          case "g":
              out.println(name+"&"+choice.toLowerCase()+"&null&null");
              out.println(name+"&g&null&null");
              System.out.println("\nGoodbye\n");
              System.exit(0);
              break;
          default:
              System.out.println("\""+choice+"\" is not a valid choice.\n");
              break;
       }

       //Receive text from server
       String response = "";
       try{
          response = in.readLine();
       }
       catch (IOException e){
          System.out.println("Read failed");
          System.exit(1);
       }

       /* Manipulate response to display information */
       switch (choice.toLowerCase()){
          case "a":
              System.out.println("\nALL KNOWN USERS\n---------------");
              allUsers = new ArrayList<String>(Arrays.asList(response.split("&")));
              for (int i = 1; i < allUsers.size(); i++){
                System.out.println(allUsers.get(i));
              }
              break;
          case "b":
              System.out.println("\nCONNECTED USERS\n---------------");
              allConnected = new ArrayList<String>(Arrays.asList(response.split("&")));

              for (int i = 1; i < allConnected.size(); i++){
                System.out.println(allConnected.get(i));
              }
              break;
          case "c":
              System.out.print("");
              System.out.println(response);
              break;
          case "d":
              System.out.print("");
              System.out.println(response);
              break;
          case "e":
              System.out.print("");
              System.out.println(response);
              break;
          case "f":
              System.out.println("\nMY MESSAGES\n-----------");
              allConnected = new ArrayList<String>(Arrays.asList(response.split("&")));

              for (int i = 1; i < allConnected.size(); i++){
                singleMessage = new ArrayList<String>(Arrays.asList(allConnected.get(i).split("#")));
                sender = singleMessage.get(1);
                message = singleMessage.get(0);
                System.out.print(String.format("%-12s" , sender+": "));
                System.out.println(message);
              }
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
        + "d. Send a text message to all currently connected users.\n"
        + "e. Send a text message to all known users.\n"
        + "f. Get my messages.\n"
        + "g. Exit.\n"
        );
      client.interact();
   }
}
