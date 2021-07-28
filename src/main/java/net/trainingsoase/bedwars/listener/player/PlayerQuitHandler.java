package net.trainingsoase.bedwars.listener.player;

import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class PlayerQuitHandler implements Listener {

    private final Bedwars bedwars;

    private final LinearPhaseSeries<TimedPhase> phaseSeries;

    public PlayerQuitHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.bedwars = bedwars;
        this.phaseSeries = phaseSeries;
    }

    @EventHandler
    public void handleQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (phaseSeries.getCurrentPhase() instanceof LobbyPhase) {
            var lobbyPhase = (LobbyPhase) phaseSeries.getCurrentPhase();
            lobbyPhase.checkStopCondition();

            for (IOasePlayer iOasePlayer : OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers()) {
                bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), iOasePlayer, "quit_message",
                        player.getDisplayName(),
                        Bukkit.getOnlinePlayers().size() - 1,
                        bedwars.getMode().getPlayers());
            }
            return;
        }

        event.setQuitMessage(null);
    }
}
