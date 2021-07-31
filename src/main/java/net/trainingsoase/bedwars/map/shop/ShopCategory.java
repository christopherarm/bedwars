package net.trainingsoase.bedwars.map.shop;

import org.bukkit.Material;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public enum ShopCategory {

    BLOCKS("shop_blocks", Material.SANDSTONE),
    ARMOR("shop_armor", Material.CHAINMAIL_CHESTPLATE),
    PICKAXES("shop_picks", Material.WOOD_PICKAXE),
    SWORDS("shop_swords", Material.GOLD_SWORD),
    BOWS("shop_bows", Material.BOW),
    CHESTS("shop_chests", Material.CHEST),
    POTIONS("shop_potions", Material.POTION),
    SPECIALS("shop_specials", Material.EMERALD);

    private String key;
    private Material material;

    ShopCategory(String key, Material material) {
        this.key = key;
        this.material = material;
    }

    public String getKey() {
        return key;
    }

    public Material getMaterial() {
        return material;
    }
}
