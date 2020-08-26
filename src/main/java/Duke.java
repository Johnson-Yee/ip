import java.util.Scanner;
import java.util.Arrays;

public class Duke {
    public static void main(String[] args) {
        String userInput = new String("");
        Scanner input = new Scanner(System.in);
        String[] currentTasks = new String[100];
        int numOfTasks = 0;

        printSeparator();
        System.out.println("Sup! I'm Dude\nWhat can a brother do for you?");
        printSeparator();

        while(!userInput.equals("bye")) {
            userInput = input.nextLine();
            //Switch cases for the different user inputs
            switch (userInput.trim()) {
                case "bye":
                    exitProgram();
                    break;

                case "list":
                    printSeparator();
                    for(int i = 0;i <numOfTasks; i++){
                        System.out.println(i + "." + currentTasks[i]);
                    }
                    printSeparator();
                    break;

                default:
                printSeparator();
                System.out.println("Added: " + userInput);
                printSeparator();
                currentTasks[numOfTasks] = userInput;
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
