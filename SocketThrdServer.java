
package socketthrdserver;

// By Greg Ozbirn, University of Texas at Dallas
// Adapted from example at Sun website:
// http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
// 11/07/07


import java.io.*;
import java.net.*;
import java.util.*;

class ClientWorker implements Runnable
{
    /*A socket is an endpoint for communication between two machines.*/
    private Socket client;

    public static ArrayList<String> allUsers = new ArrayList<>();
    public static ArrayList<String> allConnected = new ArrayList<>();

    ClientWorker(Socket client)
    {
       this.client = client;
    }

    @Override
    public void run()
    {
        String line;
        BufferedReader in = null;
        PrintWriter out = null;


        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        }

        try{
            // Receive text from client
            boolean go = true;
            while(go){
                line = in.readLine();
                System.out.println("receiving: " + line);

                if(line.contains("&")){

                    /*Parse request*/
                    ArrayList<String> request = new ArrayList<>(Arrays.asList(line.split("&")));
                    String name = request.get(0);
                    String command = request.get(1);
                    System.out.println("name = " + name+"   |   command = " + command);

                    switch (command){
                        case "1": //connect
                            System.out.println("here");
                            // add to the allUsers if they are new
                            if(!allUsers.contains(name))
                                allUsers.add(name);
                            // add to the allConnected list until they log off
                            if(!allConnected.contains(name))
                                allConnected.add(name);
                            out.println(name);
                            break;
                        case "a":
                            out.println(displayAll());
                            break;
                        case "b":
                            out.println(displayConnected());
                            break;
                        case "c":
                            out.println(line);
                            messageUser();
                            break;
                        case "d":
                            out.println(line);
                            messageAllConnected();
                            break;
                        case "e":
                            out.println(line);
                            messageAllKnown();
                            break;
                        case "f":
                            out.println(line);
                            getMessages();
                            break;
                        case "g":
                            go = false;
                            allConnected.remove(allConnected.indexOf(name));
                            break;
                        default:
                            out.println("error");
                            break;
                    }
                }
            }


            try{
                client.close();
            } catch (IOException e) {
                System.out.println("Close failed");
                System.exit(-1);
            }

        } catch (IOException e){
            System.out.println("Read failed");
            System.exit(-1);
        }

    }


    /*a. Display the names of all known users.*/
    public static String displayAll(){
      //make server loop through allUsers list and print each one out
        String names = "";
        for (int i = 0; i < allUsers.size(); i++){
            names+="&";
            names+=allUsers.get(i);
        }
        return names;
    }

    /*b. Display the names of all currently connected users.*/
    public static String displayConnected(){
      //send "displayAll()"
      //make server loop through allConnected list and print each one out
        String names = "";
        for (int i = 0; i < allConnected.size(); i++){
            names+="&";
            names+=allConnected.get(i);
        }
        return names;
    }

    /*c. Send a text message to a particular user.*/
    public static void messageUser(){
      System.out.println("\nStub method to message particular user\n");
    }

    /*d. Send a text message to all connected users.*/
    public static void messageAllConnected(){
      System.out.println("\nStub method to message all currently connected users\n");
    }

    /*e. Send a text message to all known users.*/
    public static void messageAllKnown(){
      System.out.println("\nStub method to message all known users\n");
    }

    /*f. Get my messages.*/
    public static void getMessages(){
      System.out.println("\nStub method to get messages\n");
    }
}

  class SocketThrdServer
  {
     /* A server socket waits for requests to come
      * in over the network. It performs some operation
      * based on that request, and then possibly returns
      * a result to the requester. */

     ServerSocket server = null;

     public void listenSocket(int port)
     {
        try
        {
           server = new ServerSocket(port);
           System.out.println("Server running on port " + port +
                               "," + " use ctrl-C to end");
        }
            catch (IOException e)
        {
           System.out.println("Error creating socket");
           System.exit(-1);
        }
        while(true)
        {
           ClientWorker w;
           try
           {
              w = new ClientWorker(server.accept());
              Thread t = new Thread(w);
              t.start();
           }
           catch (IOException e)
           {
              System.out.println("Accept failed");
              System.exit(-1);
           }
        }
     }

     protected void finalize()
     {
        try
        {
           server.close();
        }
        catch (IOException e)
        {
           System.out.println("Could not close socket");
           System.exit(-1);
        }
     }

     public static void main(String[] args)
     {
//        if (args.length != 1)
//        {
//           System.out.println("Usage: java SocketThrdServer port");
//           System.exit(1);
//        }

        SocketThrdServer server = new SocketThrdServer();
//        int port = Integer.valueOf(args[0]);
        int port = 3030;
        server.listenSocket(port);
    }



}
