package net.endertime.enderapi.spigot.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private Material material;
    private ItemStack itemStack;
    private int subID;
    private int amount;
    private String displayName;
    private List<String> lore;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder(int id) {
        this.itemStack = new ItemStack(id);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int subID) {
        this.itemStack = new ItemStack(material, amount, (short)subID);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(int id, int amount, int subID) {
        this.itemStack = new ItemStack(id, amount, (short)subID);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSubID() {
        return subID;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        getItemStack().setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        getItemMeta().setDisplayName(displayName);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        getItemMeta().setLore(lore);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag itemFlag){
        getItemMeta().addItemFlags(itemFlag);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public ItemBuilder setHideEnchantments() {
        getItemMeta().addEnchant(Enchantment.LUCK, 1, false);
        getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public ItemBuilder setEnchantment(Enchantment enchantment, int level) {
        getItemMeta().addEnchant(enchantment, level, true);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public ItemBuilder setDurability(short level) {
        getItemStack().setDurability(level);
        return this;
    }

    public ItemBuilder setUnsafeEnchantment(Enchantment enchantment, int level) {
        getItemStack().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        getItemMeta().spigot().setUnbreakable(true);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

}
