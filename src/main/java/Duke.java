import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String userInput = new String("");
        Scanner input = new Scanner(System.in);

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

                default:
                printSeparator();
                System.out.println(userInput);
                printSeparator();
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
