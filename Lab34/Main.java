import daugther.Human;
import daugther.Item;
import daugther.Location;
import enums.ItemStatus;
import enums.PersonStatus;
import exception.NotFound;
import interfaces.Story;

public class Main {
    public static void main(String[] args) {
        Human znaika = new Human("Знайка", PersonStatus.ALIVE);
        Human neznaika = new Human("Незнайка", PersonStatus.ALIVE);
        Human ponchik = new Human("Пончик", PersonStatus.ALIVE);
        Human girls = new Human("Фуксия и Селедочка", PersonStatus.ALIVE);
        Human engineers = new Human("инженеры-конструкторы", PersonStatus.ALIVE);

        Item rocket = new Item("ракета", ItemStatus.SINGLECONTROL, PersonStatus.ALIVE);
        Item lunit = new Item("лунит", ItemStatus.NOLUNIT, PersonStatus.ALIVE);
        Item zeroGDevice = new Item("прибор невесомости", ItemStatus.ELEMENT, PersonStatus.ALIVE);
        Item sketch = new Item("эскиз", ItemStatus.SKETCH, PersonStatus.ALIVE);
        Item engineerPlan = new Item("чертежи узлов ракеты", ItemStatus.PLAN, PersonStatus.ALIVE);
        Item equipment = new Item("аппаратура для управления космическим кораблем", ItemStatus.ELEMENT, PersonStatus.ALIVE);

        Location luna = new Location("Луна", PersonStatus.ALIVE);
        Location gorodok = new Location("Научный городок", PersonStatus.ALIVE);
        Location zavody = new Location("Заводы", PersonStatus.ALIVE);

        znaika.offer("последняя ступень " + rocket.getName() + " должна иметь " + ItemStatus.DUALCONTROL + ": управление для полетов в условиях тяжести и управление для полетов в состоянии невесомости.");
        znaika.hope("по прибытии на " + luna + " обнаружить залежи " + lunit);

        try {
            lunit.isFound();
        } catch (NotFound e){
            System.out.println(e.getMessage());
            System.out.println("Соорудить " + zeroGDevice + " не получится(\nполеты " + rocket.getName() + " вокруг " + luna + " очень усложнены и поиски " + neznaika + " и " + ponchik + " будут оочень сложными");
        }
        lunit.setCondition(ItemStatus.LUNIT);
        try {
            lunit.isFound();
            rocket.setCondition(ItemStatus.DUALCONTROL);
            System.out.println("Всех найдем и спасем");
        } catch (NotFound e){
            System.out.println(e.getMessage());
            System.out.println("Соорудить прибор невесомости не получится(\nполеты ракеты вокруг Луны очень усложнены и поиски Незнайки и Пончика будут оочень сложными");
        }

        girls.doThis("работу по составлению " + sketch);
        girls.goTo(gorodok);

        engineers.begin("делать подробные " + engineerPlan);

        engineerPlan.goTo(zavody);

        equipment.doThis("отливается, куется, штампуется, а также изготавливается");
    }
}
