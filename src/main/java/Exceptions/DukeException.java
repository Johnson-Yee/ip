package Exceptions;

public class DukeException extends Exception {
    public static final String MISSING_DESCRIPTION = "Hey! The description cannot be empty";
    public static final String UNINTELLIGIBLE_COMMAND = "Sorry! You have entered an invalid command!\n" +
            " For more help, type \"help\"";
    public static final String MISSING_QUALIFIER = "Hey!You are missing /by or /on in your input";
    public static final String MISSING_INFO = "You forgot the event date/deadline";
    public static final String MISSING_NUMBER = "You did not key in the task number that you want to mark";
    public static final String OUT_OF_RANGE = "The number you have keyed is out of the range!";
    public static final String CREATE_FILE_FAIL = "Failed to create file! Can't help you on this brother!";
    public static final String WRITE_FILE_FAIL = "Sorry to inform you! Failed to write file!";
    public static final String FETCH_FILE_FAIL = "Sorry to inform you! Failed to fetch file!";
    public static final String SET_DATE_FAIL = "You have tried to set a date that is before today. Dude.java is not" +
            " advanced enough to help you time-travel";
    public static final String INVALID_DATE = "Sorry, the date you have provided is not accepted.\n Kindly follow " +
            "this format: DD/MM/YYYY or DD/MM/YYYY HH:mm eg 28/10/2020 18:00";

    public String errorMessage;

    public DukeException(String error) {
        switch (error) {
        case "MISSING_DESCRIPTION":
            this.errorMessage = MISSING_DESCRIPTION;
            break;
        case "UNINTELLIGIBLE_COMMAND":
            this.errorMessage = UNINTELLIGIBLE_COMMAND;
            break;
        case "MISSING_QUALIFIER":
            this.errorMessage = MISSING_QUALIFIER;
            break;
        case "MISSING_INFO":
            this.errorMessage = MISSING_INFO;
            break;
        case "MISSING_NUMBER":
            this.errorMessage = MISSING_NUMBER;
            break;
        case "OUT_OF_RANGE":
            this.errorMessage = OUT_OF_RANGE;
            break;
        case "CREATE_FILE_FAIL":
            this.errorMessage = CREATE_FILE_FAIL;
            break;
        case "WRITE_FILE_FAIL":
            this.errorMessage = WRITE_FILE_FAIL;
            break;
        case "FETCH_FILE_FAIL":
            this.errorMessage = FETCH_FILE_FAIL;
            break;
        case "SET_DATE_FAIL":
            this.errorMessage = SET_DATE_FAIL;
            break;
        case "INVALID_DATE":
            this.errorMessage = INVALID_DATE;
            break;
        }
    }

    public String getMessage() {
        return errorMessage;
    }
}
