package net.endertime.enderkomplex.bungee.commands;

import java.util.UUID;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.BanReason;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.bungee.objects.ClientBan;
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

public class BanCommand extends Command {

    public BanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.ban")) {
            if(args.length == 0) {
                pp.sendMessage(TextComponent.fromLegacyText("§8§m     §4§l ✖ §4§lClientbann §4§lGründe §4§l✖ §8§m     "));
                for(BanReason br : BanReason.values()) {
                    if(pp.hasPermission(br.getPermission())) {
                        BaseComponent[] brc = new ComponentBuilder("§7§l" + br.getID() + " §8┃ §c§o" + br.getTitle())
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(br.getExamples())))
                                .create();
                        pp.sendMessage(new ComponentBuilder("§8➟ ").append(brc).create());
                    }
                }
                pp.sendMessage(TextComponent.fromLegacyText("§8§m                                             "));
            } else if(args.length == 2) {
                if(args[0].startsWith("#ER")) {
                    String reportid = args[0];
                    if(Database.isReportIdExist(reportid)) {
                        if(Database.getReportStatus(reportid).equals(ReportStatus.WORKING)) {
                            if(pp.getUniqueId().toString().equals(Database.getReportWorkerUUID(reportid))) {
                                UUID uuid = Database.getreportedUUID(reportid);
                                if(!Database.hasActiveBan(uuid)) {
                                    int banid = 0;
                                    try {
                                        banid = Integer.valueOf(args[1]);
                                    } catch (NumberFormatException error) {
                                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige BanID§8!");
                                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                    }
                                    for(BanReason reason : BanReason.values()) {
                                        if(reason.getID() == banid) {
                                            if(pp.hasPermission(reason.getPermission())) {
                                                ReportInfo ri = Database.getReportInfo(reportid);
                                                ClientBan ban = new ClientBan(pp.getUniqueId(), reason, uuid);
                                                Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                                Database.updateReportTimestamp(reportid);
                                                Database.createReportStats(pp.getUniqueId(), reportid, true, false, false, ri.getReason());
                                                ReportWorkingHandler.workingReports.remove(pp);
                                                ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "jump SilentLobby-1");
                                                if(reason.isIpBan()) {
                                                    if(ProxyServer.getInstance().getPlayer(uuid) != null) {
                                                        String ip = ProxyServer.getInstance().getPlayer(uuid).getAddress().getAddress().toString();
                                                        ProxyServer.getInstance().getPlayers().forEach(all -> {
                                                            if(all.getAddress().getAddress().toString().equals(ip)) {
                                                                all.disconnect(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                                                        + "\n"
                                                                        + "§7Grund§8: §c" + ban.getReason().getTitle() + "\n"
                                                                        + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(ban.getTimestamp(), ban.getDuration()) + "\n"
                                                                        + "\n"
                                                                        + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    if(ProxyServer.getInstance().getPlayer(uuid) != null) {
                                                        ProxyServer.getInstance().getPlayer(uuid).disconnect(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                                                + "\n"
                                                                + "§7Grund§8: §c" + ban.getReason().getTitle() + "\n"
                                                                + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(ban.getTimestamp(), ban.getDuration()) + "\n"
                                                                + "\n"
                                                                + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                                                    }
                                                }
                                                return;
                                            } else {
                                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §ckeine Berechtigung §7für diesen Banngrund§8!");
                                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                                return;
                                            }
                                        }
                                    }
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige BanID§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                } else {
                                    Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                    Database.updateReportTimestamp(reportid);
                                    ReportWorkingHandler.workingReports.remove(pp);
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist bereits §7gebannt§8!");
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
                } else if(!args[0].equalsIgnoreCase(pp.getName())) {
                    if(EnderAPI.getInstance().getUUID(args[0]) != null) {
                        if(!EnderAPI.getInstance().isInTeam(EnderAPI.getInstance().getUUID(args[0]))) {
                            UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                            if(!Database.hasActiveBan(uuid)) {
                                int banid = 0;
                                try {
                                    banid = Integer.valueOf(args[1]);
                                } catch (NumberFormatException error) {
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige BanID§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                }
                                for(BanReason reason : BanReason.values()) {
                                    if(reason.getID() == banid) {
                                        if(pp.hasPermission(reason.getPermission())) {
                                            ClientBan ban = new ClientBan(pp.getUniqueId(), reason, uuid);
                                            if(reason.isIpBan()) {
                                                if(ProxyServer.getInstance().getPlayer(uuid) != null) {
                                                    String ip = ProxyServer.getInstance().getPlayer(uuid).getAddress().getAddress().toString();
                                                    ProxyServer.getInstance().getPlayers().forEach(all -> {
                                                        if(all.getAddress().getAddress().toString().equals(ip)) {
                                                            all.disconnect(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                                                    + "\n"
                                                                    + "§7Grund§8: §c" + ban.getReason().getTitle() + "\n"
                                                                    + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(ban.getTimestamp(), ban.getDuration()) + "\n"
                                                                    + "\n"
                                                                    + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                                                        }
                                                    });
                                                }
                                            } else {
                                                if(ProxyServer.getInstance().getPlayer(uuid) != null) {
                                                    ProxyServer.getInstance().getPlayer(uuid).disconnect(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                                            + "\n"
                                                            + "§7Grund§8: §c" + ban.getReason().getTitle() + "\n"
                                                            + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(ban.getTimestamp(), ban.getDuration()) + "\n"
                                                            + "\n"
                                                            + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                                                }
                                            }
                                            return;
                                        } else {
                                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §ckeine Berechtigung §7für diesen Banngrund§8!");
                                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                            return;
                                        }
                                    }
                                }
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige BanID§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            } else {
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist bereits §7gebannt§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            }
                        } else {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst §ckein §7Teammitglied §7bannen§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Der Spieler §c" + args[0] + "§7 war noch nie auf §5EnderTime§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst dich §cnicht selbst §7bannen§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cban §8<§cname§8> <§cbanid§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

}
