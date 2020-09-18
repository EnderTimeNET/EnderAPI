package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCoins_Command implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (p.hasPermission("coinapi.removecoins")) {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        Integer amount = Integer.valueOf(Integer.parseInt(args[1]));
                        Integer has = Integer.valueOf(EnderAPI.getInstance().getEnderDatabase().getCoins(target.getUniqueId()));
                        EnderAPI.getInstance().getEnderDatabase().updateCoins(target.getUniqueId(), has.intValue() - amount.intValue());
                        p.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Du hast dem Spieler §c" + target.getName()
                                + " §6" + amount + " §5EnderCoins §centfernt");
                    } else {
                        p.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Der Spieler §c" + args[0]
                                + " §7ist §cnicht §7online!");
                    }
                } else {
                    p.sendMessage(EnderAPI.getInstance().getPrefix() + "§8/§7removecoins §8<§7Name§8> <§7Anzahl§8>");
                }
            } else {
                p.sendMessage(EnderAPI.getInstance().getNoPerm());
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                Integer amount = Integer.valueOf(Integer.parseInt(args[1]));
                Integer has = Integer.valueOf(EnderAPI.getInstance().getEnderDatabase().getCoins(target.getUniqueId()));
                EnderAPI.getInstance().getEnderDatabase().updateCoins(target.getUniqueId(), has.intValue() - amount.intValue());
                sender.sendMessage(EnderAPI.getInstance().getConsolePrefix() + "§7Du hast dem Spieler §c"
                        + target.getName() + " §6" + amount + " §5EnderCoins §centfernt");
            } else {
                sender.sendMessage(EnderAPI.getInstance().getConsolePrefix() + "§7Der Spieler §c" + args[0]
                        + " §7ist §cnicht §7online!");
            }
        } else {
            sender.sendMessage(
                    EnderAPI.getInstance().getConsolePrefix() + "§8/§7removecoins §8<§7Name§8> <§7Anzahl§8>");
        }
        return false;
    }
}
