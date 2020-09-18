package net.endertime.enderkomplex.bungee.commands;

import java.util.UUID;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.UnbanReason;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

    public UnbanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.unban")) {
            if(args.length == 0) {
                pp.sendMessage(TextComponent.fromLegacyText("§8§m     §4§l ✖ §4§lUnban §4§lGründe §4§l✖ §8§m     "));
                for(UnbanReason br : UnbanReason.values()) {
                    if(br.equals(UnbanReason.EXPIRED)) continue;
                    BaseComponent[] brc = new ComponentBuilder("§7§l" + br.getID() + " §8┃ §c§o" + br.getTitle())
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(br.getExamples())))
                            .create();
                    pp.sendMessage(new ComponentBuilder("§8➟ ").append(brc).create());
                }
                pp.sendMessage(TextComponent.fromLegacyText("§8§m                                        "));
            } else if(args.length == 2) {
                if(EnderAPI.getInstance().getUUID(args[0]) != null) {
                    UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                    if(Database.hasActiveBan(uuid)) {
                        int banid = 0;
                        try {
                            banid = Integer.valueOf(args[1]);
                        } catch (NumberFormatException error) {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige UnbanID§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                        for(UnbanReason reason : UnbanReason.values()) {
                            if(reason.equals(UnbanReason.EXPIRED)) continue;
                            if(reason.getID() == banid) {
                                Database.unbanPlayer(uuid, reason, pp.getUniqueId());
                                ProxyServer.getInstance().getPlayers().forEach(all -> {
                                    if(all.hasPermission("teamserver.join")) {
                                        if(Database.existsInNotifySettings(all.getUniqueId())) {
                                            if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0] + " §7wurde von §c" + pp.getName() + "§7 entbannt §8[§a" + reason.getTitle() + "§8]");
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                        } else {
                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0] + " §7wurde von §c" + pp.getName() + "§7 entbannt §8[§a" + reason.getTitle() + "§8]");
                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige UnbanID§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist nicht §7gebannt§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Der Spieler §c" + args[0] + "§7 war noch nie auf §5EnderTime§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cunban §8<§cname§8> <§cunbanid§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

}
