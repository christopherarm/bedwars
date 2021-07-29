package net.trainingsoase.bedwars.listener.map;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.api.MapLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class MapLoadedHandler implements Listener {

    private final Bedwars bedwars;

    public MapLoadedHandler(Bedwars bedwars) {
        this.bedwars = bedwars;
    }

    @EventHandler
    public void handleMapLoad(final MapLoadedEvent event) {
        bedwars.runTask(() -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.teleport(event.getGameMap().getSpawnLocations().get("at").toLocation());
            }
        });
    }
}
