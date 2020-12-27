package net.endertime.enderkomplex.spigot.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class EditMuteCommand implements CommandExecutor {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("em")) {
            if(p.hasPermission("ek.commands.editmute")) {
                if(args.length == 1) {
                    if(EnderAPI.getInstance().getUUID(args[0]) != null) {
                        UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                        if(Database.hasActiveMute(uuid)) {
                            if(!p.hasPermission("ek.shortenpunishall")) {
                                if(!Database.getActiveMuteExecutor(uuid).toString().equals(p.getUniqueId().toString())) {
                                    EnderAPI.getInstance().sendActionBar(p, "§7Du kannst nur §cdeine eigenen Mutes §7editieren§8!");
                                    EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                                    return false;
                                }
                            }
                            Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§c§lMute bearbeiten");
                            ArrayList<String> lore = new ArrayList<>();

                            lore.clear();
                            lore.add("§0");
                            if(Database.getActiveMuteDuration(uuid) == Database.getActiveOriginalMuteDuration(uuid)) {
                                inv.setItem(0, EnderAPI.getInstance().getItem(Material.BOOK).setDisplayName("§7Mutezeit verkürzen")
                                        .setLore(lore).getItemStack());
                            } else {
                                inv.setItem(0, EnderAPI.getInstance().getItem(Material.BOOK).setDisplayName("§7Mutezeit verkürzen")
                                        .setHideEnchantments().setLore(lore).getItemStack());
                            }

                            inv.setItem(1, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                    .setDisplayName("§c").getItemStack());

                            lore.clear();
                            lore.add("§0");
                            lore.add("§7§lMute Info:");
                            if(Database.hasActiveMute(uuid)) {
                                lore.add("§8× §7Gemutet: §4JA §8(§c" + Database.getActiveMuteReason(uuid).getTitle() + "§8)");
                            } else {
                                lore.add("§8× §7Gemutet: §2NEIN");
                            }
                            lore.add("§1");
                            lore.add("§7§lSonstiges:");
                            lore.add("§8× §7Zuletzt online: §e" + sdf.format(Database.getLastSeen(uuid)));
                            lore.add("§8× §7Unique ID: §e" + uuid.toString());
                            lore.add("§2");
                            inv.setItem(2, EnderAPI.getInstance().getSkull(uuid).setDisplayName("§6§l" + EnderAPI.getInstance()
                                    .getName(uuid)).setLore(lore).getItemStack());

                            inv.setItem(3, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                    .setDisplayName("§c").getItemStack());

                            lore.clear();
                            lore.add("§0");
                            inv.setItem(4, EnderAPI.getInstance().getItem(Material.BOOK_AND_QUILL)
                                    .setDisplayName("§7Chatlog ID bearbeiten").setLore(lore).getItemStack());

                            p.openInventory(inv);
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[0] + "§7 ist §cnicht §7gemutet!");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[0] + "§7 war noch nie auf §5EnderTime§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§cem §8<§cname§8>");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                }
            }
        }
        return false;
    }

}
