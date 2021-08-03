package net.trainingsoase.bedwars.phase;

import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.message.MessageCache;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import de.dytanic.cloudnet.common.collection.Pair;
import de.dytanic.cloudnet.ext.bridge.server.BridgeServerHelper;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.map.shop.NPCShop;
import net.trainingsoase.bedwars.map.spawner.Spawner;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.bedwars.utils.ActionbarAPI;
import net.trainingsoase.bedwars.utils.CombatlogManager;
import net.trainingsoase.bedwars.utils.MapUtils;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.Phase;
import net.trainingsoase.hopjes.api.phase.TickDirection;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import net.trainingsoase.oreo.inventory.InventoryRows;
import net.trainingsoase.oreo.location.WrappedLocation;
import net.trainingsoase.oreo.scoreboard.ScoreboardAPI;
import net.trainingsoase.oreo.util.Locations;
import net.trainingsoase.oreo.util.Strings;
import net.trainingsoase.spectator.SpectatorService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.github.paperspigot.Title;
import org.redisson.connection.decoder.MapGetAllDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

    private SpectatorService spectatorService;

    private final List<Block> breakBlocks;

    private CombatlogManager combatlogManager;

    private BukkitTask bukkitTask;

    private final TranslatedObjectCache<String> bedDestroyedTitle;

    private final TranslatedObjectCache<String> bedDestroyedSubtitle;

    public IngamePhase(Game game, boolean async, Bedwars bedwars) {
        super("Ingame", game, 20, async);
        this.bedwars = bedwars;
        setPaused(false);
        this.setCurrentTicks(0);
        this.setEndTicks(3600);
        this.setTickDirection(TickDirection.UP);
        this.spawner = new Spawner(bedwars);
        this.npcShop = new NPCShop(bedwars);
        this.breakBlocks = new ArrayList<>();

        this.bedDestroyedTitle = new TranslatedObjectCache<>(locale ->
                bedwars.getLanguageProvider().getTextProvider().getString("bed_destroyed_title", locale));

        this.bedDestroyedSubtitle = new TranslatedObjectCache<>(locale ->
                bedwars.getLanguageProvider().getTextProvider().getString("bed_destroyed_subtitle", locale));

        this.spectatorService = SpectatorService.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        this.spectatorService.owningPlugin(bedwars).useChat().useItems(InventoryRows.ONE).useSpectatorLocation(MapHelper.getInstance(bedwars).getGameMap().getSpawn());
        this.combatlogManager = new CombatlogManager(bedwars);

        Bukkit.getScheduler().runTaskLater(bedwars, () -> {
            spawner.startSpawners();
            npcShop.spawnNPCs();
        }, 5);

        String gold = bedwars.getVoting().getOnVotes().size() > bedwars.getVoting().getOffVotes().size() ? "item_on" : "item_off";

        //TODO: Reduce parsing with the actionbar
        bukkitTask = bedwars.runTaskTimer(() -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                IOasePlayer iOasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());

                bedwars.getTeamService().getTeam(onlinePlayer).ifPresent(bedwarsTeam ->
                        ActionbarAPI.setActionBarFor(onlinePlayer, WrappedChatComponent.fromText(bedwars.getLanguageProvider().getTextProvider()
                        .format("game_actionbar", iOasePlayer.getLocale(),
                                bedwars.getLanguageProvider().getTextProvider()
                                        .format(bedwarsTeam.getIdentifier(), iOasePlayer.getLocale(), bedwarsTeam.getColorData().getChatColor()),
                                bedwars.getLanguageProvider().getTextProvider().format(gold, iOasePlayer.getLocale())))));

            }
        }, 5, 40);

        bedwars.getTeamService().getTeams().removeIf(bedwarsTeam -> bedwarsTeam.getPlayers().isEmpty());

        setIngameScoreboard();

        BridgeServerHelper.setMotd("Ingame");
        BridgeServerHelper.setMaxPlayers(32);
        BridgeServerHelper.changeToIngame();
        BridgeServerHelper.updateServiceInfo();
    }

    @Override
    protected void onFinish() {
        if(bukkitTask != null) {
            bukkitTask.cancel();
        }
        spectatorService.unregister();
    }

    @Override
    public void onUpdate() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ScoreboardAPI.INSTANCE.updateTeam(player, "time", "§d", " §7Zeit §8» ", "§6" + Strings.getTimeString(getCurrentTicks()));
        });
    }

    @Override
    public void onSkip() {
        finish();
    }

    private void setIngameScoreboard() {
        final HashMap<String, Integer> sidebar = new HashMap<>();

        int counter = 3;

        for (BedwarsTeam team : bedwars.getTeamService().getTeams()) {
            sidebar.putIfAbsent(team.getColorData().getChatColor().toString(), counter);
            counter++;
        }

        sidebar.put("§8§m----------------", counter+3);
        sidebar.put("§7§r", counter+2);
        sidebar.put("§d", counter+1);
        sidebar.put("§d§r", counter);
        sidebar.put("§8", 2);
        sidebar.put("§r§8§m----------------", 1);
        sidebar.put("§c§oBedwars", 0);

        for (Player player : Bukkit.getOnlinePlayers()) {
            IOasePlayer iOasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());
            ScoreboardAPI.INSTANCE.setSidebar(player, DisplaySlot.SIDEBAR, "§e§lTrainingsOase", sidebar);
            ScoreboardAPI.INSTANCE.updateTeam(player, "time", "§d", " §7Zeit §8» ", "§6" + Strings.getTimeString(getCurrentTicks()));

            List<BedwarsTeam> teams = bedwars.getTeamService().getTeams();

            for (BedwarsTeam team : teams) {
                String active = team.getPlayers().size() == 0 ? "§7" : "§c";
                ScoreboardAPI.INSTANCE.updateTeam(player, team.getIdentifier(), team.getColorData().getChatColor().toString(), "§7(§6" + team.getCurrentSize() + "§7) " + active + "❤ ",
                        bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier()
                                , iOasePlayer.getLocale()
                                , team.getColorData().getChatColor()));
            }
        }
    }

    private BedwarsTeam getTeamByColor(String color) {
        for (BedwarsTeam team : bedwars.getTeamService().getTeams()) {
            if (team.getColorData().toString().toLowerCase().equals(color)) {
                return team;
            }
        }
        return null;
    }

    public CombatlogManager getCombatlogManager() {
        return combatlogManager;
    }

    public SpectatorService getSpectatorService() {
        return spectatorService;
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
    public void handleBlockPlace(final BlockPlaceEvent event) {
        breakBlocks.add(event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if(event.getBlock().getType() != Material.BED_BLOCK) {
            if(!breakBlocks.contains(event.getBlock())) {
                event.setCancelled(true);
            }
            return;
        }

        if (event.getBlock().getType() == Material.BED_BLOCK) {
            var gameMap = MapHelper.getInstance(bedwars).getGameMap();

            var bedLocation = gameMap.getBedLocations();

            for (WrappedLocation value : gameMap.getBedLocations().values()) {
                if (Locations.compare(event.getBlock().getLocation(), value.toLocation(), false)) {
                    var bedID = MapUtils.getKeyByValue(bedLocation, value);

                    if (bedID == null) return;

                    BedwarsTeam team = getTeamByColor(SPLIT_PATTERN.split(bedID)[0]);

                    if (team == null) return;

                    if (team.isIn(player)) {
                        bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "bed_cant_own");
                        event.setCancelled(true);
                        return;
                    }

                    if (team.getCurrentSize() == 0) {
                        bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "bed_team_already_destroyed",
                                bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier(), oasePlayer.getLocale()));
                        event.setCancelled(true);
                        return;
                    }

                    team.setHasBed(false);

                    for (Player teamPlayer : team.getPlayers()) {
                        IOasePlayer onlinePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(teamPlayer.getUniqueId());
                        teamPlayer.sendTitle(new Title(bedDestroyedTitle.get(onlinePlayer.getLocale()),
                                bedDestroyedSubtitle.get(onlinePlayer.getLocale()), 10, 10, 10));
                    }

                     bedwars.getTeamService().getTeam(player).ifPresent(playerTeam -> {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            IOasePlayer iOasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());

                            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.WITHER_DEATH, 1f, 1f);

                            bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), iOasePlayer, "bed_destroyed",
                                    playerTeam.getColorData().getChatColor() + player.getDisplayName(),
                                    bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier(), iOasePlayer.getLocale(), team.getColorData().getChatColor()));

                            ScoreboardAPI.INSTANCE.updateTeam(onlinePlayer, team.getIdentifier(), team.getColorData().getChatColor().toString(), "§7(§6" + team.getCurrentSize() + "§7) ❤ ",
                                    bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier()
                                            , iOasePlayer.getLocale()
                                            , team.getColorData().getChatColor()));
                        }
                    });

                    Bukkit.getScheduler().runTaskLater(bedwars, () -> {
                        for (Entity ent : event.getBlock().getWorld().getEntities()) {
                            if (ent.getLocation().distanceSquared(event.getBlock().getLocation()) < 3.0D && ent.getType() == EntityType.DROPPED_ITEM) {
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
    public void handleEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if ((event.getDamager() instanceof Player && event.getEntity() instanceof Player) || (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player)) {
            if(event.getDamager() instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
                if (!(shooter instanceof Player)) {
                    return;
                }
            }

            final Player damager = (Player) event.getDamager();
            final Player player = (Player) event.getEntity();

            Optional<BedwarsTeam> playerTeam = bedwars.getTeamService().getTeam(player);
            Optional<BedwarsTeam> damagerTeam = bedwars.getTeamService().getTeam(damager);

            if (playerTeam.isPresent() && damagerTeam.isPresent()) {
                if (playerTeam.get().equals(damagerTeam.get())) {
                    event.setCancelled(true);
                    return;
                }

                combatlogManager.getCombatLogMap().put(player, new Pair<>(10, damager));
            }
        }
    }

    @EventHandler
    public void handleDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();

        checkDeath(player, true);

        event.setDeathMessage(null);
    }

    @EventHandler
    public void handleMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if(spectatorService.getPlayers().contains(player)) return;

        if(player.getLocation().getY() < 60) {
            player.damage(100.0D);
        }
    }

    @EventHandler
    public void handleNPCClick(final PlayerNPCInteractEvent event) {
        final Player player = event.getPlayer();
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if(spectatorService.getPlayers().contains(event.getPlayer())) return;

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

    @EventHandler
    public void handleTeleport(final PlayerTeleportEvent event) {
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            event.getPlayer().setNoDamageTicks(0);
            event.getPlayer().setFallDistance(0f);
        }
    }

    private void checkDeath(Player player, boolean isDead) {
        bedwars.getTeamService().getTeam(player).ifPresent(team -> {
            if(team.hasBed()) {
                Bukkit.getScheduler().runTaskLater(bedwars, () -> {
                    if(isDead) {
                        player.spigot().respawn();
                    }
                    player.setNoDamageTicks(Integer.MAX_VALUE);
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    player.teleport(MapHelper.getInstance(bedwars).getGameMap().getSpawnLocations().get(team.getColorData().toString().toLowerCase()).toLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    player.getVelocity().zero();
                    player.setHealth(20.0D);
                    player.setFoodLevel(20);
                    player.setFireTicks(0);
                }, 1);

                var killer = combatlogManager.getCombatLogMap().get(player);

                if (killer != null && killer.getSecond() != null) {
                    bedwars.getTeamService().getTeam(killer.getSecond()).ifPresent(killerTeam -> {
                       // player.setDisplayName(team.getColorData().getChatColor() + player.getName());
                        sendMessage("game_player_killed", team.getColorData().getChatColor() + player.getDisplayName(), killerTeam.getColorData().getChatColor() + killer.getSecond().getDisplayName());
                    });
                    combatlogManager.getCombatLogMap().remove(player);
                } else {
                    sendMessage("game_player_died", team.getColorData().getChatColor() + player.getDisplayName());
                }
                return;
            }

            if(isDead) {
                player.spigot().respawn();
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getVelocity().zero();
            player.setHealth(20.0D);
            player.setFoodLevel(20);
            player.setFireTicks(0);

            if(team.getPlayers().size() > 1) {
                team.removePlayer(player);
                spectatorService.add(player);

                for (IOasePlayer currentOnlinePlayer : OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers()) {
                    ScoreboardAPI.INSTANCE.updateTeam(Bukkit.getPlayer(currentOnlinePlayer.getUUID()), team.getIdentifier(), team.getColorData().getChatColor().toString(), "§7(§6" + team.getCurrentSize() + "§7) ❤ ",
                            bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier()
                                    , currentOnlinePlayer.getLocale()
                                    , team.getColorData().getChatColor()));
                }

            } else if(team.getPlayers().size() == 1) {
                team.getPlayers().clear();
                bedwars.getTeamService().remove(team);

                for (IOasePlayer currentOnlinePlayer : OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers()) {
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), currentOnlinePlayer, "game_team_lost",
                            bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier(), currentOnlinePlayer.getLocale(), team.getColorData().getChatColor()));

                    //TODO: Message parsing
                    ScoreboardAPI.INSTANCE.updateTeam(Bukkit.getPlayer(currentOnlinePlayer.getUUID()), team.getIdentifier(), team.getColorData().getChatColor().toString(), "§7(§6" + team.getCurrentSize() + "§7) ❤ ",
                            bedwars.getLanguageProvider().getTextProvider().format(team.getIdentifier()
                                    , currentOnlinePlayer.getLocale()
                                    , team.getColorData().getChatColor()));
                }
            }

            if(bedwars.getTeamService().getTeams().size() == 1) {
                Bukkit.getScheduler().runTaskLater(bedwars, this::onSkip, 2);
                return;
            }

            spectatorService.add(player);
        });
    }

    private void sendMessage(String key, Object... arguments) {
        var cache = new MessageCache(bedwars.getLanguageProvider(), key, arguments);

        for (IOasePlayer currentOnlinePlayer : OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers()) {
            bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), currentOnlinePlayer, cache.getMessage(currentOnlinePlayer.getLocale()));
        }
    }
}
