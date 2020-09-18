package net.endertime.enderkomplex.spigot.commands;

import java.util.HashMap;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderkomplex.bungee.enums.ReportReason;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.objects.Report;
import net.endertime.enderkomplex.spigot.utils.ReportListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    public static HashMap<Player, Player> reports = new HashMap<>();
    public static HashMap<Player, Long> cooldown = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(label.equalsIgnoreCase("report")) {
                if(p.hasPermission("ek.commands.report")) {
                    if(!cooldown.containsKey(p)) cooldown.put(p, System.currentTimeMillis());
                    if(cooldown.get(p) <= System.currentTimeMillis()) {
                        if(args.length == 0) {
                            EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§creport §8<§cname§8>");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        } else if(args.length == 1) {
                            if(p.getName().equalsIgnoreCase(args[0])) {
                                EnderAPI.getInstance().sendActionBar(p, "§7Du kannst dich §cnicht selber §7reporten§8!");
                                EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                                return false;
                            }
                            if(EnderAPI.getInstance().getUUID(args[0]) == null) {
                                for(Player nicked : NickAPI.getInstance().getNickedPlayer().keySet()) {
                                    if(nicked.getName().equalsIgnoreCase(args[0])) {
                                        reports.put(p, nicked);
                                        ReportListener.openReportGUI(p, args[0]);
                                        cooldown.put(p, (System.currentTimeMillis() + 5000));
                                        return false;
                                    }
                                }
                                EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[0] + "§7 war noch nie auf §5EnderTime§8!");
                                EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                                return false;
                            }
                            if(Bukkit.getPlayer(args[0]) == null) {
                                for(Player nicked : NickAPI.getInstance().getNickedPlayer().keySet()) {
                                    if(nicked.getName().equalsIgnoreCase(args[0])) {
                                        reports.put(p, nicked);
                                        ReportListener.openReportGUI(p, args[0]);
                                        cooldown.put(p, (System.currentTimeMillis() + 5000));
                                        return false;
                                    }
                                }
                                EnderAPI.getInstance().sendActionBar(p, "§7Dieser Spieler ist §cnicht §7auf diesem Server§8!");
                                EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                                return false;
                            } else {
                                reports.put(p, Bukkit.getPlayer(args[0]));
                            }
                            ReportListener.openReportGUI(p, args[0]);
                            cooldown.put(p, (System.currentTimeMillis() + 5000));
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§creport §8<§cname§8>");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Bitte §cwarte einen Moment §7um diesen Befehl zu nutzen§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                }
            }
        } else {
            if(label.equalsIgnoreCase("report")) {
                if(args.length == 2) {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        if(args[1].equalsIgnoreCase("AC")) {
                            if(!Database.hasActiveReport(EnderAPI.getInstance().getUUID(args[0]))) {
                                new Report(Bukkit.getPlayer(args[0]), "AntiCheat", ReportReason.HACKING);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
