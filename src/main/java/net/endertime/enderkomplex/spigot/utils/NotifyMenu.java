package net.endertime.enderkomplex.spigot.utils;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class NotifyMenu implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (!e.getAction().equals(InventoryAction.NOTHING)) {
            if (e.getInventory().getTitle().equals("§c§lBenachrichtigungen")) {
                e.setCancelled(true);
                if(!e.getCurrentItem().getItemMeta().hasEnchants()) {
                    if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                        switch (e.getRawSlot()) {
                            case 1:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.REPORTS, true);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                            case 3:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.CONNECTION, true);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                            case 5:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.BANSYSTEM, true);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                            case 7:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.CHATFILTER, true);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                        }
                    } else if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                        switch (e.getRawSlot()) {
                            case 1:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.REPORTS, false);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                            case 3:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.CONNECTION, false);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                            case 5:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.BANSYSTEM, false);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                            case 7:
                                Database.updateNotifySettings(p.getUniqueId(), NotifyType.CHATFILTER, false);
                                e.getInventory().setItem(e.getRawSlot(), EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK)
                                        .setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName()).getItemStack());
                                EnderAPI.getInstance().playSound(p, Sound.BLOCK_LEVER_CLICK);
                                break;
                        }
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§cDiese Option ist gesperrt§8!");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                }
            }
        }
    }

    public static void openMenu(Player p) {
        if (!Database.existsInNotifySettings(p.getUniqueId()))
            Database.createNewNotifySettings(p.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 9, "§c§lBenachrichtigungen");

        if(p.hasPermission("ek.commands.reports")) {
            if (Database.getNotifySetting(p.getUniqueId(), NotifyType.REPORTS)) {
                inv.setItem(1, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK).setDisplayName("§cReport Notifys").getItemStack());
            } else {
                inv.setItem(1, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§cReport Notifys").getItemStack());
            }
        } else {
            inv.setItem(1, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§cReport Notifys")
                    .setHideEnchantments().getItemStack());
        }

        if (Database.getNotifySetting(p.getUniqueId(), NotifyType.CONNECTION)) {
            inv.setItem(3, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK).setDisplayName("§cConnection Notifys").getItemStack());
        } else {
            inv.setItem(3, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§cConnection Notifys").getItemStack());
        }

        if (Database.getNotifySetting(p.getUniqueId(), NotifyType.BANSYSTEM)) {
            inv.setItem(5, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK).setDisplayName("§cBan/Mute Notifys").getItemStack());
        } else {
            inv.setItem(5, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§cBan/Mute Notifys").getItemStack());
        }

        if(p.hasPermission("ek.notify.chatfilter")) {
            if (Database.getNotifySetting(p.getUniqueId(), NotifyType.CHATFILTER)) {
                inv.setItem(7, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK).setDisplayName("§cChatfilter").getItemStack());
            } else {
                inv.setItem(7, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§cChatfilter").getItemStack());
            }
        } else {
            inv.setItem(7, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§cChatfilter")
                    .setHideEnchantments().getItemStack());
        }

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c")
                        .getItemStack());
            }
        }
        p.openInventory(inv);
    }
}
