package net.endertime.enderkomplex.spigot.commands;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnverifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("unverify")) {
            if(args.length == 0) {
                if(Database.isVerified(p.getUniqueId())) {
                    Database.updateRanksSpigot(p.getUniqueId(), "Spieler");
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 0.5, 1);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {

                            @Override
                            public void run() {
                               Database.unverify(p.getUniqueId());
                            }
                        }, 20*6);
                    EnderAPI.getInstance().sendActionBar(p, "§7Du wurdest §aentverifiziert§8!");
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Du bist §cnicht §7verifiziert§8!");
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, (float) 0.5, 1);
                }
            } else {
                EnderAPI.getInstance().sendActionBar(p, "§7Benutze§8: /§cverify");
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, (float) 0.5, 1);
            }
        }
        return false;
    }
}
