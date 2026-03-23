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

            json.append("\"coordinates\":{");
            json.append("\"x\":").append(h.getCoordinates().getX()).append(",");
            json.append("\"y\":").append(h.getCoordinates().getY());
            json.append("},");

            json.append("\"realHero\":").append(h.isRealHero()).append(",");
            json.append("\"hasToothpick\":").append(h.getHasToothpick()).append(",");
            json.append("\"impactSpeed\":").append(h.getImpactSpeed()).append(",");
            json.append("\"soundtrackName\":\"").append(h.getSoundtrackName()).append("\",");

            json.append("\"weaponType\":")
                    .append(h.getWeaponType()==null ? "null" : "\"" + h.getWeaponType() + "\"")
                    .append(",");

            json.append("\"mood\":")
                    .append(h.getMood()==null ? "null" : "\"" + h.getMood() + "\"")
                    .append(",");

            if(h.getCar()!=null){

                json.append("\"car\":{");

                json.append("\"name\":")
                        .append(h.getCar().getName()==null ? "null" : "\"" + h.getCar().getName() + "\"")
                        .append(",");

                json.append("\"cool\":")
                        .append(h.getCar().getCool());

                json.append("}");

            }else{

                json.append("\"car\":null");
            }

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

            if(!obj.contains("name")) continue;

            HumanBeing human = parseHuman("{"+obj);

            if(human!=null){
                list.add(human);
            }
        }

        return list;
    }



    public static HumanBeing parseHuman(String json){

        try{

            // ===== REQUIRED FIELDS =====

            if(!json.contains("\"name\"")){
                System.out.println("Error: required field 'name' is missing");
                return null;
            }

            if(!json.contains("\"coordinates\"")){
                System.out.println("Error: required field 'coordinates' is missing");
                return null;
            }

            if(!json.contains("\"impactSpeed\"")){
                System.out.println("Error: required field 'impactSpeed' is missing");
                return null;
            }

            if(!json.contains("\"soundtrackName\"")){
                System.out.println("Error: required field 'soundtrackName' is missing");
                return null;
            }



            // ===== NAME =====

            String name = json.split("\"name\":\"")[1].split("\"")[0];



            // ===== COORDINATES =====

            long x = Long.parseLong(
                    json.split("\"x\":")[1].split(",")[0]);

            long y = Long.parseLong(
                    json.split("\"y\":")[1].split("}")[0]);

            Coordinates coordinates = new Coordinates(x,y);



            // ===== REAL HERO (optional) =====

            boolean realHero = false;

            if(json.contains("\"realHero\":")){

                String value = json.split("\"realHero\":")[1].split(",")[0];

                realHero = Boolean.parseBoolean(value);
            }



            // ===== HAS TOOTHPICK (optional) =====

            Boolean hasToothpick = null;

            if(json.contains("\"hasToothpick\":")){

                String value = json.split("\"hasToothpick\":")[1].split(",")[0];

                if(!value.equals("null")){
                    hasToothpick = Boolean.parseBoolean(value);
                }
            }



            // ===== IMPACT SPEED =====

            double impactSpeed = Double.parseDouble(
                    json.split("\"impactSpeed\":")[1].split(",")[0]);

            if(impactSpeed>597){
                System.out.println("Error: impactSpeed cannot be greater than 597");
                return null;
            }



            // ===== SOUNDTRACK =====

            String soundtrack = json.split("\"soundtrackName\":\"")[1]
                    .split("\"")[0];



            // ===== WEAPON TYPE (optional) =====

            WeaponType weaponType = null;

            if(json.contains("\"weaponType\":\"")){

                try{

                    String weapon = json.split("\"weaponType\":\"")[1]
                            .split("\"")[0];

                    weaponType = WeaponType.valueOf(weapon);

                }catch (Exception e){

                    System.out.println("Error: invalid weaponType");
                    return null;
                }
            }



            // ===== MOOD (optional) =====

            Mood mood = null;

            if(json.contains("\"mood\":\"")){

                try{

                    String moodStr = json.split("\"mood\":\"")[1]
                            .split("\"")[0];

                    mood = Mood.valueOf(moodStr);

                }catch (Exception e){

                    System.out.println("Error: invalid mood");
                    return null;
                }
            }



            // ===== CAR (optional) =====

            Car car = null;

            if(json.contains("\"car\":{")){

                String carName = null;

                if(json.split("\"name\":\"").length>2){
                    carName = json.split("\"name\":\"")[2].split("\"")[0];
                }

                Boolean cool = null;

                if(json.contains("\"cool\":")){
                    cool = Boolean.parseBoolean(
                            json.split("\"cool\":")[1].split("}")[0]);
                }

                car = new Car(carName,cool);
            }



            // ===== OBJECT CREATION =====

            return new HumanBeing.Builder()
                    .id(IdGenerator.generateId())
                    .name(name)
                    .coordinates(coordinates)
                    .creationDate(LocalDateTime.now())
                    .realHero(realHero)
                    .hasToothpick(hasToothpick)
                    .impactSpeed(impactSpeed)
                    .soundtrackName(soundtrack)
                    .weaponType(weaponType)
                    .mood(mood)
                    .car(car)
                    .build();

        }
        catch (Exception e){

            System.out.println("Error: invalid JSON object format");

            return null;
        }
    }
}