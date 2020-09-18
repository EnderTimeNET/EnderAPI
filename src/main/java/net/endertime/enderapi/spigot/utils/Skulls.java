package net.endertime.enderapi.spigot.utils;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class Skulls {

    private UUID uuid;
    private String name;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private SkullMeta skullMeta;

    private String displayName;
    private UUID owner;
    private List<String> lore;
    private int amount;

    public Skulls(UUID uuid) {
        this.uuid = uuid;
        if (EnderAPI.getInstance().getEnderDatabase().isUserExists(uuid)) {
            this.name = EnderAPI.getInstance().getEnderDatabase().getName(uuid);

            this.itemStack = CreateSkulls.createSkull(uuid);
            this.itemMeta = this.itemStack.getItemMeta();
        } else {
            this.itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            this.itemMeta = this.itemStack.getItemMeta();
            this.skullMeta = (SkullMeta) this.itemMeta;
            this.skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            this.itemStack.setItemMeta(skullMeta);
        }
    }

    public Skulls(String value, String non) {
        this.uuid = null;
        this.name = null;
        this.itemStack = CreateSkulls.createSkullValue(value);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public Skulls(String name) {
        try {
            this.uuid = EnderAPI.getInstance().getEnderDatabase().getUUID(name);
            this.name = name;

            this.itemStack = CreateSkulls.createSkull(uuid);
            this.itemMeta = this.itemStack.getItemMeta();
            this.skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        } catch (NullPointerException e) {
            this.itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
            skullMeta.setOwner(name);
            this.itemStack.setItemMeta(skullMeta);
            this.itemMeta = this.itemStack.getItemMeta();
            this.skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        }
    }

    public Skulls(SkullType skullType) {
        this.itemStack = skullType.getItemStack();
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

    public SkullMeta getSkullMeta() {
        return skullMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public Skulls setAmount(int amount) {
        this.amount = amount;
        getItemStack().setAmount(amount);
        return this;
    }

    public Skulls setDisplayName(String displayName) {
        this.displayName = displayName;
        getItemMeta().setDisplayName(displayName);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public Skulls setLore(List<String> lore) {
        this.lore = lore;
        getItemMeta().setLore(lore);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public Skulls setHideEnchantments() {
        getItemMeta().addEnchant(Enchantment.LUCK, 10, false);
        getItemStack().setItemMeta(getItemMeta());
        return this;
    }

    public Skulls setEnchantment(Enchantment enchantment, int level) {
        getItemStack().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public Skulls setOwner(UUID owner) {
        this.owner = owner;
        getSkullMeta().setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        getItemStack().setItemMeta(getSkullMeta());
        return this;
    }

}
