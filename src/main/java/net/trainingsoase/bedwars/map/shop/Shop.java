package net.trainingsoase.bedwars.map.shop;

import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.oreo.inventory.InventoryLayout;
import net.trainingsoase.oreo.inventory.InventoryRows;
import net.trainingsoase.oreo.inventory.translated.GlobalTranslatedInventoryBuilder;
import net.trainingsoase.oreo.inventory.translated.TranslatedSlot;
import net.trainingsoase.oreo.inventory.util.LayoutCalculator;
import net.trainingsoase.oreo.item.TranslatedItem;
import net.trainingsoase.oreo.item.builder.ColoredBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.function.Consumer;

import static net.trainingsoase.bedwars.Bedwars.GLASS_PANE;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Shop {

    private final GlobalTranslatedInventoryBuilder shopInventoryBuilder;

    private final Bedwars bedwars;

    private static final int[] CATEGORY_SLOTS = new int[]{0,1,2,3,4,5,6,7};

    private final HashMap<ShopCategory, GlobalTranslatedInventoryBuilder> shopInventories;

    public Shop(Bedwars bedwars) {
        this.bedwars = bedwars;
        this.shopInventories = new HashMap<>();

        InventoryLayout inventoryLayout = new InventoryLayout(InventoryRows.TWO);

        inventoryLayout.setNonClickItems(LayoutCalculator.quad(0, InventoryRows.TWO.getSize() - 1), GLASS_PANE);

        shopInventoryBuilder = new GlobalTranslatedInventoryBuilder(InventoryRows.TWO, bedwars.getLanguageProvider());
        shopInventoryBuilder.registerListener(bedwars);
        shopInventoryBuilder.setTitleData("inventory_shop");
        shopInventoryBuilder.setInventoryLayout(inventoryLayout);

        ShopCategory[] values = ShopCategory.values();

        for (int i = 0; i < values.length; i++) {
            inventoryLayout.setItem(CATEGORY_SLOTS[i], new TranslatedSlot(TranslatedItem.of(values[i].getMaterial())
                    .setDisplayName(values[i].getKey())), handleClick());
        }

        for (ShopCategory value : ShopCategory.values()) {
            new ShopInventory(bedwars, value, shopInventories, inventoryLayout);
        }
    }

    private Consumer<InventoryClickEvent> handleClick() {
        return inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);

            final Player player = (Player) inventoryClickEvent.getWhoClicked();
            final IOasePlayer oasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

            if(inventoryClickEvent.getClickedInventory() == player.getInventory()) return;

            var slotId = inventoryClickEvent.getSlot();

            var category = ShopCategory.values()[slotId];

            player.openInventory(shopInventories.get(category).getInventory(oasePlayer.getLocale()));

            player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
        };
    }

    public Inventory getShopInventory(Locale locale) {
        return shopInventoryBuilder.getInventory(locale);
    }
}
