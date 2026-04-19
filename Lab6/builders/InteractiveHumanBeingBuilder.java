package builders;

import managers.IdGenerator;
import managers.ScriptManager;
import models.*;

import java.time.LocalDateTime;
import java.util.Scanner;

public class InteractiveHumanBeingBuilder implements HumanBeingBuilder {

    private Scanner scanner() {
        return ScriptManager.getScanner();
    }

    @Override
    public HumanBeing build() {

        String name = readName();
        Coordinates coordinates = readCoordinates();
        boolean realHero = readRealHero();
        Boolean hasToothpick = readHasToothpick();
        double impactSpeed = readImpactSpeed();
        String soundtrackName = readSoundtrackName();
        WeaponType weaponType = readWeaponType();
        Mood mood = readMood();
        Car car = readCar();

        return new HumanBeing.Builder()
                .id(IdGenerator.generateId())
                .name(name)
                .coordinates(coordinates)
                .creationDate(LocalDateTime.now())
                .realHero(realHero)
                .hasToothpick(hasToothpick)
                .impactSpeed(impactSpeed)
                .soundtrackName(soundtrackName)
                .weaponType(weaponType)
                .mood(mood)
                .car(car)
                .build();
    }

    private String readName() {

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter name:");

            String input = scanner().nextLine().trim();

            if (input.isEmpty()) {

                String error = "Error: name cannot be empty.";

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(error);

                System.out.println(error);
                continue;
            }

            return input;
        }
    }

    private Coordinates readCoordinates() {

        long x;
        long y;

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter coordinate X:");

            String input = scanner().nextLine();

            try {
                x = Long.parseLong(input);
                break;
            } catch (NumberFormatException e) {

                String error = "Error: X must be a long number.";

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(error);

                System.out.println(error);
            }
        }

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter coordinate Y:");

            String input = scanner().nextLine();

            try {
                y = Long.parseLong(input);
                break;
            } catch (NumberFormatException e) {

                String error = "Error: Y must be a long number.";

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(error);

                System.out.println(error);
            }
        }

        return new Coordinates(x, y);
    }

    private boolean readRealHero() {

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter realHero (true/false):");

            String input = scanner().nextLine().trim();

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
                return Boolean.parseBoolean(input);

            String error = "Error: realHero must be true or false.";

            if (ScriptManager.isScriptMode())
                throw new IllegalArgumentException(error);

            System.out.println(error);
        }
    }

    private Boolean readHasToothpick() {

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter hasToothpick (true/false) or empty:");

            String input = scanner().nextLine().trim();

            if (input.isEmpty())
                return null;

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
                return Boolean.parseBoolean(input);

            String error = "Error: hasToothpick must be true/false or empty.";

            if (ScriptManager.isScriptMode())
                throw new IllegalArgumentException(error);

            System.out.println(error);
        }
    }

    private double readImpactSpeed() {

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter impactSpeed (<=597):");

            String input = scanner().nextLine();

            try {

                double value = Double.parseDouble(input);

                if (value > 597)
                    throw new IllegalArgumentException("Error: impactSpeed must be <= 597");

                return value;

            } catch (Exception e) {

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(e.getMessage());

                System.out.println(e.getMessage());
            }
        }
    }

    private String readSoundtrackName() {

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter soundtrackName:");

            String input = scanner().nextLine().trim();

            if (input.isEmpty()) {

                String error = "Error: soundtrackName cannot be empty.";

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(error);

                System.out.println(error);
                continue;
            }

            return input;
        }
    }

    private WeaponType readWeaponType() {

        while (true) {

            if (!ScriptManager.isScriptMode()) {

                System.out.println("Enter WeaponType or empty:");

                for (WeaponType w : WeaponType.values())
                    System.out.println(" - " + w);
            }

            String input = scanner().nextLine().trim();

            if (input.isEmpty())
                return null;

            try {
                return WeaponType.valueOf(input);
            } catch (Exception e) {

                String error = "Error: invalid WeaponType.";

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(error);

                System.out.println(error);
            }
        }
    }

    private Mood readMood() {

        while (true) {

            if (!ScriptManager.isScriptMode()) {

                System.out.println("Enter Mood or empty:");

                for (Mood m : Mood.values())
                    System.out.println(" - " + m);
            }

            String input = scanner().nextLine().trim();

            if (input.isEmpty())
                return null;

            try {
                return Mood.valueOf(input);
            } catch (Exception e) {

                String error = "Error: invalid Mood.";

                if (ScriptManager.isScriptMode())
                    throw new IllegalArgumentException(error);

                System.out.println(error);
            }
        }
    }

    private Car readCar() {

        if (!ScriptManager.isScriptMode())
            System.out.println("Enter car name or empty:");

        String name = scanner().nextLine().trim();

        if (name.isEmpty())
            return null;

        Boolean cool;

        while (true) {

            if (!ScriptManager.isScriptMode())
                System.out.println("Enter cool (true/false) or empty:");

            String input = scanner().nextLine().trim();

            if (input.isEmpty()) {
                cool = null;
                break;
            }

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                cool = Boolean.parseBoolean(input);
                break;
            }

            String error = "Error: cool must be true/false or empty.";

            if (ScriptManager.isScriptMode())
                throw new IllegalArgumentException(error);

            System.out.println(error);
        }

        return new Car(name, cool);
    }
}