package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.UUID;

public class Stats_Command implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("stats")) {
                Stats stats = Stats.getStats();

                if (stats != null) {
                    if (args.length == 0) {
                        player.openInventory(Stats.getInventory(player, stats));
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase(player.getName())) {
                            player.openInventory(Stats.getInventory(player, stats));
                        } else if (EnderAPI.getInstance().getUUID(args[0]) != null) {
                            player.openInventory(Stats.getInventory(player, EnderAPI.getInstance().getUUID(args[0]), stats));
                        } else {
                            if (EnderAPI.getInstance().getTeamDatabase().isNicked(args[0])) {
                                UUID uuidNick = EnderAPI.getInstance().getTeamDatabase().getUUIDFromNickedName(args[0]);
                                player.openInventory(Stats.getInventoryNick(player, EnderAPI.getInstance().getNickDatabase().getUUID(EnderAPI.getInstance()
                                        .getTeamDatabase().getNickedName(uuidNick)), uuidNick, stats));
                            } else {
                                EnderAPI.getInstance().sendActionBar(player, "§7Dieser Spieler war §cnoch nie §7auf §5EnderTime§8!");
                                EnderAPI.getInstance().playSound(player, Sound.ITEM_BREAK);
                            }
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(player, "§7Benutze§8: /§cstats §8[<§cname§8>]");
                        EnderAPI.getInstance().playSound(player, Sound.ITEM_BREAK);
                    }
                } else {
                    player.sendMessage(EnderAPI.getInstance().getNoPerm());
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().equals("§6Deine Statistik")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.BARRIER)) {
                    if(EnderAPI.getInstance().getAutoReset().getTokens(p.getUniqueId()) >= 1) {
                        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Statistik zurücksetzen");

                        inv.setItem(1, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK).setDisplayName("§2§lJA")
                                .setLore(Arrays.asList(new String[]{"§0", "§7§lAuswirkungen:", "§7Deine Kills§8-/§7Todesstatistik vom", "§7Spielmodus "
                                        + Stats.getStats().getGameName() + " §7wird resettet", "§1"}))
                                .getItemStack());

                        inv.setItem(3, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).setDisplayName("§4§lNEIN")
                                .setLore(Arrays.asList(new String[]{"§0", "§7§lAuswirkungen:", "§7§okeine", "§1"}))
                                .getItemStack());

                        p.openInventory(inv);
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Du hast §ckeine §7Reset-Tokens! Hole dir welche in unserem Online-Shop");
                        p.playSound(p.getLocation(), Sound.ITEM_BREAK, (float) 0.5, 1);
                    }
                }
            } else if(e.getInventory().getTitle().equals("§6Statistikvergleich")) {
                e.setCancelled(true);
            } else if (e.getInventory().getName().equals("§6Statistik zurücksetzen")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                    Stats stats = Stats.getStats();
                    PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM " + stats.getName() + "_ALLTIME WHERE UUID = ?");
                    ps.setString(1, p.getUniqueId().toString());
                    EnderAPI.getInstance().getAutoReset().getMysql().runAsyncUpdate(ps);

                    ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM " + stats.getName() + "_MONTHLY WHERE UUID = ?");
                    ps.setString(1, p.getUniqueId().toString());
                    EnderAPI.getInstance().getAutoReset().getMysql().runAsyncUpdate(ps);

                    EnderAPI.getInstance().getAutoReset().updateTokens(p.getUniqueId(),
                            EnderAPI.getInstance().getAutoReset().getTokens(p.getUniqueId()) -1);

                    p.closeInventory();

                    EnderAPI.getInstance().sendActionBar(p, "§7Du hast §2erfolgreich §7deine Statistik von " + stats.getGameName() + " §7zurückgesetzt!");
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, (float) 0.5, 1);
                } else if(e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, (float) 0.5, 1);
                }
            }
        }
    }
}
