package net.endertime.enderkomplex.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.objects.InfoFeed;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoFeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("if")) {
            if(p.hasPermission("ek.commands.cps")) {
                if(args.length == 0) {
                    EnderAPI.getInstance().getNoActionbar().remove(p);
                    InfoFeed.unsetTargets(p);
                    EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 2);
                } else if(args.length == 1) {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        EnderAPI.getInstance().getNoActionbar().add(p);
                        InfoFeed.setTarget(p, Bukkit.getPlayer(args[0]));
                        EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 2);
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §6" + args[0] + " §7ist §cnicht §7online§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§cif §8<§cname§8>");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                }
            }
        }
        return false;
    }

}
