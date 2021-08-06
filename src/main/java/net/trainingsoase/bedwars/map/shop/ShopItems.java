package net.trainingsoase.bedwars.map.shop;

import net.trainingsoase.bedwars.map.spawner.SpawnType;
import net.trainingsoase.bedwars.utils.ItemBuilder;
import net.trainingsoase.oreo.item.builder.LeatherArmorBuilder;
import net.trainingsoase.oreo.item.builder.PotionBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public enum ShopItems {

    BLOCK_SANDSTONE(ShopCategory.BLOCKS, SpawnType.BRONZE, 1, 10, new ItemBuilder(Material.SANDSTONE)
            .setAmount(2)
            .setLore(Arrays.asList("", " §8» §c1 Bronze"))
            .setDisplayName("§7§lSandstone").build()),

    BLOCK_ENDSTONE(ShopCategory.BLOCKS, SpawnType.BRONZE, 7, 11, new ItemBuilder(Material.ENDER_STONE)
            .setLore(Arrays.asList("", " §8» §c7 Bronze"))
            .setDisplayName("§7§lEndstone").build()),

    BLOCK_IRON(ShopCategory.BLOCKS, SpawnType.IRON, 3, 12, new ItemBuilder(Material.IRON_BLOCK)
            .setLore(Arrays.asList("", " §8» §f3 Iron"))
            .setDisplayName("§7§lIronblock").build()),

    BLOCK_GLASS(ShopCategory.BLOCKS, SpawnType.BRONZE, 10, 14, new ItemBuilder(Material.GLASS)
            .setLore(Arrays.asList("", " §8» §c10 Bronze"))
            .setDisplayName("§7§lGlass").build()),

    BLOCK_WEB(ShopCategory.BLOCKS, SpawnType.BRONZE, 16, 15, new ItemBuilder(Material.WEB)
            .setLore(Arrays.asList("", " §8» §c16 Bronze"))
            .setDisplayName("§7§lCobweb").build()),

    BLOCK_LEADER(ShopCategory.BLOCKS, SpawnType.BRONZE, 1, 16, new ItemBuilder(Material.LADDER)
            .setLore(Arrays.asList("", " §8» §c1 Bronze"))
            .setDisplayName("§7§lLadder").build()),


    ARMOR_HELMET(ShopCategory.ARMOR, SpawnType.BRONZE, 1, 10, new LeatherArmorBuilder(LeatherArmorBuilder.LeatherType.HELMET)
            .setLore(Arrays.asList("", " §8» §c1 Bronze"))
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .setDisplayName("§7§lHelmet").build()),

    ARMOR_LEGGINGS(ShopCategory.ARMOR, SpawnType.BRONZE, 1, 11, new LeatherArmorBuilder(LeatherArmorBuilder.LeatherType.LEGGINGS)
            .setLore(Arrays.asList("", " §8» §c1 Bronze"))
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .setDisplayName("§7§lLeggings").build()),

    ARMOR_BOOTS(ShopCategory.ARMOR, SpawnType.BRONZE, 1, 12, new LeatherArmorBuilder(LeatherArmorBuilder.LeatherType.BOOTS)
            .setLore(Arrays.asList("", " §8» §c1 Bronze"))
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .setDisplayName("§7§lBoots").build()),

    ARMOR_CHESTPLATE_I(ShopCategory.ARMOR, SpawnType.IRON, 1, 14, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE)
            .setLore(Arrays.asList("", " §8» §f1 Iron"))
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .setDisplayName("§7§lChestplate §8- §eLevel I").build()),

    ARMOR_CHESTPLATE_II(ShopCategory.ARMOR, SpawnType.IRON, 3, 15, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE)
            .setLore(Arrays.asList("", " §8» §f3 Iron"))
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .setDisplayName("§7§lChestplate §8- §eLevel II").build()),

    ARMOR_CHESTPLATE_III(ShopCategory.ARMOR, SpawnType.IRON, 7, 16, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE)
            .setLore(Arrays.asList("", " §8» §f7 Iron"))
            .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .setDisplayName("§7§lChestplate §8- §eLevel III").build()),


    PICKAXE_WOOD(ShopCategory.PICKAXES, SpawnType.BRONZE, 4, 11, new ItemBuilder(Material.WOOD_PICKAXE)
            .setLore(Arrays.asList("", " §8» §c4 Bronze"))
            .addEnchantment(Enchantment.DIG_SPEED, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setDisplayName("§7§lWooden Pickaxe").build()),

    PICKAXE_STONE(ShopCategory.PICKAXES, SpawnType.IRON, 2, 13, new ItemBuilder(Material.STONE_PICKAXE)
            .setLore(Arrays.asList("", " §8» §f2 Iron"))
            .addEnchantment(Enchantment.DIG_SPEED, 2)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setDisplayName("§7§lStone Pickaxe").build()),

    PICKAXE_IRON(ShopCategory.PICKAXES, SpawnType.GOLD, 1, 15, new ItemBuilder(Material.IRON_PICKAXE)
            .setLore(Arrays.asList("", " §8» §61 Gold"))
            .addEnchantment(Enchantment.DIG_SPEED, 3)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setDisplayName("§7§lIron Pickaxe").build()),


    SWORD_STICK(ShopCategory.SWORDS, SpawnType.BRONZE, 8, 11, new ItemBuilder(Material.STICK)
            .addEnchantment(Enchantment.KNOCKBACK, 1)
            .setLore(Arrays.asList("", " §8» §c8 Bronze"))
            .setDisplayName("§7§lKnockbackstick").build()),

    SWORD_GOLD_I(ShopCategory.SWORDS, SpawnType.IRON, 1, 13, new ItemBuilder(Material.GOLD_SWORD)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setLore(Arrays.asList("", " §8» §f1 Iron"))
            .setDisplayName("§7§lGolden Sword §8- §eLevel I").build()),

    SWORD_GOLD_II(ShopCategory.SWORDS, SpawnType.IRON, 3, 14, new ItemBuilder(Material.GOLD_SWORD)
            .addEnchantment(Enchantment.DAMAGE_ALL, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setLore(Arrays.asList("", " §8» §f3 Iron"))
            .setDisplayName("§7§lGolden Sword §8- §eLevel II").build()),

    SWORD_IRON(ShopCategory.SWORDS, SpawnType.GOLD, 5, 15, new ItemBuilder(Material.IRON_SWORD)
            .addEnchantment(Enchantment.DAMAGE_ALL, 2)
            .addEnchantment(Enchantment.KNOCKBACK, 1)
            .addEnchantment(Enchantment.DURABILITY, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setLore(Arrays.asList("", " §8» §65 Gold"))
            .setDisplayName("§7§lIron Sword").build()),


    BOW_BOW_I(ShopCategory.BOWS, SpawnType.GOLD, 3, 11, new ItemBuilder(Material.BOW)
            .addEnchantment(Enchantment.ARROW_INFINITE, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setLore(Arrays.asList("", " §8» §63 Gold"))
            .setDisplayName("§7§lBow §8- §eLevel I").build()),

    BOW_BOW_II(ShopCategory.BOWS, SpawnType.GOLD, 7, 12, new ItemBuilder(Material.BOW)
            .addEnchantment(Enchantment.ARROW_INFINITE, 1)
            .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setLore(Arrays.asList("", " §8» §67 Gold"))
            .setDisplayName("§7§lBow §8- §eLevel II").build()),

    BOW_BOW_III(ShopCategory.BOWS, SpawnType.GOLD, 13, 13, new ItemBuilder(Material.BOW)
            .addEnchantment(Enchantment.ARROW_INFINITE, 1)
            .addEnchantment(Enchantment.ARROW_DAMAGE, 2)
            .addEnchantment(Enchantment.ARROW_KNOCKBACK, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .setLore(Arrays.asList("", " §8» §613 Gold"))
            .setDisplayName("§7§lBow §8- §eLevel III").build()),

    BOW_ARROW(ShopCategory.BOWS, SpawnType.GOLD, 1, 15, new ItemBuilder(Material.ARROW)
            .setLore(Arrays.asList("", " §8» §61 Gold"))
            .setDisplayName("§7§lArrow").build()),


    CHEST_CHEST(ShopCategory.CHESTS, SpawnType.IRON, 1, 12, new ItemBuilder(Material.CHEST)
            .setLore(Arrays.asList("", " §8» §f1 Iron"))
            .setDisplayName("§7§lChest").build()),

    CHEST_FAKECHEST(ShopCategory.CHESTS, SpawnType.IRON, 3, 13, new ItemBuilder(Material.TRAPPED_CHEST)
            .setLore(Arrays.asList("", " §8» §f3 Iron"))
            .setDisplayName("§7§lFake Chest").build()),

    CHEST_ENDERCHEST(ShopCategory.CHESTS, SpawnType.GOLD, 1, 14, new ItemBuilder(Material.ENDER_CHEST)
            .setLore(Arrays.asList("", " §8» §61 Gold"))
            .setDisplayName("§7§lEnder Chest").build()),


    POTIONS_HEALING_I(ShopCategory.POTIONS, SpawnType.IRON, 3, 10, new ItemBuilder(Material.POTION)
            .setData((short) 8197)
            .setLore(Arrays.asList("", " §8» §f3 Iron"))
            .setDisplayName("§7§lPotion of Healing §8- §eLevel I").build()),

    POTIONS_HEALING_II(ShopCategory.POTIONS, SpawnType.IRON, 5, 11, new ItemBuilder(Material.POTION)
            .setData((short) 8229)
            .setLore(Arrays.asList("", " §8» §f5 Iron"))
            .setDisplayName("§7§lPotion of Healing §8- §eLevel II").build()),

    POTIONS_SWIFTNESS(ShopCategory.POTIONS, SpawnType.IRON, 7, 12, new PotionBuilder(PotionType.SPEED)
            .setSplash(false)
            .addEffect(new PotionEffect(PotionEffectType.SPEED, 20*90, 0, true, true))
            .setLore(Arrays.asList("", " §8» §f7 Iron"))
            .setDisplayName("§7§lPotion of Swiftness").build()),

    POTIONS_LEAPING(ShopCategory.POTIONS, SpawnType.IRON, 8, 13, new PotionBuilder(PotionType.JUMP)
            .setSplash(false)
            .addEffect(new PotionEffect(PotionEffectType.JUMP, 20*90, 1, true, true))
            .setLore(Arrays.asList("", " §8» §f8 Iron"))
            .setDisplayName("§7§lPotion of Leaping").build()),

    POTIONS_INVISIBILITY(ShopCategory.POTIONS, SpawnType.IRON, 32, 14, new PotionBuilder(PotionType.INVISIBILITY)
            .setSplash(false)
            .addEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*30, 0, true, true))
            .setLore(Arrays.asList("", " §8» §f32 Iron"))
            .setDisplayName("§7§lPotion of Invisibility").build()),

    POTIONS_STRENGTH(ShopCategory.POTIONS, SpawnType.GOLD, 7, 16, new ItemBuilder(Material.POTION)
            .setData((short) 8201)
            .setLore(Arrays.asList("", " §8» §67 Gold"))
            .setDisplayName("§7§lPotion of Strength").build()),


    SPECIALS_SLIME(ShopCategory.SPECIALS, SpawnType.IRON, 2, 10, new ItemBuilder(Material.SLIME_BLOCK)
            .setLore(Arrays.asList("", " §8» §f2 Iron"))
            .setDisplayName("§7§lSlime Block").build()),

    SPECIALS_WARPPOWDER(ShopCategory.SPECIALS, SpawnType.IRON, 3, 11, new ItemBuilder(Material.SULPHUR)
            .setLore(Arrays.asList("", " §8» §f3 Iron"))
            .setDisplayName("§7§lWarp powder").build()),

    SPECIALS_RESCUEPLATFORM(ShopCategory.SPECIALS, SpawnType.GOLD, 3, 12, new ItemBuilder(Material.BLAZE_ROD)
            .setLore(Arrays.asList("", " §8» §63 Gold"))
            .setDisplayName("§7§lRescue platform").build()),

    SPECIALS_ENDERPEARL(ShopCategory.SPECIALS, SpawnType.GOLD, 11, 13, new ItemBuilder(Material.ENDER_PEARL)
            .setLore(Arrays.asList("", " §8» §611 Gold"))
            .setDisplayName("§7§lEnderpearl").build()),

    SPECIALS_ENEMYBOOST(ShopCategory.SPECIALS, SpawnType.IRON, 7, 14, new ItemBuilder(Material.FIREWORK)
            .setLore(Arrays.asList("", " §8» §f7 Iron"))
            .setDisplayName("§7§lEnemy Boost").build()),

    SPECIALS_SHEEPS(ShopCategory.SPECIALS, SpawnType.BRONZE, 64, 15, new ItemBuilder(Material.MONSTER_EGG)
            .setData((short) 91)
            .setLore(Arrays.asList("", " §8» §c64 Bronze"))
            .setDisplayName("§7§lSophiäää").build()),

    SPECIALS_TNT(ShopCategory.SPECIALS, SpawnType.GOLD, 4, 16, new ItemBuilder(Material.TNT)
            .setData((short) 96)
            .setLore(Arrays.asList("", " §8» §64 Gold"))
            .setDisplayName("§7§lInstant TNT").build());

    private ShopCategory shopCategory;

    private SpawnType spawnType;

    private int price;

    private int slotID;

    private ItemStack item;

    public static final ShopItems[] VALUES = values();

    ShopItems(ShopCategory shopCategory, SpawnType spawnType, int price, int slotID, ItemStack item) {
        this.shopCategory = shopCategory;
        this.spawnType = spawnType;
        this.price = price;
        this.slotID = slotID;
        this.item = item;
    }

    public static List<ShopItems> getItemsFromCategory(ShopCategory category) {
        List<ShopItems> shopItems = new ArrayList<>();

        for (ShopItems value : VALUES) {
            if(value.getShopCategory() == category) {
                shopItems.add(value);
            }
        }

        return shopItems;
    }


    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public SpawnType getSpawnType() {
        return spawnType;
    }

    public int getSlotID() {
        return slotID;
    }
}
