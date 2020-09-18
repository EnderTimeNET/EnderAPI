package net.endertime.enderkomplex.spigot.commands;

import java.util.Random;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.utils.ApplyMenu;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ApplyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("apply")) {
            if(args.length == 0) {
                if(!p.hasPermission("teamserver.join")) {
                    if(!Database.isOpenApplyExist(p.getUniqueId())) {
                        String id = ProxyHandler.randomAlphaNumeric(6);
                        while(Database.isApplyIdExist(id)) {
                            id = ProxyHandler.randomAlphaNumeric(6);
                        }
                        int tsnumber = 10000 + new Random().nextInt(90000);
                        while(Database.isTS_NumberExist(tsnumber)) {
                            tsnumber = 10000 + new Random().nextInt(90000);
                        }
                        Database.deleteAllApplys(p.getUniqueId());
                        Database.addApplyID(id, tsnumber, p.getUniqueId());
                        p.sendMessage("§7Klicke auf folgenden Link um dich zu bewerben:");
                        p.sendMessage(ProxyData.ChatPrefix + ProxyData.ApplyLink + id);
                    } else {
                        String id = Database.getOpenApplyID(p.getUniqueId());
                        p.sendMessage("§7Klicke auf folgenden Link um dich zu bewerben:");
                        p.sendMessage(ProxyData.ChatPrefix + ProxyData.ApplyLink + id);
                    }
                } else {
                    if(p.hasPermission("apply.changephase")) {
                        ApplyMenu.openPhaseInventory(p);
                    }
                }
            } else if(args.length == 1) {
                if(p.hasPermission("apply.decide")) {
                    int tsnumber = 0;
                    try {
                        tsnumber = Integer.valueOf(args[0]);
                    } catch (NumberFormatException error) {
                        EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§capply §8<§cGesprächsnummer§8>");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        return false;
                    }
                    if(Database.isTS_NumberExist(tsnumber)) {
                        if(Database.getApplyStatus(tsnumber) == -1) {
                            ApplyMenu.openAcceptInventory(p, tsnumber);
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Über diese Gesprächsnummer §cwurde bereits §7entschieden§8!");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Diese Gesprächsnummer §cexistiert nicht§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                }
            } else {
                EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§capply");
                EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
            }
        }
        return false;
    }

}
