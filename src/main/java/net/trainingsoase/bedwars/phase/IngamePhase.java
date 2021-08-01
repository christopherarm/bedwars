package net.trainingsoase.bedwars.phase;

import at.rxcki.strigiformes.message.MessageCache;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.map.shop.NPCShop;
import net.trainingsoase.bedwars.map.spawner.Spawner;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.bedwars.utils.ActionbarAPI;
import net.trainingsoase.bedwars.utils.MapUtils;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.data.model.OasePlayer;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import net.trainingsoase.oreo.location.WrappedLocation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class IngamePhase extends TimedPhase implements Listener {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\.");

    private final Bedwars bedwars;

    private Spawner spawner;

    private NPCShop npcShop;

    public IngamePhase(Game game, boolean async, Bedwars bedwars) {
        super("Ingame", game, 20, async);
        this.bedwars = bedwars;
        setPaused(false);
        this.setCurrentTicks(3600);
        this.addPhaseListener(this);
        this.spawner = new Spawner(bedwars);
        this.npcShop = new NPCShop(bedwars);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bukkit.getScheduler().runTaskLater(bedwars, () -> {
            spawner.startSpawners();
            npcShop.spawnNPCs();
        }, 5);

        bedwars.runTaskTimer(() -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayerAsync(onlinePlayer.getUniqueId()).thenAccept(iOasePlayer -> {
                    bedwars.getTeamService().getTeam(onlinePlayer).ifPresent(bedwarsTeam -> {
                        ActionbarAPI.setActionBarFor(onlinePlayer, WrappedChatComponent.fromText(bedwars.getLanguageProvider().getTextProvider()
                                .format("game_actionbar", iOasePlayer.getLocale(),
                                        bedwars.getLanguageProvider().getTextProvider()
                                                .format(bedwarsTeam.getIdentifier(), iOasePlayer.getLocale(), bedwarsTeam.getColorData().getChatColor()))));
                    });

                });
            }
        }, 5, 40);
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onTick() {
    }

    private BedwarsTeam getTeamByColor(String color) {
        for (BedwarsTeam team : bedwars.getTeamService().getTeams()) {
            if (team.getColorData().toString().toLowerCase().equals(color)) {
                return team;
            }
        }
        return null;
    }

    @EventHandler
    public void handleInteract(final PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BED_BLOCK) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handleBlockBreak(final BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.BED_BLOCK) {
            final Player player = event.getPlayer();

            var gameMap = MapHelper.getInstance(bedwars).getGameMap();
            var bedLocation = gameMap.getBedLocations();

            for (WrappedLocation value : gameMap.getBedLocations().values()) {
                if (WrappedLocation.of(event.getBlock().getLocation()).equals(value)) {
                    var bedID = MapUtils.getKeyByValue(bedLocation, value);

                    if (bedID == null) return;

                    BedwarsTeam team = getTeamByColor(SPLIT_PATTERN.split(bedID)[0]);

                    if (team == null) return;

                    if (team.getPlayers().contains(player)) {
                        player.sendMessage("Nicht selber zerstören");
                        event.setCancelled(true);
                        return;
                    }

                    if (team.getCurrentSize() == 0) {
                        player.sendMessage("Team existiert nicht");
                        event.setCancelled(true);
                        return;
                    }

                    team.setHasBed(false);
                    player.sendMessage("Du hast das Bett von " + team.getIdentifier() + " zerstört");

                    Bukkit.getScheduler().runTaskLater(bedwars, () -> {
                        for (Entity ent : event.getBlock().getWorld().getEntities()) {
                            if (ent.getLocation().distance(event.getBlock().getLocation()) < 3.0D && ent.getType() == EntityType.DROPPED_ITEM) {
                                Item e = (Item) ent;
                                if (e.getItemStack().getType() == Material.BED || e.getItemStack().getType() == Material.BED_BLOCK)
                                    ent.remove();
                            }
                        }
                    }, 3L);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void handleDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();

        event.setDeathMessage(null);
    }

    private final List<Player> falldownPlayers = new ArrayList<>();

    @EventHandler
    public void handleMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if(player.getLocation().getY() < 50) {
            OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayerAsync(player.getUniqueId()).thenAccept(iOasePlayer -> {
                var deadTeam = bedwars.getTeamService().getTeam(player);

                if(!deadTeam.isPresent()) return;

                if(deadTeam.get().hasBed()) {
                    falldownPlayers.add(player);
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    player.teleport(MapHelper.getInstance(bedwars).getGameMap().getSpawnLocations().get(deadTeam.get().getColorData().toString().toLowerCase()).toLocation());
                    player.setVelocity(new Vector().zero());
                    player.setHealth(20.0D);
                    player.setFoodLevel(20);
                    player.setFireTicks(0);
                    return;
                }
            });
        }
    }

    @EventHandler
    public void handleEntityDamage(final EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player) event.getEntity();
            if(falldownPlayers.contains(player)) {
                event.setDamage(0);
                event.setCancelled(true);
                falldownPlayers.remove(player);
            }
        }
    }

    @EventHandler
    public void handleNPCClick(PlayerNPCInteractEvent event) {
        final Player player = event.getPlayer();
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if(event.getUseAction() == PlayerNPCInteractEvent.EntityUseAction.INTERACT) {
            player.openInventory(bedwars.getShop().getShopInventory(oasePlayer.getLocale()));
        }
    }
}
