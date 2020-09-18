package net.endertime.enderkomplex.spigot.utils;

import java.util.ArrayList;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.bungee.enums.ReportReason;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.commands.ReportCommand;
import net.endertime.enderkomplex.spigot.objects.Report;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class ReportListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().equals("§4§lWähle einen Reportgrund")) {
                if(e.getRawSlot() <= 26) {
                    e.setCancelled(true);
                    if(!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                        if(!e.getCurrentItem().getItemMeta().hasEnchants()) {
                            if(!Database.hasActiveReport(ReportCommand.reports.get(p).getUniqueId())) {
                                if(!ReportCommand.reports.get(p).hasPermission("teamserver.join")) {
                                    EnderAPI.getInstance().sendActionBar(p, "§7Du hast §aerfolgreich §7einen Report für " +
                                            e.getCurrentItem().getItemMeta().getDisplayName() + "§7 erstellt§8!");
                                    p.closeInventory();
                                    EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                                    new Report(ReportCommand.reports.get(p), p.getUniqueId().toString(),
                                            ReportReason.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§4§l", "")));
                                    ReportCommand.reports.remove(p);
                                } else {
                                    EnderAPI.getInstance().sendActionBar(p, "§7Du hast §aerfolgreich §7einen Report für " +
                                            e.getCurrentItem().getItemMeta().getDisplayName() + "§7 erstellt§8!");
                                    p.closeInventory();
                                    EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                                    ReportCommand.reports.remove(p);
                                }
                            } else {
                                EnderAPI.getInstance().sendActionBar(p, "§7Du hast §aerfolgreich §7einen Report für "
                                        + e.getCurrentItem().getItemMeta().getDisplayName() + "§7 erstellt§8!");
                                p.closeInventory();
                                EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                                ReportCommand.reports.remove(p);
                            }
                        } else {
                            if(e.getCurrentItem().getType().equals(Material.BOOK_AND_QUILL)) {
                                p.closeInventory();
                                EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                                EnderAPI.getInstance().sendActionBar(p, "§7Dieser Spieler hat §cnoch nichts §7geschrieben§8!");
                            } else if(e.getCurrentItem().getType().equals(Material.RABBIT_FOOT)) {
                                p.closeInventory();
                                EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                                EnderAPI.getInstance().sendActionBar(p, "§7Du musst §c2 Spieler §7angeben um den Grund §4§lTEAMING §7zu wählen§8!");
                            }
                        }
                    }
                }
            }
        }
    }

    public static void openReportGUI(Player p, String playername) {
        Inventory inv = Bukkit.createInventory(null, 9*3, "§4§lWähle einen Reportgrund");
        ArrayList<String> lore = new ArrayList<>();
        for(ReportReason rr : ReportReason.values()) {
            lore.clear();
            lore.add("§0");
            lore.add("§7§lBetroffener Spieler:");
            lore.add("§8× §c" + playername);
            lore.add("§1");
            inv.setItem(rr.getSlot(), EnderAPI.getInstance().getItem(Material.valueOf(rr.getMaterial())).setDisplayName("§4§l" +
                    rr.getTitle().toUpperCase()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setLore(lore).getItemStack());
        }
        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null) {
                inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
            }
        }
        p.openInventory(inv);
    }
}
