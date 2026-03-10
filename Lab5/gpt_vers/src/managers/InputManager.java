package managers;

import models.*;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Ввод данных пользователя
 */
public class InputManager {

    private Scanner scanner;

    public InputManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readName() {

        while (true) {

            System.out.println("Введите name:");

            String name = scanner.nextLine();

            if (name == null || name.isEmpty()) {
                System.out.println("name не может быть пустым");
                continue;
            }

            return name;
        }
    }

    public double readImpactSpeed() {

        while (true) {

            System.out.println("Введите impactSpeed:");

            try {

                double speed = Double.parseDouble(scanner.nextLine());

                if (speed > 597) {
                    System.out.println("impactSpeed максимум 597");
                    continue;
                }

                return speed;

            } catch (Exception e) {
                System.out.println("Введите число");
            }
        }
    }

    public Coordinates readCoordinates() {

        long x;
        long y;

        while (true) {

            try {

                System.out.println("Введите x:");
                x = Long.parseLong(scanner.nextLine());

                System.out.println("Введите y:");
                y = Long.parseLong(scanner.nextLine());

                return new Coordinates(x, y);

            } catch (Exception e) {
                System.out.println("Ошибка ввода координат");
            }
        }
    }

    public HumanBeing readHuman() {

        String name = readName();

        Coordinates coordinates = readCoordinates();

        System.out.println("realHero (true/false):");
        boolean realHero = Boolean.parseBoolean(scanner.nextLine());

        System.out.println("hasToothpick (true/false/null):");
        String tooth = scanner.nextLine();
        Boolean hasToothpick = tooth.isEmpty() ? null : Boolean.parseBoolean(tooth);

        double impactSpeed = readImpactSpeed();

        System.out.println("soundtrackName:");
        String soundtrack = scanner.nextLine();

        System.out.println("WeaponType:");
        for (WeaponType w : WeaponType.values()) {
            System.out.println(w);
        }

        WeaponType weapon = null;
        String weaponInput = scanner.nextLine();

        if (!weaponInput.isEmpty()) {
            weapon = WeaponType.valueOf(weaponInput);
        }

        System.out.println("Mood:");
        for (Mood m : Mood.values()) {
            System.out.println(m);
        }

        Mood mood = null;
        String moodInput = scanner.nextLine();

        if (!moodInput.isEmpty()) {
            mood = Mood.valueOf(moodInput);
        }

        System.out.println("Car name:");
        String carName = scanner.nextLine();

        System.out.println("Car cool (true/false):");
        String coolInput = scanner.nextLine();

        Car car = null;

        if (!carName.isEmpty() || !coolInput.isEmpty()) {

            Boolean cool = coolInput.isEmpty() ? null : Boolean.parseBoolean(coolInput);

            car = new Car(carName, cool);
        }

        return new HumanBeing.Builder()
                .id(IdGenerator.generateId())
                .name(name)
                .coordinates(coordinates)
                .creationDate(LocalDateTime.now())
                .realHero(realHero)
                .hasToothpick(hasToothpick)
                .impactSpeed(impactSpeed)
                .soundtrackName(soundtrack)
                .weaponType(weapon)
                .mood(mood)
                .car(car)
                .build();
    }
}