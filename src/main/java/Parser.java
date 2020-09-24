import Exceptions.DukeException;
import FileStorage.FileManager;
import Tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    /*Command Prefixes*/
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_DONE = "done";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";

    /*External classes used*/
    private static final Scanner input = new Scanner(System.in);

    /**
     * Execute commands as intended by user
     *
     * @param numOfTasks Number of tasks
     * @param isOngoing  Whether the program has yet to be exited
     * @throws DukeException to catch error specified under DukeException
     */
    public static void executeCommands(int numOfTasks, boolean isOngoing) throws DukeException, IOException {
        String userInput;
        while (isOngoing) {
            FileManager fileManager = new FileManager(Duke.FILE_NAME, Duke.FILE_DIR);

            userInput = getTrimmedUserInput();
            String[] splitUserInput = splitCommands(userInput);
            String userCommand = splitUserInput[0].toLowerCase();
            switch (userCommand) {
            case COMMAND_BYE:
                fileManager.saveFile(TaskList.currentTasks);
                UI.printExitMessage();
                isOngoing = false;
                break;
            case COMMAND_LIST:
                TaskList.commandList(numOfTasks);
                break;
            case COMMAND_DONE:
                TaskList.commandDone(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_DEADLINE:
                numOfTasks = TaskList.commandDeadline(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_TODO:
                numOfTasks = TaskList.commandToDo(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_EVENT:
                numOfTasks = TaskList.commandEvent(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_DELETE:
                numOfTasks = TaskList.commandDelete(numOfTasks, splitUserInput[1]);
                break;
            case COMMAND_FIND:
                ArrayList<Task> matchesFound = TaskList.commandFind(splitUserInput[1].toLowerCase());
                UI.printMatchesFound(matchesFound);
                break;
            case COMMAND_HELP:
                TaskList.commandHelp();
                break;
            default:
                try {
                    throw new DukeException("UNINTELLIGIBLE_COMMAND");
                } catch (DukeException exception) {
                    UI.printSeparator();
                    System.out.println(exception.getMessage());
                    UI.printSeparator();
                }
            }
        }
    }

    /*Function to get command from sentence and split sentence into 2*/
    private static String[] splitCommands(String userInput) {
        final String[] split = userInput.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[]{split[0], " "};
    }

    private static String getTrimmedUserInput() {
        String inputLine = input.nextLine();
        return inputLine.trim();
    }
}
