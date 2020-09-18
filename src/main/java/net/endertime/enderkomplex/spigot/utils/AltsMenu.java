package net.endertime.enderkomplex.spigot.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AltsMenu implements Listener {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void createInventory(Player p, UUID uuid) {
        ArrayList<UUID> alts = Database.getAlts(Database.getIP(uuid));
        ArrayList<String> lore = new ArrayList<>();
        int invsize;
        if(alts.contains(uuid)) alts.remove(uuid);
        if(alts.isEmpty()) {
            invsize = 0;
        } else {
            invsize = (int)((double)Math.ceil(alts.size()/9d)*9);
        }
        Inventory inv = Bukkit.createInventory(null, invsize, "§7Alts von §c" + EnderAPI.getInstance().getName(uuid));

        alts.forEach(alt -> {
            if(!alt.equals(uuid)) {
                lore.clear();
                lore.add("§0");
                if(Database.hasActiveBan(alt)) {
                    lore.add("§8× §7Gebannt: §4§lJA §8§l(§c§l" + Database.getActiveBanReason(alt).getTitle() + "§8§l)");
                } else {
                    if(Database.isIpExistAlts(alt)) {
                        if(Database.hasActiveBan(Database.getIPFromAlts(alt))) {
                            if(Database.isBannumgehung(Database.getIPFromAlts(alt))) {
                                lore.add("§8× §7Gebannt: §2§lNEIN §8§l(§c§lIP-Sperre§8§l)");
                            } else if(Database.isAdminban(Database.getIPFromAlts(alt))) {
                                lore.add("§8× §7Gebannt: §2§lNEIN §8§l(§c§lIP-Sperre§8§l)");
                            } else {
                                if(Database.hasActiveBan(alt)) {
                                    lore.add("§8× §7Gebannt: §4§lJA §8§l(§c§l" + Database.getActiveBanReason(alt).getTitle() + "§8§l)");
                                } else {
                                    lore.add("§8× §7Gebannt: §2§lNEIN");
                                }
                            }
                        } else {
                            if(Database.hasActiveBan(alt)) {
                                lore.add("§8× §7Gebannt: §4§lJA §8§l(§c§l" + Database.getActiveBanReason(alt).getTitle() + "§8§l)");
                            } else {
                                lore.add("§8× §7Gebannt: §2§lNEIN");
                            }
                        }
                    } else {
                        lore.add("§8× §7Gebannt: §2§lNEIN");
                    }
                }
                lore.add("§1");
                if(Database.hasActiveMute(alt)) {
                    lore.add("§8× §7Gemutet: §4§lJA §8§l(§c§l" + Database.getActiveMuteReason(alt).getTitle() + "§8§l)");
                } else {
                    lore.add("§8× §7Gemutet: §2§lNEIN");
                }
                lore.add("§2");
                if(EnderAPI.getInstance().getSettings().getOnline(alt)) {
                    if(EnderAPI.getInstance().getVersion(alt) != null) {
                        lore.add("§8× §7Online auf: §3" + EnderAPI.getInstance().getSettings().getServer(alt) + " §8(§c"
                                + EnderAPI.getInstance().getVersion(alt).getVersionName() + "§8)");
                    } else {
                        lore.add("§8× §7Online auf: §3" + EnderAPI.getInstance().getSettings().getServer(alt));
                    }
                } else {
                    lore.add("§8× §7Zuletzt online: §e" + sdf.format(Database.getLastSeen(alt)) + " §7auf §3"
                            + EnderAPI.getInstance().getSettings().getServer(alt));
                }
                lore.add("§8× §7Unique ID: §e" + alt.toString());
                lore.add("§3");
                inv.addItem(EnderAPI.getInstance().getSkull(alt).setDisplayName("§6§l" + EnderAPI.getInstance().getName(alt)).setLore(lore).getItemStack());
            }
        });

        p.openInventory(inv);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().startsWith("§7Alts von §c")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                    UUID uuid = UUID.fromString(e.getCurrentItem().getItemMeta().getLore().get(6).replaceAll("§8× §7Unique ID: §e", ""));
                    PlayerInfoMenu.createInventory(p, uuid);
                }
            }
        }
    }

}
