package net.trainingsoase.bedwars.utils;

import de.dytanic.cloudnet.common.collection.Pair;
import net.trainingsoase.bedwars.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CombatlogManager {

    private Bedwars plugin;

    public Map<Player, Pair<Integer, Player>> combatLogMap;

    public CombatlogManager(Bedwars plugin) {
        this.plugin = plugin;
        this.combatLogMap = new ConcurrentHashMap<>();
        startTimer();
    }

    private void startTimer() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            for (Player player : this.combatLogMap.keySet()) {
                Pair<Integer, Player> playerPair = this.combatLogMap.get(player);
                if (playerPair.getFirst() >= 1) {
                    playerPair.setFirst(playerPair.getFirst() - 1);
                    continue;
                }
                if (playerPair.getFirst() == 0)
                    this.combatLogMap.remove(player);
            }
        },20L, 20L);
    }

    public Map<Player, Pair<Integer, Player>> getCombatLogMap() {
        return this.combatLogMap;
    }
}
