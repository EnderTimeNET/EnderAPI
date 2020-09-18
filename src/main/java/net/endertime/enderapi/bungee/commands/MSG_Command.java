package net.endertime.enderapi.bungee.commands;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.FriendManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MSG_Command extends Command {

    private FriendManager friend;

    public MSG_Command(FriendManager friend) {
        super("msg");
        this.friend = friend;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
            if (args.length > 1) {
                if (EnderAPI.getInstance().isInTeam(sender.getUniqueId())) {
                    String name = args[0];
                    try {
                        ProxiedPlayer pa = ProxyServer.getInstance().getPlayer(name);

                        String msg = "";
                        for (int i = 1; i < args.length; i++)
                            msg = msg + args[i] + " ";

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§8[§7Du §8➟ " +
                                        EnderAPI.getInstance().getPrefix(pa) + pa.getName() + "§8] §e" + msg));
                        pa.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                + "§8[" + EnderAPI.getInstance().getPrefix(sender) + sender.getName()
                                + " §8➟§7 dir§8] §e" + msg));

                        EnderAPI.getInstance().getMsg().put(pa, sender);
                    } catch (NullPointerException ex) {
                        sender.sendMessage(
                                EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                        + "§7Der Spieler ist §cnicht §7online"));
                        ex.printStackTrace();
                    }
                } else {
                    if (friend.getSettings().getToggleMessage(sender.getUniqueId())) {
                        sender.sendMessage(
                                EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                        + "§7Du hast private Nachrichten §cdeaktiviert§7"));
                        return;
                    }

                    String name = args[0];
                    try {
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                        if (!friend.getFriends().isFriend(sender.getUniqueId(), target.getUniqueId())) {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                    + "§7Du bist §cnicht " + EnderAPI.getInstance().getPrefix(target)
                                    + target.getName() + " §7befreundet"));
                            return;
                        }
                        if (friend.getSettings().getToggleMessage(target.getUniqueId())) {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                    + EnderAPI.getInstance().getPrefix(target) + target.getName()
                                    + " §7hat private Nachrichten §cdeaktiviert§7"));
                            return;
                        }

                        String msg = "";
                        for (int i = 1; i < args.length; i++)
                            msg = msg + args[i] + " ";

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§8[§7Du §8➟ " +
                                        EnderAPI.getInstance().getPrefix(target) + target.getName() + "§8] §e" + msg));
                        target.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                + "§8[" + EnderAPI.getInstance().getPrefix(sender) + sender.getName()
                                + " §8➟§7 dir§8] §e" + msg));

                        EnderAPI.getInstance().getMsg().put(target, sender);
                    } catch (NullPointerException ex) {
                        sender.sendMessage(
                                EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Der Spieler ist §cnicht §7online"));
                        ex.printStackTrace();
                    }
                }
            } else {
                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Bitte benutzte §e/msg <Name> <Nachricht>"));
            }
        }
    }

}
