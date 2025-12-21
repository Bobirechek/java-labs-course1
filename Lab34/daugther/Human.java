package daugther;

import enums.PersonStatus;
// import exception.NotBAd;
import interfaces.Story;
import parents.Person;
import daugther.Location;

public class Human extends Person implements Story {
    public static boolean Dressed;
    boolean BecBad;
    public static boolean BadHab;

    public Human(String name, PersonStatus status) {
        super(name, status);
    }

    // public void becomeBadGuy() throws NotBAd {
    //         if (BadHab && Dressed) {
    //             setStat(PersonStatus.BADGUY);
    //             System.out.println(this.getName() + " become " + PersonStatus.BADGUY);
    //             this.BecBad = true;
    //         } else {
    //             throw new NotBAd(this.getName() + " is not " + PersonStatus.BADGUY);
    //     }
    // }
    // public void dresS() {
    //     System.out.println(this.getName() + " dress green and yellow ridiculous pants and jackets with with foolish sleeves");
    //     this.Dressed = true;
    // }
    // public void getHabit() {
    //     setALive(AliveStatus.BADGNOME);
    //     System.out.println(this.getName() +" " + this.getASTAT());
    //     this.BadHab = true;
    // }
    @Override
    public void offer(String a){
        String res;
        if (this.getStatus() == PersonStatus.SIMPLE ) {
            res = " предлагает: "; 
        } else {
            res = " предлагают: ";
        }
        System.out.println(this.getName() + res + a);
    }
    @Override
    public void hope(String a){
        String res;
        if (this.getStatus() == PersonStatus.SIMPLE ) {
            res = " надеется: "; 
        } else {
            res = " надеются: ";
        }
        System.out.println(this.getName() + res + a);
    }
    @Override
    public void doThis(String a){
        String res;
        if (this.getStatus() == PersonStatus.SIMPLE ) {
            res = " сделал "; 
        } else {
            res = " сделали ";
        }
        System.out.println(this.getName() + res + a);
    }
    @Override
    public void goTo(Location l) {
        String res;
        if (this.getStatus() == PersonStatus.SIMPLE ) {
            res = " отправился в "; 
        } else {
            res = " отправились в ";
        }
        System.out.println(this.getName() + res + l);
    }
    @Override
    public void begin(String a) {
        String res;
        if (this.getStatus() == PersonStatus.SIMPLE ) {
            res = " начал "; 
        } else {
            res = " начали ";
        }
        System.out.println(this.getName() + res + a);
    }
}
