package net.endertime.enderkomplex.bungee.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.BanReason;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.bungee.utils.InfoCollector;
import net.endertime.enderkomplex.bungee.utils.ReportWorkingHandler;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClientBan {

    String banID, ip;
    UUID executor, target;
    BanReason reason;
    int banamount;
    long duration, timestamp = System.currentTimeMillis();


    public ClientBan(UUID executorUUID, BanReason reason, UUID targetUUID) {
        this.executor = executorUUID;
        this.target = targetUUID;
        this.reason = reason;
        this.banamount = Database.getBanAmount(targetUUID) +1;
        this.duration = reason.getDuration(this.banamount - Database.getExcusedBanAmount(targetUUID));
        ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(targetUUID);
        this.banID = "#EB" + System.currentTimeMillis();
        if(pp != null) {
            this.ip = pp.getAddress().getAddress().toString();
        } else {
            if(Database.isIpExistAlts(targetUUID)) {
                this.ip = Database.getIP(targetUUID);
            } else {
                this.ip = null;
            }
        }
        if(this.ip != null) {
            if(Database.hasActiveBan(this.ip)) {
                this.reason = BanReason.BANNUMGEHUNG;
                this.duration = BanReason.BANNUMGEHUNG.getDuration(1);
                ProxyServer.getInstance().getPlayers().forEach(all -> {
                    if(all.getAddress().getAddress().toString().equals(ip)) {
                        all.disconnect(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                + "\n"
                                + "§7Grund§8: §c" + this.reason.getTitle() + "\n"
                                + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(this.timestamp, this.duration) + "\n"
                                + "\n"
                                + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen " +
                                "Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                    }
                });
            }
        }
        Database.saveBan(this);
        Database.deleteAllOpenReports(targetUUID);
        ArrayList<String> rids = Database.getAllWorkingReports(targetUUID);
        if(!rids.isEmpty()) {
            ProxiedPlayer exec = ProxyServer.getInstance().getPlayer(executorUUID);
            for(String reportid : rids) {
                Iterator<ProxiedPlayer> iter = ReportWorkingHandler.workingReports.keySet().iterator();
                while(iter.hasNext()) {
                    ProxiedPlayer worker = iter.next();
                    if(!worker.getUniqueId().toString().equals(executorUUID.toString())) continue;
                    if(!ReportWorkingHandler.workingReports.get(worker).equals(reportid)) continue;
                    Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                    Database.updateReportTimestamp(reportid);
                    Database.createReportStats(executorUUID, reportid, true, false, false, Database.getReportReason(reportid));
                    iter.remove();
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(exec, "jump SilentLobby-1");
                    ProxyHandler.sendPluginMessage(exec, PluginMessage.SEND_ACTIONBAR, "§7Du hast den Report §aerfolgreich §7geschlossen§8!");
                    ProxyHandler.sendPluginMessage(exec, PluginMessage.PLAY_SUCCESS_SOUND, null);
                }
            }
        }
        ProxyServer.getInstance().getPlayers().forEach(all -> {
            if(all.hasPermission("teamserver.join")) {
                if(Database.existsInNotifySettings(all.getUniqueId())) {
                    if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" +
                                EnderAPI.getInstance().getName(targetUUID) + " §7wurde von §c" + EnderAPI.getInstance().getName(executorUUID)
                                + "§7 gebannt §8[§4" + this.reason.getTitle() + "§8]");
                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" +
                            EnderAPI.getInstance().getName(targetUUID) + " §7wurde von §c" + EnderAPI.getInstance().getName(executorUUID)
                            + "§7 gebannt §8[§4" + this.reason.getTitle() + "§8]");
                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                }
            }
        });
        InfoCollector.bans++;
    }

    public String getID() {
        return this.banID;
    }

    public String getTargetIP() {
        return this.ip;
    }

    public UUID getExecutor() {
        return this.executor;
    }

    public UUID getTarget() {
        return this.target;
    }

    public BanReason getReason() {
        return this.reason;
    }

    public int getBanAmount() {
        return this.banamount;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

}
