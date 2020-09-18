package net.endertime.enderapi.bungee.commands;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reply_Command extends Command {

    public Reply_Command(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ((sender instanceof ProxiedPlayer)) {
            ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (EnderAPI.getInstance().getMsg().get(pp) != null) {
                ProxiedPlayer pa = ProxyServer.getInstance().getPlayer(EnderAPI.getInstance().getMsg().get(pp).getUniqueId());
                try {
                    String msg = "";
                    for (int i = 0; i < args.length; i++)
                        msg = msg + args[i] + " ";

                    pp.sendMessage(EnderAPI.getInstance().getMessage(
                            EnderAPI.getInstance().getPrefixFriend() + "§8[§7Du §8➟ " + EnderAPI.getInstance().getPrefix(pa) + pa.getName() + "§8] §e" + msg));
                    pa.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§8["
                            + EnderAPI.getInstance().getPrefix(pp) + pp.getName()
                            + " §8➟§7 dir§8] §e" + msg));

                    EnderAPI.getInstance().getMsg().put(pa, pp);
                } catch (NullPointerException ex) {
                    pp.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                            + EnderAPI.getInstance().getPrefix(EnderAPI.getInstance().getMsg().get(pp))
                            + EnderAPI.getInstance().getMsg().get(pp) + "ist §cnicht §7mehr online"));
                }
            } else {
                pp.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Dir hat §ckeiner §7geschrieben"));
            }
        }
    }
}
