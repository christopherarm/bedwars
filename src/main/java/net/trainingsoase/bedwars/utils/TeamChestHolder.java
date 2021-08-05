package net.trainingsoase.bedwars.utils;

import net.trainingsoase.bedwars.team.BedwarsTeam;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Objects;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class TeamChestHolder implements InventoryHolder {

    private final BedwarsTeam team;

    private Inventory inventory;

    public TeamChestHolder(BedwarsTeam team) {
        this.team = team;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public BedwarsTeam getTeam() {
        return team;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamChestHolder that = (TeamChestHolder) o;
        return team.getIdentifier().equals(that.team.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(team.getIdentifier());
    }
}
