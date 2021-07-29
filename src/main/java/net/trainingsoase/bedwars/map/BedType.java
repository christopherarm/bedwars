package net.trainingsoase.bedwars.map;

public enum BedType {

    TOP("top"),
    BOTTOM("bottom");

    String name;

    BedType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
