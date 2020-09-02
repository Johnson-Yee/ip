import java.util.Scanner;
//import java.util.Arrays;

public class Duke {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String userInput = new String("");
        Task[] currentTasks = new Task[100];
        int numOfTasks = 0;

        showWelcomeMessage();

        while(!userInput.equals("bye")) {
            userInput = getUserInput();
            //Split the user input to get first word which is command of the user
            String[] splitUserInput = splitCommands(userInput);
            //Switch cases for the different user inputs based on first word of command
            switch (splitUserInput[0]) {

                case "bye":
                    exitProgram();
                    break;

                // List out all the current tasks
                case "list":
                    commandList(currentTasks, numOfTasks);
                    break;

                //Mark a completed task
                case "done":
                    markAsDone(currentTasks, numOfTasks, splitUserInput[1]);
                    break;

                //Set a deadline
                case "deadline":
                    numOfTasks = commandDeadline(currentTasks, numOfTasks, splitUserInput[1]);
                    break;
                //Set an impending task
                case "todo":
                    numOfTasks = commandToDo(currentTasks, numOfTasks, splitUserInput);
                    break;
                //Create an event
                case "event":
                    numOfTasks = commandEvent(currentTasks, numOfTasks, splitUserInput[1]);
                    break;
                //Display possible commands
                case "help":
                    commandHelp();
                    break;
                //Commands that are not recognised by program
                default:
                    invalidCommand();
                }
        }
    }



    /*
     * ===========================================
     *           MAIN COMMANDS
     * ===========================================
     */
    private static void commandList(Task[] currentTasks, int numOfTasks) {
        printSeparator();
        for(int i = 0; i < numOfTasks; i++){
            System.out.println((i+1) + "." +addBrackets(currentTasks[i].getType()) + addBrackets(currentTasks[i].getStatusIcon()) + " " + currentTasks[i].getDescription());
        }
        printSeparator();
    }

    private static int commandDeadline(Task[] currentTasks, int numOfTasks, String s) {
        String[] splitInfoAndDeadline = s.trim().split("/by",2 );

        currentTasks[numOfTasks] = new Deadline(splitInfoAndDeadline[0], splitInfoAndDeadline[1]);
        printSeparator();
        System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks[numOfTasks].getType())
                + addBrackets(currentTasks[numOfTasks].getStatusIcon()) +" " + splitInfoAndDeadline[0] + " (by: " +
                splitInfoAndDeadline[1] +  ")\n" + "Now you have " +(numOfTasks +1)+ (numOfTasks == 0 ? " task" : " tasks" )
                +" in the list");
        printSeparator();
        numOfTasks++;
        return numOfTasks;
    }

    private static int commandToDo(Task[] currentTasks, int numOfTasks, String[] splitUserInput) {
        currentTasks[numOfTasks] = new ToDo(splitUserInput[1]);
        printSeparator();
        System.out.println("Got it. I've added this task: \n" +"  " + addBrackets(currentTasks[numOfTasks].getType())
                + addBrackets(currentTasks[numOfTasks].getStatusIcon()) + " "+ splitUserInput[1] + " \n" + "Now you have "
                + (numOfTasks +1) + (numOfTasks == 0 ? " task" : " tasks" ) +" in the list");
        numOfTasks++;
        printSeparator();
        return numOfTasks;
    }

    private static int commandEvent(Task[] currentTasks, int numOfTasks, String s) {
        String[] splitInfoAndDate = s.trim().split("/at",2 );
        currentTasks[numOfTasks] = new Event(splitInfoAndDate[0], splitInfoAndDate[1] );
        printSeparator();
        System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks[numOfTasks].getType())
                + addBrackets(currentTasks[numOfTasks].getStatusIcon()) +" " + splitInfoAndDate[0] + " (at: " +
                splitInfoAndDate[1] +  ")\n" + "Now you have " + (numOfTasks +1) + (numOfTasks == 0 ? " task" : " tasks" )
                +" in the list");
        numOfTasks++;
        printSeparator();
        return numOfTasks;
    }

    //List of valid commands
    private static void commandHelp(){
        printSeparator();
        System.out.println("Here are the range of commands:\n 1.todo\n 2.deadline\n 3.event\n 4.list\n 5.done\n 6.bye\n ");
        printSeparator();
    }


    /*
     * ===========================================
     *           COMMAND LOGIC
     * ===========================================
     */
    private static void showWelcomeMessage() {
        printSeparator();
        String logo = "           ____          ____       \n"
                + " __(\")__  |  _ \\ _   _  |  _ \\  ____ \n"
                + "(__   __) | | | | | | | | | | || ___\\\n"
                + "  /   \\   | |_| | |_| | | |_| |\\  __/\n"
                + " (_/ \\_)  |____/ \\__,_| \\____/  \\____|\n";
        System.out.println(logo);
        System.out.println("Sup! I'm Dude!\nWhat can a brother do for you?");
        printSeparator();
    }
    private static String addBrackets(String description){
        String s;
        s = "[" + description +"]";
        return s;
    }
    //Function overload of addBrackets
    private static String addBrackets(char description){
        String s;
        s = "[" + description +"]";
        return s;
    }

    //Command "bye" is given; Exit program
    private static void exitProgram(){
        printSeparator();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }
    //Error message when command is not recognised
    private static void invalidCommand(){
        printSeparator();
        System.out.println("Sorry! You have entered an invalid command!\n For more help, type help");
        printSeparator();
    }
    //Function to get user input
    private static String getUserInput(){
        String inputLine = input.nextLine();
        return inputLine;
    }

    private static void printSeparator(){
        String line = "___________________________________________________";
        System.out.println(line);
    }
    //function to get words from sentence
    private static String[] splitCommands(String userInput) {
        final String[] split = userInput.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[]{split[0], " "};
    }
    //Function to mark as done
    private static void markAsDone(Task[] currentTasks, int numOfTasks, String s) {
        int taskNum = Integer.parseInt(s);

        if(taskNum <= 0 || taskNum > numOfTasks){
            printSeparator();
            System.out.println("Invalid input");
            printSeparator();
            return;
        }
        currentTasks[taskNum-1].setAsDone();
        printSeparator();
        System.out.println("Nice! I've marked this task as done: \n" + "[" +
                currentTasks[taskNum - 1].getStatusIcon() + "] " + currentTasks[taskNum - 1].getDescription());
        printSeparator();
    }

}
