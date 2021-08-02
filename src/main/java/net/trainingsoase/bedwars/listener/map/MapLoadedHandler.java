package net.trainingsoase.bedwars.listener.map;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.api.MapLoadedEvent;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
            for (BedwarsTeam team : bedwars.getTeamService().getTeams()) {
                for (Player player : team.getPlayers()) {
                    var spawnloc = event.getGameMap().getSpawnLocations().get(team.getColorData().toString().toLowerCase()).toLocation();

                    player.teleport(spawnloc);
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setBedSpawnLocation(spawnloc, true);
                }
            }
        });
    }
}
