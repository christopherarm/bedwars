package net.trainingsoase.bedwars.phase;

import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;

import java.util.concurrent.TimeUnit;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class IngamePhase extends TimedPhase {

    public IngamePhase(Game game, boolean async) {
        super("Ingame", game, 20, async);
        this.setPaused(true);
        this.setCurrentTicks((int) TimeUnit.HOURS.toSeconds(1));
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

    }
}
