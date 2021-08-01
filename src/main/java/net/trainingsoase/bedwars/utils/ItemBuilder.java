package net.trainingsoase.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class ItemBuilder {

    protected ItemStack stack;
    protected ItemMeta itemMeta;

    public ItemBuilder(@NotNull Material material) {
        this.stack = new ItemStack(material);
        this.itemMeta = stack.getItemMeta();
    }

    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.stack = itemStack;
        this.itemMeta = stack.getItemMeta();
    }

    public static ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder ofClone(@NotNull ItemStack itemStack) {
        return ItemBuilder.of(itemStack.clone());
    }

    /**
     * Set the number of items from this stack
     * @param amount New amount of items in this stack
     * @return
     */

    public ItemBuilder setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    /**
     * Add enchantment item builder.
     *
     * @param enchantment        the enchantment
     * @param level              the level
     * @param ignoreRestrictions ignore restrictions
     * @return the item builder
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreRestrictions) {
        itemMeta.addEnchant(enchantment, level, ignoreRestrictions);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantmentMap) {
        enchantmentMap.forEach((enchantment, level) -> {
            itemMeta.addEnchant(enchantment, level, true);
        });
        return this;
    }

    /**
     * Add enchantment item builder.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the item builder
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        return addEnchantment(enchantment, level, true);
    }

    /**
     * Sets the displayname
     * @param name The name to set
     * @return
     */

    public ItemBuilder setDisplayName(@NotNull String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setData(@NotNull short data) {
        this.stack.setDurability(data);
        return this;
    }

    /**
     * Sets an empty display name.
     * @return
     */

    public ItemBuilder setEmptyName() {
        itemMeta.setDisplayName(" ");
        return this;
    }

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     * @param unbreakable true if set unbreakable
     * @return
     */

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Set an {@link ItemFlag} to the stack.
     * @param flag The {@link ItemFlag} to set.
     * @return
     */

    public ItemBuilder addItemFlag(ItemFlag flag) {
        itemMeta.addItemFlags(flag);
        return this;
    }

    /**
     * Sets the lore for this item. Removes lore when given null
     * @param lore The lore that will be set
     * @return
     */

    public ItemBuilder addLore(@NotNull String... lore) {
        List<String> currentLore = itemMeta.getLore();
        if (currentLore == null) currentLore = new ArrayList<>();
        currentLore.addAll(Arrays.asList(lore));
        itemMeta.setLore(currentLore);
        return this;
    }

    /**
     * Change a specific line of a lore.
     * @param index The index for the line
     * @param text The new text for the line
     * @return
     */

    public ItemBuilder setLoreLine(int index, @NotNull String text) {
        List<String> currentLore = itemMeta.getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();
            currentLore.add(text);
        }

        if (currentLore.size() < index) {
            currentLore.add(text);
        } else {
            currentLore.set(index, text);
        }
        itemMeta.setLore(currentLore);
        return this;
    }

    /**
     * Sets the lore for this item. Removes lore when given null
     * @param lore The lore that will be set
     * @return
     */

    public ItemBuilder setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * Sets the repair penalty
     * @param repairCosts repair penalty
     * @return
     */

    public ItemBuilder setRepairCosts(int repairCosts) {
        Repairable meta = (Repairable) itemMeta;
        meta.setRepairCost(repairCosts);
        stack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set the ItemMeta of this ItemStack
     * @param meta  new ItemMeta, or null to indicate meta data be cleared
     * @return
     */

    private ItemBuilder setItemMeta(ItemMeta meta) {
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Builds the stack.
     * @return The stack
     */

    public ItemStack build() {
        stack.setItemMeta(itemMeta);
        return this.stack;
    }

    /**
     * Returns the lore from a item.
     * @return a empty list when the lore is null otherwise the lore from the stack
     */

    public List<String> getLore() {
        if (itemMeta.getLore() == null) {
            return new ArrayList<>();
        }
        return itemMeta.getLore();
    }

    /**
     * Returns from a item the {@link ItemMeta}
     * @return The underlying {@link ItemMeta}
     */

    protected ItemMeta getItemMeta() {
        return itemMeta;
    }
}
