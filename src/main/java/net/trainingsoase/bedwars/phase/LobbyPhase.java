package net.trainingsoase.bedwars.phase;

import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class LobbyPhase extends TimedPhase {


    public LobbyPhase(Game game, boolean async) {
        super("Lobby", game, 20, async);
        this.setPaused(true);
        this.setCurrentTicks(60);
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onTick() {

    }
}
