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

    public boolean checkStartCondition() {
        if(Bukkit.getOnlinePlayers().size() >= MIN_PLAYERS && isPaused()) {
            setPaused(false);
            return true;
        }
        return false;
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

        switch (getCurrentTicks()) {
            case 30:
                // 30 Sekunden
                break;

            case 15:
                // 15 Sekunden
                break;

            case 10:
                // 10 Sekunden
                break;

            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                // <= 5 Sekunden
                break;

            case 0:
                // Spiel starten
                break;

            default:
                break;
        }
    }
}
