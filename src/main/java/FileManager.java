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
    private static File savedFile;
    static final String SAVE_PATH = "data/fileStorage.txt";
    public static ArrayList<Task> tasksList = new ArrayList<>();

    public FileManager(File userFile){
        this.savedFile = userFile;
    }
    /**
     * Saves all tasks into a text file in a specified format
     * @param tasksList array list of current taks
     * @throws DukeException If file can't be created
     */
    public static void saveFile(ArrayList<Task> tasksList) throws DukeException, IOException {
        File savedFile = new File(SAVE_PATH);
        try {
            if(!savedFile.exists()){
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
     * @param taskList array list of current taks
     * @throws IOException If file can't be created
     * @throws DukeException If file can't be created
     */
    private static void saveToFile(ArrayList<Task> taskList) throws DukeException, IOException {
        File savedFile = new File(SAVE_PATH);
        FileWriter writer = new FileWriter(savedFile);
        convertToSaveFormat(taskList, writer);
    }
    /**
     * Converts all tasks to a  specified format
     * @param taskList array list of current taks
     * @throws IOException If file can't be created
     */
    private static void convertToSaveFormat(ArrayList<Task> taskList, FileWriter writer) throws DukeException {
        try {
            for(int i = 0; i< taskList.size(); i++){
                Task currTask = taskList.get(i);
                String textFormat = currTask.getType() + " | " +
                        (currTask.getIsDone()?"1":"0") +" | " + currTask.getRawDescription();
                if(currTask instanceof Event){
                    textFormat = textFormat + "|" + ((Event)currTask).getAt();
                }else if(currTask instanceof Deadline){
                    textFormat = textFormat + "|" + ((Deadline)currTask).getBy();
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
     * @throws IOException If file can't be loaded/found
     * @return taskList Array list of tasks containing all stored tasks
     */
    public static ArrayList<Task> loadFile() throws IOException, DukeException {
        try {
            File savedFile = new File(SAVE_PATH);
            if(savedFile.exists()){
                loadFromExistingFile();
            }else{
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
     * @throws IOException If file can't be loaded
     * @return taskList Array list of tasks containing all stored tasks
     */
    private static ArrayList<Task> loadFromExistingFile() throws IOException {
        File savedFile = new File(SAVE_PATH);
        Scanner fileScanner = new Scanner(savedFile);
        while(fileScanner.hasNext()){
            String currLine = fileScanner.nextLine();
            String[] splitString = currLine.trim().split("\\|");
            String taskType = splitString[0].trim();
            switch (taskType){
            case "D":
                String deadlineDescription = splitString[2].trim();
                String by = splitString[3].trim();
                Boolean hasCompletedDeadline = (splitString[1].trim().equals("1"));
                Deadline newDeadline = new Deadline(deadlineDescription,by);
                if(hasCompletedDeadline){
                    newDeadline.setAsDone();
                }
                tasksList.add(newDeadline);
                break;
            case "T":
                String toDoDescription = splitString[2].trim();
                ToDo newToDo = new ToDo(toDoDescription);
                Boolean hasCompletedToDo = (splitString[1].trim().equals("1"));
                if(hasCompletedToDo){
                    newToDo.setAsDone();
                }
                tasksList.add(newToDo);

                break;
            case "E":
                String eventDescription = splitString[2].trim();
                String at = splitString[3].trim();
                Boolean hasCompletedEvent = (splitString[1].trim().equals("1"));
                Event newEvent = new Event(eventDescription,at);
                if(hasCompletedEvent){
                    newEvent.setAsDone();
                }
                tasksList.add(newEvent);
                break;
            }
        }
        return tasksList;
    }
}
