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
import net.trainingsoase.hopjes.api.ColorData;
import net.trainingsoase.hopjes.api.phase.TickDirection;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import net.trainingsoase.oreo.location.WrappedLocation;
import net.trainingsoase.oreo.scoreboard.ScoreboardAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.DisplaySlot;
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
        this.setCurrentTicks(0);
        this.setEndTicks(3600);
        this.setTickDirection(TickDirection.UP);
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

        String gold = bedwars.getVoting().getOnVotes().size() > bedwars.getVoting().getOffVotes().size() ? "item_on" : "item_off";

        bedwars.runTaskTimer(() -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayerAsync(onlinePlayer.getUniqueId()).thenAccept(iOasePlayer -> {
                    bedwars.getTeamService().getTeam(onlinePlayer).ifPresent(bedwarsTeam ->
                            ActionbarAPI.setActionBarFor(onlinePlayer, WrappedChatComponent.fromText(bedwars.getLanguageProvider().getTextProvider()
                            .format("game_actionbar", iOasePlayer.getLocale(),
                                    bedwars.getLanguageProvider().getTextProvider()
                                            .format(bedwarsTeam.getIdentifier(), iOasePlayer.getLocale(), bedwarsTeam.getColorData().getChatColor()),
                                    bedwars.getLanguageProvider().getTextProvider().format(gold, iOasePlayer.getLocale())))));

                });
            }
        }, 5, 40);

        setIngameScoreboard();
    }

    @Override
    protected void onFinish() {
        Bukkit.shutdown();
    }

    @Override
    public void onUpdate() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ScoreboardAPI.INSTANCE.updateTeam(player, "time", "§d", " §7Zeit §8» ", "§6" + convertToSeconds(getCurrentTicks()));
        });

    }

    private void setIngameScoreboard() {
        final HashMap<String, Integer> sidebar = new HashMap<>();
        sidebar.put("§8§m----------------", 14);
        sidebar.put("§7§r", 13);
        sidebar.put("§d", 12);
        sidebar.put("§d§r", 11);
        sidebar.put(ColorData.RED.getChatColor().toString(), 10);
        sidebar.put(ColorData.BLUE.getChatColor().toString(), 9);
        sidebar.put(ColorData.LIGHT_GREEN.getChatColor().toString(), 8);
        sidebar.put(ColorData.YELLOW.getChatColor().toString(), 7);
        sidebar.put(ColorData.GREEN.getChatColor().toString(), 6);
        sidebar.put(ColorData.GOLD.getChatColor().toString(), 5);
        sidebar.put(ColorData.WHITE.getChatColor().toString(), 4);
        sidebar.put(ColorData.AQUA.getChatColor().toString(), 3);
        sidebar.put("§8", 2);
        sidebar.put("§r§8§m----------------", 1);
        sidebar.put("§c§oBedwars", 0);

        Bukkit.getOnlinePlayers().forEach(player -> {
            OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayerAsync(player.getUniqueId()).thenAccept(iOasePlayer -> {
                ScoreboardAPI.INSTANCE.setSidebar(player, DisplaySlot.SIDEBAR, "§e§lTrainingsOase", sidebar);
                ScoreboardAPI.INSTANCE.updateTeam(player, "time", "§d", " §7Zeit §8» ", "§6" + convertToSeconds(getCurrentTicks()));

                List<BedwarsTeam> teams = bedwars.getTeamService().getTeams();

                for (BedwarsTeam team : teams) {
                    ScoreboardAPI.INSTANCE.updateTeam(player, team.getIdentifier(), team.getColorData().getChatColor().toString(), "§7(§6" + team.getCurrentSize() + "§7) §c❤ ",
                            bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier()
                                    , iOasePlayer.getLocale()
                                    , team.getColorData().getChatColor()));
                }
            });
        });


    }

    private String convertToSeconds(int time) {
        int stunden = time / 3600;
        int minuten = (time - stunden * 3600) / 60;
        int sekunden = time - stunden * 3600 - minuten * 60;
        return String.format("%02d:%02d", minuten, sekunden);
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
    public void handleNPCClick(final PlayerNPCInteractEvent event) {
        final Player player = event.getPlayer();
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if(event.getUseAction() == PlayerNPCInteractEvent.EntityUseAction.INTERACT) {
            player.openInventory(bedwars.getShop().getShopInventory(oasePlayer.getLocale()));
        }
    }

    @EventHandler
    public void handleChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        String msg = event.getMessage().replace("%", "%%");
        bedwars.getTeamService().getTeam(player).ifPresent(team -> {
            msg.replaceAll("%", "%%");

            if(bedwars.getMode().getPlayers() / bedwars.getMode().getTeams() == 1) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§8§l「§a§lAll§8§l」" + team.getColorData().getChatColor() + player.getDisplayName() + " §8» §7" + msg.replace("@a", "").replace("@all", ""));
                    event.setCancelled(true);
                }
                return;
            }

            if(msg.startsWith("@a") || msg.startsWith("@all")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§8§l「§a§lAll§8§l」" + team.getColorData().getChatColor() + player.getDisplayName() + " §8» §7"+ msg.replace("@all", "").replace("@a", ""));
                    event.setCancelled(true);
                }
                return;
            }

            for (Player all : team.getPlayers()) {
                all.sendMessage("§8§l「" + team.getColorData().getChatColor() + "Team§8§l」§7" + player.getDisplayName() + " §8» §7" + msg);
                event.setCancelled(true);
            }
        });
    }
}
