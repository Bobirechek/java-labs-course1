package models;

public class Coordinates {

    private long x;
    private long y;

    public Coordinates(long x,long y){
        this.x=x;
        this.y=y;
    }

    public long getX() {
        return this.x;
    }

    public long getY() {
        return this.y;
    }
}