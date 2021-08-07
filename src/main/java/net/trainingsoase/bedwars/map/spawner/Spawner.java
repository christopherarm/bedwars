package net.trainingsoase.bedwars.map.spawner;

import com.google.common.collect.Lists;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.GameMap;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.oreo.item.builder.ItemBuilder;
import net.trainingsoase.oreo.location.WrappedLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Spawner {

    private final Bedwars bedwars;

    private final List<Location> bronze;
    private final List<Location> iron;
    private final List<Location> gold;

    public Spawner(Bedwars bedwars) {
        this.bedwars = bedwars;

        this.bronze = new ArrayList<>();
        this.iron = new ArrayList<>();
        this.gold = new ArrayList<>();
    }

    public void startSpawners() {
        var gameMap = MapHelper.getInstance(bedwars).getGameMap();
        var phase = ((IngamePhase)bedwars.getLinearPhaseSeries().getCurrentPhase());

        for (int i = 0; i < gameMap.getBronzeSpawnerLocations().size(); i++) {
            Location loc = gameMap.getBronzeSpawnerLocations().get(i).toLocation();
            bronze.add(loc);
        }

        for (int i = 0; i < gameMap.getIronSpawnerLocations().size(); i++) {
            Location loc = gameMap.getIronSpawnerLocations().get(i).toLocation();
            iron.add(loc);
        }

        for (int i = 0; i < gameMap.getGoldSpawnerLocations().size(); i++) {
            Location loc = gameMap.getGoldSpawnerLocations().get(i).toLocation();
            gold.add(loc);
        }

        ItemStack bronze = new ItemBuilder(Material.CLAY_BRICK).setDisplayName("§cBronze").build();
        ItemStack iron = new ItemBuilder(Material.IRON_INGOT).setDisplayName("§fIron").build();
        ItemStack gold = new ItemBuilder(Material.GOLD_INGOT).setDisplayName("§6Gold").build();

        bedwars.runTaskTimer(() -> {
            this.bronze.forEach(loc -> Bukkit.getWorld((loc).getWorld().getName()).dropItem(loc, bronze)
                    .setVelocity(new Vector(0, 0, 0)));
        }, 13L, 13L);

        bedwars.runTaskTimer(() -> {
            this.iron.forEach(loc -> {
                Bukkit.getWorld((loc).getWorld().getName()).dropItem(loc, iron)
                        .setVelocity(new Vector(0, 0, 0));
                phase.getEffectStorage().playIronSpawn(loc);
            });
        }, 20 * 15L, 20 * 15L);


        if(bedwars.getVoting().getOnVotes().size() > bedwars.getVoting().getOffVotes().size()) {
            bedwars.runTaskTimer(() -> {
                this.gold.forEach(loc -> {
                    Bukkit.getWorld((loc).getWorld().getName()).dropItem(loc, gold)
                        .setVelocity(new Vector(0, 0, 0));
                    phase.getEffectStorage().playGoldSpawn(loc);
                });
            }, 20 * 60L, 20 * 60L);
        }
    }
}
