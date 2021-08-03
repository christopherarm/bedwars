package net.trainingsoase.bedwars.map;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.api.MapLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class SlimeManager {

    private static final SlimeWorld.SlimeProperties PROPERTIES = SlimeWorld.SlimeProperties.builder()
            .difficulty(1)
            .allowAnimals(true)
            .allowMonsters(false)
            .readOnly(true)
            .build();

    public static SlimeLoader SLIME_LOADER;
    private final SlimePlugin slimePlugin;
    private final Bedwars bedwars;

    public SlimeManager(Bedwars bedwars) {
        this.bedwars = bedwars;
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SLIME_LOADER = slimePlugin.getLoader("mongodb");
    }

    public void loadGameArena(@NotNull String map, @NotNull GameMap gameMap) {
        try {
            String mapname = gameMap.getMode() + "_" + map;
            SLIME_LOADER.unlockWorld(mapname);
            CompletableFuture<SlimeWorld> futureSlimeWorld = CompletableFuture.completedFuture(slimePlugin.loadWorld(SLIME_LOADER, mapname,  PROPERTIES));

            futureSlimeWorld.whenComplete((slimeWorld, throwable) -> {
                CompletableFuture<Void> worldFuture = CompletableFuture.runAsync(() -> {
                    slimePlugin.generateWorld(slimeWorld);
                });

                worldFuture.whenComplete((unused, throwable1) -> {
                    World createdWorld = Bukkit.getWorld(mapname);

                    try {
                        SLIME_LOADER.unlockWorld(mapname);
                    } catch (UnknownWorldException | IOException e) {
                        bedwars.getLogger().log(Level.SEVERE, "SLIME: Could not unlock the world in mongodb");
                        e.printStackTrace();
                    }

                    MapLoadedEvent mapLoadedEvent = new MapLoadedEvent(createdWorld, gameMap);
                    bedwars.getServer().getPluginManager().callEvent(mapLoadedEvent);
                });
            });
        } catch (UnknownWorldException | NewerFormatException | CorruptedWorldException | IOException | WorldInUseException e) {
            Bukkit.getOnlinePlayers().forEach(all -> {
                all.sendMessage("§cERROR: The Server you was previously on was misconfigurated, please contact a staff member.");
            });
            Bukkit.shutdown();
            e.printStackTrace();
        }
    }
}
