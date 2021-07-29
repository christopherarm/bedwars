package net.trainingsoase.bedwars.map;

import com.google.gson.Gson;
import net.trainingsoase.hopjes.api.map.BaseMap;
import net.trainingsoase.oreo.location.WrappedLocation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LobbyMap extends BaseMap {

    /**
     * Default Constructor
     * @param name Name of the map
     * @param spawn Default spawn for the map
     * @param builders Builders of the map
     */
    public LobbyMap(String name, WrappedLocation spawn, String... builders) {
        super(name, spawn, builders);
    }

    /**
     * Saves the map.json file which contains the information
     * about the map.
     * @param path Path to the Mapfolder
     * @param gson
     */
    public void saveMap(Path path, Gson gson) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            if (!Files.exists(path)) {
                Files.createFile(path).getFileName();
            }
            gson.toJson(this, writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void replaceMap(Path path, Gson gson) {
        try {
            if(Files.exists(path)) {
                Files.delete(path);
                saveMap(path, gson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
