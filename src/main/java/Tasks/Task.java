package Tasks;

public class Task {
    protected String description;
    protected boolean isDone; //Status of completion
    protected char type; //Type of task


    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "\u2713" : "\u2718"); //return tick or X symbols
    }

    public String getDescription(){
        return(description);
    }
    public void setAsDone(){
        isDone = true;
    }

    public char getType(){
        return type;
    }
}