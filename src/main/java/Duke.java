import Exceptions.DukeException;
import Tasks.Deadline;
import Tasks.Event;
import Tasks.Task;
import Tasks.ToDo;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    /*Command Prefixes*/
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_DONE = "done";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_DELETE = "delete";

    /*Delimiter Prefixes*/
    private static final String EVENT_QUALIFIER = "/at";
    private static final String DEADLINE_QUALIFIER = "/by";

    /*External classes used*/
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Task> currentTasks = new ArrayList<>();

    public static void main(String[] args) throws DukeException, IOException {
        boolean isOngoing = true;
        int numOfTasks = loadTaskFromTXT();
        printWelcomeMessage();
        executeCommands(numOfTasks, isOngoing);
    }


    /*
     * ===========================================
     *           MAIN COMMANDS
     * ===========================================
     */


    /**
     * Execute commands as intended by user
     *
     * @param numOfTasks Number of tasks
     * @param isOngoing  Whether the program has yet to be exited
     * @throws DukeException to catch error specified under DukeException
     */
    private static void executeCommands(int numOfTasks, boolean isOngoing) throws DukeException, IOException {
        String userInput;
        while (isOngoing) {
            userInput = getUserInput();
            String[] splitUserInput = splitCommands(userInput);
            String userCommand = splitUserInput[0].toLowerCase();
            switch (userCommand) {
            case COMMAND_BYE:
                FileManager.saveFile(currentTasks);
                printExitMessage();
                isOngoing = false;
                break;
            case COMMAND_LIST:
                commandList(numOfTasks);
                break;
            case COMMAND_DONE:
                commandDone(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_DEADLINE:
                numOfTasks = commandDeadline(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_TODO:
                numOfTasks = commandToDo(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_EVENT:
                numOfTasks = commandEvent(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_DELETE:
                numOfTasks = commandDelete(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_HELP:
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

    /**
     * Prints all tasks in task list
     *
     * @param numOfTasks Total number of tasks
     */
    private static void commandList(int numOfTasks) {
        try {
            printSeparator();
            if (numOfTasks == 0) {
                System.out.println("You have zero task at hand!");
            }
            for (int i = 0; i < numOfTasks; i++) {
                System.out.println((i + 1) + "." + addBrackets(currentTasks.get(i).getType()) +
                        addBrackets(currentTasks.get(i).getStatusIcon()) + " " + currentTasks.get(i).getDescription());
            }
            printSeparator();
        } catch (ArrayIndexOutOfBoundsException error) {
            printSeparator();
            System.out.println("Array index out of bounds detected!\n");
            printSeparator();
        }
    }

    /**
     * Adds a Deadline task to the currentTasks Task array
     *
     * @param numOfTasks Number of tasks
     * @param userInput  User input
     * @return numOfTasks Updated number of tasks
     * @throws DukeException to catch error specified under DukeException
     */
    private static int commandDeadline(int numOfTasks, String userInput) throws DukeException{
        try {
            /*If user input contains only one word*/
            if (userInput.length() < 1) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*Missing qualifier*/
            if (!userInput.contains("/by")) {
                throw new DukeException("MISSING_QUALIFIER");
            }
            String[] splitInfoAndDeadline = userInput.trim().split(DEADLINE_QUALIFIER, 2);
            /*If user input has no description*/
            if (splitInfoAndDeadline[0].isEmpty()) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*If user input has no deadline*/
            if (splitInfoAndDeadline[1].isEmpty()) {
                throw new DukeException("MISSING_INFO");
            }
            Deadline newDeadline = new Deadline(splitInfoAndDeadline[0], splitInfoAndDeadline[1]);
            currentTasks.add(newDeadline);
            printSeparator();
            printTaskConfirmation(numOfTasks, splitInfoAndDeadline);
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
     * @param numOfTasks     Number of tasks
     * @param splitUserInput User input
     * @return numOfTasks Updated number of tasks
     * @throws DukeException to catch error specified under DukeException
     */
    private static int commandToDo(int numOfTasks, String splitUserInput) throws DukeException {
        try {
            if (splitUserInput.isBlank()) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            ToDo newTodo = new ToDo(splitUserInput);
            currentTasks.add(newTodo);
            printSeparator();
            printTaskConfirmation(numOfTasks, splitUserInput);
            numOfTasks++;
            printSeparator();

        } catch (DukeException error) {
            printSeparator();
            System.out.println(error);
            printSeparator();
        }
        return numOfTasks;
    }

    /**
     * Adds a Event task to the currentTasks Task array
     *
     * @param numOfTasks Number of tasks
     * @param userInput  User input
     * @return numOfTasks Updated number of tasks
     * @throws DukeException to catch error specified under DukeException
     */
    private static int commandEvent(int numOfTasks, String userInput) throws DukeException {
        try {
            /*If user input contains only one word*/
            if (userInput.length() < 1) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*Missing qualifier*/
            if (!userInput.contains("/at")) {
                throw new DukeException("MISSING_QUALIFIER");
            }
            /*Split into 2 sections with index 0 being the Description and index 1 being event date*/
            String[] splitInfoAndDate = userInput.trim().split(EVENT_QUALIFIER, 2);
            String eventDescription = splitInfoAndDate[0];
            String eventDate = splitInfoAndDate[1];

            /*If user input has no description*/
            if (eventDescription.isEmpty()) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*If user input has no event date*/
            if (eventDate.isEmpty()) {
                throw new DukeException("MISSING_INFO");
            }
            Event newEvent = new Event(eventDescription, eventDate);
            currentTasks.add(newEvent);
            printSeparator();
            printTaskConfirmation(numOfTasks, splitInfoAndDate);
            numOfTasks++;
            printSeparator();
        } catch (DukeException error) {
            printSeparator();
            System.out.println(error);
            printSeparator();
        }
        return numOfTasks;
    }

    /**
     * Mark task as done
     *
     * @param numOfTasks total number of tasks
     * @param number     task number
     * @throws DukeException         to catch error specified under DukeException
     * @throws NumberFormatException specified task number is not recognised eg not a number/ is empty
     */
    private static void commandDone(int numOfTasks, String number) throws DukeException {
        try {
            int taskNum = Integer.parseInt(number);
            if (taskNum <= 0 || taskNum > numOfTasks) {
                throw new DukeException("OUT_OF_RANGE");
            }
            if (number.isBlank()) {
                throw new DukeException("MISSING_NUMBER");
            }
            updateAsDone(taskNum);
        } catch (DukeException error) {
            printSeparator();
            System.out.println(error);
            printSeparator();
        } catch (NumberFormatException numberError) {
            printSeparator();
            System.out.println("Gosh you did not key in a valid number");
            printSeparator();
        }
    }

    /**
     * Removes a task
     *
     * @param numOfTasks total number of tasks
     * @param number     task number
     * @return numberOfTasks Returns updated number of task
     * @throws DukeException         to catch error specified under DukeException
     * @throws NumberFormatException specified task number is not recognised eg not a number/ is empty
     */
    private static int commandDelete(int numOfTasks, String number) throws DukeException {
        try {
            int taskNum = Integer.parseInt(number);
            if (taskNum <= 0 || taskNum > numOfTasks) {
                throw new DukeException("OUT_OF_RANGE");
            }
            if (number.isBlank()) {
                throw new DukeException("MISSING_NUMBER");
            }
            numOfTasks = removeFromArray(numOfTasks, taskNum);
        } catch (DukeException error) {
            printSeparator();
            System.out.println(error);
            printSeparator();
        } catch (NumberFormatException numberError) {
            printSeparator();
            System.out.println("Gosh you did not key in a valid number");
            printSeparator();
        }
        return numOfTasks;
    }

    private static void commandHelp() {
        printSeparator();
        printHelpMessage();
        printSeparator();
    }

    /*
     * ===========================================
     *           COMMAND LOGIC
     * ===========================================
     */

    private static void printWelcomeMessage() {
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
     * @param description String to be placed in between brackets
     * @return enclosedString String in between brackets
     */
    private static String addBrackets(String description) {
        String enclosedString;
        enclosedString = "[" + description + "]";
        return enclosedString;
    }

    /**
     * Surround character with brackets
     *
     * @param description Character to be placed in between brackets
     * @return enclosedString String in between brackets
     */
    private static String addBrackets(char description) {
        String enclosedString;
        enclosedString = "[" + description + "]";
        return enclosedString;
    }

    /*Command "bye" is given; Exit program*/
    private static void printExitMessage() {
        printSeparator();
        System.out.println("Goodbye. Sad to see you leave!\n Hope to see you again soon!");
        printSeparator();
    }


    /*Trims user input*/
    private static String getUserInput() {
        String inputLine = input.nextLine();
        return inputLine.trim();
    }
    /**
     * Load file from txt storage file using FileManager class method
     * @throws IOException If file can't be loaded
     * @return taskList Array list of tasks containing all stored tasks
     */
    private static int loadTaskFromTXT() throws IOException, DukeException {
        int numOfTasks;
        currentTasks = FileManager.loadFile();
        numOfTasks = currentTasks.size();
        return numOfTasks;
    }

    public static void printSeparator() {
        String line = "__________________________________________________________________________________";
        System.out.println(line);
    }

    /*Function to get command from sentence and split sentence into 2*/
    private static String[] splitCommands(String userInput) {
        final String[] split = userInput.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[]{split[0], " "};
    }

    /**
     * Prints task confirmation message for deadline and event
     *
     * @param numOfTasks       Current total number of tasks
     * @param splitInfoAndDate String array containing description and date (Either deadline or event date)
     */
    private static void printTaskConfirmation(int numOfTasks, String[] splitInfoAndDate) {
        char type = currentTasks.get(numOfTasks).getType();
        switch (type) {
        /*Print confirmation for DEADLINE*/
        case 'D':
            System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks.get(numOfTasks).getType())
                    + addBrackets(currentTasks.get(numOfTasks).getStatusIcon()) + " " + splitInfoAndDate[0] + " (by: " +
                    splitInfoAndDate[1] + ")\n" + "Now you have " + (numOfTasks + 1) + (numOfTasks == 0 ? " task" : " tasks")
                    + " in the list");
            break;
        /*Print confirmation for EVENT*/
        case 'E':
            System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks.get(numOfTasks).getType())
                    + addBrackets(currentTasks.get(numOfTasks).getStatusIcon()) + " " + splitInfoAndDate[0] + " (at: " +
                    splitInfoAndDate[1] + ")\n" + "Now you have " + (numOfTasks + 1) + (numOfTasks == 0 ? " task" : " tasks")
                    + " in the list");
            break;
        }

    }

    /**
     * Prints task confirmation message for todo (Overloaded function with different parameters)
     *
     * @param numOfTasks  Current total number of tasks
     * @param description Task Description
     */
    private static void printTaskConfirmation(int numOfTasks, String description) {
        System.out.println("Got it. I've added this task: \n" + "  " + addBrackets(currentTasks.get(numOfTasks).getType())
                + addBrackets(currentTasks.get(numOfTasks).getStatusIcon()) + " " + description + "\n" + "Now you have "
                + (numOfTasks + 1) + (numOfTasks == 0 ? " task" : " tasks") + " in the list");
    }
    /**
     * Update task as Done and print edited task message
     *
     * @param taskNum specific task number
     */
    private static void updateAsDone(int taskNum) {
        currentTasks.get(taskNum - 1).setAsDone();
        printSeparator();
        System.out.println("Nice! I've marked this task as done: \n" + "[" +
                currentTasks.get(taskNum - 1).getStatusIcon() + "] " + currentTasks.get(taskNum - 1).getDescription());
        printSeparator();
    }
    /**
     * Update task as Done and print edited task message
     *
     * @param taskNum specific task number
     * @return numOfTasks Updated number of tasks after remove that specific entry
     */
    private static int removeFromArray(int numOfTasks, int taskNum) {
        printSeparator();
        System.out.println("Got it brother! I've removed this task: \n" + addBrackets(currentTasks.get(taskNum - 1).getType())
                + addBrackets(currentTasks.get(taskNum - 1).getStatusIcon()) +" "+ currentTasks.get(taskNum - 1).getDescription() +
                "\n Now you have " + (numOfTasks -1) + ((numOfTasks -1) < 2 ? " task" : " tasks")+" in the list.");

        currentTasks.remove(taskNum - 1);
        numOfTasks--;
        printSeparator();
        return numOfTasks;
    }

    private static void printHelpMessage() {
        System.out.println("Here are the range of commands:\n" +
                "1.todo\n Command used to record impending tasks\n" +
                " For \"todo\", kindly input in this format: *todo* *description*\n\n" +
                "2.deadline\n Command used to record tasks with deadlines\n" +
                " For \"deadline\", kindly input in this format: *deadline* " +
                "*description* */by* *date of deadline*\n" + " eg. deadline read book /by Sunday \n\n" +
                "3.event\n Command used to record tasks with events with dates\n" +
                " For \"event\", kindly input in this format: *event* " +
                "*description* */at* *date of event*\n" + " eg. event Google Hackathon /at 16/09/2020 \n\n" +
                "4.list\n Command used to list out all tasks at hand \n No additional info needed!\n\n" +
                "5.done\n Command used to mark task as done\n" +
                " For \"done\", kindly input task number behind\n" +
                " eg. done 2\n\n" +
                "6.delete\n Command used to delete specified task\n"+
                " eg. delete 2\n\n"+
                "7.bye\n Command used to exit program\n No additional info needed!");
    }
}
