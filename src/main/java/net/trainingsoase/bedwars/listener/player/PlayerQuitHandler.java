package net.trainingsoase.bedwars.listener.player;

import at.rxcki.strigiformes.message.MessageCache;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.api.player.IPlayerExecutor;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class PlayerQuitHandler implements Listener {

    private final Bedwars bedwars;

    private final LinearPhaseSeries<TimedPhase> phaseSeries;

    private final IPlayerExecutor<UUID, IOasePlayer> executor;

    public PlayerQuitHandler(Bedwars bedwars, LinearPhaseSeries<TimedPhase> phaseSeries) {
        this.bedwars = bedwars;
        this.phaseSeries = phaseSeries;
        this.executor = OaseAPIImpl.INSTANCE.getPlayerExecutor();
    }

    @EventHandler
    public void handleQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        final Player player = event.getPlayer();

        if (phaseSeries.getCurrentPhase() instanceof LobbyPhase) {
            var lobbyPhase = (LobbyPhase) phaseSeries.getCurrentPhase();
            lobbyPhase.checkStopCondition();

            var cache = new MessageCache(bedwars.getLanguageProvider(), "quit_message",
                    player.getDisplayName());

            for (IOasePlayer iOasePlayer : this.executor.getCurrentOnlinePlayers()) {
                bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), iOasePlayer, cache.getMessage(iOasePlayer.getLocale()));
            }

            bedwars.getTeamService().getTeam(player).ifPresent(bedwarsTeam -> bedwarsTeam.removePlayer(player));
            bedwars.getTeamselector().updateTeamSelector();
            bedwars.getMapvoting().removePlayerFromVoting(player);
            bedwars.getVoting().getOnVotes().remove(player);
            bedwars.getVoting().getOffVotes().remove(player);
            bedwars.getVoting().updateVotingInventory();

        } else if(phaseSeries.getCurrentPhase() instanceof IngamePhase) {
            var ingamePhase = (IngamePhase) phaseSeries.getCurrentPhase();
            ingamePhase.getCombatlogManager().getCombatLogMap().remove(player);
            ingamePhase.checkQuit(player);
        }
    }
}
