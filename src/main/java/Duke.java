import Exceptions.DukeException;
import FileStorage.FileManager;

import java.io.IOException;

public class Duke {
    public static final String FILE_NAME = "fileStorage.txt";
    public static final String FILE_DIR = "data";

    private static TaskList currentTasks;
    private static UI userInterface;
    private static FileManager fileManager;

    public Duke() {
        userInterface = new UI();
        fileManager = new FileManager(FILE_NAME, FILE_DIR);
        try {
            currentTasks = new TaskList(fileManager.loadFile());
        } catch (DukeException exception) {
            UI.printSeparator();
            System.out.println(exception.getMessage());
            UI.printSeparator();
        }
    }

    public static void main(String[] args) throws IOException, DukeException {
        new Duke();
        run();
    }

    private static void run() throws DukeException, IOException {
        boolean isOngoing = true;
        int numOfTasks = currentTasks.getSizeOfTaskList();
        userInterface.printWelcomeMessage();
        Parser.executeCommands(numOfTasks, isOngoing);
    }
}
