package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.utils.Sounds;
import net.endertime.enderapi.spigot.utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.List;
import java.util.UUID;

public class Reset_Command implements CommandExecutor, Listener {

    @EventHandler
    public void onClick (InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (!e.getAction().equals(InventoryAction.NOTHING)) {
            if (e.getRawSlot() < p.getOpenInventory().getTopInventory().getSize()) {
                if (e.getCurrentItem() != null) {
                    if (!e.getCurrentItem().getType().equals(Material.AIR)) {
                        if (e.getCurrentItem().hasItemMeta()) {
                            if (e.getInventory().getTitle().startsWith("§7Statsreset von ")) {
                                e.setCancelled(true);
                                if (!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                                    if(e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
                                        p.closeInventory();
                                        EnderAPI.getInstance().playSound(p, Sounds.FAILED);
                                    } else if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                                        UUID uuid = EnderAPI.getInstance().getUUID(e.getInventory().getTitle().substring(19));

                                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Alle Stats §cresetten")) {
                                            reset(uuid);
                                        } else {
                                            String modus = e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0].substring(2);
                                            for (Stats stats : Stats.getAllStats()) {
                                                if (stats.getName().equals(modus))
                                                    reset(uuid, stats);
                                            }
                                        }
                                        EnderAPI.getInstance().sendActionBar(p, "§7Du hast die Stats von "
                                                + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid) + " §cresettet");

                                        p.closeInventory();
                                        EnderAPI.getInstance().playSound(p, Sounds.SUCCESS);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void reset(UUID uuid) {
        List<Stats> stats = Stats.getAllStats();

        for (Stats stat : stats) {
            reset(uuid, stat);
        }
    }

    private void reset(UUID uuid, Stats stats) {
        if (!stats.equals(Stats.ONEVSONE)) {
            delete(uuid, stats.getMysql(), stats.getName() + "_ALLTIME");
            delete(uuid, stats.getMysql(), stats.getName() + "_MONTHLY");
        }
    }

    private void delete (UUID uuid, MySQL mysql, String table) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM " + table + " WHERE UUID = ?");
        ps.setString(1, uuid.toString());

        mysql.runAsyncUpdate(ps);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("reset.stats")) {
                if (args.length == 1) {
                    UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                    if (uuid != null) {
                        Inventory inventory = EnderAPI.getInstance().getInventory(Bukkit.createInventory(null, InventoryType.HOPPER, "§7Statsreset von "
                                + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid)));

                        inventory.setItem(1, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK)
                                .setDisplayName("§7Alle Stats §cresetten").getItemStack());
                        inventory.setItem(3, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK).
                                setDisplayName("§7Keine Stats §cresetten").getItemStack());

                        player.openInventory(inventory);
                    } else {
                        EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                        EnderAPI.getInstance().sendActionBar(player, "§7" + args[0] + " war noch §cnie §7auf dem Netzwerk");
                    }
                } else if (args.length == 2) {
                    UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                    if (uuid != null) {
                        List<Stats> stats = Stats.getAllStats();
                        String modus = args[1];
                        Inventory inventory = null;
                        for (Stats stat : stats) {
                            if (stat.getName().equalsIgnoreCase(modus)) {
                                inventory = EnderAPI.getInstance().getInventory(Bukkit.createInventory(null, InventoryType.HOPPER, "§7Statsreset von "
                                        + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid)));

                                inventory.setItem(1, EnderAPI.getInstance().getItem(Material.EMERALD_BLOCK)
                                        .setDisplayName("§7" + stat.getName() + " Stats §cresetten").getItemStack());
                                inventory.setItem(3, EnderAPI.getInstance().getItem(Material.REDSTONE_BLOCK)
                                        .setDisplayName("§7Keine Stats §cresetten").getItemStack());
                            }
                        }

                        if (inventory == null) {
                            EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                            player.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Verfügbare Spiemodi");
                            for (Stats stat : stats) {
                                player.sendMessage("§8- §c" + stat.getName());
                            }
                        } else{
                            player.openInventory(inventory);
                        }
                    } else {
                        EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                        EnderAPI.getInstance().sendActionBar(player, "§7" + args[0] + " war noch §cnie §7auf dem Netzwerk");
                    }
                } else {
                    EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                    EnderAPI.getInstance().sendActionBar(player, "§8/§creset §8<§cname§8> §8<§cmodus§8>");
                }
            } else {
                sender.sendMessage(EnderAPI.getInstance().getNoPerm());
            }
        }
        return false;
    }
}
