import Tasks.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UI {
    public static void printMatchesFound(ArrayList<Task> matchesFound, String type) {
        if (matchesFound.size() == 0) {
            printSeparator();
            System.out.println("No matches found! Better luck next time!");
        } else {
            printSeparator();
            String matchesFoundIntro = (type.equals("due")? "Here are the tasks due on the specified date!":
                    "Here are the match(es) found!");
            System.out.println(matchesFoundIntro);
            for (int i = 0; i < matchesFound.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + matchesFound.get(i).getDescription());
            }
        }
        printSeparator();
    }

    public static void printDeleteTaskMessage(int numOfTasks, int taskNum) {
        printSeparator();
        System.out.println("Got it brother! I've removed this task: \n" +
                TaskList.styleOutput(taskNum-1) + "\n Now you have " + (numOfTasks - 1) +
                ((numOfTasks - 1) < 2 ? " task" : " tasks") + " in the list.");
    }

    /**
     * Prints task confirmation message for todo (Overloaded function with different parameters)
     *
     * @param numOfTasks  Current total number of tasks
     * @param description Task Description
     */
    public static void printTaskConfirmation(int numOfTasks, String description) {
        System.out.println("Got it. I've added this task: \n" + "  "
                + styleTypeAndStatus(numOfTasks) + " " + description + "\n"
                + "Now you have " + (numOfTasks + 1) + (numOfTasks == 0 ? " task" : " tasks") + " in the list");
    }

    /*Add brackets to task type and status icon*/
    private static String styleTypeAndStatus(int numOfTasks) {
        return addBrackets(TaskList.currentTasks.get(numOfTasks).getType())
                + addBrackets(TaskList.currentTasks.get(numOfTasks).getStatusIcon());
    }

    /**
     * Prints task confirmation message for deadline and event
     *
     * @param numOfTasks       Current total number of tasks
     * @param splitInfoAndDate String array containing description and date (Either deadline or event date)
     */
    public static void printTaskConfirmation(int numOfTasks, String[] splitInfoAndDate, String reformattedDate) {
        char type = TaskList.currentTasks.get(numOfTasks).getType();
        switch (type) {
        /*Print confirmation for DEADLINE*/
        case 'D':
            System.out.println("Got it. I've added this task: \n" + "  "
                    + styleTypeAndStatus(numOfTasks) + " " + splitInfoAndDate[0]
                    + " (by: " + reformattedDate + ")\n" + "Now you have " + (numOfTasks + 1) +
                    (numOfTasks == 0 ? " task" : " tasks") + " in the list");
            break;
        /*Print confirmation for EVENT*/
        case 'E':
            System.out.println("Got it. I've added this task: \n" + "  " +
                    styleTypeAndStatus(numOfTasks) + " " + splitInfoAndDate[0] +
                    " (on: " + reformattedDate + ")\n" + "Now you have " + (numOfTasks + 1) +
                    (numOfTasks == 0 ? " task" : " tasks") + " in the list");
            break;
        }
    }

    public static void printHelpMessage() {
        System.out.println("Here are the range of commands:\n" +
                "1.todo\n Command used to record impending tasks\n" +
                " For \"todo\", kindly input in this format: *todo* *description*\n\n" +
                "2.deadline\n Command used to record tasks with deadlines\n" +
                " For \"deadline\", kindly input in this format: *deadline* " +
                "*description* */by* *date of deadline*\n" + " eg. deadline read book /by 18/10/2020 1800 \n\n" +
                "3.event\n Command used to record tasks with events with dates\n" +
                " For \"event\", kindly input in this format: *event* " +
                "*description* */on* *date of event*\n" + " eg. event Google Hackathon /on 28/10/2020 1000 \n\n" +
                "4.list\n Command used to list out all tasks at hand \n No additional info needed!\n\n" +
                "5.done\n Command used to mark task as done\n" +
                " For \"done\", kindly input task number behind\n" +
                " eg. done 2\n\n" +
                "6.delete\n Command used to delete specified task\n" +
                " eg. delete 2\n\n" +
                "7.find\n Command used to search for tasks that contains certain keywords\n" +
                " eg. find book\n\n" +
                "8.bye\n Command used to exit program\n No additional info needed!\n\n"+
                "9.help\n Command used to list all available commands\n No additional info needed!");
    }

    public static void printWelcomeMessage() {
        LocalDate todayDate = LocalDate.now();
        String formattedDate = todayDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        printSeparator();
        String logo = "           ____          ____       \n"
                + " __(\")__  |  _ \\ _   _  |  _ \\  ____ \n"
                + "(__   __) | | | | | | | | | | || ___\\\n"
                + "  /   \\   | |_| | |_| | | |_| |\\  __/\n"
                + " (_/ \\_)  |____/ \\__,_| \\____/  \\____|\n";
        System.out.println(logo);
        System.out.println("Sup! I'm Dude!\nWhat can a brother do for you?");
        System.out.println("Today is " + formattedDate);
        printSeparator();
    }

    /**
     * Surround text with brackets
     *
     * @param description String to be placed in between brackets
     * @return enclosedString String in between brackets
     */
    public static String addBrackets(String description) {
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
    public static String addBrackets(char description) {
        String enclosedString;
        enclosedString = "[" + description + "]";
        return enclosedString;
    }

    public static void printExitMessage() {
        printSeparator();
        System.out.println("Goodbye. Sad to see you leave!\n Hope to see you again soon!");
        printSeparator();
    }

    public static void printSeparator() {
        String line = "_____________________________________________________________________________________________";
        System.out.println(line);
    }
}
