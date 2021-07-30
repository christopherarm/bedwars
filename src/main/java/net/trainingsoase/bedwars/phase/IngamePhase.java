package net.trainingsoase.bedwars.phase;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.spawner.Spawner;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class IngamePhase extends TimedPhase {

    private final Bedwars bedwars;

    private Spawner spawner;

    public IngamePhase(Game game, boolean async, Bedwars bedwars) {
        super("Ingame", game, 20, async);
        this.bedwars = bedwars;
        setPaused(false);
        this.setCurrentTicks(60);
        this.spawner = new Spawner(bedwars);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bukkit.getScheduler().runTaskLater(bedwars, () -> {
            spawner.startSpawners();
        }, 5);
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onTick() {
    }
}
