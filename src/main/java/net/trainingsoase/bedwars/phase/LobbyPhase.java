package net.trainingsoase.bedwars.phase;

import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class LobbyPhase extends TimedPhase {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 2;

    public LobbyPhase(Game game, boolean async) {
        super("Lobby", game, 20, async);
        this.setPaused(true);
        this.setCurrentTicks(60);
    }

    public void checkStartCondition() {
        if(Bukkit.getOnlinePlayers().size() >= MIN_PLAYERS && isPaused()) {
            setPaused(false);
            Bukkit.broadcastMessage("active");
        }
    }

    public void checkStopCondition() {
        if(Bukkit.getOnlinePlayers().size() < MIN_PLAYERS && !isPaused()) {
            setPaused(true);
            setCurrentTicks(61);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onTick() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setLevel(getCurrentTicks());
        }
    }
}
