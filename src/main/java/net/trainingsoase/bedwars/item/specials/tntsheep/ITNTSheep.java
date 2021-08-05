package net.trainingsoase.bedwars.item.specials.tntsheep;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

public interface ITNTSheep {

    Location getLocation();

    TNTPrimed getTNT();

    void setTNT(TNTPrimed tnt);

    void remove();

    void setPassenger(TNTPrimed tnt);

    void setTNTSource(Entity source);

}
