package enums;

public enum PersonStatus {
    TALK("говорит"),
    DOING("делает"),
    MOVE("передвигается"),
    ALIVE("существует");

    public final String status;

    PersonStatus(String status){
        this.status = status;

    }
    @Override
    public String toString(){
        return this.status;
    }
}
