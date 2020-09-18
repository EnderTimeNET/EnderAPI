package net.endertime.enderkomplex.spigot.utils;

import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossbarHandler {

    public static BossBar bossbar1 = Bukkit.createBossBar("§7" + Database.getOpenReportCount(), BarColor.PURPLE, BarStyle.SEGMENTED_6);

    public static void deleteBossbar(Player p) {
        if(bossbar1.getPlayers().contains(p)) {
            bossbar1.removePlayer(p);
        }
    }

    public static void startUpdater() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ServerData.Instance, new Runnable() {

            @Override
            public void run() {
                int openreports =  Database.getOpenReportCount();
                if(openreports > 0) {
                    bossbar1.setTitle("§7§lOffene Reports: §c§l" + openreports);
                    bossbar1.setColor(BarColor.RED);
                } else {
                    bossbar1.setTitle("§7§lOffene Reports: §a§l" + openreports);
                    bossbar1.setColor(BarColor.GREEN);
                }
                Bukkit.getOnlinePlayers().forEach(all -> {
                    if(all.hasPermission("ek.commands.reports")) {
                        if(Database.existsInNotifySettings(all.getUniqueId())) {
                            if(Database.getNotifySetting(all.getUniqueId(), NotifyType.REPORTS)) {
                                if(!bossbar1.getPlayers().contains(all)) {
                                    bossbar1.addPlayer(all);
                                }
                            } else {
                                if(bossbar1.getPlayers().contains(all)) {
                                    bossbar1.removePlayer(all);
                                }
                            }
                        } else {
                            if(!bossbar1.getPlayers().contains(all)) {
                                bossbar1.addPlayer(all);
                            }
                        }
                    } else {
                        if(bossbar1.getPlayers().contains(all)) {
                            bossbar1.removePlayer(all);
                        }
                    }
                });
            }
        }, 20, 20);
    }

}
