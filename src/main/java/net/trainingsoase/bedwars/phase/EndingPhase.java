package net.trainingsoase.bedwars.phase;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.ServicePlayer;
import de.dytanic.cloudnet.ext.bridge.server.BridgeServerHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.bedwars.utils.ActionbarAPI;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.github.paperspigot.Title;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class EndingPhase extends TimedPhase {

    private Location spawnLocation;

    private final Bedwars bedwars;

    private static final IPlayerManager PLAYER_MANAGER = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public EndingPhase(Bedwars bedwars, boolean async) {
        super("Ending", bedwars, 20, async);
        this.setCurrentTicks(6);
        this.bedwars = bedwars;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.spawnLocation = MapHelper.getInstance(bedwars).getLobbyMap().getSpawn().toLocation();

        BedwarsTeam winnerTeam = bedwars.getTeamService().getTeams().get(0);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            onlinePlayer.teleport(spawnLocation);
            onlinePlayer.getInventory().clear();
            onlinePlayer.getInventory().setArmorContents(null);
            onlinePlayer.getVelocity().zero();
            onlinePlayer.setHealth(20.0D);
            onlinePlayer.setFoodLevel(20);
            onlinePlayer.setFireTicks(0);
            onlinePlayer.setGameMode(GameMode.SURVIVAL);
            onlinePlayer.setFlying(false);
            onlinePlayer.setAllowFlight(false);

            IOasePlayer iOasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());
            onlinePlayer.sendTitle(new Title(
                    winnerTeam.getColorData().getChatColor() +
                            bedwars.getLanguageProvider().getTextProvider().format(winnerTeam.getIdentifier(), iOasePlayer.getLocale(), winnerTeam.getColorData().getChatColor()),
                    bedwars.getLanguageProvider().getTextProvider().format("game_team_won", iOasePlayer.getLocale()), 20, 20, 20));
        }
    }

    @Override
    public void onUpdate() {
        if (getCurrentTicks() == 1) {
            BridgeServiceProperty.PLAYERS.get(Wrapper.getInstance().getCurrentServiceInfoSnapshot()).ifPresent(servicePlayers -> {
                for (ServicePlayer servicePlayer : servicePlayers) {
                    PLAYER_MANAGER.getOnlinePlayerAsync(servicePlayer.getUniqueId()).onComplete(iCloudPlayer -> {
                        iCloudPlayer.getPlayerExecutor().connectToFallback();
                    });
                }
            });
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            IOasePlayer iOasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());
            ActionbarAPI.setActionBarFor(onlinePlayer, WrappedChatComponent.fromText(
                        bedwars.getLanguageProvider().getTextProvider().format("game_ends_in", iOasePlayer.getLocale(), getCurrentTicks() - 1)));
        }
    }

    @Override
    protected void onFinish() {
        Bukkit.shutdown();
    }
}
