package net.trainingsoase.bedwars.phase;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.commands.StartCommand;
import net.trainingsoase.bedwars.inventory.Mapvoting;
import net.trainingsoase.bedwars.item.JoinItems;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import net.trainingsoase.hopjes.api.teams.TeamService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

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
        setPaused(true);
        this.setCurrentTicks(61);
        this.joinItems = new JoinItems(bedwars.getLanguageProvider());
        bedwars.getCommand("start").setExecutor(new StartCommand(bedwars));
    }

    public void checkStartCondition() {
        if(Bukkit.getOnlinePlayers().size() >= bedwars.getMode().getStartSize() && isPaused()) {
            setPaused(false);

            if(Bukkit.getOnlinePlayers().size() == bedwars.getMode().getPlayers()) {
                setCurrentTicks(6);
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

    public void sendJoinMessage() {
        List<IOasePlayer> currentOnlinePlayers = OaseAPIImpl.INSTANCE.getPlayerExecutor().getCurrentOnlinePlayers();

        StringBuilder builders = new StringBuilder();
        for(String builder : MapHelper.getInstance(bedwars).getGameMap().getBuilders()) {
            builders.append(builder + ",");
        }

        String gold = bedwars.getVoting().getOnVotes().size() > bedwars.getVoting().getOffVotes().size() ? "item_on" : "item_off";

        for (IOasePlayer oasePlayer : currentOnlinePlayers) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n ").append(bedwars.getLanguageProvider().getTextProvider().getString("voting_end", oasePlayer.getLocale()))
                    .append("\n §8「§7§lMap: §e").append(bedwars.getMapvoting().getPlayedMap())
                    .append("\n §8「§7§lBuilder: §e").append(builders.substring(0, builders.length() - 1))
                    .append("\n §8「§7§lGold: ").append(bedwars.getLanguageProvider().getTextProvider().format(gold, oasePlayer.getLocale()))
                    .append("\n ");

            Bukkit.getPlayer(oasePlayer.getUUID()).sendMessage(builder.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onFinish() {
        this.removePhaseListener(this);
        sendJoinMessage();
    }

    @Override
    public void onUpdate() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setLevel(getCurrentTicks());
        }

        switch (getCurrentTicks()) {
            case 30:
            case 15:
            case 10:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    var oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "game_start_in_more", getCurrentTicks());
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.DIG_WOOD, 1f, 1f);
                }
                break;

            case 5:
            case 4:
            case 3:
            case 2:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    var oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "game_start_in_more", getCurrentTicks());
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 1f, 1f);
                }
                break;

            case 1:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    var oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(onlinePlayer.getUniqueId());
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "game_start_in_one");
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS, 1f, 1f);
                }
                break;

            case 0:
                pickRandomTeams();

                String map = bedwars.getMapvoting().getVotedMap();

                bedwars.getSlimeManager().loadGameArena(map,
                        MapHelper.getInstance(bedwars).loadGameMap(map));

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 2f, 5f);
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void handleInteract(final PlayerInteractEvent event) {
        var player = event.getPlayer();
        var oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

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

        } else if(event.getItem().getItemMeta().getDisplayName().equals(
                bedwars.getLanguageProvider().getTextProvider().getString("item_voting", oasePlayer.getLocale()))) {
            player.openInventory(bedwars.getVoting().getVotingInventory(oasePlayer.getLocale()));
        }
    }

    private void pickRandomTeams() {
        var teamService = bedwars.getTeamService();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!teamService.getTeam(player).isPresent()) {
                for (BedwarsTeam team : teamService.getTeams()) {
                    if(team.getPlayers().size() != bedwars.getMode().getPlayers() / bedwars.getMode().getTeams()) {
                        team.addPlayer(player);
                        break;
                    }
                }
            }
        }
    }

    public JoinItems getJoinItems() {
        return joinItems;
    }
}
