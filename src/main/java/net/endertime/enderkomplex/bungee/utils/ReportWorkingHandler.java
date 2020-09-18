package net.endertime.enderkomplex.bungee.utils;

import java.util.HashMap;
import java.util.UUID;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.BanReason;
import net.endertime.enderkomplex.bungee.enums.MuteReason;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.objects.ReportInfo;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportWorkingHandler {

    public static HashMap<ProxiedPlayer, String> workingReports = new HashMap<>();

    public static void assignReport(ProxiedPlayer pp, String reportid) {
        workingReports.put(pp, reportid);
        Database.updateReportStatus(reportid, ReportStatus.WORKING);
        Database.updateReportWorker(reportid, pp.getUniqueId());
        ReportInfo ri = Database.getReportInfo(reportid);
        if(ri.getReason().isInstantJump()) ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "vjump " + ri.getReportedName());
        sendReportMessage(pp, ri);
        InfoCollector.reports++;
    }

    public static void sendReportMessage(ProxiedPlayer pp, ReportInfo ri) {
        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_NOTIFY_SOUND, null);
        ComponentBuilder cb = new ComponentBuilder("§8§m          §4§l ⚠ §c§lClient §c§lReport §4§l⚠ §8§m          \n");
        if(ri.getReporter().equalsIgnoreCase("AntiCheat")) {
            cb.append("§7Ersteller§8: §3" + ri.getReporter() + "\n");
        } else {
            cb.append("§7Ersteller§8: §3" + EnderAPI.getInstance().getName(UUID.fromString(ri.getReporter())) + "\n");
        }
        switch(ri.getReason()) {
            case CHAT:
                String url = ProxyData.ChatlogLink + ri.getChatlogID();
                BaseComponent[] chat = new ComponentBuilder("§c" + ri.getReportedName())
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Chatlog ID: §a" + ri.getChatlogID())))
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).create();
                cb.append("§7Grund§8: §4" + ri.getReason().getTitle() + " §8┃ ").append(chat).append("\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case SKIN:
                BaseComponent[] skin = new ComponentBuilder("§c" + ri.getReportedName())
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Klicke um Skin anzuzeigen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/showskin " + ri.getID())).create();
                cb.append("§7Grund§8: §4" + ri.getReason().getTitle() + " §8┃ ").append(skin).append("\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            default:
                cb.append("§7Grund§8: §4" + ri.getReason().getTitle() + " §8┃ §c" + ri.getReportedName() + "\n");
                break;
        }
        cb.append("§7Aktionen§8: ");
        BaseComponent[] CLOSE = new ComponentBuilder("§c§lCLOSE")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Schliesst diesen Report ohne Folgen")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/closereport " + ri.getID()))
                .create();
        BaseComponent[] REJECT = new ComponentBuilder("§c§lREJECT")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Stellt diesen Report wieder für andere frei")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rejectreport " + ri.getID()))
                .create();
        switch(ri.getReason()) {
            case BUGUSING:
                BaseComponent[] ban1 = new ComponentBuilder("§c§lBAN")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + BanReason.BUGUSING.getTitle() +" §7bannen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + ri.getID() + " " + BanReason.BUGUSING.getID()))
                        .create();
                cb.append("§8[").append(ban1).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(REJECT).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case CHAT:
                BaseComponent[] LV = new ComponentBuilder("§c§lLV")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + MuteReason.CHAT1.getTitle() +" §7muten")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + ri.getID() + " " + MuteReason.CHAT1.getID()))
                        .create();
                BaseComponent[] MV = new ComponentBuilder("§c§lMV")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + MuteReason.CHAT2.getTitle() +" §7muten")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + ri.getID() + " " + MuteReason.CHAT2.getID()))
                        .create();
                BaseComponent[] SV = new ComponentBuilder("§c§lSV")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + MuteReason.CHAT3.getTitle() +" §7muten")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + ri.getID() + " " + MuteReason.CHAT3.getID()))
                        .create();
                cb.append("§8[").append(LV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(MV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(SV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case HACKING:
                BaseComponent[] ban2 = new ComponentBuilder("§c§lBAN")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + BanReason.CLIENTMODS.getTitle() +" §7bannen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + ri.getID() + " " + BanReason.CLIENTMODS.getID()))
                        .create();
                cb.append("§8[").append(ban2).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(REJECT).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case NAME:
                BaseComponent[] ban3 = new ComponentBuilder("§c§lBAN")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + BanReason.SKIN_NAME.getTitle() +" §7bannen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + ri.getID() + " " + BanReason.SKIN_NAME.getID()))
                        .create();
                cb.append("§8[").append(ban3).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case SKIN:
                BaseComponent[] ban4 = new ComponentBuilder("§c§lBAN")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + BanReason.SKIN_NAME.getTitle() +" §7bannen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + ri.getID() + " " + BanReason.SKIN_NAME.getID()))
                        .create();
                cb.append("§8[").append(ban4).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case TEAMING:
                BaseComponent[] ban5 = new ComponentBuilder("§c§lBAN")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + BanReason.TEAMING.getTitle() +" §7bannen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + ri.getID() + " " + BanReason.TEAMING.getID()))
                        .create();
                cb.append("§8[").append(ban5).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(REJECT).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            case TROLLING:
                BaseComponent[] ban6 = new ComponentBuilder("§c§lBAN")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + ri.getReportedName() + "§7 für §4" + BanReason.TROLLING.getTitle() +" §7bannen")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + ri.getID() + " " + BanReason.TROLLING.getID()))
                        .create();
                cb.append("§8[").append(ban6).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(CLOSE).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).append(REJECT).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                break;
            default:
                break;

        }
        cb.append("§8§m                                                       ").event((HoverEvent) null);
        pp.sendMessage(cb.create());
    }

}
