package net.trainingsoase.bedwars.voting;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class VoteMap {

    private String name;

    private int votes;

    private HashSet<Player> players;

    public VoteMap(String name, int votes) {
        this.name = name;
        this.votes = votes;
        this.players = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void addVote() {
        ++this.votes;
    }

    public void removeVote() {
        --this.votes;
    }

    public HashSet<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
