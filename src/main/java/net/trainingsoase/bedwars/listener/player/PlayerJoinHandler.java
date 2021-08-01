package net.trainingsoase.bedwars.listener.player;

import at.rxcki.strigiformes.message.MessageCache;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.api.player.IPlayerExecutor;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.item.JoinItems;
import net.trainingsoase.bedwars.utils.ActionbarAPI;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import net.trainingsoase.bedwars.phase.LobbyPhase;

import java.util.HashMap;
import java.util.UUID;

import static net.trainingsoase.oreo.scoreboard.ScoreboardAPI.INSTANCE;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class PlayerJoinHandler implements Listener {

    private final Bedwars bedwars;

    private final LinearPhaseSeries<TimedPhase> phaseSeries;

    private final HashMap<String, Integer> sidebar = new HashMap<>();

    private final JoinItems joinItems;

    private final IPlayerExecutor<UUID, IOasePlayer> playerExecutor;

    private BukkitTask bukkitTask;

    public PlayerJoinHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.bedwars = bedwars;
        this.phaseSeries = phaseSeries;

        sidebar.put("§8§m----------------", 7);
        sidebar.put("§7", 6);
        sidebar.put(" §8➥ §7", 5);
        sidebar.put(" §b", 4);
        sidebar.put("§e■ §7Map:", 3);
        sidebar.put(" §8➥ §e", 2);
        sidebar.put("§r§8§m----------------", 1);
        sidebar.put("§c§oBedwars", 0);

        /*sidebar.put("§8§m----------------", 14);
        sidebar.put("§7", 13);
        sidebar.put(" §7Zeit §8» §600:00", 12);
        sidebar.put("§d", 11);
        sidebar.put(" §c❤ §cÖsterreich", 10);
        sidebar.put(" §c❤ §9Russland", 9);
        sidebar.put(" §c❤ §aItalien", 8);
        sidebar.put(" §c❤ §eDeutschland", 7);
        sidebar.put(" §c❤ §6Spanien", 6);
        sidebar.put(" §c❤ §fPolen", 5);
        sidebar.put(" §c❤ §2Portugal", 4);
        sidebar.put(" §c❤ §bFrankreich", 3);
        sidebar.put("§8", 2);
        sidebar.put("§r§8§m----------------", 1);
        sidebar.put("§c§oBedwars", 0);*/

        this.playerExecutor = OaseAPIImpl.INSTANCE.getPlayerExecutor();

        this.joinItems = ((LobbyPhase) phaseSeries.getCurrentPhase()).getJoinItems();

        sendCountDownBar();
    }

    @EventHandler
    public void handleJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);
        final Player player = event.getPlayer();
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if (phaseSeries.getCurrentPhase() instanceof LobbyPhase) {
            var lobbyPhase = (LobbyPhase) phaseSeries.getCurrentPhase();
            lobbyPhase.checkStartCondition();

            var cache = new MessageCache(bedwars.getLanguageProvider(), "join_message",
                    player.getDisplayName(),
                    Bukkit.getOnlinePlayers().size(),
                    bedwars.getMode().getPlayers());


            for (IOasePlayer iOasePlayer : this.playerExecutor.getCurrentOnlinePlayers()) {
                bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), iOasePlayer, cache.getMessage(iOasePlayer.getLocale()));
            }

            setupPlayer(player);
            setupScoreboard(player);
            setupJoinItems(player, oasePlayer);
        }
    }

    private void setupPlayer(Player player) {
        player.setLevel(0);
        player.setHealthScale(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    private void setupScoreboard(Player player) {
        INSTANCE.setSidebar(player, DisplaySlot.SIDEBAR, "§e§lTrainingsOase", sidebar);
        INSTANCE.updateTeam(player, "Team", " §8➥ §7", "", "§4✖");
        INSTANCE.updateTeam(player, "PreTeam", "§7", "§7■ Team:", "");
        INSTANCE.updateTeam(player, "Map", " §8➥ §e", "§e", "§eVoting");
    }

    private void setupJoinItems(Player player, IOasePlayer oasePlayer) {
        player.getInventory().setItem(0, joinItems.getTeamSelectionItem().get(oasePlayer.getLocale()));
        player.getInventory().setItem(2, joinItems.getVotingItem().get(oasePlayer.getLocale()));
        player.getInventory().setItem(4, joinItems.getGuardianItem().get(oasePlayer.getLocale()));
        player.getInventory().setItem(6, joinItems.getMapVotingItem().get(oasePlayer.getLocale()));
        player.getInventory().setItem(8, joinItems.getLobbyItem().get(oasePlayer.getLocale()));
    }

    private void sendCountDownBar() {
        bukkitTask = bedwars.runTaskTimer(() -> {
            int startSize = bedwars.getMode().getStartSize();
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            int sizeNeeded = startSize - onlinePlayers;

            var moreCache = new MessageCache(bedwars.getLanguageProvider(), "actionbar_waiting_more", sizeNeeded);
            var oneCache = new MessageCache(bedwars.getLanguageProvider(), "actionbar_waiting_one");

            if(onlinePlayers < startSize) {

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayerAsync(onlinePlayer.getUniqueId()).thenAccept(iOasePlayer -> {
                        if (sizeNeeded == 1) {
                            ActionbarAPI.setActionBarFor(onlinePlayer, WrappedChatComponent.fromText(
                                    bedwars.getLanguageProvider().getTextProvider().format(oneCache.getTextData(), iOasePlayer.getLocale())));
                        } else {
                            ActionbarAPI.setActionBarFor(onlinePlayer, WrappedChatComponent.fromText(
                                    bedwars.getLanguageProvider().getTextProvider().format(moreCache.getTextData(), iOasePlayer.getLocale())));                        }
                    });
                }
            }
        }, 0, 40);
    }
}
