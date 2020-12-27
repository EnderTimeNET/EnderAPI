package net.endertime.enderkomplex.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.utils.NotifyMenu;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NotifysCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("notifys")) {
            if(p.hasPermission("ek.commands.notifys")) {
                if(args.length == 0) {
                    NotifyMenu.openMenu(p);
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§cnotifys");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                }
            }
        }
        return false;
    }

}
