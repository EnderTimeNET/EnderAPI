package net.endertime.enderkomplex.bungee.commands;

import java.util.HashMap;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.objects.Chatlog;
import net.endertime.enderkomplex.bungee.utils.ChatListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatlogCommand extends Command {

    public ChatlogCommand(String name) {
        super(name);
    }

    public static HashMap<ProxiedPlayer, Long> cooldown = new HashMap<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.chatlog")) {
            if(!cooldown.containsKey(pp)) cooldown.put(pp, System.currentTimeMillis());
            if(cooldown.get(pp) <= System.currentTimeMillis()) {
                if(args.length == 1) {
                    if(!args[0].equalsIgnoreCase(pp.getName())) {
                        if(ProxyServer.getInstance().getPlayer(args[0]) != null) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                            if(ChatListener.chathistory.containsKey(target)) {
                                String link = new Chatlog(pp, target).getLink();
                                pp.sendMessage(TextComponent.fromLegacyText("§8§l┃ §5ChatLog §8» §c" + link));
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §aerfolgreich §7einen Chatlog von §c"
                                        + target.getName() + " §7erstellt");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_SUCCESS_SOUND, null);
                                cooldown.put(pp, (System.currentTimeMillis() + 5000));
                            } else {
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler hat §cnoch nichts §7geschreiben§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            }
                        } else {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler ist aktuell §cnicht §7auf diesem Server§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst §ckeinen §7Chatlog von dir erstellen§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cchatlog §8<§cname§8>");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte §cwarte einen Moment §7um diesen Befehl zu nutzen§8!");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }
}
