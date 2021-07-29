package net.trainingsoase.bedwars.api;

import net.trainingsoase.bedwars.map.GameMap;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class MapLoadedEvent extends Event {

    public World world;

    public GameMap gameMap;

    public MapLoadedEvent(World world, GameMap gameMap) {
        this.world = world;
        this.gameMap = gameMap;
    }

    public static HandlerList hl = new HandlerList();

    public HandlerList getHandlers()
    {
        return hl;
    }

    public static HandlerList getHandlerList()
    {
        return hl;
    }

    public World getWorld() {
        return world;
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}
