package Tasks;

public class Event extends Task {

    protected String on;

    public Event(String description, String on) {
        super(description);
        this.type = 'E';
        this.on = on;
    }

    @Override
    public String getDescription() {
        return (super.getDescription() + " (on: " + on + ")");
    }

    public char getType() {
        return type;
    }

    public String getOn() {
        return on;
    }
}
