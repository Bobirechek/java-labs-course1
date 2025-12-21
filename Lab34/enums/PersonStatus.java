package enums;

public enum PersonStatus {
    SIMPLE("Simple"),
    GROUP("Group");


    public final String status;

    PersonStatus(String status){
        this.status = status;

    }
    @Override
    public String toString(){
        return this.status;
    }
}
