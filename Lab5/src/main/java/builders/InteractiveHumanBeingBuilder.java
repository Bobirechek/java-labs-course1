package builders;

import managers.IdGenerator;
import models.*;

import java.time.LocalDateTime;
import java.util.Scanner;

public class InteractiveHumanBeingBuilder implements HumanBeingBuilder {

    private final Scanner scanner;

    public InteractiveHumanBeingBuilder(Scanner scanner) {
        this.scanner = scanner;
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

            System.out.println("Enter name:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println("Error: name cannot be empty.");

                continue;
            }

            return input;
        }
    }

    private Coordinates readCoordinates() {

        long x;
        long y;

        while (true) {

            try {

                System.out.println("Enter coordinate X:");

                x = Long.parseLong(scanner.nextLine());

                break;

            } catch (NumberFormatException e) {

                System.out.println("Error: X must be a long number.");
            }
        }

        while (true) {

            try {

                System.out.println("Enter coordinate Y:");

                y = Long.parseLong(scanner.nextLine());

                break;

            } catch (NumberFormatException e) {

                System.out.println("Error: Y must be a long number.");
            }
        }

        return new Coordinates(x, y);
    }

    private boolean readRealHero() {

        while (true) {

            System.out.println("Enter realHero (true/false):");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {

                return Boolean.parseBoolean(input);
            }

            System.out.println("Error: Only true or false values are allowed..");
        }
    }

    private Boolean readHasToothpick() {

        while (true) {

            System.out.println("Enter hasToothpick (true/false) or nothing:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                return null;
            }

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {

                return Boolean.parseBoolean(input);
            }

            System.out.println("Error: valid values are true/false or nothing..");
        }
    }

    private double readImpactSpeed() {

        while (true) {

            try {

                System.out.println("Enter impactSpeed (<=597):");

                double value = Double.parseDouble(scanner.nextLine());

                if (value > 597) {

                    System.out.println("Errror: impactSpeed can't be more than 597.");

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println("Ошибка: impactSpeed must be a number.");
            }
        }
    }

    private String readSoundtrackName() {

        while (true) {

            System.out.println("Enter soundtrackName:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println("Error: soundtrackName can't be empty.");

                continue;
            }

            return input;
        }
    }

    private WeaponType readWeaponType() {

        while (true) {

            System.out.println("Enter WeaponType or nothing:");

            for (WeaponType w : WeaponType.values()) {

                System.out.println(" - " + w);
            }

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                return null;
            }

            try {

                return WeaponType.valueOf(input);

            } catch (IllegalArgumentException e) {

                System.out.println("Error: such a WeaponType does not exist.");
            }
        }
    }

    private Mood readMood() {

        while (true) {

            System.out.println("Enter Mood or nothing:");

            for (Mood m : Mood.values()) {

                System.out.println(" - " + m);
            }

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                return null;
            }

            try {

                return Mood.valueOf(input);

            } catch (IllegalArgumentException e) {

                System.out.println("Error: such a Mood does not exist.");
            }
        }
    }

    private Car readCar() {

        System.out.println("Enter Car name or nothing:");

        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {

            return null;
        }

        Boolean cool;

        while (true) {

            System.out.println("Enter cool (true/false) or nothing:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                cool = null;

                break;
            }

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {

                cool = Boolean.parseBoolean(input);

                break;
            }

            System.out.println("Error: Cool must be true/false or nothing.");
        }

        return new Car(name, cool);
    }
}