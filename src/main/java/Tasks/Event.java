package Tasks;

public class Event extends Task{

    protected String at;

    public Event(String description, String at) {
        super(description);
        this.type = 'E';
        this.at = at;
    }

    @Override
    public String getDescription(){
        return (super.getDescription() + " (at: " + at +")");
    }

    public char getType(){
        return type;
    }

    public String getAt(){
        return at;
    }
}
