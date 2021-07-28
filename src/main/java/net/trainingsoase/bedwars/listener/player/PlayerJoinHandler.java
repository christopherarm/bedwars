package net.trainingsoase.bedwars.listener.player;

import at.rxcki.strigiformes.message.MessageCache;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class PlayerJoinHandler implements Listener {

    private final Bedwars bedwars;

    private final LinearPhaseSeries<TimedPhase> phaseSeries;

    public PlayerJoinHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.bedwars = bedwars;
        this.phaseSeries = phaseSeries;
    }

    @EventHandler
    public void handleJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);
        final Player player = event.getPlayer();

        if (phaseSeries.getCurrentPhase() instanceof LobbyPhase) {
            var lobbyPhase = (LobbyPhase) phaseSeries.getCurrentPhase();
            lobbyPhase.checkStartCondition();

            var cache = new MessageCache(bedwars.getLanguageProvider(), "join_message", player.getDisplayName());

            for (IOasePlayer oasePlayer : OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers()) {
                bedwars.getLanguageProvider().sendMessage(Bukkit.getPlayer(oasePlayer.getUUID()), oasePlayer, cache.getMessage(oasePlayer.getLocale()));
            }

            setupPlayer(player);
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
}
