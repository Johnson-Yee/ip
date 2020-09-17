package Tasks;

public class Deadline extends Task {

    protected String by;

    public Deadline(String description, String by){
        super(description);
        this.by = by;
        this.type = 'D';
    }

    @Override
    public String getDescription(){
        return (super.getDescription() + " (by: " + by +")");
    }


    public char getType(){
        return type;
    }

    public String getBy(){ return by;}
}
