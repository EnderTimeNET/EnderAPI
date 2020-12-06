package net.endertime.enderkomplex.bungee.commands;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.State;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class VjumpCommand extends Command {

    public VjumpCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.vjump")) {
            if(args.length == 1) {
                String arg = args[0];
                if(ProxyServer.getInstance().getPlayer(arg) != null) {
                    if(pp.getServer().getInfo().getName().equals(arg)) {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du §cbist bereits §7auf dem betroffenen Server!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    } else {
                        ServerInfo serverinfo = ProxyServer.getInstance().getServerInfo(ProxyServer.getInstance().getPlayer(arg).getServer().getInfo().getName());
                        if(EnderAPI.getInstance().getState(serverinfo.getName()).equals(State.ONLINE) |
                                EnderAPI.getInstance().getState(serverinfo.getName()).equals(State.INGAME)) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(arg);
                            ChannelMessage.builder()
                                    .channel("enderkomplex")
                                    .message("pluginmessage")
                                    .json(JsonDocument.newDocument().append("action", PluginMessage.SET_VANISH.toString()).append("uuid", pp.getUniqueId()))
                                    .targetService(target.getServer().getInfo().getName())
                                    .build()
                                    .send();
                            pp.connect(ProxyServer.getInstance().getServerInfo(ProxyServer.getInstance().getPlayer(arg).getServer().getInfo().getName()));
                        } else {
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst §cjetzt nicht §7dort hin springen§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    }
                } else if(EnderAPI.getInstance().getUUID(arg) != null) {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler ist aktuell §cnicht §7online§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Deine Angabe ist §cungültig§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cvjump §8<§cspieler§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }
}
