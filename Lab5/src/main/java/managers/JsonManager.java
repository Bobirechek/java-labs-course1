package managers;

import models.*;

import java.time.LocalDateTime;
import java.util.Vector;

public class JsonManager {

    public static String toJson(Vector<HumanBeing> collection){

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        for(HumanBeing h:collection){

            json.append("{");

            json.append("\"id\":").append(h.getId()).append(",");
            json.append("\"name\":\"").append(h.getName()).append("\",");
            json.append("\"impactSpeed\":").append(h.getImpactSpeed()).append(",");
            json.append("\"soundtrackName\":\"")
                    .append(h.getSoundtrackName()).append("\"");

            json.append("},\n");
        }

        json.append("]");

        return json.toString();
    }

    public static Vector<HumanBeing> parse(String json){

        Vector<HumanBeing> list = new Vector<>();

        if(json==null || json.isEmpty()) return list;

        String[] objects = json.split("\\{");

        for(String obj:objects){

            if(!obj.contains("id")) continue;

            Long id = Long.parseLong(
                    obj.split("\"id\":")[1].split(",")[0]);

            String name = obj.split("\"name\":\"")[1]
                    .split("\"")[0];

            double speed = Double.parseDouble(
                    obj.split("\"impactSpeed\":")[1].split(",")[0]);

            String soundtrack = obj.split("\"soundtrackName\":\"")[1]
                    .split("\"")[0];

            HumanBeing h = new HumanBeing.Builder()
                    .id(id)
                    .name(name)
                    .impactSpeed(speed)
                    .soundtrackName(soundtrack)
                    .creationDate(LocalDateTime.now())
                    .build();

            IdGenerator.update(id);

            list.add(h);
        }

        return list;
    }
}