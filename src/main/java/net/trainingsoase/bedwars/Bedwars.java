package net.trainingsoase.bedwars;

import de.dytanic.cloudnet.ext.bridge.server.BridgeServerHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.trainingsoase.api.database.AbstractSentryConnector;
import net.trainingsoase.api.database.sentry.Environment;
import net.trainingsoase.api.database.sentry.SentryConnector;
import net.trainingsoase.bedwars.listener.player.PlayerJoinHandler;
import net.trainingsoase.bedwars.listener.player.PlayerQuitHandler;
import net.trainingsoase.bedwars.listener.player.PlayerSpawnLocationHandler;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.phase.EndingPhase;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.bedwars.utils.Mode;
import net.trainingsoase.data.i18n.LanguageProvider;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.LinearPhaseSeries;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import net.trainingsoase.spigot.i18n.BukkitSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Bedwars extends Game {

    public Bedwars() {
        super(false, 0);
    }

    private Mode mode;

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

        setupGame();
    }

    @Override
    public void onDisable() {

    }

    private void setupGame() {
        // Instanziert den Modus mit den ersten 5 Zeichen des Servers (BW2x1, etc.)
        var serverName = Wrapper.getInstance().getCurrentServiceInfoSnapshot().getName().substring(0,5);

        if(serverName.startsWith("BW")) {
            this.mode = Mode.valueOf(serverName);
        } else {
            this.getLogger().log(Level.SEVERE, "Das Plugin muss sich auf einem Bedwars Server befinden, der nach dem folgenden Syntax aufgebaut ist: BW-*");
            Bukkit.shutdown();
        }

        BridgeServerHelper.setMotd("Voting");
        BridgeServerHelper.setMaxPlayers(mode.getPlayers());
        BridgeServerHelper.updateServiceInfo();

        MapHelper.getInstance().loadLobby();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(this, linearPhaseSeries), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitHandler(this, linearPhaseSeries), this);
        getServer().getPluginManager().registerEvents(new PlayerSpawnLocationHandler(), this);
    }

    public LanguageProvider<CommandSender> getLanguageProvider() {
        return languageProvider;
    }

    public Mode getMode() {
        return mode;
    }
}