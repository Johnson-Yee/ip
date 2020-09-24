import Exceptions.DukeException;
import Tasks.Deadline;
import Tasks.Event;
import Tasks.Task;
import Tasks.ToDo;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class TaskList {

    /*Delimiter Prefixes*/
    private static final String EVENT_QUALIFIER = "/on";
    private static final String DEADLINE_QUALIFIER = "/by";

    public static ArrayList<Task> currentTasks = new ArrayList<>();

    public TaskList(ArrayList<Task> loadUserList) {
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
    public static int commandDeadline(int numOfTasks, String userInput) throws DukeException {
        try {
            /*If user input contains only one word*/
            if (userInput.length() < 1) {
                throw new DukeException("MISSING_DESCRIPTION");
            }
            /*Missing qualifier*/
            if (!userInput.contains(DEADLINE_QUALIFIER)) {
                throw new DukeException("MISSING_QUALIFIER");
            }
            String[] splitInfoAndDeadline = userInput.trim().split(DEADLINE_QUALIFIER, 2);
            String deadlineDescription = splitInfoAndDeadline[0];
            String deadlineDate = formatDate(splitInfoAndDeadline[1].trim());
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
            UI.printTaskConfirmation(numOfTasks, splitInfoAndDeadline, deadlineDate);
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
            if (!userInput.contains(EVENT_QUALIFIER)) {
                throw new DukeException("MISSING_QUALIFIER");
            }
            /*Split into 2 sections with index 0 being the Description and index 1 being event date*/
            String[] splitInfoAndDate = userInput.trim().split(EVENT_QUALIFIER, 2);
            String eventDescription = splitInfoAndDate[0];
            String eventDate = formatDate(splitInfoAndDate[1].trim());

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
            UI.printTaskConfirmation(numOfTasks, splitInfoAndDate, eventDate);
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

    public static ArrayList<Task> commandFind(String stringToSearch) {
        ArrayList<Task> resultArray = new ArrayList<>();
        for (int i = 0; i < currentTasks.size(); i++) {
            boolean isFound = currentTasks.get(i).getDescription().toLowerCase().contains(stringToSearch);
            if (isFound) {
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

    /**
     * Reformats date accordingly
     *
     * @param rawDate user input for date of deadline/event
     * @return finalEditedDate Returns reformatted date
     * @throws DukeException to catch error specified under DukeException
     */
    private static String formatDate(String rawDate) throws DukeException {
        String finalEditedDate = new String();
        try {
            //Contains both date and time
            if (rawDate.trim().length() > 12) {
                finalEditedDate = reformatDateAndTime(rawDate);

            } else {
                finalEditedDate = reformatDate(rawDate);
            }
        } catch (DukeException e) {
            UI.printSeparator();
            e.getMessage();
            UI.printSeparator();
        } catch (DateTimeException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new DukeException("INVALID_DATE");
        }
        return finalEditedDate;
    }

    /**
     * Reformats date from DD-MM-YYYY to MMM DD YYYY
     *
     * @param rawDate user input for date of deadline/event
     * @return finalEditedDate Returns reformatted date eg OCT 12 2020
     * @throws DukeException to catch error specified under DukeException
     */
    private static String reformatDate(String rawDate) throws DukeException {
        String intermediateDate;
        String finalEditedDate;
        LocalDate date;
        LocalDate todayDate = LocalDate.now();
        intermediateDate = processDate(rawDate.trim());
        String[] dateComponents = intermediateDate.split("-");
        int year = Integer.parseInt(dateComponents[2]);
        int month = Integer.parseInt(dateComponents[1]);
        int day = Integer.parseInt(dateComponents[0]);
        date = LocalDate.of(year, month, day);
        if (date.isBefore(todayDate)) {
            throw new DukeException("SET_DATE_FAIL");
        }
        finalEditedDate = date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        return finalEditedDate;
    }

    /**
     * Reformats date and time from DD-MM-YYYY HH:mm to MMM DD YYYY HH:mm
     *
     * @param rawDateAndTime user input for date of deadline/event
     * @return finalEditedDate Returns reformatted date eg OCT 12 2020 18:00
     * @throws DukeException to catch error specified under DukeException
     */
    private static String reformatDateAndTime(String rawDateAndTime) throws DukeException {
        String intermediateDate;
        String intermediateTime;
        String finalEditedDate;
        LocalDateTime dateTime;
        LocalDateTime todayDateTime = LocalDateTime.now();

        String[] splitDateAndTime = rawDateAndTime.trim().split(" ", 2);
        intermediateDate = processDate(splitDateAndTime[0].trim());
        String[] dateComponents = intermediateDate.split("-");
        int year = Integer.parseInt(dateComponents[2]);
        int month = Integer.parseInt(dateComponents[1]);
        int day = Integer.parseInt(dateComponents[0]);

        intermediateTime = processTime(splitDateAndTime[1].trim());
        String[] timeComponents = intermediateTime.split(":");
        int hour = Integer.parseInt(timeComponents[0]);
        int min = Integer.parseInt(timeComponents[1]);

        dateTime = LocalDateTime.of(year, month, day, hour, min);
        if (dateTime.isBefore(todayDateTime)) {
            throw new DukeException("SET_DATE_FAIL");
        }
        finalEditedDate = dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"));
        return finalEditedDate;
    }

    /*Replace all occurrences of / or . in date given by user.*/
    private static String processDate(String originalDate) {
        String processedDate = originalDate.replace('/', '-').replace('.', '-');
        return processedDate;
    }

    /*Replace all occurrences of . in time given by user. Additionally, add a ':' when users do not include it*/
    private static String processTime(String originalTime) {
        String processedTime;
        if (!originalTime.contains(":")) {
            processedTime = addCharAtIndex(originalTime, ':', 2);
        } else {
            processedTime = originalTime.replace('.', ':');
        }
        return processedTime;
    }

    public static String addCharAtIndex(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }

    public int getSizeOfTaskList() {
        return currentTasks.size();
    }
}
