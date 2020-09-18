package net.endertime.enderkomplex.bungee.commands;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.KickReason;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

    public KickCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.kick")) {
            if(args.length == 0) {
                pp.sendMessage(TextComponent.fromLegacyText("§8§m     §4§l ⚙ §4§lClientkick §4§lGründe §4§l⚙ §8§m     "));
                for(KickReason br : KickReason.values()) {
                    if(pp.hasPermission(br.getPermission())) {
                        BaseComponent[] brc = new ComponentBuilder("§7§l" + br.getID() + " §8┃ §c§o" + br.getTitle())
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(br.getExamples())))
                                .create();
                        pp.sendMessage(new ComponentBuilder("§8➟ ").append(brc).create());
                    }
                }
                pp.sendMessage(TextComponent.fromLegacyText("§8§m                                             "));
            } else if(args.length == 2) {
                if(!args[0].equalsIgnoreCase(pp.getName())) {
                    if(!EnderAPI.getInstance().isInTeam(EnderAPI.getInstance().getUUID(args[0]))) {
                        if(ProxyServer.getInstance().getPlayer(args[0]) != null) {
                            int kickid = 0;
                            try {
                                kickid = Integer.valueOf(args[1]);
                            } catch (NumberFormatException error) {
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige KickID§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            }
                            for(KickReason reason : KickReason.values()) {
                                if(reason.getID() == kickid) {
                                    if(pp.hasPermission(reason.getPermission())) {
                                        ProxyServer.getInstance().getPlayer(args[0]).disconnect(TextComponent.fromLegacyText("§7§lDein §c§lClient" +
                                                " §7§lwurde vom §5§lEnderTime-Netzwerk §7§lgekickt!\n"
                                                + "\n"
                                                + "§7Grund§8: §c" + reason.getTitle()));
                                        ProxyServer.getInstance().getPlayers().forEach(all -> {
                                            if(all.hasPermission("teamserver.join")) {
                                                if(Database.existsInNotifySettings(all.getUniqueId())) {
                                                    if(Database.getNotifySetting(all.getUniqueId(), NotifyType.BANSYSTEM)) {
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0]
                                                                + " §7wurde von §c" + pp.getName() + "§7 gekickt §8[§4" + reason.getTitle() + "§8]");
                                                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                    }
                                                } else {
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§c" + args[0]
                                                            + " §7wurde von §c" + pp.getName() + "§7 gekickt §8[§4" + reason.getTitle() + "§8]");
                                                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                                }
                                            }
                                        });
                                        return;
                                    } else {
                                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §ckeine Berechtigu" +
                                                "ng §7für diesen Kickgrund§8!");
                                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                        return;
                                    }
                                }
                            }
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte benutze eine gültige KickID§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        } else {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler ist §cnicht §7online§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst §ckein §7Teammitglied §7kicken§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst dich §cnicht selbst §7kicken§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§ckick §8<§cname§8> <§ckickid§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }
}
