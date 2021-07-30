package net.trainingsoase.bedwars.phase;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.shop.NPCShop;
import net.trainingsoase.bedwars.map.spawner.Spawner;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class IngamePhase extends TimedPhase {

    private final Bedwars bedwars;

    private Spawner spawner;

    private NPCShop npcShop;

    public IngamePhase(Game game, boolean async, Bedwars bedwars) {
        super("Ingame", game, 20, async);
        this.bedwars = bedwars;
        setPaused(false);
        this.setCurrentTicks(60);
        this.spawner = new Spawner(bedwars);
        this.npcShop = new NPCShop(bedwars);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bukkit.getScheduler().runTaskLater(bedwars, () -> {
            spawner.startSpawners();
            npcShop.spawnNPCs();
        }, 5);
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onTick() {
    }
}
