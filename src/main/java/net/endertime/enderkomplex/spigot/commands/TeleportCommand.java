package net.endertime.enderkomplex.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("tp")) {
            if(p.hasPermission("ek.commands.teleport")) {
                if(args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target != null) {
                        p.teleport(target);
                        EnderAPI.getInstance().playSound(p, Sound.ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE);
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[0] + "§7 ist §cnicht §7online§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                } else if(args.length == 2) {
                    Player target1 = Bukkit.getPlayer(args[0]);
                    Player target2 = Bukkit.getPlayer(args[1]);
                    if(target1 != null) {
                        if(target2 != null) {
                            target1.teleport(target2);
                            EnderAPI.getInstance().playSound(p, Sound.ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE);
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[1] + "§7 ist §cnicht §7online§8!");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Der Spieler §c" + args[0] + "§7 ist §cnicht §7online§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                } else if(args.length == 3) {
                    int x = 0, y = 0, z = 0;
                    try {
                        x = Integer.valueOf(args[0]);
                        y = Integer.valueOf(args[1]);
                        z = Integer.valueOf(args[2]);
                    } catch (NumberFormatException error) {
                        EnderAPI.getInstance().sendActionBar(p, "§cDeine Angabe ist ungültig§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        return false;
                    }
                    p.teleport(new Location(p.getWorld(), x, y, z));
                    EnderAPI.getInstance().playSound(p, Sound.ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE);
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§ctp §8<§cname§8> [<§cname§8>] §7oder §8/§ctp x y z");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                }
            }
        }
        return false;
    }

}
