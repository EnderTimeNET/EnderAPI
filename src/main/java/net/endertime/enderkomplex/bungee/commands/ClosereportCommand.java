package net.endertime.enderkomplex.bungee.commands;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.bungee.utils.ReportWorkingHandler;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClosereportCommand extends Command {

    public ClosereportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.closereport")) {
            if(args.length == 1) {
                if(args[0].startsWith("#ER")) {
                    if(Database.isReportIdExist(args[0])) {
                        String reportid = args[0];
                        if(Database.getReportStatus(reportid).equals(ReportStatus.WORKING)) {
                            if(Database.getReportWorkerUUID(reportid).equals(pp.getUniqueId().toString())) {
                                Database.updateReportStatus(reportid, ReportStatus.FINISHED);
                                Database.updateReportTimestamp(reportid);
                                Database.createReportStats(pp.getUniqueId(), reportid, false, true, false, Database.getReportReason(reportid));
                                ReportWorkingHandler.workingReports.remove(pp);
                                ProxyServer.getInstance().getPluginManager().dispatchCommand(pp, "jump SilentLobby-1");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast den Report §aerfolgreich §7geschlossen§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_SUCCESS_SOUND, null);
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
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§cDiese ReportID existiert nicht§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cshowskin §8<§creportid§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

}
