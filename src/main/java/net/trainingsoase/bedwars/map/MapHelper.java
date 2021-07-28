package net.trainingsoase.bedwars.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.trainingsoase.oreo.location.WrappedLocation;
import net.trainingsoase.oreo.location.adapter.WrappedLocationTypeAdapter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class MapHelper {

    private static MapHelper instance;

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

    public MapHelper() {
        instance = this;
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

    public LobbyMap getLobbyMap() {
        return lobbyMap;
    }

    public static synchronized MapHelper getInstance() {
        if(instance == null) {
            new MapHelper();
        }
        return instance;
    }
}
