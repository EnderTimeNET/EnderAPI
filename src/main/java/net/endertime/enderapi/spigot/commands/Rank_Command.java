package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.permission.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Rank_Command implements CommandExecutor, Listener {

    @EventHandler
    public void onClick (InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (!e.getAction().equals(InventoryAction.NOTHING)) {
            if (e.getRawSlot() < p.getOpenInventory().getTopInventory().getSize()) {
                if (e.getCurrentItem() != null) {
                    if (!e.getCurrentItem().getType().equals(Material.AIR)) {
                        if (e.getCurrentItem().hasItemMeta()) {
                            UUID uuid = null;
                            if (e.getInventory().getTitle().startsWith("§7RankGUI von ")) {
                                e.setCancelled(true);
                                if (!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                                    uuid = EnderAPI.getInstance().getUUID(e.getInventory().getTitle().substring(16));

                                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Ender")) {
                                        Inventory inv = Bukkit.createInventory(null, 9 * 1, "§7Ender für "
                                                + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid));

                                        for (int i = 0; i < inv.getSize(); i++)
                                            inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE,1, 15).setDisplayName("§7").getItemStack());

                                        inv.setItem(1, EnderAPI.getInstance().getItem(Material.NAME_TAG).setDisplayName("§6Ender I")
                                                .setLore(Arrays.asList(new String[]{"", "§7§lDauer:", "§8➟ §c3 Tage", ""})).getItemStack());

                                        inv.setItem(3, EnderAPI.getInstance().getItem(Material.NAME_TAG).setDisplayName("§6Ender II")
                                                .setLore(Arrays.asList(new String[]{"", "§7§lDauer:", "§8➟ §c1 Monat", ""})).getItemStack());

                                        inv.setItem(5, EnderAPI.getInstance().getItem(Material.NAME_TAG).setDisplayName("§6Ender III")
                                                .setLore(Arrays.asList(new String[]{"", "§7§lDauer:", "§8➟ §c3 Monate", ""})).getItemStack());

                                        inv.setItem(7, EnderAPI.getInstance().getItem(Material.NAME_TAG).setDisplayName("§6Ender IV")
                                                .setLore(Arrays.asList(new String[]{"", "§7§lDauer:", "§8➟ §cPermanent", ""})).getItemStack());

                                        p.openInventory(inv);
                                    } else {
                                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Ender+")
                                                || e.getCurrentItem().getItemMeta().getDisplayName().equals("§5YouTuber")) {
                                            PermAPI.getInstance().setGroup(uuid, e.getCurrentItem().getItemMeta().getDisplayName().substring(2), 30 * 24 * 60 * 60 * 1000l);
                                        } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Spieler")) {
                                            PermAPI.getInstance().setGroup(uuid, "default", -1);
                                        } else {
                                            PermAPI.getInstance().setGroup(uuid, e.getCurrentItem().getItemMeta().getDisplayName().substring(2), -1);
                                        }
                                        p.closeInventory();
                                        EnderAPI.getInstance().sendActionBar(p, "§7Du hast " + EnderAPI.getInstance().getPrefix(uuid)
                                                + EnderAPI.getInstance().getName(uuid) + " §7den Rang " + e.getCurrentItem().getItemMeta().getDisplayName()
                                                + " §7gesetzt");
                                    }
                                }
                            } else if (e.getInventory().getTitle().startsWith("§7Ender für ")) {
                                e.setCancelled(true);
                                if (!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                                    uuid = EnderAPI.getInstance().getUUID(e.getInventory().getTitle().substring(14));

                                    if (e.getCurrentItem().getItemMeta().getDisplayName().endsWith(" I")) {
                                        PermAPI.getInstance().setGroup(uuid, "Ender", 3 * 24 * 60 * 60 * 1000l);
                                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().endsWith(" II")) {
                                        PermAPI.getInstance().setGroup(uuid, "Ender", 30 * 24 * 60 * 60 * 1000l);
                                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().endsWith(" III")) {
                                        PermAPI.getInstance().setGroup(uuid, "Ender", 3 * 30 * 24 * 60 * 60 * 1000l);
                                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().endsWith(" IV")) {
                                        PermAPI.getInstance().setGroup(uuid, "Ender", -1);
                                    }
                                    p.closeInventory();

                                    EnderAPI.getInstance().sendActionBar(p, "§7Du hast " + EnderAPI.getInstance().getPrefix(uuid)
                                            + EnderAPI.getInstance().getName(uuid) + " §7den Rang " + "§6Ender"
                                            + " §7gesetzt");

                                }
                            }
                            if (uuid != null)
                                EnderAPI.getInstance().getEnderDatabase().updateRanksSpigot(uuid, PermAPI.getInstance().getGroup(uuid));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (sender.hasPermission("rankgui.use")) {
                if (args.length == 1) {
                    UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                    if (uuid != null) {
                        if (!PermAPI.getInstance().getGroup(uuid).equals("Admin")
                                && !PermAPI.getInstance().getGroup(uuid).equals("SrMod")) {
                            openInv(sender, uuid);
                        } else {
                            EnderAPI.getInstance().sendActionBar(sender, "§7Du kannst " + EnderAPI.getInstance().getPrefix(uuid)
                                    + EnderAPI.getInstance().getName(uuid) + " §ckeinen §7Rang setzten");
                            EnderAPI.getInstance().playSound(sender, Sounds.FAILED);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(sender, "§c" + args[0] + " §7war noch §cnie §7auf dem Netzwerk");
                        EnderAPI.getInstance().playSound(sender, Sounds.FAILED);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(sender, "§7Benutze§8: /§crank §8<§cname§8>");
                    EnderAPI.getInstance().playSound(sender, Sounds.FAILED);
                }
            } else {
                if (PermAPI.getInstance().getUsers().getTime(sender.getUniqueId()) == -1) {
                    EnderAPI.getInstance().sendActionBar(sender, "§7Dein Rang ist " +
                            PermAPI.getInstance().getRanks().getName(PermAPI.getInstance().getGroup(sender.getUniqueId()))
                            + " §7und läuft §cnicht §7ab");
                } else {
                    long time = PermAPI.getInstance().getUsers().getTimeSet(sender.getUniqueId()) +
                            PermAPI.getInstance().getUsers().getTime(sender.getUniqueId()) - System.currentTimeMillis();
                    long days = TimeUnit.MILLISECONDS.toDays(time);
                    if (days == 0) {
                        EnderAPI.getInstance().sendActionBar(sender, "§7Dein Rang ist " +
                                PermAPI.getInstance().getRanks().getName(PermAPI.getInstance().getGroup(sender.getUniqueId()))
                                + " §7und läuft in §cheute §7ab");
                    } else if (days == 1) {
                        EnderAPI.getInstance().sendActionBar(sender, "§7Dein Rang ist " +
                                        PermAPI.getInstance().getRanks().getName(PermAPI.getInstance().getGroup(sender.getUniqueId()))
                                 + " §7und läuft in §c" + days + " Tag §7ab");
                    } else {
                        EnderAPI.getInstance().sendActionBar(sender, "§7Dein Rang ist " +
                                PermAPI.getInstance().getRanks().getName(PermAPI.getInstance().getGroup(sender.getUniqueId()))
                                + " §7und läuft in §c" + days + " Tagen §7ab");
                    }
                }
            }
        }
        return false;
    }

    public void openInv (Player player, UUID uuid) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, "§7RankGUI von " + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid));

        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE,1, 15).setDisplayName("§7").getItemStack());
        for (int i = 37; i < 44; i++)
            inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE,1, 10).setDisplayName("§7").getItemStack());

        ItemStack partner = EnderAPI.getInstance().getItem(Material.LEATHER_BOOTS).setDisplayName("§3Partner").getItemStack();
        LeatherArmorMeta meta = (LeatherArmorMeta) partner.getItemMeta();
        meta.setColor(Color.AQUA);
        partner.setItemMeta(meta);

        ItemStack youtube = EnderAPI.getInstance().getItem(Material.LEATHER_BOOTS).setDisplayName("§5YouTuber").getItemStack();
        meta = (LeatherArmorMeta) youtube.getItemMeta();
        meta.setColor(Color.PURPLE);
        youtube.setItemMeta(meta);

        ItemStack enderp = EnderAPI.getInstance().getItem(Material.LEATHER_BOOTS).setDisplayName("§6Ender+").getItemStack();
        meta = (LeatherArmorMeta) enderp.getItemMeta();
        meta.setColor(Color.ORANGE);
        enderp.setItemMeta(meta);

        inv.setItem(46, EnderAPI.getInstance().getItem(Material.INK_SACK, 1, 8).setDisplayName("§7Spieler").getItemStack());
        inv.setItem(47, EnderAPI.getInstance().getItem(Material.INK_SACK, 1, 14).setDisplayName("§6Ender").getItemStack());

        inv.setItem(49, enderp);
        inv.setItem(51, youtube);
        inv.setItem(52, partner);

        inv.setItem(7, EnderAPI.getInstance().getItem(Material.LAVA_BUCKET).setDisplayName("§cSrDev").getItemStack());
        inv.setItem(16, EnderAPI.getInstance().getItem(Material.WATER_BUCKET).setDisplayName("§cDev").getItemStack());
        inv.setItem(25, EnderAPI.getInstance().getItem(Material.BUCKET).setDisplayName("§cJrDev").getItemStack());

        inv.setItem(6, EnderAPI.getInstance().getItem(Material.BOOK_AND_QUILL).setDisplayName("§cSrContent").getItemStack());
        inv.setItem(15, EnderAPI.getInstance().getItem(Material.BOOK).setDisplayName("§cContent").getItemStack());
        inv.setItem(24, EnderAPI.getInstance().getItem(Material.FEATHER).setDisplayName("§cJrContent").getItemStack());

        inv.setItem(4, EnderAPI.getInstance().getItem(Material.DIAMOND_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cSrMod").getItemStack());
        inv.setItem(13, EnderAPI.getInstance().getItem(Material.GOLD_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cMod").getItemStack());
        inv.setItem(22, EnderAPI.getInstance().getItem(Material.IRON_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cSup").getItemStack());
        inv.setItem(31, EnderAPI.getInstance().getItem(Material.STONE_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cJrSup").getItemStack());

        inv.setItem(2, EnderAPI.getInstance().getItem(Material.DIAMOND_PICKAXE).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cSrBuild").getItemStack());
        inv.setItem(11, EnderAPI.getInstance().getItem(Material.GOLD_PICKAXE).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cBuild").getItemStack());
        inv.setItem(20, EnderAPI.getInstance().getItem(Material.STONE_PICKAXE).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayName("§cJrBuild").getItemStack());

        inv.setItem(1, EnderAPI.getInstance().getItem(Material.MAP).setDisplayName("§cSrDesign").getItemStack());
        inv.setItem(10, EnderAPI.getInstance().getItem(Material.EMPTY_MAP).setDisplayName("§cDesign").getItemStack());
        inv.setItem(19, EnderAPI.getInstance().getItem(Material.PAPER).setDisplayName("§cJrDesign").getItemStack());

        if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("SrDev")) {
            inv.setItem(7, EnderAPI.getInstance().getItem(inv.getItem(7)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("SrContent")) {
            inv.setItem(6, EnderAPI.getInstance().getItem(inv.getItem(6)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("SrMod")) {
            inv.setItem(4, EnderAPI.getInstance().getItem(inv.getItem(4)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("SrBuild")) {
            inv.setItem(2, EnderAPI.getInstance().getItem(inv.getItem(2)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("SrDesign")) {
            inv.setItem(1, EnderAPI.getInstance().getItem(inv.getItem(1)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Dev")) {
            inv.setItem(16, EnderAPI.getInstance().getItem(inv.getItem(16)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Content")) {
            inv.setItem(15, EnderAPI.getInstance().getItem(inv.getItem(15)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Mod")) {
            inv.setItem(13, EnderAPI.getInstance().getItem(inv.getItem(13)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Build")) {
            inv.setItem(11, EnderAPI.getInstance().getItem(inv.getItem(11)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Design")) {
            inv.setItem(10, EnderAPI.getInstance().getItem(inv.getItem(10)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("JrDev")) {
            inv.setItem(25, EnderAPI.getInstance().getItem(inv.getItem(25)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("JrContent")) {
            inv.setItem(24, EnderAPI.getInstance().getItem(inv.getItem(24)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Sup")) {
            inv.setItem(22, EnderAPI.getInstance().getItem(inv.getItem(22)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("JrBuild")) {
            inv.setItem(20, EnderAPI.getInstance().getItem(inv.getItem(20)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("JrDesign")) {
            inv.setItem(19, EnderAPI.getInstance().getItem(inv.getItem(19)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("JrSup")) {
            inv.setItem(31, EnderAPI.getInstance().getItem(inv.getItem(31)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Partner")) {
            inv.setItem(52, EnderAPI.getInstance().getItem(inv.getItem(52)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("YouTuber")) {
            inv.setItem(51, EnderAPI.getInstance().getItem(inv.getItem(51)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Ender+")) {
            inv.setItem(49, EnderAPI.getInstance().getItem(inv.getItem(49)).setHideEnchantments().getItemStack());
        } else if (PermAPI.getInstance().getGroup(uuid).equalsIgnoreCase("Ender")) {
            inv.setItem(47, EnderAPI.getInstance().getItem(inv.getItem(47)).setHideEnchantments().getItemStack());
        } else {
            inv.setItem(46, EnderAPI.getInstance().getItem(inv.getItem(46)).setHideEnchantments().getItemStack());
        }

        player.openInventory(inv);
    }

}
