package managers;

public class IdGenerator {

    private static long currentId;
    
    public static void setCurrencyId(long id){
        currentId = id;
    }
    
    public static long generateId(){
        return ++currentId;
    }

    public static void update(long id){

        if(id>=currentId)
            currentId=id+1;
    }
}