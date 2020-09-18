package net.endertime.enderkomplex.bungee.utils;

import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.mysql.Database;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer pp = e.getPlayer();

        Database.updateLastSeen(pp.getUniqueId());
        Database.updateLastLocationToSpawnLocation(pp.getUniqueId());
        if (ReportWorkingHandler.workingReports.containsKey(pp)) {
            String reportid = ReportWorkingHandler.workingReports.get(pp);
            Database.updateReportStatus(reportid, ReportStatus.OPEN);
            Database.updateReportWorker(reportid, null);
            ReportWorkingHandler.workingReports.remove(pp);
        }
        if (ChatListener.chathistory.containsKey(pp)) {
            ChatListener.chathistory.remove(pp);
        }
        if (ChatListener.lastMessage.containsKey(pp)) {
            ChatListener.lastMessage.remove(pp);
        }
    }

    @EventHandler
    public void onQuit(ServerConnectEvent e) {
        ProxiedPlayer pp = e.getPlayer();

        if (pp.hasPermission("ek.commands.reports")) {
            if (e.getTarget().getName().contains("Lobby")) {
                if (pp.getServer() != null) {
                    if (!pp.getServer().getInfo().getName().contains("Lobby")) {
                        if (ReportWorkingHandler.workingReports.containsKey(pp)) {
                            String reportid = ReportWorkingHandler.workingReports.get(pp);
                            Database.updateReportStatus(reportid, ReportStatus.OPEN);
                            Database.updateReportWorker(reportid, null);
                            ReportWorkingHandler.workingReports.remove(pp);
                        }
                    }
                }
            }
        }
    }
}

