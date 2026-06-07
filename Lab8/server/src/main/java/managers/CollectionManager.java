package managers;

import models.HumanBeing;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {

    private final List<HumanBeing> collection;
    private final LocalDateTime initializationDate;

    public CollectionManager(List<HumanBeing> initial) {
        this.collection = Collections.synchronizedList(new ArrayList<>(initial));
        this.initializationDate = LocalDateTime.now();
    }

    public CollectionManager() {
        this.collection = Collections.synchronizedList(new ArrayList<>());
        this.initializationDate = LocalDateTime.now();
    }


    public List<HumanBeing> getCollection() {
        return collection;
    }

    public void add(HumanBeing human) {
        collection.add(human);
    }

    public int size() {
        return collection.size();
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public String info() {
        return "Collection type: java.util.ArrayList (synchronized)" +
               "\nInitialization date: " + initializationDate +
               "\nNumber of elements: " + collection.size();
    }
    
   
    public boolean hasElementsByOwner(String ownerLogin) {
        
            return collection.stream()
                    .anyMatch(h -> ownerLogin.equals(h.getOwnerLogin()));
        
    }


    public boolean removeById(long id) {
        
            return collection.removeIf(h -> h.getId() == id);
        
    }

    
    public void removeGreaterByOwner(HumanBeing ref, String ownerLogin) {
            collection.removeIf(h ->
                    ownerLogin.equals(h.getOwnerLogin()) && h.compareTo(ref) > 0
            );
    }

    
    public void clearByOwner(String ownerLogin) {
            collection.removeIf(h -> ownerLogin.equals(h.getOwnerLogin()));
    }

    public void update(HumanBeing updated) {
            collection.removeIf(h -> h.getId().equals(updated.getId()));
            collection.add(updated);
    }
    
    public void sortByOwner(String ownerLogin) {
            List<Integer> indices = new ArrayList<>();
            List<HumanBeing> userElements = new ArrayList<>();

            for (int i = 0; i < collection.size(); i++) {
                if (ownerLogin.equals(collection.get(i).getOwnerLogin())) {
                    indices.add(i);
                    userElements.add(collection.get(i));
                }
            }

            Collections.sort(userElements);

            for (int i = 0; i < indices.size(); i++) {
                collection.set(indices.get(i), userElements.get(i));
            }
        
    }

    public void reorderByOwner(String ownerLogin) {
            List<Integer> indices = new ArrayList<>();
            List<HumanBeing> userElements = new ArrayList<>();

            for (int i = 0; i < collection.size(); i++) {
                if (ownerLogin.equals(collection.get(i).getOwnerLogin())) {
                    indices.add(i);
                    userElements.add(collection.get(i));
                }
            }

            Collections.sort(userElements);
            Collections.reverse(userElements);

            for (int i = 0; i < indices.size(); i++) {
                collection.set(indices.get(i), userElements.get(i));
            }
    }

    

    public HumanBeing getById(long id) {
            return collection.stream()
                    .filter(h -> h.getId() == id)
                    .findFirst()
                    .orElse(null);
        
    }

    public boolean existsById(long id) {
        return getById(id) != null;
    }

    public int countBySoundtrackName(String name) {
            return (int) collection.stream()
                    .filter(h -> name.equals(h.getSoundtrackName()))
                    .count();
        
    }

    public String filterContainsName(String name) {
            String result = collection.stream()
                    .filter(h -> h.getName() != null && h.getName().contains(name))
                    .map(HumanBeing::toString)
                    .collect(Collectors.joining("\n" + "=".repeat(45) + "\n"));
            return result.isEmpty() ? "No matches found." : result;
        
    }

    public String showAll() {
            if (collection.isEmpty()) return "Collection is empty.";
            return collection.stream()
                    .sorted()
                    .map(HumanBeing::toString)
                    .collect(Collectors.joining("\n" + "=".repeat(45) + "\n"));
    }

    public String printImpactSpeedDescending() {
            StringBuilder sb = new StringBuilder();
            collection.stream()
                    .map(HumanBeing::getImpactSpeed)
                    .sorted(Comparator.reverseOrder())
                    .forEach(s -> sb.append(s).append("\n"));
            if (sb.length() > 0) sb.setLength(sb.length() - 1);
            return sb.toString();
        }

        public List<HumanBeing> getCollectionSnapshot() {
        synchronized (collection) {
                return new ArrayList<>(collection);
        }
        }
}