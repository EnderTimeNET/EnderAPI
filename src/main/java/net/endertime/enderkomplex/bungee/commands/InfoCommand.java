package net.endertime.enderkomplex.bungee.commands;

import java.util.ArrayList;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.utils.InfoCollector;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class InfoCommand extends Command {

    public InfoCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.info")) {
            if(args.length == 0) {
                ArrayList<BaseComponent[]> topservers = InfoCollector.getTopServers();
                ComponentBuilder cb = new ComponentBuilder("§0\n");
                cb.append("§8§m§l                   §8§l[ §4§lInformation §8§l]§8§m§l                   \n");
                cb.append("§1\n");
                cb.append("§7Letzer Neustart§8: ").append(InfoCollector.getLastRestart()).append("§r\n");
                cb.append("§7Ländertracker§8: ").append(InfoCollector.getCountries()).append("§r\n").event((HoverEvent) null);
                cb.append("§7Statistiken§8: §5CPM§8: §c" + InfoCollector.cpm.size() + " §8┃ §5PPM§8: §c" + InfoCollector.ppm.size()
                        + " §8┃ §5UPC§8: §c" + InfoCollector.upc.size() + " §8┃ §5TPC§8: §c" + InfoCollector.tpc).append("§r\n");
                cb.append("§7Modcounter§8: §9LM§8: §c" + InfoCollector.labymod.size() + " §8┃ §9BC§8: §c" + EnderAPI.getInstance().getBadlion().size()
                        + " §8┃ §9F§8: §c" + InfoCollector.forge.size() + "§r\n");
                if(topservers.isEmpty()) {
                    cb.append("§7Topserver§8: ").append("§r\n");
                } else if(topservers.size() == 1) {
                    cb.append("§7Topserver§8: ").append(topservers.get(0)).append("§8, ").event((HoverEvent) null).append("§r\n");
                } else if(topservers.size() == 2) {
                    cb.append("§7Topserver§8: ").append(topservers.get(0)).append("§8, ").event((HoverEvent) null).append(topservers.get(1))
                            .append("§8, ").event((HoverEvent) null).append("§r\n");
                } else {
                    cb.append("§7Topserver§8: ").append(topservers.get(0)).append("§8, ").event((HoverEvent) null).append(topservers.get(1))
                            .append("§8, ").event((HoverEvent) null).append(topservers.get(2)).append("§8, ").event((HoverEvent) null).append("§r\n");
                }
                cb.append("§7Moderation§8: §6B§8: §c" + InfoCollector.bans + " §8┃ §6M§8: §c" + InfoCollector.mutes + " §8┃ §6R§8: §c" +
                        InfoCollector.reports + " §8┃ §6C§8: §c" + InfoCollector.chatfilter + " §8┃ §6J§8: §c" + InfoCollector.joinmes).append("§r\n");
                cb.append("§2\n");
                cb.append("§8» ").append(InfoCollector.getVersionCount(InfoCollector.Version.v1_8)).append(" §8┃ ")
                        .append(InfoCollector.getVersionCount(InfoCollector.Version.v1_9)).append(" §8┃ ")
                        .append(InfoCollector.getVersionCount(InfoCollector.Version.v1_10))
                        .append(" §8┃ ").append(InfoCollector.getVersionCount(InfoCollector.Version.v1_11)).append(" §8┃ ")
                        .append(InfoCollector.getVersionCount(InfoCollector.Version.v1_12)).append("§r\n");
                cb.append("§8§m§l                                                                                                ");
                pp.sendMessage(cb.create());
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cinfo");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }

    }

}
