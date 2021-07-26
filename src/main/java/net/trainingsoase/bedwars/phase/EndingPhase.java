package net.trainingsoase.bedwars.phase;

import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class EndingPhase extends TimedPhase {

    public EndingPhase(Game game, boolean async) {
        super("Ending", game, 20, async);
        this.setPaused(true);
        this.setCurrentTicks(61);
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onTick() {

    }
}
