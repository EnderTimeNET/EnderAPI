package net.endertime.enderkomplex.spigot.utils;

import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NewsBossbar implements Listener {

    private static BossBar bb = Bukkit.createBossBar("", BarColor.PURPLE, BarStyle.SOLID);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String servername = Bukkit.getServerName();

        if(!Database.getBossbarNews(servername).equals("null")) {
            String title = Database.getBossbarNews(servername).replace('&', '§');
            if(!bb.getTitle().equals(title)) {
                bb.setTitle(title);
            }
            bb.addPlayer(p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(bb.getPlayers().contains(p)) {
            bb.removePlayer(p);
        }
    }

}
