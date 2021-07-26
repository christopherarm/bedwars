package net.trainingsoase.bedwars.listener.player;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
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

    private final LinearPhaseSeries<TimedPhase> phaseSeries;

    public PlayerJoinHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        bedwars.getServer().getPluginManager().registerEvents(this, bedwars);
        this.phaseSeries = phaseSeries;
    }

    @EventHandler
    public void handleJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        System.out.println(player.getName());
        Bukkit.broadcastMessage(phaseSeries.getCurrentPhase().toString());
        if (phaseSeries.getCurrentPhase() instanceof LobbyPhase) {
            Bukkit.broadcastMessage("Lobbyphase");
            var lobbyPhase = (LobbyPhase) phaseSeries.getCurrentPhase();
            lobbyPhase.checkStartCondition();
            return;
        }

        event.setJoinMessage(null);



    }
}
