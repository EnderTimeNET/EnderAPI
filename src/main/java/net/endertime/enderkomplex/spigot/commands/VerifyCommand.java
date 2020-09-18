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

public class VerifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("verify")) {
            if(args.length == 0) {
                if(!Database.isVerified(p.getUniqueId())) {
                    if(!Database.isUUIDExistVerify(p.getUniqueId())) {
                        Database.verify(p.getUniqueId());
                        EnderAPI.getInstance().sendActionBar(p, "§7Dein §6Verifizierungsantrag §7wurde an den §3TeamSpeak §7gesendet...");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 0.5, 1);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {

                            @Override
                            public void run() {
                                if(!Database.isVerified(p.getUniqueId())) {
                                    EnderAPI.getInstance().sendActionBar(p, "§7Es wurde §ckein gleichnamiger §7TeamSpeak Client gefunden!");
                                    Database.unverify(p.getUniqueId());
                                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, (float) 0.5, 1);
                                }

                            }
                        }, 20*6);
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§cDer Verifizierungsprozess läuft bereits!");
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, (float) 0.5, 1);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Du §cbist bereits §7verifiziert§8!");
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
