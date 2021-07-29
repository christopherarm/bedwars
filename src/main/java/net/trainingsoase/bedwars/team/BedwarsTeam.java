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

    public BedwarsTeam(MessageProvider messageProvider, String key, int initialCapacity, ColorData colorData, String skinValue) {
        super(messageProvider, key, initialCapacity, colorData);
        this.skinValue = skinValue;
    }

    public String getSkinValue() {
        return skinValue;
    }
}
