package net.endertime.enderkomplex.spigot.commands;

import java.util.UUID;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.utils.PlayerInfoMenu;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("pi")) {
            if(p.hasPermission("ek.commands.playerinfo")) {
                if(args.length == 1) {
                    UUID uuid = null;
                    if(Bukkit.getPlayer(args[0]) != null) {
                        uuid = Bukkit.getPlayer(args[0]).getUniqueId();
                        PlayerInfoMenu.createInventory(p, uuid);
                    } else {
                        if(EnderAPI.getInstance().getUUID(args[0]) != null) {
                            uuid = EnderAPI.getInstance().getUUID(args[0]);
                            PlayerInfoMenu.createInventory(p, uuid);
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[0] + "§7 war noch nie auf §5EnderTime§8!");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        }
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§cpi §8<§cname§8>");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                }
            }
        }
        return false;
    }

}
