package net.trainingsoase.bedwars;

import net.trainingsoase.api.database.AbstractSentryConnector;
import net.trainingsoase.api.database.sentry.Environment;
import net.trainingsoase.api.database.sentry.SentryConnector;
import net.trainingsoase.api.i18n.ILanguageProvider;
import net.trainingsoase.api.spigot.i18n.BukkitSender;
import net.trainingsoase.bedwars.listener.player.PlayerJoinHandler;
import net.trainingsoase.bedwars.listener.player.PlayerQuitHandler;
import net.trainingsoase.bedwars.phase.EndingPhase;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.data.i18n.LanguageProvider;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Bedwars extends Game {

    public Bedwars() {
        super(false, 0);
    }

    private LinearPhaseSeries<TimedPhase> linearPhaseSeries;

    private LanguageProvider<CommandSender> languageProvider;

    @Override
    public void onLoad() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
            this.onDisable();
        } else {
            SentryConnector connector = new AbstractSentryConnector() {
                @Override
                public void initSentry(@NotNull String dsn, @NotNull Environment environment) {
                    super.initSentry(dsn, environment);
                }

                @Override
                public String getVersion() {
                    return getDescription().getVersion();
                }
            };
            connector.initSentry(getConfig().getString("sentry.dsn"), Environment.valueOf(getConfig().getString("sentry.env")));
        }
    }

    @Override
    public void onEnable() {
        linearPhaseSeries = new LinearPhaseSeries<>();
        linearPhaseSeries.add(new LobbyPhase(this, true));
        linearPhaseSeries.add(new IngamePhase(this, true));
        linearPhaseSeries.add(new EndingPhase(this, true));
        linearPhaseSeries.start();
        registerListeners();

        languageProvider = new LanguageProvider<>(getClassLoader(), "bedwars", new BukkitSender(this), Locale.GERMAN, Locale.ENGLISH);

    }

    @Override
    public void onDisable() {

    }

    private void registerListeners() {
        new PlayerJoinHandler(this, linearPhaseSeries);
        new PlayerQuitHandler(this, linearPhaseSeries);
    }

    public LanguageProvider<CommandSender> getLanguageProvider() {
        return languageProvider;
    }
}