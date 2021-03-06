package net.endertime.enderkomplex.bungee.commands;

import java.util.UUID;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.UnmuteReason;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommand extends Command {

    public UnmuteCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.unmute")) {
            if(args.length == 0) {
                pp.sendMessage(TextComponent.fromLegacyText("§8§m     §4§l ✎ §4§lUnmute §4§lGründe §4§l✎ §8§m     "));
                for(UnmuteReason umr : UnmuteReason.values()) {
                    if(umr.equals(UnmuteReason.EXPIRED)) continue;
                    BaseComponent[] brc = new ComponentBuilder("§7§l" + umr.getID() + " §8┃ §c§o" + umr.getTitle())
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(umr.getExamples())))
                            .create();
                    pp.sendMessage(new ComponentBuilder("§8➟ ").append(brc).create());
                }
                pp.sendMessage(TextComponent.fromLegacyText("§8§m                                        "));
            } else if(args.length == 2) {
                if(EnderAPI.getInstance().getUUID(args[0]) != null) {
                    UUID uuid = EnderAPI.getInstance().getUUID(args[0]);
                    if(Database.hasActiveMute(uuid)) {
                        int muteid = 0;
                        try {
                            muteid = Integer.valueOf(args[1]);
                        } catch (NumberFormatException error) {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige UnmuteID§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                        for(UnmuteReason reason : UnmuteReason.values()) {
                            if(reason.equals(UnmuteReason.EXPIRED)) continue;
                            if(reason.getID() == muteid) {
                                Database.unmutePlayer(uuid, reason, pp.getUniqueId());
                                ProxyServer.getInstance().getPlayers().forEach(all -> {
                                    if(all.hasPermission("teamserver.join")) {
                                        if(Database.existsInNotifySettings(all.getUniqueId())) {
                                            if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0]
                                                        + " §7wurde von §c" + pp.getName() + "§7 entmutet §8[§a" + reason.getTitle()+ "§8]");
                                                ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                            }
                                        } else {
                                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0]
                                                    + " §7wurde von §c" + pp.getName() + "§7 entmutet §8[§a" + reason.getTitle()+ "§8]");
                                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige UnmuteID§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler §cist nicht §7gemutet§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Der Spieler §c" + args[0] + "§7 war noch nie auf §5EnderTime§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cunmute §8<§cname§8> <§cunmuteid§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

}
