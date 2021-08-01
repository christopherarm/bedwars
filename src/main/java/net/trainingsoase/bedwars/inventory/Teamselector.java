package net.trainingsoase.bedwars.inventory;

import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.oreo.inventory.InventoryLayout;
import net.trainingsoase.oreo.inventory.InventoryRows;
import net.trainingsoase.oreo.inventory.translated.GlobalTranslatedInventoryBuilder;
import net.trainingsoase.oreo.inventory.translated.TranslatedSlot;
import net.trainingsoase.oreo.inventory.util.LayoutCalculator;
import net.trainingsoase.oreo.item.TranslatedItem;
import net.trainingsoase.oreo.item.builder.ColoredBuilder;
import net.trainingsoase.oreo.item.builder.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.function.Consumer;

import static net.trainingsoase.oreo.scoreboard.ScoreboardAPI.INSTANCE;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Teamselector {

    private final GlobalTranslatedInventoryBuilder teamSelectorInventoryBuilder;

    private final Bedwars bedwars;

    private static final ItemStack GLASS_PANE = new ColoredBuilder(ColoredBuilder.DyeType.GLASS_PANE)
            .setColor(DyeColor.GRAY).setEmptyName().build();

    public Teamselector(Bedwars bedwars) {
        this.bedwars = bedwars;

        InventoryLayout inventoryLayout = new InventoryLayout(InventoryRows.THREE);

        inventoryLayout.setNonClickItems(LayoutCalculator.quad(0, InventoryRows.THREE.getSize() - 1), GLASS_PANE);

        teamSelectorInventoryBuilder = new GlobalTranslatedInventoryBuilder(InventoryRows.THREE, bedwars.getLanguageProvider());
        teamSelectorInventoryBuilder.registerListener(bedwars);
        teamSelectorInventoryBuilder.setTitleData("inventory_teamselector");
        teamSelectorInventoryBuilder.setInventoryLayout(inventoryLayout);
        teamSelectorInventoryBuilder.setTaskChainFactory(bedwars);
        teamSelectorInventoryBuilder.setDataLayoutChainFunction((dataLayoutTaskChain ->
                dataLayoutTaskChain.async(dataLayout -> {
                    dataLayout = dataLayout == null ? new InventoryLayout(InventoryRows.THREE) : dataLayout;

                    int[] slots = bedwars.getMode().getInvSlots();
                    dataLayout.blank(slots);

                    for (int i = 0; i < bedwars.getMode().getTeams(); i++) {
                        buildTeamItem(dataLayout, slots, i, bedwars.getTeamService().getTeams().get(i));
                    }

                    return dataLayout;
                })));
    }

    private void buildTeamItem(InventoryLayout layout, int[] slots, int id, BedwarsTeam bedwarsTeam) {
        layout.setItem(slots[id], new TranslatedSlot(TranslatedItem.of(
                        new SkullBuilder().setSkinOverValue(bedwarsTeam.getSkinValue()).setAmount(bedwarsTeam.getCurrentSize()))
                .setDisplayName(bedwarsTeam.getIdentifier(), bedwarsTeam.getColorData().getChatColor()))
                .setClickListener(handleTeamClick(bedwarsTeam)));
    }

    public void updateTeamSelector() {
        teamSelectorInventoryBuilder.invalidateDataLayout();
    }

    private Consumer<InventoryClickEvent> handleTeamClick(BedwarsTeam bedwarsTeam) {
        return event -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();
            final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

            var team = bedwars.getTeamService().getTeam(player);

            if(team.isPresent()) {
                if(team.get().equals(bedwarsTeam)) {
                    bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "team_already_in_team");
                    return;
                }
                team.get().removePlayer(player);
            }

            bedwarsTeam.addPlayer(player);

            INSTANCE.updateTeam(player, "PreTeam", "§7", bedwarsTeam.getColorData().getChatColor() + "■ §7Team:", "");

            INSTANCE.updateTeam(player, "Team", " §8➥ §7", "",
                    bedwars.getLanguageProvider().getTextProvider().format(bedwarsTeam.getIdentifier(), oasePlayer.getLocale(), bedwarsTeam.getColorData().getChatColor()));


            bedwars.getLanguageProvider().sendMessage(Bukkit.getConsoleSender(), oasePlayer, "team_now_in_team",
                            bedwars.getLanguageProvider().getTextProvider()
                                    .format(bedwarsTeam.getIdentifier(), oasePlayer.getLocale(), bedwarsTeam.getColorData().getChatColor()));
            updateTeamSelector();
        };
    }

    public Inventory getTeamSelectorInventory(Locale locale) {
        return teamSelectorInventoryBuilder.getInventory(locale);
    }
}
