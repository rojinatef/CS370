
import java.io.*;
import java.net.*;

public class DateClient extends Thread
{

    private int port;

    public DateClient (String[] args){
        // the port which is listining from the input that will get it
        // the port is a string so we need to convert it to the integer
        this.port = Integer.parseInt(args[0]);
    }

public void run () {
    try {
    /* make connection to server socket */
        Socket sock = new Socket("127.0.0.1", port);
        InputStream in = sock.getInputStream();
        BufferedReader bin = new BufferedReader(new InputStreamReader(in));
        /* read the date from the socket */
        String line;
        while ( (line = bin.readLine()) != null)
            System.out.println(line);
            /* close the socket connection*/
            sock.close();
    }
    catch (IOException ioe) {
         System.err.println(ioe);
    }
    SysLib.exit();
}

}










