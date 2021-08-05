package net.trainingsoase.bedwars.listener.protection;

import net.trainingsoase.bedwars.phase.EndingPhase;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class ProtectionHandler implements Listener {

    private LinearPhaseSeries<TimedPhase> phaseSeries;

    public ProtectionHandler(LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.phaseSeries = phaseSeries;
    }

    @EventHandler
    public void handleDropItem(final PlayerDropItemEvent event) {
        if ((!(phaseSeries.getCurrentPhase() instanceof IngamePhase))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePickupItem(final PlayerPickupItemEvent event) {
        if ((!(phaseSeries.getCurrentPhase() instanceof IngamePhase))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockBreak(final BlockBreakEvent event) {
        if ((!(phaseSeries.getCurrentPhase() instanceof IngamePhase))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockPlace(final BlockPlaceEvent event) {
        if ((!(phaseSeries.getCurrentPhase() instanceof IngamePhase))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityDamage(final EntityDamageEvent event) {
        if ((!(phaseSeries.getCurrentPhase() instanceof IngamePhase))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleAchievemtnAward(final PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleBedEnter(final PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleCraftItem(final CraftItemEvent event) {
        event.setCancelled(true);
    }
}
