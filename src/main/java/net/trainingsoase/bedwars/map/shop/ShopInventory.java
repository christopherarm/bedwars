package net.trainingsoase.bedwars.map.shop;

import at.rxcki.strigiformes.message.MessageCache;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.trainingsoase.api.player.IOasePlayer;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.bedwars.utils.ActionbarAPI;
import net.trainingsoase.bedwars.utils.ItemBuilder;
import net.trainingsoase.data.OaseAPIImpl;
import net.trainingsoase.oreo.inventory.InventoryLayout;
import net.trainingsoase.oreo.inventory.InventoryRows;
import net.trainingsoase.oreo.inventory.InventorySlot;
import net.trainingsoase.oreo.inventory.translated.GlobalTranslatedInventoryBuilder;
import net.trainingsoase.oreo.item.builder.LeatherArmorBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class ShopInventory {

    private final GlobalTranslatedInventoryBuilder shopItemInventoryBuilder;

    private final HashMap<ShopCategory, GlobalTranslatedInventoryBuilder> shopInventories;

    private final InventoryLayout layout;

    private final Bedwars bedwars;

    private final MessageCache ressourceCache;

    public ShopInventory(Bedwars bedwars, ShopCategory category, HashMap<ShopCategory, GlobalTranslatedInventoryBuilder> shopInventories, InventoryLayout layoutMain) {
        this.bedwars = bedwars;
        this.shopInventories = shopInventories;
        this.layout = layoutMain.clone();
        this.ressourceCache = new MessageCache(bedwars.getLanguageProvider(), "not_enough_ressources");

        shopItemInventoryBuilder = new GlobalTranslatedInventoryBuilder(InventoryRows.TWO, bedwars.getLanguageProvider());
        shopItemInventoryBuilder.registerListener(bedwars);
        shopItemInventoryBuilder.setTitleData(category.getKey());
        shopItemInventoryBuilder.setInventoryLayout(layout);

        for (ShopItems shopItems : ShopItems.getItemsFromCategory(category)) {
            layout.setItem(shopItems.getSlotID(), new InventorySlot(shopItems.getItem()).setClickListener(handleItemClick(shopItems)));
        }

        shopInventories.put(category, shopItemInventoryBuilder);
    }

    private Consumer<InventoryClickEvent> handleItemClick(ShopItems shopItem) {
        return event -> {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();
            final IOasePlayer iOasePlayer = OaseAPIImpl.INSTANCE.getPlayerExecutor().getOnlinePlayer(player.getUniqueId());

            if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            bedwars.getTeamService().getTeam(player).ifPresent(team -> {
                Material buyMaterial = shopItem.getSpawnType().getMaterial();
                if(getItemAmount(player, buyMaterial) < shopItem.getPrice()) {

                    ActionbarAPI.setActionBarFor(player,
                            WrappedChatComponent.fromText(bedwars.getLanguageProvider().getTextProvider()
                                    .format(ressourceCache.getTextData(), iOasePlayer.getLocale())));

                            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1f, 1f);
                    return;
                }

                if(shopItem == ShopItems.BLOCK_SANDSTONE && event.isShiftClick()) {
                    int max = getItemAmount(player, buyMaterial);

                    if(max > 32) max = 32;

                    var item = new ItemBuilder(event.getCurrentItem().getType())
                            .addEnchantments(shopItem.getItem().getEnchantments())
                            .setAmount(max * 2).build();

                    removeItem(player, max, buyMaterial);
                    player.getInventory().addItem(item);
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
                    return;
                }

                ItemStack item;

                if(shopItem.getItem().getItemMeta() instanceof LeatherArmorMeta) {
                    item = new LeatherArmorBuilder(shopItem.getItem())
                            .setColor(team.getColorData().getColor()).build();

                } else if(shopItem.getShopCategory() == ShopCategory.POTIONS || shopItem.getItem().getType() == Material.MONSTER_EGG) {
                    item = shopItem.getItem();

                } else {
                    item = new ItemBuilder(event.getCurrentItem().getType())
                            .addEnchantments(shopItem.getItem().getEnchantments())
                            .setAmount(shopItem.getItem().getAmount()).build();
                }

                removeItem(player, shopItem.getPrice(), buyMaterial);

                if(event.getClick() == ClickType.NUMBER_KEY) {
                    if(player.getInventory().getItem(event.getHotbarButton()) != null) {
                        ItemStack stack = player.getInventory().getItem(event.getHotbarButton());
                        player.getInventory().setItem(event.getHotbarButton(), item);
                        player.getInventory().addItem(stack);
                    } else {
                        player.getInventory().setItem(event.getHotbarButton(), item);
                    }

                } else {
                    player.getInventory().addItem(item);
                }
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
            });

        };
    }

    private void removeItem(Player player, int count, Material material) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(material);
        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return;
        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);
            int removed = Math.min(count, stack.getAmount());
            count -= removed;
            if (stack.getAmount() == removed) {
                player.getInventory().setItem(index.intValue(), null);
            } else {
                stack.setAmount(stack.getAmount() - removed);
            }
            if (count <= 0)
                break;
        }
        player.updateInventory();
    }

    private int getItemAmount(Player player, Material material) {
        int size = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType() == material)
                size += itemStack.getAmount();
        }
        return size;
    }
}
