package net.trainingsoase.bedwars.listener.map;

import net.trainingsoase.bedwars.api.MapLoadedEvent;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class MapLoadedHandler implements Listener {

    @EventHandler
    public void handleMapLoad(final MapLoadedEvent event) {
        final World world = event.getWorld();
    }
}
