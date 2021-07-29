package net.trainingsoase.bedwars.phase;

import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.inventory.InventoryService;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.hopjes.Game;
import net.trainingsoase.hopjes.api.phase.TimedPhase;
import org.bukkit.Bukkit;
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

    public LobbyPhase(Bedwars bedwars, Game game, boolean async) {
        super("Lobby", game, 20, async);
        this.bedwars = bedwars;
        this.setPaused(true);
        this.setCurrentTicks(61);
        this.addPhaseListener(this);
    }

    public void checkStartCondition() {
        if(Bukkit.getOnlinePlayers().size() >= bedwars.getMode().getStartSize() && isPaused()) {
            setPaused(false);
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

    }

    @Override
    protected void onTick() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setLevel(getCurrentTicks());
        }

        switch (getCurrentTicks()) {
            case 30:
                // 30 Sekunden
                break;

            case 15:
                // 15 Sekunden
                break;

            case 10:
                // 10 Sekunden
                break;

            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                // <= 5 Sekunden
                break;

            case 0:
                // Spiel starten
                break;

            default:
                break;
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
            player.openInventory(InventoryService.getInstance(bedwars).getTeamselector().getTeamSelectorInventory(oasePlayer.getLocale()));
        }
    }
}
