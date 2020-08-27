import java.util.Scanner;
import java.util.Arrays;

public class Duke {
    public static void main(String[] args) {
        String userInput = new String("");
        Scanner input = new Scanner(System.in);
        Task[] currentTasks = new Task[100];
        int numOfTasks = 0;

        printSeparator();
        System.out.println("Sup! I'm Dude\nWhat can a brother do for you?");
        printSeparator();

        while(!userInput.equals("bye")) {
            userInput = input.nextLine();
            //Split the user input to get first word which is command of the user
            String[] splitUserInput = userInput.split(" ",2);

            //Switch cases for the different user inputs based on first word of command
            switch (splitUserInput[0]) {

                case "bye":
                    exitProgram();
                    break;

                // List out all the current tasks
                case "list":
                    printSeparator();
                    for(int i = 0;i <numOfTasks; i++){
                        System.out.println((i+1) + ".[" + currentTasks[i].getStatusIcon() +"] " + currentTasks[i].getDescription());
                    }
                    printSeparator();
                    break;

                //Mark a completed task
                case "done":
                    int taskNum = Integer.parseInt(splitUserInput[1]);

                    if(taskNum <= 0 || taskNum > numOfTasks){
                        printSeparator();
                        System.out.println("Invalid input");
                        printSeparator();
                        break;
                    }
                    currentTasks[taskNum-1].setAsDone();
                    printSeparator();
                    System.out.println("Nice! I've marked this task as done: \n" + "[" +
                            currentTasks[taskNum - 1].getStatusIcon() + "] " + currentTasks[taskNum - 1].getDescription());
                    printSeparator();
                    break;

                default:
                printSeparator();
                System.out.println("Added: " + userInput);
                printSeparator();
                currentTasks[numOfTasks] = new Task(userInput);
                numOfTasks++;
                }
        }
    }
    //Command "bye" is given; Exit program
    public static void exitProgram(){
        printSeparator();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }

    public static void printSeparator(){
        String line = "___________________________________________________";
        System.out.println(line);
    }

}
