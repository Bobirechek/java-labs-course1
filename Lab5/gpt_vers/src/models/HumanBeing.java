package models;

import java.time.LocalDateTime;


public class HumanBeing implements Comparable<HumanBeing> {

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

        public Builder id(Long id){ this.id=id; return this;}
        public Builder name(String name){this.name=name; return this;}
        public Builder coordinates(Coordinates c){this.coordinates=c; return this;}
        public Builder creationDate(LocalDateTime d){this.creationDate=d; return this;}
        public Builder realHero(boolean r){this.realHero=r; return this;}
        public Builder hasToothpick(Boolean t){this.hasToothpick=t; return this;}
        public Builder impactSpeed(double s){this.impactSpeed=s; return this;}
        public Builder soundtrackName(String s){this.soundtrackName=s; return this;}
        public Builder weaponType(WeaponType w){this.weaponType=w; return this;}
        public Builder mood(Mood m){this.mood=m; return this;}
        public Builder car(Car c){this.car=c; return this;}

        public HumanBeing build(){
            return new HumanBeing(this);
        }
    }

    public Long getId(){return id;}
    public String getName(){return name;}
    public double getImpactSpeed(){return impactSpeed;}
    public String getSoundtrackName(){return soundtrackName;}

    @Override
    public int compareTo(HumanBeing o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Id - " + id + "\nИмя - " + name +
        "\nКоординаты - (x = " + coordinates.getX() + ", y = " + coordinates.getY() + ")\n" +
        "Дата создания - " + creationDate + 
        "\nНастоящий герой - " + realHero + 
        "\nЕсть зубочист - " + hasToothpick +
        "\nСкорость удара - " + impactSpeed +
        "\nНазвание саундтрека - " + soundtrackName +
        "\nТип оружия - " + weaponType +
        "\nНастроение - " + mood +
        ((car != null && car.getName() != null && !car.getName().isEmpty())
                ? "\nМашина:\n" +
                  "   Имя - " + car.getName() +
                  "\n   Прохладный - " + car.getCool()
                : "");
    }
}