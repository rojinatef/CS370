import java.net.*;
import java.io.*;
public class DateServer extends Thread
{

public DateServer(){

}
    public void run(){
        try {

            // part1) it connects to a random port (ServerSocket(0)) rather than a static port and prints out the port where it is listening
            ServerSocket sock = new ServerSocket(0);
            SysLib.cout(" it is listening to " + sock.getLocalPort());
            while (true){

                Socket client = sock.accept();
                PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
                /* write the Date to the socket */
                pout.println(new java.util.Date().toString());
                /* close the socket and resume */
                /* listening for connections */
                client.close();
            
            }
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
        SysLib.exit();
    }
    
}
