package net.trainingsoase.bedwars.listener.player;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.LobbyMap;
import net.trainingsoase.bedwars.map.MapHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class PlayerSpawnLocationHandler implements Listener {

    private final Bedwars bedwars;

    public PlayerSpawnLocationHandler(Bedwars bedwars) {
        this.bedwars = bedwars;
    }

    @EventHandler
    public void handleSpawnLocation(final PlayerSpawnLocationEvent event) {
        LobbyMap lobbyMap = MapHelper.getInstance(bedwars).getLobbyMap();

        if(lobbyMap != null && lobbyMap.getSpawn() != null) {
            event.setSpawnLocation(lobbyMap.getSpawn().toLocation());
        }
    }
}
