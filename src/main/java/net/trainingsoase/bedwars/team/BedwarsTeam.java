package net.trainingsoase.bedwars.team;

import at.rxcki.strigiformes.MessageProvider;
import net.trainingsoase.hopjes.api.ColorData;
import net.trainingsoase.hopjes.api.teams.TranslatedTeam;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class BedwarsTeam extends TranslatedTeam {

    private String skinValue;

    private boolean hasBed;

    public BedwarsTeam(MessageProvider messageProvider, String key, int initialCapacity, ColorData colorData, String skinValue) {
        super(messageProvider, key, initialCapacity, colorData);
        this.skinValue = skinValue;
        this.hasBed = true;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean hasBed() {
        return hasBed;
    }

    public String getSkinValue() {
        return skinValue;
    }
}
