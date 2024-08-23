public class TestThread3 extends Thread {
    // we have 2 type, one for numerical computing, another one for I/O
    private String type;

    public TestThread3 (String[] args){
        
            this.type = args[0]; 
        
    }
    
    public void run(){
        if ("computation".equals(type)){
            Compute();
        }
        else 
        SimulateI_O();
    }

    // one conducting only numerical computation
    public void Compute(){
       // calculate prime numbers and perform operations on them
    int limit=5000; 
    // prime number starts from 2
    for (int number=2; number <limit; number++) {
        if (isPrime(number)) {
            // return the tang and  hyperbolic cosine of number for testing CPU performance 
            double operationResult= Math.tan(Math.sqrt(number))+ Math.cosh(number);
        }
    }
    SysLib.exit();
    }
    public boolean isPrime(int number) {
    if (number<=1) {
        return false;
    }
    for (int i=2; i <= Math.sqrt(number); i++) {
        if (number % i == 0) {
            return false;
        }
    }
    return true;
}
    
// according to the Prog4 

/* Disk.java
Disk.java simulates a slow disk device composed of 1000 blocks, each
containing 512 bytes. Those blocks are divided into 100 tracks, each of which
thus includes 10 blocks. 
*/

    public void SimulateI_O(){
        byte[] buffer = new byte[512];
        for (int i = 0; i<1000; i++){
            // go through all availble blocks 
             int blockId = i%1000; 
//  SysLib.rawread(int blk, byte b[])
//  SysLib.rawwrite(int blk, byte b[])
             // write(int blockId,byte buffer[] ) 
			SysLib.rawwrite(blockId, buffer);	
            // read(int blockId,byte buffer[] )
			SysLib.rawread(blockId, buffer);					
		}
		SysLib.exit();
    }

}