package enums;

public enum ItemStatus{
    SKETCH("эскиз"),
    PLAN("чертеж"),
    LUNIT("линит найден"),
    NOLUNIT("нет лунита"),
    DUALCONTROL("двоякое управление"),
    SINGLECONTROL("одиночное управление"),
    GRAVITY("gravity control"),
    ZEROGRAVITY("zero gravity"),
    LOCATION("локация"),
    ELEMENT("элемент");

    public final String condition;

    ItemStatus(String status) {
        this.condition = status;

    }
    @Override
    public String toString(){
        return this.condition;
    }
}
