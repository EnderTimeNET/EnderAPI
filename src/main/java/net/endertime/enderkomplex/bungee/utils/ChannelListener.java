package net.endertime.enderkomplex.bungee.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.objects.Chatlog;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChannelListener implements Listener {

    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("enderkomplex"))
            return;

        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream in = new DataInputStream(stream);
        try {
            String subChannel = in.readUTF();
            UUID playeruuid = UUID.fromString(in.readUTF());
            if (subChannel.equalsIgnoreCase("pluginmessage")) {
                String actionstring = in.readUTF();
                if(actionstring.equals(PluginMessage.SEND_REPORT_NOTIFY.toString())) {
                    UUID targetuuid = UUID.fromString(in.readUTF());
                    String reportid = in.readUTF();
                    String reason = in.readUTF();
                    if(reason.equals("CHAT")) {
                        if(ChatListener.chathistory.containsKey(ProxyServer.getInstance().getPlayer(targetuuid))) {
                            String logid = new Chatlog(ProxyServer.getInstance().getPlayer(playeruuid), ProxyServer.getInstance().getPlayer(targetuuid)).getLogID();
                            Database.updateReportChatlogID(reportid, logid);
                            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                if(all.hasPermission("ek.notify.reports")) {
                                    if(Database.existsInNotifySettings(all.getUniqueId())) {
                                        if(Database.getNotifySetting(all.getUniqueId(), NotifyType.REPORTS)) {
                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Ein neuer §cReport §7wurde erstellt§8!");
                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                        }
                                    } else {
                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Ein neuer §cReport §7wurde erstellt§8!");
                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                }
                            }
                        } else {
                            Database.deleteReport(reportid);
                        }
                    } else {
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if(all.hasPermission("ek.notify.reports")) {
                                if(Database.existsInNotifySettings(all.getUniqueId())) {
                                    if(Database.getNotifySetting(all.getUniqueId(), NotifyType.REPORTS)) {
                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Ein neuer §cReport §7wurde erstellt§8!");
                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                } else {
                                    ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Ein neuer §cReport §7wurde erstellt§8!");
                                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                }
                            }
                        }
                    }
                } else if(actionstring.equals(PluginMessage.WORKING_REPORT_ID.toString())) {
                    String reportid = in.readUTF();
                    if(ProxyServer.getInstance().getPlayer(playeruuid) != null) {
                        ReportWorkingHandler.assignReport(ProxyServer.getInstance().getPlayer(playeruuid), reportid);
                    }
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

}
