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

            System.out.println("Введите name:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println("Ошибка: name не может быть пустым.");

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

                System.out.println("Введите координату X:");

                x = Long.parseLong(scanner.nextLine());

                break;

            } catch (NumberFormatException e) {

                System.out.println("Ошибка: X должен быть числом типа long.");
            }
        }

        while (true) {

            try {

                System.out.println("Введите координату Y:");

                y = Long.parseLong(scanner.nextLine());

                break;

            } catch (NumberFormatException e) {

                System.out.println("Ошибка: Y должен быть числом типа long.");
            }
        }

        return new Coordinates(x, y);
    }

    private boolean readRealHero() {

        while (true) {

            System.out.println("Введите realHero (true/false):");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {

                return Boolean.parseBoolean(input);
            }

            System.out.println("Ошибка: допустимы только true или false.");
        }
    }

    private Boolean readHasToothpick() {

        while (true) {

            System.out.println("Введите hasToothpick (true/false) или пустую строку:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                return null;
            }

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {

                return Boolean.parseBoolean(input);
            }

            System.out.println("Ошибка: допустимы true, false или пустая строка.");
        }
    }

    private double readImpactSpeed() {

        while (true) {

            try {

                System.out.println("Введите impactSpeed (<=597):");

                double value = Double.parseDouble(scanner.nextLine());

                if (value > 597) {

                    System.out.println("Ошибка: impactSpeed не может быть больше 597.");

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println("Ошибка: impactSpeed должен быть числом.");
            }
        }
    }

    private String readSoundtrackName() {

        while (true) {

            System.out.println("Введите soundtrackName:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println("Ошибка: soundtrackName не может быть пустым.");

                continue;
            }

            return input;
        }
    }

    private WeaponType readWeaponType() {

        while (true) {

            System.out.println("Введите WeaponType или пустую строку:");

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

                System.out.println("Ошибка: такого WeaponType не существует.");
            }
        }
    }

    private Mood readMood() {

        while (true) {

            System.out.println("Введите Mood или пустую строку:");

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

                System.out.println("Ошибка: такого Mood не существует.");
            }
        }
    }

    private Car readCar() {

        System.out.println("Введите имя машины или пустую строку:");

        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            return null;
        }

        Boolean cool;

        while (true) {

            System.out.println("Введите cool (true/false) или пустую строку:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {

                cool = null;

                break;
            }

            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {

                cool = Boolean.parseBoolean(input);

                break;
            }

            System.out.println("Ошибка: допустимы true, false или пустая строка.");
        }

        return new Car(name, cool);
    }
}