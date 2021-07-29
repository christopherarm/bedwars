package net.trainingsoase.bedwars.inventory;

import net.trainingsoase.bedwars.Bedwars;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class InventoryService {

    private static InventoryService instance;

    private Teamselector teamselector;

    public InventoryService(Bedwars bedwars) {
        teamselector = new Teamselector(bedwars);
    }

    public Teamselector getTeamselector() {
        return teamselector;
    }

    public static synchronized InventoryService getInstance(Bedwars bedwars) {
        if(instance == null) {
            return new InventoryService(bedwars);
        }
        return instance;
    }
}
