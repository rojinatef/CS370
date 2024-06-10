/*
This program will counts the number of processors wich is running on the pc
with a given name. 
This program will do this ps -A | grep argv[1] | wc -l. 
*/

#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>

int main (int argc, char *argv[]) {

    // fork will create a new process
    pid_t pid = fork();
    //pip1 will pip ps to Grep, and pip2 will pip Grep to wc -l
    int pip1[2], pip2[2];
    if(pipe(pip1) < 0 & pipe(pip2) < 0) {
      perror("pipe error");
      }

if (pid < 0) {
    perror("error to create child");
    return 1;
}

// parent process, wait for child 
if (pid > 0) {
    wait(NULL);
    return 1;
}

pid = fork();
if (pid < 0) {
     perror ("for craeting the child, fork should not be less than zero");
    return 1;
}

// when pid=0, create the second child
if (pid == 0) {
    pid = fork();
    if (pid < 0) {
        perror("error for creating the fork");
        return 1;
    }
    // create the child
    if (pid == 0) {
    //child
    //only need to read from pip2 which has Grep to wc -l 
    //so no need to write from pip1 and pip2 and we can delete them.
          close(pip2[1]); 
          close(pip1[1]);
          dup2(pip2[0], 0);  
          close(pip1[0]);
          execlp("wc", "wc", "-l" ,NULL);
          return 1;
    }
    //grand child
    // we need to write the output of pip2 which is grep to wc -l
    close(pip2[0]);
    dup2(pip1[0], 0);
    dup2(pip2[1], 1); 
    close(pip1[1]);
    execlp("grep", "grep", argv[1], NULL);
    return 1;
}
      //great grand child
      // need to write the pip1 which is ps to grep
      close(pip1[0]);
      close(pip2[0]);
      close(pip2[1]);
      dup2(pip1[1], 1); //redirect the messages fron pip1 to write 
      execlp("ps", "ps", "-A", NULL); 
      return 1;

}