package net.trainingsoase.bedwars.item.specials.tntsheep;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TNTSheepPlaceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player = null;
    private Location startLocation = null;
    private Player targetPlayer = null;

    public TNTSheepPlaceEvent(Player player, Player targetPlayer,
                              Location startLocation) {
        this.player = player;
        this.startLocation = startLocation;
        this.targetPlayer = targetPlayer;
    }

    public static HandlerList getHandlerList() {
        return TNTSheepPlaceEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return TNTSheepPlaceEvent.handlers;
    }

    public CommandSender getPlayer() {
        return this.player;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(Location loc) {
        this.startLocation = loc;
    }

    public Player getTargetPlayer() {
        return this.targetPlayer;
    }

    public void setTargetPlayer(Player target) {
        this.targetPlayer = target;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
