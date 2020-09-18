package net.endertime.enderkomplex.spigot.commands;

import java.util.ArrayList;
import java.util.HashMap;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.bungee.enums.ReportReason;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.objects.ReportInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class ReportsCommand implements CommandExecutor {

    public static HashMap<Player, ReportInfo> working = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("reports")) {
            if(p.hasPermission("ek.commands.reports")) {
                if(Bukkit.getServerName().startsWith("SilentLobby")) {
                    if(args.length == 0) {
                        if(!Database.hasOpenReport(p.getUniqueId())) {
                            Inventory inv = Bukkit.createInventory(null, 9*3, "§c§lOffene Reports");
                            ArrayList<String> lore = new ArrayList<>();

                            for(ReportReason rpr : ReportReason.values()) {
                                if(!rpr.equals(ReportReason.CHAT)) {
                                    if(!p.hasPermission("ek.canworkallreports")) {
                                        int count = Database.getRealReportCount(rpr);
                                        lore.clear();
                                        lore.add("§0");
                                        lore.add("§7Anzahl: §c" + count);
                                        lore.add("§1");
                                        inv.setItem(rpr.getSlot(), EnderAPI.getInstance().getItem(Material.valueOf(rpr.getMaterial()))
                                                .setLore(lore).setAmount(Database.getReportCount(rpr)).setHideEnchantments()
                                                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§4§l" + rpr.getTitle()).getItemStack());
                                    } else {
                                        int count = Database.getRealReportCount(rpr);
                                        lore.clear();
                                        lore.add("§0");
                                        lore.add("§7Anzahl: §c" + count);
                                        lore.add("§1");
                                        if(count > 0) {
                                            inv.setItem(rpr.getSlot(), EnderAPI.getInstance().getItem(Material.valueOf(rpr.getMaterial()))
                                                    .setLore(lore).setAmount(Database.getReportCount(rpr)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                    .setDisplayName("§4§l" + rpr.getTitle()).getItemStack());
                                        } else {
                                            inv.setItem(rpr.getSlot(), EnderAPI.getInstance().getItem(Material.valueOf(rpr.getMaterial()))
                                                    .setLore(lore).setAmount(Database.getReportCount(rpr)).setHideEnchantments()
                                                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§4§l" + rpr.getTitle()).getItemStack());
                                        }
                                    }
                                } else {
                                    int count = Database.getRealReportCount(rpr);
                                    lore.clear();
                                    lore.add("§0");
                                    lore.add("§7Anzahl: §c" + count);
                                    lore.add("§1");
                                    if(count > 0) {
                                        inv.setItem(rpr.getSlot(), EnderAPI.getInstance().getItem(Material.valueOf(rpr.getMaterial()))
                                                .setLore(lore).setAmount(Database.getReportCount(rpr)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                .setDisplayName("§4§l" + rpr.getTitle()).getItemStack());
                                    } else {
                                        inv.setItem(rpr.getSlot(), EnderAPI.getInstance().getItem(Material.valueOf(rpr.getMaterial()))
                                                .setLore(lore).setAmount(Database.getReportCount(rpr)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                .setHideEnchantments().setDisplayName("§4§l" + rpr.getTitle()).getItemStack());
                                    }
                                }
                            }
                            for(int i = 0; i < inv.getSize(); i++) {
                                if(inv.getItem(i) == null) {
                                    inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                            .setDisplayName("§c").getItemStack());
                                }
                            }

                            p.openInventory(inv);
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Dir §cwurde bereits §7ein Report zugewiesen§8!");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§creports §8[§cnotify§8]");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Du kannst diesen Befehl §cnur auf der SilentLobby §7verwenden§8!");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                }
            }
        }
        return false;
    }

}
