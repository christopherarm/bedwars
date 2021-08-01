package net.trainingsoase.bedwars.map;

import org.bukkit.Material;

public enum SpawnType {

    BRONZE("§cBronze", Material.CLAY_BRICK),
    IRON("§fEisen", Material.IRON_INGOT),
    GOLD("§6Gold", Material.GOLD_INGOT);

    private String name;

    private Material material;

    SpawnType(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public static final SpawnType[] VALUES = values();
}
