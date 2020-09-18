package net.endertime.enderkomplex.bungee.commands;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

    public BroadcastCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.broadcast")) {
            if(args.length > 0) {
                ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText("§1 "));
                ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText("§2 "));
                ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText("§8§l┃ §5DURCHSAGE §8» §7" +
                        ProxyHandler.argsToString(args).replace('&', '§')));
                ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText("§3 "));
                ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText("§4 "));
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cbc §8<§cmessage§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }

    }

}
