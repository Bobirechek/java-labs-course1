package enums;

public enum ItemStatus{
    SKETCH("sketch"),
    PLAN("plan"),
    LUNIT("lunit is found"),
    NOLUNIT("no lunit"),
    GRAVITY("gravity control"),
    ZEROGRAVITY("zero gravity"),
    LOCATION("location"),
    ELEMENT("element");

    public final String condition;

    ItemStatus(String status) {
        this.condition = status;

    }
    @Override
    public String toString(){
        return this.condition;
    }
}
