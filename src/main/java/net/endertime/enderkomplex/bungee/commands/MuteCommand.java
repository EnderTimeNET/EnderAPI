package net.endertime.enderkomplex.bungee.commands;

import java.util.UUID;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.MuteReason;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.bungee.objects.ChatNotify;
import net.endertime.enderkomplex.bungee.objects.ClientMute;
import net.endertime.enderkomplex.bungee.utils.ReportWorkingHandler;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.objects.ReportInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

    public MuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.mute")) {
            if(args.length == 0) {
                pp.sendMessage(TextComponent.fromLegacyText("§8§m     §4§l ✎ §4§lChatmute §4§lGründe §4§l✎ §8§m     "));
                for(MuteReason mr : MuteReason.values()) {
                    BaseComponent[] mrc = new ComponentBuilder("§7§l" + mr.getID() + " §8┃ §c§o" + mr.getTitle())
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(mr.getExamples())))
                            .create();
                    pp.sendMessage(new ComponentBuilder("§8➟ ").append(mrc).create());
                }
                pp.sendMessage(TextComponent.fromLegacyText("§8§m                                            "));
            } else if(args.length == 2) {
                if(args[0].startsWith("#CN")) {
                    String notifyID = args[0];
                    if(ChatNotify.notifys.containsKey(notifyID)) {
                        ChatNotify cn = ChatNotify.notifys.get(notifyID);
                        if(!Database.hasActiveMute(cn.getSender().getUniqueId())) {
                            switch(args[1]) {
                                case "1":
                                    new ClientMute(pp, MuteReason.CHAT1, cn.getSender().getUniqueId(), true);
                                    ProxyServer.getInstance().getPlayers().forEach(all -> {
                                        if(all.hasPermission("teamserver.join")) {
                                            if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                            + cn.getSender().getName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                            + MuteReason.CHAT1.getTitle() + "§8]");
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                }
                                            } else {
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                        + cn.getSender().getName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                        + MuteReason.CHAT1.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                        }
                                    });
                                    if(ProxyServer.getInstance().getPlayer(cn.getSender().getUniqueId()) != null) {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(cn.getSender().getUniqueId());
                                        ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                + MuteReason.CHAT1.getTitle() + "§8]");
                                        ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                    break;
                                case "2":
                                    new ClientMute(pp, MuteReason.CHAT2, cn.getSender().getUniqueId(), true);
                                    ProxyServer.getInstance().getPlayers().forEach(all -> {
                                        if(all.hasPermission("teamserver.join")) {
                                            if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                            + cn.getSender().getName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                            + MuteReason.CHAT2.getTitle() + "§8]");
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                }
                                            } else {
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                        + cn.getSender().getName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                        + MuteReason.CHAT2.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                        }
                                    });
                                    if(ProxyServer.getInstance().getPlayer(cn.getSender().getUniqueId()) != null) {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(cn.getSender().getUniqueId());
                                        ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                + MuteReason.CHAT2.getTitle() + "§8]");
                                        ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                    break;
                                case "3":
                                    new ClientMute(pp, MuteReason.CHAT3, cn.getSender().getUniqueId(), true);
                                    ProxyServer.getInstance().getPlayers().forEach(all -> {
                                        if(all.hasPermission("teamserver.join")) {
                                            if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                            + cn.getSender().getName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                            + MuteReason.CHAT3.getTitle() + "§8]");
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                }
                                            } else {
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                        + cn.getSender().getName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4" +
                                                        MuteReason.CHAT3.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                        }
                                    });
                                    if(ProxyServer.getInstance().getPlayer(cn.getSender().getUniqueId()) != null) {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(cn.getSender().getUniqueId());
                                        ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                + MuteReason.CHAT3.getTitle() + "§8]");
                                        ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                    break;
                            }
                            ChatNotify.notifys.remove(notifyID);
                        } else {
                            ChatNotify.notifys.remove(notifyID);
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist bereits §7gemutet§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Verdacht §cwurde bereits §7bearbeitet§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else if(args[0].startsWith("#ER")) {
                    String reportid = args[0];
                    if(Database.isReportIdExist(reportid)) {
                        if(Database.getReportStatus(reportid).equals(ReportStatus.WORKING)) {
                            if(pp.getUniqueId().toString().equals(Database.getReportWorkerUUID(reportid))) {
                                ReportInfo ri = Database.getReportInfo(reportid);
                                if(!Database.hasActiveMute(ri.getReported())) {
                                    switch(args[1]) {
                                        case "1":
                                            new ClientMute(pp, MuteReason.CHAT1, ri.getReported(), true);
                                            Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                            Database.updateReportTimestamp(reportid);
                                            ReportWorkingHandler.workingReports.remove(pp);
                                            ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "hub");
                                            ProxyServer.getInstance().getPlayers().forEach(all -> {
                                                if(all.hasPermission("teamserver.join")) {
                                                    if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                        if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                                    + ri.getReportedName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                                    + MuteReason.CHAT1.getTitle() + "§8]");
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                        }
                                                    } else {
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                                + ri.getReportedName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                                + MuteReason.CHAT1.getTitle() + "§8]");
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                    }
                                                }
                                            });
                                            if(ProxyServer.getInstance().getPlayer(ri.getReported()) != null) {
                                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(ri.getReported());
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                        + MuteReason.CHAT1.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                            break;
                                        case "2":
                                            new ClientMute(pp, MuteReason.CHAT2, ri.getReported(), true);
                                            Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                            Database.updateReportTimestamp(reportid);
                                            ReportWorkingHandler.workingReports.remove(pp);
                                            ProxyServer.getInstance().getPlayers().forEach(all -> {
                                                if(all.hasPermission("teamserver.join")) {
                                                    if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                        if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                                    + ri.getReportedName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                                    + MuteReason.CHAT2.getTitle() + "§8]");
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                        }
                                                    } else {
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                                + ri.getReportedName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                                + MuteReason.CHAT2.getTitle() + "§8]");
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                    }
                                                }
                                            });
                                            if(ProxyServer.getInstance().getPlayer(ri.getReported()) != null) {
                                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(ri.getReported());
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                        + MuteReason.CHAT2.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                            break;
                                        case "3":
                                            new ClientMute(pp, MuteReason.CHAT3, ri.getReported(), true);
                                            Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                            Database.updateReportTimestamp(reportid);
                                            ReportWorkingHandler.workingReports.remove(pp);
                                            ProxyServer.getInstance().getPlayers().forEach(all -> {
                                                if(all.hasPermission("teamserver.join")) {
                                                    if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                        if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                                    + ri.getReportedName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                                    + MuteReason.CHAT3.getTitle() + "§8]");
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                        }
                                                    } else {
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c"
                                                                + ri.getReportedName() + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4"
                                                                + MuteReason.CHAT3.getTitle() + "§8]");
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                    }
                                                }
                                            });
                                            if(ProxyServer.getInstance().getPlayer(ri.getReported()) != null) {
                                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(ri.getReported());
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                        + MuteReason.CHAT3.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                            break;
                                    }
                                } else {
                                    Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                    Database.updateReportTimestamp(reportid);
                                    ReportWorkingHandler.workingReports.remove(pp);
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist bereits §7gemutet§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                }
                            } else {
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du bist §cnicht zuständig §7für diesen Report§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            }
                        } else {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Report §cwurde bereits §7bearbeitet§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§cDiese ReportID existiert nicht§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    if(!args[0].equalsIgnoreCase(pp.getName())) {
                        if(EnderAPI.getInstance().getUUID(args[0]) != null) {
                            if(!EnderAPI.getInstance().isInTeam(EnderAPI.getInstance().getUUID(args[0]))) {
                                UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                                if(!Database.hasActiveMute(uuid)) {
                                    int muteid = 0;
                                    try {
                                        muteid = Integer.valueOf(args[1]);
                                    } catch (NumberFormatException error) {
                                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige MuteID!");
                                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                    }
                                    for(MuteReason reason : MuteReason.values()) {
                                        if(reason.getID() == muteid) {
                                            new ClientMute(pp, reason, uuid, false);
                                            ProxyServer.getInstance().getPlayers().forEach(all -> {
                                                if(all.hasPermission("teamserver.join")) {
                                                    if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                        if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0]
                                                                    + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4" + reason.getTitle() + "§8]");
                                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                        }
                                                    } else {
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0]
                                                                + " §7wurde von §c" + pp.getName() + "§7 gemutet §8[§4" + reason.getTitle() + "§8]");
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                    }
                                                }
                                            });
                                            if(ProxyServer.getInstance().getPlayer(uuid) != null) {
                                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.SEND_ACTIONBAR, "§7Du wurdest gemutet §8[§4"
                                                        + reason.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(target, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                            return;
                                        }
                                    }
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige MuteD!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                } else {
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist bereits §7gemutet§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                }
                            } else {
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst §ckein §7Teammitglied §7muten§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            }
                        } else {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Der Spieler §c" + args[0] +
                                    "§7 war noch nie auf §5EnderTime§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst dich §cnicht selbst §7muten§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cmute §8<§cname§8> <§cmuteid§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

}
