package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.HumanBeing;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Vector;

public class FileManager {

    private final String fileName;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public FileManager(String fileName) {

        this.fileName = fileName;

    }

    public Vector<HumanBeing> load() {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            HumanBeing[] arr = gson.fromJson(reader, HumanBeing[].class);

            Vector<HumanBeing> vector = new Vector<>();

            if (arr != null) {

                for (HumanBeing h : arr) {

                    vector.add(h);

                }
            }

            return vector;

        } catch (Exception e) {

            System.out.println("File upload error.");

            return new Vector<>();
        }
    }

    public void save(Vector<HumanBeing> collection) {

        try (PrintWriter writer = new PrintWriter(fileName)) {

            gson.toJson(collection, writer);

        } catch (Exception e) {

            System.out.println("File save error.");
        }
    }
}