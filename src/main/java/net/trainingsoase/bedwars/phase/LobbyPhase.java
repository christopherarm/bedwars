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
        this.setCurrentTicks(61);
    }

    public void checkStartCondition() {
        if(Bukkit.getOnlinePlayers().size() >= MIN_PLAYERS && isPaused()) {
            setPaused(false);
        }
    }

    public void checkStopCondition() {
        if((Bukkit.getOnlinePlayers().size() - 1) < MIN_PLAYERS && !isPaused()) {
            setPaused(true);
            setCurrentTicks(61);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.setLevel(60);
            }
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

        if(getCurrentTicks() == 30) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage("30");
            }
        } else if(getCurrentTicks() == 15) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage("15");
            }
        } else if(getCurrentTicks() == 10) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage("10");
            }
        } else if(getCurrentTicks() <= 5 && getCurrentTicks() >= 1) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(getCurrentTicks() + "");
            }
        } else if(getCurrentTicks() == 0) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage("START");
            }
        }
    }
}
