package managers;

import models.HumanBeing;
import managers.CollectionManager;

public class IdGenerator {

    private final CollectionManager manager = new CollectionManager();
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