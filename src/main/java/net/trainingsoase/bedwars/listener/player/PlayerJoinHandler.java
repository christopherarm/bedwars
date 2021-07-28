package net.trainingsoase.bedwars.listener.player;

import at.rxcki.strigiformes.message.MessageCache;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.data.model.OasePlayer;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import net.trainingsoase.oreo.scoreboard.ScoreboardAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;

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

    public PlayerJoinHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.bedwars = bedwars;
        this.phaseSeries = phaseSeries;

        sidebar.put("§8§m----------------", 12);
        sidebar.put("§7", 11);
        sidebar.put(" §8➥ §7", 10);
        sidebar.put(" §b", 9);
        sidebar.put("§b■ §7Map:", 8);
        sidebar.put(" §8➥ §b", 7);
        sidebar.put(" §c", 6);
        sidebar.put("§e■ §7Coins:", 5);
        sidebar.put(" §8➥ §e", 4);
        sidebar.put(" §d", 3);
        sidebar.put("§a■ §7Ranking:", 2);
        sidebar.put(" §8➥ §a", 1);
        sidebar.put("         ", 0);
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

            for (IOasePlayer iOasePlayer : OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers()) {
                bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), iOasePlayer, cache.getMessage(iOasePlayer.getLocale()));
            }

            setupPlayer(player);
            setupScoreboard(player, oasePlayer);
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

    private void setupScoreboard(Player player, IOasePlayer oasePlayer) {
        INSTANCE.setSidebar(player, DisplaySlot.SIDEBAR, "§c§lBedwars §8§l︳§eLobby", sidebar);
        INSTANCE.updateTeam(player, "Team", " §8➥ §7", "", "§4✖");
        INSTANCE.updateTeam(player, "PreTeam", "§7", "§7■ Team:", "");
        INSTANCE.updateTeam(player, "Coins", " §8➥ §e", "§e", "" + oasePlayer.getCoins());
        INSTANCE.updateTeam(player, "Ranking", " §8➥ §a", "§a", "#1");
        INSTANCE.updateTeam(player, "Map", " §8➥ §b", "§b", "§bTest");

    }
}
