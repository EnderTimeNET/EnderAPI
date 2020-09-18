package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCoins_Command implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (p.hasPermission("coinapi.resetcoins")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        EnderAPI.getInstance().getEnderDatabase().resetCoins(target.getUniqueId());

                        p.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Du hast die §5EnderCoins §7von §c"
                                + target.getName() + " §9resettet");
                    } else {
                        p.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Der Spieler §c" + args[0]
                                + " §7ist §cnicht §7online!");
                    }
                } else {
                    p.sendMessage(EnderAPI.getInstance().getPrefix() + "§8/§7resetcoins §8<§7Name§8>");
                }
            } else {
                p.sendMessage(EnderAPI.getInstance().getNoPerm());
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                EnderAPI.getInstance().getEnderDatabase().resetCoins(target.getUniqueId());

                sender.sendMessage(EnderAPI.getInstance().getConsolePrefix() + "§7Du hast die §5EnderCoins §7von §c"
                        + target.getName() + " §9resettet");
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
