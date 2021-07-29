package net.trainingsoase.bedwars.map;

public enum SpawnType {

    BRONZE("§cBronze"),
    IRON("§fEisen"),
    GOLD("§6Gold");

    String name;

    SpawnType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final SpawnType[] VALUES = values();
}
