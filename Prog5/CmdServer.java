import java.net.*;
import java.io.*;
public class CmdServer extends Thread
{

public CmdServer(){

}
    public void run(){
        // part2
        // For CmdServer, have the server loop through ports in the range 5000 – 5500,inclusive.
        ServerSocket sock = null ;
        ServerSocket randomPortSocket = null;
        for (int i=5000; i<=5500; i++){
           try {
            sock = new ServerSocket(i);
            SysLib.cout(InetAddress.getLocalHost().getHostName()+ "  is listening to " + i);
            break;
           }
           catch (IOException ioe) {
            // try next port if the current port is unavailble
            continue;
        }

        }
        try{

            System.out.println("");
            System.out.println(" Plaese waite for connection!");
            while (true){
                
                Socket client = sock.accept();
                PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
                BufferedReader bin = new BufferedReader(new InputStreamReader(client.getInputStream()));

            //handshake
            /*The server should check its first received message against its own username
            (obtained from the OS using Java) to ensure they match. If they do not match, the
            server should disconnect and exit*/

                String clientUsername = bin.readLine(); 
                String serverUsername = System.getProperty("user.name"); 
            // continue listining if the connection is null
                if (clientUsername == null) {
                    client.close();
                    continue;
                }
                System.out.println("cmdserver username: " + serverUsername);
                // exit if do not match 
                if (!clientUsername.equals(serverUsername)) {
                    System.out.println("Usernames do not match!");
                    // sending null to client to informe it to exit
                    pout.println("null");
                    client.close(); 
                    SysLib.exit();
                   
                }
            // if usernames match connect to a new random port 
                if (clientUsername.equals(serverUsername))  {             
                randomPortSocket = new ServerSocket(0);
                SysLib.cout("Authorized connection, now is listening to:" + randomPortSocket.getLocalPort());
                 SysLib.cout("\n");
                pout.println(randomPortSocket.getLocalPort());

                String message = bin.readLine();

  /* Also modify the server to close the connection and accept new connections (with
handshaking) after receiving a “bye” message, and to gracefully exit entirely after
receiving a “die” message.*/              
                if ("bye".equalsIgnoreCase(message)) {
                System.out.println("Received 'bye' message.");
                // close the current client 
                client.close();
                // continue for the connection again
                 System.out.println("continue for a new connection");
                continue;
                } 
                else 

                if ("die".equalsIgnoreCase(message)) 
                {
                    System.out.println("Received 'die' message! Exiting server.");
                    SysLib.exit();
                }

               


            /*if it is one of the following commands, instead execute the command locally and then transmit the results to the
            client. Note that the commands should be able to run with parameters, but do not worry about properly parsing and executing wildcards. Further, note that all previous
            functionality should continue to function after executing the command.
            a. "whoami"
            b. "ls"
            c. "pwd"
            d. "ps"
            e. "man"
            f. "echo"
            g. "date"
            */
           else{
           // Check if the message is a command, else send the reversed text 
                if (isCommand(message)) {
                    String commandResult = executeCommand(message);
                    pout.println(commandResult);
                } else {
                    // Reverse the message
                    String reversed = new StringBuilder(message).reverse().toString();
                    // Send reversed text back to client
                    pout.println(reversed);
                }
                /* write the Date to the socket */
                pout.println(new java.util.Date().toString());
                /* close the socket and resume */
                /* listening for connections */
                client.close();
            
            }
                }
        
        }
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
        SysLib.exit();
    }
    // check for being commands
    private boolean isCommand(String message) {
        String[] commands = {"whoami", "ls","pwd", "ps", "man", "echo", "date"};
        for (String command : commands) {
            // if the commands start with these are executable
            if (message.toLowerCase().startsWith(command.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
// execute the commands 
    private String executeCommand(String command) {
        try {
              String[] commandParts = command.split(" ");
            // Executes the specified string command in a separate process.
            //  Runtime.getRuntime().exec(""), this will executes the command
            Process process = Runtime.getRuntime().exec(commandParts);
            // read the output of the command 
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "we cannot execute this command: " + command;
        }
    }
    
}

