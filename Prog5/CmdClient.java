
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CmdClient extends Thread
{

    private int port;
    private String hostname;

    public CmdClient (String[] args){
   //part 2
  //  If no hostname parameter is provided, default to “localhost”,
  //otherwise set hostname equal to passed parameter before trying connection to server.
    if (args.length >=2 ){
        // sigint.eecs.wsu.edu
        this.hostname =args[0];
        this.port = Integer.parseInt(args[1]);
    }
    else
    {
     this.hostname = "localhost";
     this.port = Integer.parseInt(args[0]);

    }
    // test the port number specified as a parameter to ensure it is in the range 5000 – 5500, inclusive   
       if (this.port < 5000 || this.port > 5500){
        SysLib.cout("the port number must be between 5000 and 5500");
         System.exit(1);
       }

    }

public void run () {
    try {
    /* make connection to server socket */
     System.out.println("the host is: "+ this.hostname);
    Socket sock = new Socket(this.hostname, this.port);
    InputStream in = sock.getInputStream();
    PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
    BufferedReader bin = new BufferedReader(new InputStreamReader(in));

//handshake
// the username of the client’s owner (obtained from the OS using Java).
    String username = System.getProperty("user.name"); 
    SysLib.cout("cmdclient username: "+ username+"\n");
    // sending username 
    pout.println(username); 

// Client should check for a response if receiving a “null”, client should exit
    String serverResponse = bin.readLine();
    if ("null".equals(serverResponse)) {
        System.out.println("Authentication failed, server rejected connection.");
        SysLib.exit();
        // exit completley 
        System.exit(0);
    }


// Client should then connect to the new port received from the server and be ready for user input.  
    Socket newSocket = new Socket(this.hostname, this.port);
    PrintWriter newPout = new PrintWriter(newSocket.getOutputStream(), true);
    BufferedReader newBin = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));

            
/*2. Modify client to transmit a message entered by the user. Modify server to accept the
message, reverse the text, and send it back to the client. Client should print the reversed
text once received*/ 
        SysLib.cout("Please enter your message:\n");
        Scanner scanner = new Scanner(System.in); 
        String input = scanner.nextLine();

        // Modify client to disconnect and exit if the message entered by the user is “bye” or “die”

        if ("bye".equalsIgnoreCase(input)) {
        System.out.println("disconnec!");
        pout.println(input);
    } 
    else if ("die".equalsIgnoreCase(input)) {
        System.out.println("disconnect and exit");
        pout.println(input);
        SysLib.exit();
    }
        // send input to server
        pout.println(input); 
        // read the reversed string from the server
        String reversed = bin.readLine();
        /* read the date from the socket */
        System.out.println("server response is: " + reversed);
        String line;
        while ( (line = bin.readLine()) != null)
            System.out.println(line);
            /* close the socket connection*/
            newSocket.close();
            sock.close();
    }
    catch (IOException ioe) {
         System.err.println(ioe);
    }
    SysLib.exit();
}

}










