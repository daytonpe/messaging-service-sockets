// Patrick Dayton
// CS5348 - Ozbirn
// Due 4/23/18


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

class ClientWorker implements Runnable
{
    /*A socket is an endpoint for communication between two machines.*/
    private Socket client;

    public static ArrayList<String> allUsers = new ArrayList<>();
    public static ArrayList<String> allConnected = new ArrayList<>();
    static Semaphore mutex = new Semaphore(1);

    //messages array will be edited at the same time as the allUsers array so we can match them via index
    public static ArrayList<ArrayList<String>> messages = new ArrayList<>();

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

                if(line.contains("&")){

                    /*Parse request*/
                    ArrayList<String> request = new ArrayList<>(Arrays.asList(line.split("&")));
                    String name = request.get(0);
                    String command = request.get(1);
                    String recipient = request.get(2);
                    String message = request.get(3);

                    switch (command){
                        case "1": //connect
                            // add to the allConnected list until they log off
                            if(allConnected.contains(name)){
                                out.println("User already online.");
                                break;
                            }

                            allConnected.add(name);

                            boolean go1 = true;

                            // add to the allUsers if they are new
                            if(!allUsers.contains(name)){
                                allUsers.add(name);
                                ArrayList<String> temp = new ArrayList<>();
                                while(go1){
                                    try{
                                        // use mutual exclusion to keep from corrupting messages
                                        mutex.acquire();
                                        messages.add(temp);
                                        mutex.release();
                                        go1 = false;
                                    } catch(InterruptedException e) {
                                        System.out.println("InterruptedException caught");
                                    }
                                }

                                System.out.println("Connection by unknown user "+name);
                            }
                            else{
                                System.out.println("Connection by known user "+name);
                            }

                            out.println(name);
                            break;
                        case "a":
                            out.println(displayAll());
                            System.out.println(name+" displays all known users. ");
                            break;
                        case "b":
                            out.println(displayConnected());
                            System.out.println(name+" displays all currently connected users. ");
                            break;
                        case "c":

                            message = message+"#"+name;
                            boolean cGo;
                            int recipientIndex;

                            //find the recipient and add message to his/her message list
                            cGo = true;
                            while(cGo){
                                try{
                                    // use mutual exclusion to keep from corrupting messages
                                    mutex.acquire();
                                    try{
                                        recipientIndex = allUsers.indexOf(recipient);
                                        messages.get(recipientIndex).add(message);
                                    } catch (ArrayIndexOutOfBoundsException exception){
                                        //if recipient doesn't exist, add them
                                        allUsers.add(recipient);
                                        ArrayList<String> temp = new ArrayList<>();
                                        messages.add(temp);
                                        recipientIndex = allUsers.indexOf(recipient);
                                        messages.get(recipientIndex).add(message);
                                    }

                                    mutex.release();
                                    cGo = false;
                                } catch(InterruptedException e) {
                                    System.out.println("InterruptedException caught");
                                }
                            }
                            out.println("Message delivered.");

                            System.out.println(name+" posts a messge for " + recipient);
                            break;

                        case "d":
                            message = message+"#"+name;
                            boolean dGo = true;

                            while(dGo){
                                try{
                                    // use mutual exclusion to keep from corrupting messages
                                    mutex.acquire();
                                    for (int i = 0; i < allConnected.size(); i++) {
                                        messages.get(i).add(message);
                                    }
                                    mutex.release();
                                    dGo = false;
                                } catch(InterruptedException e) {
                                    System.out.println("InterruptedException caught");
                                }
                            }
                            out.println("Messages delivered.");
                            System.out.println(name+" posts a messge for all currently connected users.");
                            break;
                        case "e":
                            message = message+"#"+name;
                            boolean eGo = true;
                            while(eGo){
                                try{
                                    // use mutual exclusion to keep from corrupting messages
                                    mutex.acquire();
                                    for (int i = 0; i < allUsers.size(); i++) {
                                        messages.get(i).add(message);
                                    }
                                    mutex.release();
                                    eGo = false;
                                } catch(InterruptedException e) {
                                    System.out.println("InterruptedException caught");
                                }
                            }

                            out.println("Messages delivered.");
                            System.out.println(name+" posts a messge for all users.");
                            break;
                        case "f":
                            int currentIndex = allUsers.indexOf(name);
                            String mString = "";
                            for (int i = 0; i < messages.get(currentIndex).size(); i++) {
                                mString+="&";
                                mString+=messages.get(currentIndex).get(i);
                            }
                            out.println(mString);
                            messages.get(currentIndex).removeAll(messages.get(currentIndex));
                            System.out.println(name+" gets messages.");
                            break;
                        case "g":
                            go = false;
                            allConnected.remove(allConnected.indexOf(name));
                            System.out.println(name+" exits.");
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
       if (args.length != 1)
       {
          System.out.println("Usage: java SocketThrdServer port");
          System.exit(1);
       }

        SocketThrdServer server = new SocketThrdServer();
        int port = Integer.valueOf(args[0]);
        //int port = 3030;
        server.listenSocket(port);
    }
}
