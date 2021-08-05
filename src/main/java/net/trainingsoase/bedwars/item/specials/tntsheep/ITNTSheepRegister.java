package net.trainingsoase.bedwars.item.specials.tntsheep;

import net.trainingsoase.bedwars.Bedwars;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ITNTSheepRegister {

    void registerEntities(int entityId);

    ITNTSheep spawnCreature(Bedwars bedwars, Sophiaee specialItem, Location location, Player owner,
                            Player target, DyeColor color);

}
