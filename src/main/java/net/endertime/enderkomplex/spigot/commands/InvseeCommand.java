package net.endertime.enderkomplex.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class InvseeCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (label.equalsIgnoreCase("invsee")) {
            if (p.hasPermission("ek.commands.invsee")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        p.openInventory(target.getInventory());
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Dieser Spieler ist §cnicht §7online§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§cinvsee §8<§cname§8>");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                }
            }
        }
        return false;
    }

}
