package net.trainingsoase.bedwars.utils;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public enum Mode {

    BW2x1("2x1", 0,2, 2, 2),
    BW2x2("2x2", 0,4, 2, 3),
    BW4x1("4x1", 1,4, 4, 3),
    BW4x2("4x2", 1,8, 4, 4),
    BW8x1("8x1", 2,8, 8, 4),
    BW8x2("8x2", 2,16, 8, 8);

    String mode;
    int id;
    int players;
    int teams;
    int startSize;

    public static final Mode[] VALUES = values();

    Mode(String mode, int id, int players, int teams, int startSize) {
        this.mode = mode;
        this.id = id;
        this.players = players;
        this.teams = teams;
        this.startSize = startSize;
    }

    public int getPlayers() {
        return players;
    }

    public String getMode() {
        return mode;
    }

    public int getTeams() {
        return teams;
    }

    public int getStartSize() {
        return startSize;
    }

    public int getId() {
        return id;
    }
}
