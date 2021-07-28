package net.trainingsoase.bedwars.listener.player;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
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

    private final LinearPhaseSeries<TimedPhase> phaseSeries;

    public PlayerQuitHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.phaseSeries = phaseSeries;
    }

    @EventHandler
    public void handleQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (phaseSeries.getCurrentPhase() instanceof LobbyPhase) {
            var lobbyPhase = (LobbyPhase) phaseSeries.getCurrentPhase();
            lobbyPhase.checkStopCondition();
            return;
        }

        event.setQuitMessage(null);
    }
}
