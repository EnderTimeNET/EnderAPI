package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.PermAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeePerm_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("seeperm")) {
            if (commandSender instanceof Player) {
                Player sender = (Player) commandSender;
                if (sender.hasPermission("teamnet.admin")) {
                    if (args.length == 1) {
                        String name = args[0];
                        if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                            sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Permission von §c" + name);

                            for (String s : PermAPI.getInstance().getRankPermissions().getPerms(name))
                                sender.sendMessage("§8-> §7" + s);

                            sender.sendMessage("");
                            sender.sendMessage("");
                            sender.sendMessage("");
                            sender.sendMessage("");

                            for (String s : PermAPI.getInstance().getRankPermissions().getPermsWithOut(name))
                                sender.sendMessage("§8-> §7" + s);
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Der Rang §c" + name + " §7existiert §cnicht");
                        }
                    }
                } else {
                    sender.sendMessage(EnderAPI.getInstance().getNoPerm());
                }
            }
        } else {
            commandSender.sendMessage(EnderAPI.getInstance().getNoPerm());
        }
        return false;
    }
}
