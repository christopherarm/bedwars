package net.trainingsoase.bedwars.item.specials.warppowder;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.slikey.effectlib.Effect;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.utils.ActionbarAPI;
import net.trainingsoase.bedwars.utils.effects.EffectStorage;
import net.trainingsoase.data.OaseAPIImpl;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Warppowder  implements Runnable{

    public final Map<Player, ActivatedTeleport> teleportMap;

    private final EffectStorage effectStorage;

    private final Bedwars bedwars;

    public Warppowder(Bedwars bedwars) {
        this.bedwars = bedwars;
        this.effectStorage = new EffectStorage();
        this.teleportMap = new HashMap<>();
    }

    public void startTeleport(Player player) {
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if (this.teleportMap.containsKey(player)) return;

        this.teleportMap.put(player, new ActivatedTeleport(player, player.getLocation(), effectStorage.playTeleportEffect(player.getLocation())));
        ActionbarAPI.setActionBarFor(player, WrappedChatComponent.fromText(
                bedwars.getLanguageProvider().getTextProvider().format("specials_warp_started_teleporting", oasePlayer.getLocale())
        ));
        player.setMetadata("isTPProgress", new FixedMetadataValue(bedwars, true));
    }

    public boolean isTeleporting(Player player) {
        return this.teleportMap.containsKey(player);
    }

    private void doTeleport(final Player player) {
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if (!player.getInventory().contains(Material.SULPHUR)) return;
        if (player.getWorld().getHighestBlockYAt(player.getLocation()) > player.getEyeLocation().getBlockY()) {
            bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "specials_warp_teleporting_aborted_block_above");
            return;
        }
        removeItem(player, new ItemStack(Material.SULPHUR));
        player.getWorld().playSound(player.getEyeLocation(), Sound.FIREWORK_LAUNCH, 1F, 0.8F);

        new BukkitRunnable() {
            int time = 30;
            @Override
            public void run() {
                if (--time <= 0) {
                    player.setVelocity(new Vector());
                    player.setFallDistance(0F);

                    bedwars.getTeamService().getTeam(player).ifPresent(team -> {
                        player.teleport(MapHelper.getInstance(bedwars).getGameMap().getSpawnLocations().get(team.getColorData().toString().toLowerCase()).toLocation());
                        player.setFallDistance(0F);
                    });

                    player.removeMetadata("isTPProgress", bedwars);
                    this.cancel();
                    return;
                }

                player.setVelocity(new Vector(0, 4, 0));
            }
        }.runTaskTimer(bedwars, 0L, 1L);
    }

    public void removeTeleport(Player player) {
        ActivatedTeleport teleport = this.teleportMap.get(player);
        if (teleport != null)
            this.removeTeleport(teleport);
    }

    public void removeTeleport(ActivatedTeleport teleport) {
        this.removeTeleport(teleport, true);
    }

    public void removeTeleport(ActivatedTeleport teleport, boolean removeMessage) {
        this.teleportMap.remove(teleport.getPlayer());
        teleport.getEffect().cancel();
    }

    public void clearTeleports() {
        for (ActivatedTeleport teleport : this.teleportMap.values()) {
            teleport.getEffect().cancel();
        }
        this.teleportMap.clear();
    }

    @Override
    public void run() {
        List<ActivatedTeleport> toRemove = new ArrayList<>();

        for (ActivatedTeleport teleport : this.teleportMap.values()) {
            final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(teleport.getPlayer().getUniqueId());

            if (teleport.getPlayer().getLocation().distanceSquared(teleport.getLocation()) > 0.2F) {
                bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "specials_warp_do_not_move");
                toRemove.add(teleport);
                continue;
            }

            if (teleport.getTime() <= 0) {
                this.doTeleport(teleport.getPlayer());
                toRemove.add(teleport);
                continue;
            }

            this.updateTimeBar(teleport.getPlayer(), teleport.getTime());
            teleport.getPlayer().playSound(teleport.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1F, 0.7F);
            teleport.setTime(teleport.getTime() - 1);
        }

        for (ActivatedTeleport teleport : toRemove) {
            this.removeTeleport(teleport, false);
        }
    }

    private void updateTimeBar(Player player, int time) {
        String blocks = "";
        int red = 5 - time;

        for (int i = 0; i < red; i++) {
            blocks = blocks + ChatColor.GREEN + "\u2588";
        }

        for (int i = 0; i < time; i++) {
            blocks = blocks + ChatColor.RED + "\u2588";
        }
        ActionbarAPI.setActionBarFor(player, WrappedChatComponent.fromText("§7§lTeleport in "  + time + " " + blocks));
    }

    private boolean removeItem(Player player, ItemStack item) {
        PlayerInventory inventory = player.getInventory();
        List<Integer> slots = new ArrayList<>();
        int amount = item.getAmount();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (amount <= 0) break;
            ItemStack i = inventory.getItem(slot);

            if (i != null && i.getType() == item.getType()) {
                amount -= i.getAmount();
                slots.add(slot);
            }
        }

        if (amount > 0) {
            // The player has too little items
            return false;
        }

        amount = item.getAmount();
        for (int slot : slots) {
            ItemStack i = inventory.getItem(slot);

            amount -= i.getAmount();
            if (amount <= 0) {
                // Processed all items
                i.setAmount(Math.abs(amount));
                inventory.setItem(slot, i);
                break;
            } else {
                inventory.setItem(slot, null);
            }
        }

        return true;
    }

    public class ActivatedTeleport {
        private final Player player;
        private final Location location;
        private int time = 5;
        private final Effect effect;

        public ActivatedTeleport(Player player, Location location, Effect effect) {
            this.player = player;
            this.location = location;
            this.effect = effect;
        }

        public Player getPlayer() {
            return player;
        }

        public Location getLocation() {
            return location;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public Effect getEffect() {
            return effect;
        }
    }
}
