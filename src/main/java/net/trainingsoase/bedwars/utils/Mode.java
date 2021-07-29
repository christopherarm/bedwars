package net.trainingsoase.bedwars.utils;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since 1.0.0
 **/

public enum Mode {

    BW2x1("2x1", 0,2, 2, 2, new int[]{11, 15}),
    BW2x2("2x2", 0,4, 2, 3, new int[]{12, 16}),
    BW4x1("4x1", 1,4, 4, 3, new int[]{2, 2, 2, 2}),
    BW4x2("4x2", 1,8, 4, 4, new int[]{2, 2, 2, 2}),
    BW8x1("8x1", 2,8, 8, 4, new int[]{2, 2, 2, 2, 2, 2, 2, 2}),
    BW8x2("8x2", 2,16, 8, 8, new int[]{2, 2, 2, 2, 2, 2, 2, 2});

    private String mode;
    private int id;
    private int players;
    private int teams;
    private int startSize;
    private int[] invSlots;

    public static final Mode[] VALUES = values();

    Mode(String mode, int id, int players, int teams, int startSize, int[] invSlots) {
        this.mode = mode;
        this.id = id;
        this.players = players;
        this.teams = teams;
        this.startSize = startSize;
        this.invSlots = invSlots;
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

    public int[] getInvSlots() {
        return invSlots;
    }
}
