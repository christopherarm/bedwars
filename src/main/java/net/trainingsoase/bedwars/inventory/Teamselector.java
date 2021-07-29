package net.trainingsoase.bedwars.inventory;

import at.rxcki.strigiformes.MessageProvider;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.oreo.inventory.InventoryLayout;
import net.trainingsoase.oreo.inventory.InventoryRows;
import net.trainingsoase.oreo.inventory.translated.GlobalTranslatedInventoryBuilder;
import net.trainingsoase.oreo.inventory.translated.TranslatedSlot;
import net.trainingsoase.oreo.inventory.util.LayoutCalculator;
import net.trainingsoase.oreo.item.TranslatedItem;
import net.trainingsoase.oreo.item.builder.ColoredBuilder;
import net.trainingsoase.oreo.item.builder.SkullBuilder;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Teamselector {

    private static final ItemStack GLASS_PANE = new ColoredBuilder(ColoredBuilder.DyeType.GLASS_PANE)
            .setColor(DyeColor.GRAY).setEmptyName().build();

    private final GlobalTranslatedInventoryBuilder teamSelectorInventoryBuilder;

    private final Bedwars bedwars;

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

                    switch (bedwars.getMode().getId()) {
                        case 0:
                            BedwarsTeam team1 = bedwars.getTeamService().getTeams().get(0);
                            BedwarsTeam team2 = bedwars.getTeamService().getTeams().get(1);

                            dataLayout.setItem(slots[0], new TranslatedSlot(TranslatedItem.of(
                                    new SkullBuilder().setSkinOverValue(team1.getSkinValue()).setAmount(team1.getCurrentSize()))
                                    .setDisplayName(team1.getIdentifier())).setClickListener(handleTeamClick(team1)));

                            dataLayout.setItem(slots[1], new TranslatedSlot(TranslatedItem.of(
                                    new SkullBuilder().setSkinOverValue(team2.getSkinValue()).setAmount(team2.getCurrentSize()))
                                    .setDisplayName(team2.getIdentifier())).setClickListener(handleTeamClick(team2)));
                            break;

                        case 1:
                            break;

                        case 2:
                            break;

                        default:
                            break;
                    }

                    return dataLayout;
                })));
    }

    public void updateTeamSelector() {
        teamSelectorInventoryBuilder.invalidateDataLayout();
    }

    private Consumer<InventoryClickEvent> handleTeamClick(BedwarsTeam bedwarsTeam) {
        return event -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            bedwars.getTeamService().getTeam(player).ifPresent(playerTeam -> {
                if(playerTeam.equals(bedwarsTeam)) {
                    return;
                }
                playerTeam.removePlayer(player);
            });

            bedwarsTeam.addPlayer(player);
            updateTeamSelector();
        };
    }

    public Inventory getTeamSelectorInventory(Locale locale) {
        return teamSelectorInventoryBuilder.getInventory(locale);
    }
}
