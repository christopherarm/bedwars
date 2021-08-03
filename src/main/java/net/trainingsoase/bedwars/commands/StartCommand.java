package net.trainingsoase.bedwars.commands;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.LobbyPhase;
import net.trainingsoase.hopjes.api.phase.Phase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class StartCommand implements CommandExecutor {

    private Bedwars bedwars;

    public StartCommand(Bedwars bedwars) {
        this.bedwars = bedwars;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(!(cs instanceof Player)) return true;

        final Player player = (Player) cs;

        if(args.length == 0) {
            if(bedwars.getLinearPhaseSeries().getCurrentPhase() instanceof LobbyPhase) {
                LobbyPhase lobbyPhase = (LobbyPhase) bedwars.getLinearPhaseSeries().getCurrentPhase();
                lobbyPhase.setCurrentTicks(6);
            }
        }

        return false;
    }
}
