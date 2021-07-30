package net.trainingsoase.bedwars.phase;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.item.JoinItems;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class LobbyPhase extends TimedPhase implements Listener {

    private final Bedwars bedwars;

    private final JoinItems joinItems;

    private static final IPlayerManager PLAYER_MANAGER = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public LobbyPhase(Bedwars bedwars, Game game, boolean async) {
        super("Lobby", game, 20, async);
        this.bedwars = bedwars;
        this.setPaused(true);
        this.setCurrentTicks(61);
        this.addPhaseListener(this);
        this.joinItems = new JoinItems(bedwars.getLanguageProvider());
    }

    public void checkStartCondition() {
        if(Bukkit.getOnlinePlayers().size() >= bedwars.getMode().getStartSize() && isPaused()) {
            setPaused(false);

            if(Bukkit.getOnlinePlayers().size() == bedwars.getMode().getPlayers()) {
                setCurrentTicks(1);
            }
        }
    }

    public void checkStopCondition() {
        if((Bukkit.getOnlinePlayers().size() - 1) < bedwars.getMode().getStartSize() && !isPaused()) {
            setPaused(true);
            setCurrentTicks(61);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.setLevel(60);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onFinish() {
        bedwars.getSlimeManager().loadGameArena(bedwars.getMapvoting().getVotedMap(),
                MapHelper.getInstance(bedwars).loadGameMap(bedwars.getMapvoting().getVotedMap()));
    }

    @Override
    protected void onTick() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setLevel(getCurrentTicks());
            final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());

            switch (getCurrentTicks()) {
                case 30:
                case 15:
                case 10:
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "game_start_in_more", getCurrentTicks());
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.DIG_WOOD, 1f, 1f);
                    break;

                case 5:
                case 4:
                case 3:
                case 2:
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "game_start_in_more", getCurrentTicks());
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 1f, 1f);
                    break;

                case 1:
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "game_start_in_one");
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 1f, 1f);
                    break;

                case 0:
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 2f, 5f);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void handleInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

        if(event.getItem() == null) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(event.getItem().getItemMeta().getDisplayName().equals(
                bedwars.getLanguageProvider().getTextProvider().getString("item_teamselector", oasePlayer.getLocale()))) {
            player.openInventory(bedwars.getTeamselector().getTeamSelectorInventory(oasePlayer.getLocale()));

        } else if(event.getItem().getItemMeta().getDisplayName().equals(
                bedwars.getLanguageProvider().getTextProvider().getString("item_lobby", oasePlayer.getLocale()))) {
            PLAYER_MANAGER.getOnlinePlayerAsync(player.getUniqueId()).onComplete(iCloudPlayer ->
                    iCloudPlayer.getPlayerExecutor().connectToFallback());

        } else if(event.getItem().getItemMeta().getDisplayName().equals(
                bedwars.getLanguageProvider().getTextProvider().getString("item_mapvoting", oasePlayer.getLocale()))) {
            player.openInventory(bedwars.getMapvoting().getMapVotingInventory(oasePlayer.getLocale()));
        }
    }

    public JoinItems getJoinItems() {
        return joinItems;
    }
}
