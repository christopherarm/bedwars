package net.trainingsoase.bedwars.inventory;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.oreo.item.builder.ColoredBuilder;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class InventoryService {

    public static final ItemStack GLASS_PANE = new ColoredBuilder(ColoredBuilder.DyeType.GLASS_PANE)
            .setColor(DyeColor.GRAY).setEmptyName().build();

    private static InventoryService instance;

    private Teamselector teamselector;

    private Mapvoting mapvoting;

    public InventoryService(Bedwars bedwars) {
        teamselector = new Teamselector(bedwars);
        mapvoting = new Mapvoting(bedwars);
    }

    public Teamselector getTeamselector() {
        return teamselector;
    }

    public Mapvoting getMapvoting() {
        return mapvoting;
    }

    public static synchronized InventoryService getInstance(Bedwars bedwars) {
        if(instance == null) {
            return new InventoryService(bedwars);
        }
        return instance;
    }
}
