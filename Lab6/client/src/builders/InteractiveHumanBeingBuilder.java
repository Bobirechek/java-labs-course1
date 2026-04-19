package builders;

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
                .id(null) // сервер сам выставит id
                .name(name)
                .coordinates(coordinates)
                .creationDate(LocalDateTime.now()) // сервер может перезаписать
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
            System.out.println("Enter coordinate X:");
            try {
                x = Long.parseLong(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: X must be a long number.");
            }
        }

        while (true) {
            System.out.println("Enter coordinate Y:");
            try {
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

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
                return Boolean.parseBoolean(input);

            System.out.println("Error: realHero must be true or false.");
        }
    }

    private Boolean readHasToothpick() {
        while (true) {
            System.out.println("Enter hasToothpick (true/false) or empty:");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                return null;

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"))
                return Boolean.parseBoolean(input);

            System.out.println("Error: hasToothpick must be true/false or empty.");
        }
    }

    private double readImpactSpeed() {
        while (true) {
            System.out.println("Enter impactSpeed (<=597):");
            try {
                double value = Double.parseDouble(scanner.nextLine());

                if (value > 597) {
                    System.out.println("Error: impactSpeed must be <= 597");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                System.out.println("Error: impactSpeed must be a number.");
            }
        }
    }

    private String readSoundtrackName() {
        while (true) {
            System.out.println("Enter soundtrackName:");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: soundtrackName cannot be empty.");
                continue;
            }

            return input;
        }
    }

    private WeaponType readWeaponType() {
        while (true) {
            System.out.println("Enter WeaponType or empty:");
            for (WeaponType w : WeaponType.values())
                System.out.println(" - " + w);

            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                return null;

            try {
                return WeaponType.valueOf(input);
            } catch (Exception e) {
                System.out.println("Error: invalid WeaponType.");
            }
        }
    }

    private Mood readMood() {
        while (true) {
            System.out.println("Enter Mood or empty:");
            for (Mood m : Mood.values())
                System.out.println(" - " + m);

            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                return null;

            try {
                return Mood.valueOf(input);
            } catch (Exception e) {
                System.out.println("Error: invalid Mood.");
            }
        }
    }

    private Car readCar() {

        System.out.println("Enter car name or empty:");
        String name = scanner.nextLine().trim();

        if (name.isEmpty())
            return null;

        Boolean cool;

        while (true) {
            System.out.println("Enter cool (true/false) or empty:");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                cool = null;
                break;
            }

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                cool = Boolean.parseBoolean(input);
                break;
            }

            System.out.println("Error: cool must be true/false or empty.");
        }

        return new Car(name, cool);
    }
}