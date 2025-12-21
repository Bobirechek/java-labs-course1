import daugther.Human;
import daugther.Item;
import daugther.Location;
import enums.ItemStatus;
import enums.PersonStatus;
import exception.NotFound;
import interfaces.Story;

public class Main {
    public static void main(String[] args) {
        Human znaika = new Human("Знайка", PersonStatus.SIMPLE);
        Human neznaika = new Human("Незнайка", PersonStatus.SIMPLE);
        Human ponchik = new Human("Пончик", PersonStatus.SIMPLE);
        Human fucsiyaSeledochka = new Human("Фуксия", PersonStatus.SIMPLE);
        fucsiyaSeledochka.addPerson("Селедочка");
        Human engineers = new Human("инженеры-конструкторы", PersonStatus.GROUP);

        Item lunit = new Item("лунит", ItemStatus.NOLUNIT, PersonStatus.SIMPLE);
        Item zeroGDevice = new Item("прибор невесомости", ItemStatus.ELEMENT, PersonStatus.SIMPLE);
        Item sketch = new Item("эскиз", ItemStatus.SKETCH, PersonStatus.SIMPLE);
        Item engineerPlan = new Item("чертежи узлов ракеты", ItemStatus.PLAN, PersonStatus.GROUP);
        Item equipment = new Item("аппаратура для управления космическим кораблем", ItemStatus.ELEMENT, PersonStatus.SIMPLE);

        Location luna = new Location("Луна", PersonStatus.SIMPLE);
        Location gorodok = new Location("Научный городок", PersonStatus.SIMPLE);
        Location zavody = new Location("Заводы", PersonStatus.GROUP);

        znaika.offer("последняя ступень ракеты должна иметь двоякое управление, а именно: управление для полетов в условиях тяжести и управление для полетов в состоянии невесомости.");
        znaika.hope("по прибытии на " + luna + " мы обнаружим залежи " + lunit);

        try {
            lunit.isFound();
        } catch (NotFound e){
            System.out.println(e.getMessage());
            System.out.println("Соорудить " + zeroGDevice + " не получится(\nполеты ракеты вокруг " + luna + " очень усложнены и поиски " + neznaika + " и " + ponchik + " будут оочень сложными");
        }
        lunit.setCondition(ItemStatus.LUNIT);
        try {
            lunit.isFound();
        } catch (NotFound e){
            System.out.println(e.getMessage());
            System.out.println("Соорудить прибор невесомости не получится(\nполеты ракеты вокруг Луны очень усложнены и поиски Незнайки и Пончика будут оочень сложными");
        }

        fucsiyaSeledochka.doThis("работу по составлению " + sketch);
        fucsiyaSeledochka.goTo(gorodok);

        engineers.begin("делать подробные " + engineerPlan);

        engineerPlan.goTo(zavody);

        equipment.doThis("отливалась, ковалась, штамповалась, а также изготовливалась");
    }
}
