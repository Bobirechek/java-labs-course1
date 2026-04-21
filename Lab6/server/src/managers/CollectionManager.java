package managers;

import models.HumanBeing;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Vector;
import java.util.stream.Collectors;

public class CollectionManager {

    String file = System.getenv("LAB_FILE");

    private Vector<HumanBeing> collection;
    private final LocalDateTime initializationDate;
    private final FileManager fileManager = new FileManager(file);

    public CollectionManager() {
        this.collection = new Vector<>();
        this.initializationDate = LocalDateTime.now();
    }

    public CollectionManager(Vector<HumanBeing> collection) {
        this.collection = collection;
        this.initializationDate = LocalDateTime.now();
    }

    public Vector<HumanBeing> getCollection() {
        return collection;
    }

    public void setCollection(Vector<HumanBeing> collection) {
        this.collection = collection;
    }

    public void add(HumanBeing human) {
        collection.add(human);
    }

    public void clear() {
        collection.clear();
        fileManager.save(collection);
        System.out.println("Collection clear.");
    }

    public int size() {
        return collection.size();
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public String info() {
        return "Collection type: " + collection.getClass().getName() + 
               "\nInitialization date: " + initializationDate + 
               "\nNumber of elements: " + collection.size();
    }

    public boolean removeById(long id) {
        for (HumanBeing human : collection) {
            if (human.getId() == id) {
                collection.remove(human);
                return true;
            }
        }
        return false;
    }

    public void sort() {
        Collections.sort(collection);
    }

    public void reorder() {
        Collections.sort(collection);
        Collections.reverse(collection);
    }

    public HumanBeing getById(long id) {
        for (HumanBeing human : collection) {
            if (human.getId() == id) {
                return human;
            }
        }
        return null;
    }

    public boolean existsById(long id) {
        return getById(id) != null;
    }

    public void removeGreater(models.HumanBeing ref) {
        collection.removeIf(h -> h.compareTo(ref) > 0);
    }

    public void update(HumanBeing newHuman) {
        removeById(newHuman.getId());
        collection.add(newHuman);
    }

    public int countBySoundtrackName(String name) {
        return (int) collection.stream()
                .filter(h -> h.getSoundtrackName() != null)
                .filter(h -> h.getSoundtrackName().equals(name))
                .count();
    }

    public String filterContainsName(String name) {
        String result = collection.stream()
                .filter(h -> h.getName() != null)
                .filter(h -> h.getName().contains(name))
                .map(HumanBeing::toString)
                .collect(Collectors.joining("\n" + "=".repeat(45) + "\n"));
        
        return result.isEmpty() ? "No matches were found." : result;
    }
}