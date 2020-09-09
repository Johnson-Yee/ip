import java.util.Scanner;
import java.util.Arrays;

public class Duke {
    /*Command Prefixes*/
    private static final String COMMAND_LIST = "list";

    /*Delimiter Prefixes*/
    private static final String EVENT_QUALIFIER = "/at";
    private static final String DEADLINE_QUALIFIER = "/by";

    /*Constants used*/
    private static final int MAX_TASKS = 100;

    //private static boolean hasExited = false;
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String userInput = "";
        Task[] currentTasks = new Task[MAX_TASKS];
        int numOfTasks = 0;
        boolean isOngoing = true;

        showWelcomeMessage();

        while(isOngoing) {
            userInput = getUserInput();
            String[] splitUserInput = splitCommands(userInput);
            switch (splitUserInput[0].toLowerCase()) {
            case "bye":
                printExitMessage();
                isOngoing = false;
                break;
            case "list":
                commandList(currentTasks, numOfTasks);
                break;
            case "done":
                markAsDone(currentTasks, numOfTasks, splitUserInput[1]);
                break;
            case "deadline":
                numOfTasks = commandDeadline(currentTasks, numOfTasks, splitUserInput[1]);
                break;
            case "todo":
                numOfTasks = commandToDo(currentTasks, numOfTasks, splitUserInput[1]);
                break;
            case "event":
                try {
                    numOfTasks = commandEvent(currentTasks, numOfTasks, splitUserInput[1]);
                } catch (DukeException e) {
                    e.printStackTrace();
                }
                break;
            case "help":
                commandHelp();
                break;
            default:
                try {
                    throw new DukeException("UNINTELLIGIBLE_COMMAND");
                } catch (DukeException exception) {
                    printSeparator();
                    System.out.println(exception);
                    printSeparator();
                }
            }
        }
    }



    /*
     * ===========================================
     *           MAIN COMMANDS
     * ===========================================
     */

    /**
     * Prints all tasks in task list
     *
     * @param currentTasks  Tasks Array of all tasks
     */
    private static void commandList(Task[] currentTasks, int numOfTasks) {
        printSeparator();
        if(numOfTasks == 0){
            System.out.println("You have zero task at hand!");
        }
        for(int i = 0; i < numOfTasks; i++){
            System.out.println((i+1) + "." +addBrackets(currentTasks[i].getType()) +
                    addBrackets(currentTasks[i].getStatusIcon()) + " " + currentTasks[i].getDescription());
        }
        printSeparator();
    }
    /**
     * Adds a Deadline task to the currentTasks Task array
     *
     * @param currentTasks  Tasks Array of all tasks
     * @param numOfTasks  Number of tasks
     * @param userInput User input
     * @return numOfTasks Updated number of tasks
     */
    private static int commandDeadline(Task[] currentTasks, int numOfTasks, String userInput) {
        try {
            /*If user input contains only one word*/
            if (userInput.length() < 1) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*Missing qualifier*/
            if (!userInput.contains("/by")) {
                throw new DukeException("MISSING_QUALIFIER");
            }
            String[] splitInfoAndDeadline = userInput.trim().split(DEADLINE_QUALIFIER,2 );
            /*If user input has no description*/
            if (splitInfoAndDeadline[0].isEmpty()) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*If user input has no deadline*/
            if (splitInfoAndDeadline[1].isEmpty()) {
                throw new DukeException("MISSING_INFO");
            }
            currentTasks[numOfTasks] = new Deadline(splitInfoAndDeadline[0], splitInfoAndDeadline[1]);
            printSeparator();
            System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks[numOfTasks].getType())
                    + addBrackets(currentTasks[numOfTasks].getStatusIcon()) +" " + splitInfoAndDeadline[0] + " (by: " +
                    splitInfoAndDeadline[1] +  ")\n" + "Now you have " +(numOfTasks +1)+ (numOfTasks == 0 ? " task" :
                    " tasks" ) +" in the list");
            printSeparator();
            numOfTasks++;

        } catch (DukeException error) {
            printSeparator();
            System.out.println(error);
            printSeparator();
        }
        return numOfTasks;
    }
    /**
     * Adds a ToDo task to the currentTasks Task array
     *
     * @param currentTasks  Tasks Array of all tasks
     * @param numOfTasks  Number of tasks
     * @param splitUserInput User input
     * @return numOfTasks Updated number of tasks
     */
    private static int commandToDo(Task[] currentTasks, int numOfTasks, String splitUserInput) {
        try {
            if(splitUserInput.isBlank()){
                throw new DukeException("MISSING_DESCRIPTION");
            }
            System.out.println(splitUserInput + splitUserInput.length());
            currentTasks[numOfTasks] = new ToDo(splitUserInput);
            printSeparator();
            System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks[numOfTasks].getType())
                    + addBrackets(currentTasks[numOfTasks].getStatusIcon()) + " " + splitUserInput + " \n" + "Now you have "
                    + (numOfTasks + 1) + (numOfTasks == 0 ? " task" : " tasks") + " in the list");
            numOfTasks++;
            printSeparator();

        }catch (DukeException error){
            printSeparator();
            System.out.println(error);
            printSeparator();
        }
        return numOfTasks;
    }
    /**
     * Adds a ToDo task to the currentTasks Task array
     *
     * @param currentTasks  Tasks Array of all tasks
     * @param numOfTasks  Number of tasks
     * @param userInput User input
     * @return numOfTasks Updated number of tasks
     */
    private static int commandEvent(Task[] currentTasks, int numOfTasks, String userInput) throws DukeException{
        try {
            /*If user input contains only one word*/
            if(userInput.length() < 1){
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*Missing qualifier*/
            if(!userInput.contains("/at")){
                throw new DukeException("MISSING_QUALIFIER");
            }
            String[] splitInfoAndDate = userInput.trim().split(EVENT_QUALIFIER, 2);
            /*If user input has no description*/
            if(splitInfoAndDate[0].isEmpty()){
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*If user input has no event date*/
            if(splitInfoAndDate[1].isEmpty()){
                throw new DukeException("MISSING_INFO");
            }

            System.out.println(Arrays.toString(splitInfoAndDate));
            currentTasks[numOfTasks] = new Event(splitInfoAndDate[0], splitInfoAndDate[1]);
            printSeparator();
            System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks[numOfTasks].getType())
                    + addBrackets(currentTasks[numOfTasks].getStatusIcon()) + " " + splitInfoAndDate[0] + " (at: " +
                    splitInfoAndDate[1] + ")\n" + "Now you have " + (numOfTasks + 1) + (numOfTasks == 0 ? " task" : " tasks")
                    + " in the list");
            numOfTasks++;
            printSeparator();
        } catch (DukeException error) {
            printSeparator();
            //error.printStackTrace();
            System.out.println(error);
            printSeparator();
        }
        return numOfTasks;
    }
    /*Prints out a list of available commands*/
    private static void commandHelp(){
        printSeparator();
        System.out.println("Here are the range of commands:\n 1.todo\n 2.deadline\n 3.event\n 4.list\n 5.done\n 6.bye");
        printSeparator();
    }


    /*
     * ===========================================
     *           COMMAND LOGIC
     * ===========================================
     */

    /*Prints welcome message*/
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
    /**
     * Surround text with brackets
     *
     * @param description  String to be placed in between brackets
     * @return enclosedString String in between brackets
     */
    private static String addBrackets(String description){
        String enclosedString;
        enclosedString = "[" + description +"]";
        return enclosedString;
    }
    /**
     * Surround character with brackets
     *
     * @param description  Character to be placed in between brackets
     * @return enclosedString String in between brackets
     */
    private static String addBrackets(char description){
        String enclosedString;
        enclosedString = "[" + description +"]";
        return enclosedString;
    }

    /*Command "bye" is given; Exit program*/
    private static void printExitMessage(){
        printSeparator();
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }
    /*Print message for invalid command*/
    private static void printInvalidCommand(){
        printSeparator();
        System.out.println("Sorry! You have entered an invalid command!\n For more help, type help");
        printSeparator();
    }
    /*Trim user input*/
    private static String getUserInput(){
        String inputLine = input.nextLine();
        return inputLine.trim();
    }

    private static void printSeparator(){
        String line = "________________________________________________________________________";
        System.out.println(line);
    }
    /*Function to get command from sentence and split sentence into 2*/
    private static String[] splitCommands(String userInput) {
        final String[] split = userInput.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[]{split[0], " "};
    }
    /**
     * Mark task as done
     *
     * @param currentTasks
     * @param numOfTasks
     * @param number
     */
    private static void markAsDone(Task[] currentTasks, int numOfTasks, String number) {
        int taskNum = Integer.parseInt(number);

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
