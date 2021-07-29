package net.trainingsoase.bedwars.map;

import com.google.gson.Gson;
import net.trainingsoase.bedwars.utils.Mode;
import net.trainingsoase.hopjes.api.map.BaseMap;
import net.trainingsoase.oreo.location.WrappedLocation;
import org.bukkit.Location;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class GameMap extends BaseMap {
    /**
     * Locations of the spawners
     */
    private final List<WrappedLocation> bronzeSpawnerLocations;

    private final List<WrappedLocation> ironSpawnerLocations;

    private final List<WrappedLocation> goldSpawnerLocations;

    private final HashMap<String, WrappedLocation> bedLocations;

    /**
     * Locations of the spawns
     */
    private final HashMap<String, WrappedLocation> spawnLocations;

    /**
     * Locations of the shops
     */
    private final List<WrappedLocation> shopLocations;

    private Mode mode;

    /**
     * Instantiates a GameMap with the given parameters
     * @param name Mapname
     * @param spawn Spec Spawnlocation
     * @param spawnLocations map of spawn locations
     * @param shopLocations map of shop locations
     * @param builders builders of the map
     */
    public GameMap(String name, Mode mode, WrappedLocation spawn, List<WrappedLocation> bronzeSpawnerLocations,
                   List<WrappedLocation> ironSpawnerLocations, List<WrappedLocation> goldSpawnerLocations,
                   HashMap<String, WrappedLocation> spawnLocations, HashMap<String, WrappedLocation> bedLocations,
                   List<WrappedLocation> shopLocations, String... builders) {
        super(name, spawn, builders);
        this.mode = mode;
        this.bronzeSpawnerLocations = bronzeSpawnerLocations;
        this.ironSpawnerLocations = ironSpawnerLocations;
        this.goldSpawnerLocations = goldSpawnerLocations;
        this.bedLocations = bedLocations;
        this.spawnLocations = spawnLocations;
        this.shopLocations = shopLocations;
    }

    /**
     * Default constructor
     */
    public GameMap() {
        super("", null, "TrainingsOase");
        this.shopLocations = new ArrayList<>();
        this.bronzeSpawnerLocations = new ArrayList<>();
        this.ironSpawnerLocations = new ArrayList<>();
        this.goldSpawnerLocations = new ArrayList<>();
        this.bedLocations = new HashMap<>();
        this.spawnLocations = new HashMap<>();
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

    /**
     * Adds a spawner to the locations map
     * @param type SpawnerType (Bronze, Iron, Gold)
     * @param location Location of the spawner
     */
    public void addSpawner(SpawnType type, Location location) {
        switch (type) {
            case BRONZE:
                bronzeSpawnerLocations.add(WrappedLocation.of(location));
                break;

            case IRON:
                ironSpawnerLocations.add(WrappedLocation.of(location));
                break;

            case GOLD:
                goldSpawnerLocations.add(WrappedLocation.of(location));
                break;

            default:
                break;
        }
    }

    /**
     * Adds a spawn for the provided Team
     * @param team Team which the spawn should be set for
     * @param location Location of the spawn
     */
    public void addSpawn(String team, Location location) {
        spawnLocations.putIfAbsent(team, WrappedLocation.of(location));
    }

    public void addBed(String team, BedType bedType, Location location) {
        bedLocations.putIfAbsent(team + "." + bedType.getName(), WrappedLocation.of(location));
    }

    /**
     * Adds a shop location to the locations map
     * @param location Location of the shop
     */
    public void addShop(Location location) {
        shopLocations.add(WrappedLocation.of(location));
    }

    @Override
    public void setSpawn(WrappedLocation spawn) {
        super.setSpawn(spawn);
    }

    public HashMap<String, WrappedLocation> getSpawnLocations() {
        return spawnLocations;
    }

    public List<WrappedLocation> getBronzeSpawnerLocations() {
        return bronzeSpawnerLocations;
    }

    public List<WrappedLocation> getGoldSpawnerLocations() {
        return goldSpawnerLocations;
    }

    public List<WrappedLocation> getIronSpawnerLocations() {
        return ironSpawnerLocations;
    }

    public List<WrappedLocation> getShopLocations() {
        return shopLocations;
    }

    public HashMap<String, WrappedLocation> getBedLocations() {
        return bedLocations;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
