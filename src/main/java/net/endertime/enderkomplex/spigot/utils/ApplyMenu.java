package net.endertime.enderkomplex.spigot.utils;

import java.util.UUID;

import net.endertime.enderapi.permission.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class ApplyMenu implements Listener {

    public static void openPhaseInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Bewerbungsphasen");

        if(Database.getApplyphaseStatus("Design")) {
            inv.setItem(0, EnderAPI.getInstance().getItem(Material.PAPER).setDisplayName("§cDesign").setHideEnchantments().getItemStack());
        } else {
            inv.setItem(0, EnderAPI.getInstance().getItem(Material.PAPER).setDisplayName("§cDesign").getItemStack());
        }

        if(Database.getApplyphaseStatus("Build")) {
            inv.setItem(1, EnderAPI.getInstance().getItem(Material.IRON_PICKAXE).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setDisplayName("§cBuild").setHideEnchantments().getItemStack());
        } else {
            inv.setItem(1, EnderAPI.getInstance().getItem(Material.IRON_PICKAXE).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setDisplayName("§cBuild").getItemStack());
        }

        if(Database.getApplyphaseStatus("Sup")) {
            inv.setItem(2, EnderAPI.getInstance().getItem(Material.IRON_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setDisplayName("§cSup").setHideEnchantments().getItemStack());
        } else {
            inv.setItem(2, EnderAPI.getInstance().getItem(Material.IRON_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setDisplayName("§cSup").getItemStack());
        }

        if(Database.getApplyphaseStatus("Content")) {
            inv.setItem(3, EnderAPI.getInstance().getItem(Material.BOOK_AND_QUILL).setDisplayName("§cContent").setHideEnchantments().getItemStack());
        } else {
            inv.setItem(3, EnderAPI.getInstance().getItem(Material.BOOK_AND_QUILL).setDisplayName("§cContent").getItemStack());
        }

        if(Database.getApplyphaseStatus("Dev")) {
            inv.setItem(4, EnderAPI.getInstance().getItem(Material.WATER_BUCKET).setDisplayName("§cDev").setHideEnchantments().getItemStack());
        } else {
            inv.setItem(4, EnderAPI.getInstance().getItem(Material.WATER_BUCKET).setDisplayName("§cDev").getItemStack());
        }

        p.openInventory(inv);
    }

    public static void openAcceptInventory(Player p, int tsnumber) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Entscheidung von §c" + tsnumber);

        inv.setItem(1, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK).setDisplayName("§2§lANNEHMEN").getItemStack());

        inv.setItem(3, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§4§lABLEHNEN").getItemStack());

        p.openInventory(inv);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().equals("§6Bewerbungsphasen")) {
                e.setCancelled(true);
                p.closeInventory();
                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                if(e.getCurrentItem().getItemMeta().hasEnchants()) {
                    Database.updateApplyphaseStatus(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§c", ""), false);
                } else {
                    Database.updateApplyphaseStatus(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§c", ""), true);
                }
            } else if(e.getInventory().getTitle().startsWith("§6Entscheidung von §c")) {
                e.setCancelled(true);
                p.closeInventory();
                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                int tsid = Integer.valueOf(e.getInventory().getTitle().replaceAll("§6Entscheidung von §c", ""));
                if(e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                    Database.updateApplyStatus(tsid, -2, true);
                    String rank = Database.getApplyRank(tsid);
                    UUID targetUUUID = UUID.fromString(Database.getApplyUUID(tsid));
                    switch (rank) {
                        case "Developer":
                            PermAPI.getInstance().setGroup(targetUUUID, "JrDev", -1);
                            break;
                        case "Designer":
                            PermAPI.getInstance().setGroup(targetUUUID, "JrDesign", -1);
                            break;
                        case "Content":
                            PermAPI.getInstance().setGroup(targetUUUID, "JrContent", -1);
                            break;
                        case "Supporter":
                            PermAPI.getInstance().setGroup(targetUUUID, "JrSup", -1);
                            break;
                        case "Ender+":
                            PermAPI.getInstance().setGroup(targetUUUID, "Ender+", -1);
                            break;
                        case "YouTuber":
                            PermAPI.getInstance().setGroup(targetUUUID, "YouTuber", -1);
                            break;
                        default:
                            break;
                    }
                } else if(e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                    Database.updateApplyStatus(tsid, -2, false);
                }
            }
        }
    }

}
