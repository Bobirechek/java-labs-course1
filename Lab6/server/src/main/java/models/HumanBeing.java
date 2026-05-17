package models;

import java.time.LocalDateTime;
import java.io.Serializable;

public class HumanBeing implements Comparable<HumanBeing>, Serializable {

    private Long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private boolean realHero;
    private Boolean hasToothpick;
    private double impactSpeed;
    private String soundtrackName;
    private WeaponType weaponType;
    private Mood mood;
    private Car car;

    private HumanBeing(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.coordinates = builder.coordinates;
        this.creationDate = builder.creationDate;
        this.realHero = builder.realHero;
        this.hasToothpick = builder.hasToothpick;
        this.impactSpeed = builder.impactSpeed;
        this.soundtrackName = builder.soundtrackName;
        this.weaponType = builder.weaponType;
        this.mood = builder.mood;
        this.car = builder.car;
    }

    public static class Builder {

        private Long id;
        private String name;
        private Coordinates coordinates;
        private LocalDateTime creationDate;
        private boolean realHero;
        private Boolean hasToothpick;
        private double impactSpeed;
        private String soundtrackName;
        private WeaponType weaponType;
        private Mood mood;
        private Car car;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder coordinates(Coordinates c) {
            this.coordinates = c;
            return this;
        }

        public Builder creationDate(LocalDateTime d) {
            this.creationDate = d;
            return this;
        }

        public Builder realHero(boolean r) {
            this.realHero = r;
            return this;
        }

        public Builder hasToothpick(Boolean t) {
            this.hasToothpick = t;
            return this;
        }

        public Builder impactSpeed(double s) {
            this.impactSpeed = s;
            return this;
        }

        public Builder soundtrackName(String s) {
            this.soundtrackName = s;
            return this;
        }

        public Builder weaponType(WeaponType w) {
            this.weaponType = w;
            return this;
        }

        public Builder mood(Mood m) {
            this.mood = m;
            return this;
        }

        public Builder car(Car c) {
            this.car = c;
            return this;
        }

        public HumanBeing build() {
            return new HumanBeing(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isRealHero() {
        return realHero;
    }

    public Boolean getHasToothpick() {
        return hasToothpick;
    }

    public double getImpactSpeed() {
        return impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public Mood getMood() {
        return mood;
    }

    public Car getCar() {
        return car;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(HumanBeing o) {
        int compareX = Double.compare(this.coordinates.getX(), o.coordinates.getX());
        if (compareX != 0) {
            return compareX;
        }
        return Double.compare(this.coordinates.getY(), o.coordinates.getY());
    }

    @Override
    public String toString() {
        return "Id - " + id + "\nName - " + name +
                "\nCoordinates - (x = " + coordinates.getX() + ", y = " + coordinates.getY() + ")" +
                "\ncreationDate - " + creationDate +
                "\nRealHero - " + realHero +
                "\nHasToothpick - " + hasToothpick +
                "\nImpactSpeed - " + impactSpeed +
                "\nSoundtrackName - " + soundtrackName +
                "\nWeaponType - " + weaponType +
                "\nMood - " + mood +
                ((car != null && car.getName() != null && !car.getName().isEmpty())
                        ? "\nCar:" +
                                "\n   Name - " + car.getName() +
                                "\n   Cool - " + car.getCool()
                        : "");
    }
}