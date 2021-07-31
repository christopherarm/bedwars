package net.trainingsoase.bedwars.inventory;

import at.rxcki.strigiformes.text.TextData;
import net.trainingsoase.bedwars.Bedwars;
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

import java.util.*;
import java.util.function.Consumer;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Voting {

    private final GlobalTranslatedInventoryBuilder votingInventoryBuilder;

    private final Bedwars bedwars;

    private final List<Player> onVotes;

    private final List<Player> offVotes;

    private static final ItemStack GLASS_PANE = new ColoredBuilder(ColoredBuilder.DyeType.GLASS_PANE)
            .setColor(DyeColor.GRAY).setEmptyName().build();

    private static final ItemStack GOLD_ITEM = new SkullBuilder().setSkinOverValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ1ZjQ3ZmViNGQ3NWNiMzMzOTE0YmZkYjk5OWE0ODljOWQwZTMyMGQ1NDhmMzEwNDE5YWQ3MzhkMWUyNGI5In19fQ==")
            .setDisplayName("§8» §6Gold").build();

    public Voting(Bedwars bedwars) {
        this.bedwars = bedwars;
        this.onVotes = new ArrayList<>();
        this.offVotes = new ArrayList<>();

        InventoryLayout inventoryLayout = new InventoryLayout(InventoryRows.ONE);

        inventoryLayout.setNonClickItems(LayoutCalculator.quad(0, InventoryRows.ONE.getSize() - 1), GLASS_PANE);

        votingInventoryBuilder = new GlobalTranslatedInventoryBuilder(InventoryRows.ONE, bedwars.getLanguageProvider());
        votingInventoryBuilder.registerListener(bedwars);
        votingInventoryBuilder.setTitleData("inventory_voting");
        votingInventoryBuilder.setInventoryLayout(inventoryLayout);
        votingInventoryBuilder.setTaskChainFactory(bedwars);
        votingInventoryBuilder.setDataLayoutChainFunction((dataLayoutTaskChain ->
                dataLayoutTaskChain.async(dataLayout -> {
                    dataLayout = dataLayout == null ? new InventoryLayout(InventoryRows.ONE) : dataLayout;

                    dataLayout.blank(0, 4, 8);

                    dataLayout.setNonClickItem(4, GOLD_ITEM);

                    dataLayout.setItem(0, new TranslatedSlot(TranslatedItem.of(
                                    new ColoredBuilder(ColoredBuilder.DyeType.CLAY_BLOCK).setColor(DyeColor.RED).setAmount(offVotes.size()))
                            .setDisplayName(new TextData("item_off"))).setClickListener(handleItemClick(false)));

                    dataLayout.setItem(8, new TranslatedSlot(TranslatedItem.of(
                                    new ColoredBuilder(ColoredBuilder.DyeType.CLAY_BLOCK).setColor(DyeColor.LIME).setAmount(onVotes.size()))
                            .setDisplayName(new TextData("item_on"))).setClickListener(handleItemClick(true)));

                    return dataLayout;
                })));
    }

    public void updateVotingInventory() {
        votingInventoryBuilder.invalidateDataLayout();
    }

    private Consumer<InventoryClickEvent> handleItemClick(boolean on) {
        return event -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            if(on) {
                offVotes.remove(player);
                if(!onVotes.contains(player)) {
                    onVotes.add(player);
                }
                updateVotingInventory();
                return;
            }
            onVotes.remove(player);
            if(!offVotes.contains(player)) {
                offVotes.add(player);
            }
            updateVotingInventory();

        };
    }

    public Inventory getVotingInventory(Locale locale) {
        return votingInventoryBuilder.getInventory(locale);
    }
}
