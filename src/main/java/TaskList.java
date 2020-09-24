import Exceptions.DukeException;
import Tasks.Deadline;
import Tasks.Event;
import Tasks.Task;
import Tasks.ToDo;

import java.util.ArrayList;

public class TaskList {

    /*Delimiter Prefixes*/
    private static final String EVENT_QUALIFIER = "/at";
    private static final String DEADLINE_QUALIFIER = "/by";

    public static ArrayList<Task> currentTasks = new ArrayList<>();

    public TaskList(ArrayList<Task> loadUserList){
        this.currentTasks = loadUserList;
    }
    /**
     * Adds a Deadline task to the currentTasks Task array
     *
     * @param numOfTasks Number of tasks
     * @param userInput  User input
     * @return numOfTasks Updated number of tasks
     * @throws DukeException to catch error specified under DukeException
     */
    public static int commandDeadline(int numOfTasks, String userInput) throws DukeException{
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
            UI.printSeparator();
            UI.printTaskConfirmation(numOfTasks, splitInfoAndDeadline);
            UI.printSeparator();
            numOfTasks++;

        } catch (DukeException error) {
            UI.printSeparator();
            System.out.println(error.getMessage());
            UI.printSeparator();
        }
        return numOfTasks;
    }

    /**
     * Prints all tasks in task list
     *
     * @param numOfTasks Total number of tasks
     */
    public static void commandList(int numOfTasks) {
        try {
            UI.printSeparator();
            if (numOfTasks == 0) {
                System.out.println("You have zero task at hand!");
            }
            for (int i = 0; i < numOfTasks; i++) {
                System.out.println((i + 1) + "." + UI.addBrackets(currentTasks.get(i).getType()) +
                        UI.addBrackets(currentTasks.get(i).getStatusIcon()) + " " + currentTasks.get(i).getDescription());
            }
            UI.printSeparator();
        } catch (ArrayIndexOutOfBoundsException error) {
            UI.printSeparator();
            System.out.println("Array index out of bounds detected!\n");
            UI.printSeparator();
        }
    }

    /**
     * Adds a ToDo task to the currentTasks Task array
     *
     * @param numOfTasks     Number of tasks
     * @param splitUserInput User input
     * @return numOfTasks Updated number of tasks
     * @throws DukeException to catch error specified under DukeException
     */
    public static int commandToDo(int numOfTasks, String splitUserInput) throws DukeException {
        try {
            if (splitUserInput.isBlank()) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            ToDo newTodo = new ToDo(splitUserInput);
            currentTasks.add(newTodo);
            UI.printSeparator();
            UI.printTaskConfirmation(numOfTasks, splitUserInput);
            numOfTasks++;
            UI.printSeparator();

        } catch (DukeException error) {
            UI.printSeparator();
            System.out.println(error.getMessage());
            UI.printSeparator();
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
    public static int commandEvent(int numOfTasks, String userInput) throws DukeException {
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
            UI.printSeparator();
            UI.printTaskConfirmation(numOfTasks, splitInfoAndDate);
            numOfTasks++;
            UI.printSeparator();
        } catch (DukeException error) {
            UI.printSeparator();
            System.out.println(error.getMessage());
            UI.printSeparator();
        }
        return numOfTasks;
    }

    /**
     * Mark task as done
     *
     * @param numOfTasks total number of tasks
     * @param number     task number
     * @throws NumberFormatException specified task number is not recognised eg not a number/ is empty
     */
    public static void commandDone(int numOfTasks, String number) {
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
            UI.printSeparator();
            System.out.println(error.getMessage());
            UI.printSeparator();
        } catch (NumberFormatException numberError) {
            UI.printSeparator();
            System.out.println("Gosh you did not key in a valid number");
            UI.printSeparator();
        }
    }
    public static ArrayList<Task> commandFind(String stringToSearch){
        ArrayList<Task> resultArray = new ArrayList<>();
        for(int i=0; i<currentTasks.size(); i++){
            boolean isFound = currentTasks.get(i).getDescription().toLowerCase().contains(stringToSearch);
            if(isFound){
                resultArray.add(currentTasks.get(i));
            }
        }
        return resultArray;
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
    public static int commandDelete(int numOfTasks, String number) throws DukeException {
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
            UI.printSeparator();
            System.out.println(error.getMessage());
            UI.printSeparator();
        } catch (NumberFormatException numberError) {
            UI.printSeparator();
            System.out.println("Gosh you did not key in a valid number");
            UI.printSeparator();
        }
        return numOfTasks;
    }

    static void commandHelp() {
        UI.printSeparator();
        UI.printHelpMessage();
        UI.printSeparator();
    }

    /**
     * Update task as Done and print edited task message
     *
     * @param taskNum specific task number
     * @return numOfTasks Updated number of tasks after remove that specific entry
     */
    public static int removeFromArray(int numOfTasks, int taskNum) {
        UI.printDeleteTaskMessage(numOfTasks, taskNum);
        currentTasks.remove(taskNum - 1);
        numOfTasks--;
        UI.printSeparator();
        return numOfTasks;
    }

    /**
     * Update task as Done and print edited task message
     *
     * @param taskNum specific task number
     */
    private static void updateAsDone(int taskNum) {
        currentTasks.get(taskNum - 1).setAsDone();
        UI.printSeparator();
        System.out.println("Nice! I've marked this task as done: \n" + "[" +
                currentTasks.get(taskNum - 1).getStatusIcon() + "] " + currentTasks.get(taskNum - 1).getDescription());
        UI.printSeparator();
    }

    public int getSizeOfTaskList(){
        int numOfTasks = currentTasks.size();
        return numOfTasks;
    }
}
