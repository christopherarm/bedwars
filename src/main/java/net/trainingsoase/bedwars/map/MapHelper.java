package net.trainingsoase.bedwars.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.oreo.location.WrappedLocation;
import net.trainingsoase.oreo.location.adapter.WrappedLocationTypeAdapter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class MapHelper {

    private static MapHelper instance;

    private final Bedwars bedwars;

    /**
     * Default map file constant
     */
    public final String MAP_FILE = "map.json";

    /**
     * GSON instance with the WrappedLocation TypeAdapter
     */
    public final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(WrappedLocation.class, new WrappedLocationTypeAdapter())
            .create();

    /**
     * Default lobby map
     */
    private LobbyMap lobbyMap;

    /**
     * Default Game map
     */
    private GameMap gameMap;

    private List<String> mapNames;

    public MapHelper(Bedwars bedwars) {
        instance = this;
        this.bedwars = bedwars;
        this.mapNames = new ArrayList<>();
    }

    /**
     * Loads the Lobbymap from the HomeDirectory
     */
    public void loadLobby() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("/home/Maps/Bedwars/world", MAP_FILE))) {
            Optional<LobbyMap> lobbyMap = Optional.ofNullable(GSON.fromJson(reader, LobbyMap.class));

            if(lobbyMap.isPresent()) {
                this.lobbyMap = lobbyMap.get();
            } else {
                Bukkit.getLogger().log(Level.WARNING, "LobbyMap could not be found");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void loadMapNames() {
        try {
            List<Path> subfolder = Files.walk(Paths.get("/home/Maps/Bedwars/" + bedwars.getMode().getMode()), 1)
                    .filter(Files::isDirectory)
                    .filter(it -> !it.getFileName().toString().equalsIgnoreCase("world"))
                    .collect(Collectors.toList());

            subfolder.remove(0);
            // 2x[0-9]
            // 2x2
            // -9x2-

            for (Path path : subfolder) {
                mapNames.add(path.getFileName().toString());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public GameMap loadGameMap(String mapName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("/home/Maps/Bedwars/" + bedwars.getMode().getMode() + "/" + mapName, MAP_FILE))) {
            gameMap = Optional.ofNullable(GSON.fromJson(reader, GameMap.class)).get();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return gameMap;
    }

    public LobbyMap getLobbyMap() {
        return lobbyMap;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public List<String> getMapNames() {
        return mapNames;
    }

    public static synchronized MapHelper getInstance(Bedwars bedwars) {
        if(instance == null) {
            new MapHelper(bedwars);
        }
        return instance;
    }
}
