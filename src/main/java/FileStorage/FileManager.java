package FileStorage;

import Exceptions.DukeException;
import Tasks.Deadline;
import Tasks.Event;
import Tasks.Task;
import Tasks.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {

    public static String relativePath;
    public static ArrayList<Task> tasksList = new ArrayList<>();

    public FileManager(String fileName, String fileDir) {
        relativePath = fileDir + '/' + fileName;
    }

    /**
     * Saves all tasks into a text file in a specified format
     *
     * @param tasksList array list of current task
     * @throws DukeException If file can't be created
     */
    public void saveFile(ArrayList<Task> tasksList) throws DukeException, IOException {
        File savedFile = new File(relativePath);
        try {
            if (!savedFile.exists()) {
                savedFile.getParentFile().mkdirs();
                savedFile.createNewFile();
            }
        } catch (IOException error) {
            throw new DukeException("CREATE_FILE_FAIL");
        }
        saveToFile(tasksList);
    }

    /**
     * Saves all tasks in a specified format
     *
     * @param taskList array list of current task
     * @throws IOException   If file can't be created
     * @throws DukeException If file can't be created
     */
    private void saveToFile(ArrayList<Task> taskList) throws DukeException, IOException {
        File savedFile = new File(relativePath);
        FileWriter writer = new FileWriter(savedFile);
        convertToSaveFormat(taskList, writer);
    }

    /**
     * Converts all tasks to a  specified format
     *
     * @param taskList array list of current task
     * @throws DukeException file can't be created
     */
    private void convertToSaveFormat(ArrayList<Task> taskList, FileWriter writer) throws DukeException {
        try {
            for (int i = 0; i < taskList.size(); i++) {
                Task currTask = taskList.get(i);
                String textFormat = currTask.getType() + " | " +
                        (currTask.getIsDone() ? "1" : "0") + " | " + currTask.getRawDescription();
                if (currTask instanceof Event) {
                    textFormat = textFormat + " |" + ((Event) currTask).getOn();
                } else if (currTask instanceof Deadline) {
                    textFormat = textFormat + " |" + ((Deadline) currTask).getBy();
                }
                writer.write(textFormat + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new DukeException("WRITE_FILE_FAIL");
        }
    }

    /**
     * Load stored TXT file by calling on private loadFromExistingFile function
     *
     * @return taskList Array list of tasks containing all stored tasks
     */
    public ArrayList<Task> loadFile() throws DukeException {
        try {
            File savedFile = new File(relativePath);
            if (savedFile.exists()) {
                tasksList = loadFromExistingFile();
            } else {
                savedFile.getParentFile().mkdirs();
                savedFile.createNewFile();
            }
        } catch (IOException e) {
            throw new DukeException("FETCH_FILE_FAIL");
        }
        return tasksList;
    }

    /**
     * Private function to load file from txt storage file
     *
     * @return taskList Array list of tasks containing all stored tasks
     * @throws IOException If file can't be loaded
     */
    private ArrayList<Task> loadFromExistingFile() throws IOException {
        File savedFile = new File(relativePath);
        Scanner fileScanner = new Scanner(savedFile);
        while (fileScanner.hasNext()) {
            String currLine = fileScanner.nextLine();
            String[] splitString = currLine.trim().split("\\|");
            String taskType = splitString[0].trim();
            switch (taskType) {
            case "D":
                String deadlineDescription = splitString[2].trim();
                String by = splitString[3].trim();
                boolean hasCompletedDeadline = (splitString[1].trim().equals("1"));
                Deadline newDeadline = new Deadline(deadlineDescription, by);
                if (hasCompletedDeadline) {
                    newDeadline.setAsDone();
                }
                tasksList.add(newDeadline);
                break;
            case "T":
                String toDoDescription = splitString[2].trim();
                ToDo newToDo = new ToDo(toDoDescription);
                boolean hasCompletedToDo = (splitString[1].trim().equals("1"));
                if (hasCompletedToDo) {
                    newToDo.setAsDone();
                }
                tasksList.add(newToDo);

                break;
            case "E":
                String eventDescription = splitString[2].trim();
                String on = splitString[3].trim();
                boolean hasCompletedEvent = (splitString[1].trim().equals("1"));
                Event newEvent = new Event(eventDescription, on);
                if (hasCompletedEvent) {
                    newEvent.setAsDone();
                }
                tasksList.add(newEvent);
                break;
            }
        }
        return tasksList;
    }
}
