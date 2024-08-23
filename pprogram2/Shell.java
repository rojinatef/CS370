public class Shell extends Thread{

// shell starts at 1 like shell[1]
static int number = 1;
static boolean run = true;

public Shell( ) {

}

// it has to have run function, if the name was somethingelse, it would fail
public void run(){
// keep the shell running like infinity
while (run){
    StringBuffer buffer = new StringBuffer();
    // to print shell[i]%
    SysLib.cout("Shell["+ number +"]% ");
    // read the input from the user
    SysLib.cin(buffer); 
    // we need to convert the buffer into a string
    String input =  buffer.toString();
    // when user do not enter their input
    if (input.isEmpty()){
        SysLib.cout("you did not eneter your input, try again!");
        SysLib.cout("\n");
        continue;
    }
     // when the user eneter exit, it should exit from the shell
    if (input.equalsIgnoreCase("exit")){
        SysLib.cout("you exit the shell!");
        SysLib.exit();
        break;
    }
    // processes the input
    else{ 
    number++;

    String[] sequential = input.split(";");

    for (String seq: sequential) {
        // split the sequential when has & while going through it
        String[] concurrent = seq.split("&");        
        // keep track of thread IDs for concurrent
        int length = concurrent.length;
        int[] thread_ids = new int[length];

    for (int i = 0; i< length; i++) {
        String command = concurrent[i];
        if (!command.isEmpty()) {
            // convert command string into arguments 
            String[] args = SysLib.stringToArgs(command);
            // run the command and store the thread ID
            thread_ids[i] = SysLib.exec(args);
        }
    }
        // for concuurent we need to wait for thread to complete
            for (int tid : thread_ids) {
                // if the invalid thread id is -1
                if (tid != -1) { 
                    SysLib.join(); 
                }
            }
            

    }
}

}
}
}

