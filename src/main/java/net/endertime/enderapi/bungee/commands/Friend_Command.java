package net.endertime.enderapi.bungee.commands;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.FriendManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.*;

public class Friend_Command extends Command implements TabExecutor {

    FriendManager friend;

    public Friend_Command(String name, FriendManager friendManagerRework) {
        super(name);
        friend = friendManagerRework;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
            if (args.length == 1) {
                String cmd = args[0];
                if (cmd.equalsIgnoreCase("help")) {
                    sendHelpFirst(sender);
                } else if (cmd.equalsIgnoreCase("list")) {
                    List<UUID> list = friend.getFriends().getFriends(sender.getUniqueId());

                    if (list.size() > 0) {
                        Map<ProxiedPlayer, ServerInfo> online = new HashMap<ProxiedPlayer, ServerInfo>();

                        for (UUID uuid : list) {
                            if (ProxyServer.getInstance().getPlayer(uuid) != null) {
                                online.put(ProxyServer.getInstance().getPlayer(uuid), ProxyServer.getInstance().getPlayer(uuid).getServer().getInfo());
                            }
                        }
                        if (!online.keySet().isEmpty()) {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Liste deiner Freunde"));

                            for (ProxiedPlayer pa : online.keySet()) {
                                sender.sendMessage(EnderAPI.getInstance().getMessage("§7-" + EnderAPI.getInstance().getPrefix(pa) + pa.getName()
                                        + " §8(§aOnline §7auf " + "§e" + online.get(pa).getName() + "§8)"));
                            }
                        } else {
                            sender.sendMessage(
                                    friend.getMessage(friend.getPrefix() + "§7Derzeit sind §ckeine §7deiner Freunde online"));
                        }
                    } else {
                        sender.sendMessage(
                                friend.getMessage(friend.getPrefix() + "§7Derzeit sind §ckeine §7deiner Freunde online"));
                    }
                } else if (cmd.equalsIgnoreCase("clear")) {
                    List<UUID> list = friend.getFriends().getFriends(sender.getUniqueId());

                    for (UUID uuid : list) {
                        friend.getFriends().removeFriends(sender.getUniqueId(), uuid);
                    }

                    sender.sendMessage(EnderAPI.getInstance()
                            .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast deine komplette §7Freundesliste §agelöscht"));
                } else if (cmd.equalsIgnoreCase("requests")) {
                    List<UUID> request = friend.getRequests().getRequests(sender.getUniqueId());
                    sender.sendMessage(EnderAPI.getInstance().getMessage(
                            EnderAPI.getInstance().getPrefixFriend() + "§7Du hast §e" + request.size() + " §7Freundschaftsanfrage(n)"));

                    for (UUID uuid : request) {
                        sender.sendMessage(EnderAPI.getInstance().getMessage("§7- " + EnderAPI.getInstance().getPrefix(uuid)
                                + EnderAPI.getInstance().getName(uuid)));
                    }
                } else if (cmd.equalsIgnoreCase("acceptall")) {
                    List<UUID> requests = friend.getRequests().getRequests(sender.getUniqueId());

                    if (requests.size() == 0) {
                        sender.sendMessage(
                                friend.getMessage(friend.getPrefix() + "§7Du hast derzeit §ckeine §7Freundschaftsanfragen"));
                    } else if (requests.size() == 1) {
                        for (UUID uuid : requests) {
                            friend.getRequests().removeRequests(sender.getUniqueId(), uuid);
                            friend.getFriends().createFriend(sender.getUniqueId(), uuid);
                        }
                        sender.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast eine Freundschaftsanfragen §aangenommen"));
                    } else {
                        for (UUID uuid : requests) {
                            friend.getRequests().removeRequests(sender.getUniqueId(), uuid);
                            friend.getFriends().createFriend(sender.getUniqueId(), uuid);
                        }
                        sender.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast alle Freundschaftsanfragen §aangenommen"));
                    }
                } else if (cmd.equalsIgnoreCase("denyall")) {
                    List<UUID> requests = friend.getRequests().getRequests(sender.getUniqueId());

                    if (requests.size() == 0) {
                        sender.sendMessage(
                                EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast derzeit §ckeine §7Freundschaftsanfragen"));
                    } else if (requests.size() == 1) {
                        for (UUID uuid : requests) {
                            friend.getRequests().removeRequests(sender.getUniqueId(), uuid);
                        }
                        sender.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast eine Freundschaftsanfragen §cabgelehnt"));
                    } else {
                        for (UUID uuid : requests) {
                            friend.getRequests().removeRequests(sender.getUniqueId(), uuid);
                        }
                        sender.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast alle Freundschaftsanfragen §cabgelehnt"));
                    }
                } else if (cmd.equalsIgnoreCase("toggle")) {
                    if (!friend.getSettings().getToggle(sender.getUniqueId())) {
                        friend.getSettings().updateToggle(sender.getUniqueId(), true);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§7Du bekommst nun §ckeine §7Freundschaftsanfragen mehr"));
                    } else {
                        friend.getSettings().updateToggle(sender.getUniqueId(), false);

                        sender.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du bekommst nun §awieder §7Freundschaftsanfragen"));
                    }
                } else if (cmd.equalsIgnoreCase("togglenotify")) {
                    if (!friend.getSettings().getToggleNotify(sender.getUniqueId())) {
                        friend.getSettings().updateToggleNotify(sender.getUniqueId(), true);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§7Du erhälst nun §ckeine §7Online/Offline Nachrichten mehr"));
                    } else {
                        friend.getSettings().updateToggleNotify(sender.getUniqueId(), false);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§7Du erhälst nun §awieder §7Online/Offline Nachrichten"));
                    }
                } else if (cmd.equalsIgnoreCase("togglemessage")) {
                    if (!friend.getSettings().getToggleMessage(sender.getUniqueId())) {
                        friend.getSettings().updateToggleMessage(sender.getUniqueId(), true);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§7Du erhälst nun §ckeine §7privaten Nachrichten mehr"));
                    } else {
                        friend.getSettings().updateToggleMessage(sender.getUniqueId(), false);

                        sender.sendMessage(
                                EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                        + "§7Du erhälst nun §awieder §7private Nachrichten"));
                    }
                } else if (cmd.equalsIgnoreCase("togglejump")) {
                    if (!friend.getSettings().getToggleJump(sender.getUniqueId())) {
                        friend.getSettings().updateToggleJump(sender.getUniqueId(), true);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(
                                EnderAPI.getInstance().getPrefixFriend() + "§7Es können nun §ckeine §7Freunde mehr zu dir springen"));
                    } else {
                        friend.getSettings().updateToggleJump(sender.getUniqueId(), false);

                        sender.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Es können nun §awieder §7Freunde zu dir springen"));
                    }
                } else {
                    sendHelpFirst(sender);
                }
            } else if (args.length == 2) {
                String cmd = args[0];
                String name = args[1];

                if (cmd.equalsIgnoreCase("help")) {
                    if (name.equals("1")) {
                        sendHelpFirst(sender);
                    } else if (name.equals("2")) {
                        sendHelpSecond(sender);
                    } else {
                        sendHelpFirst(sender);
                    }
                } else if (cmd.equalsIgnoreCase("add")) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                    if (target != null) {
                        friend.addRequests(sender, target);
                    } else {
                        if (EnderAPI.getInstance().getTeamDatabase().isNicked(name)) {
                            UUID uuidNick = EnderAPI.getInstance().getTeamDatabase().getUUIDFromNickedName(name);
                            if (EnderAPI.getInstance().getTeamDatabase().isState(uuidNick)) {
                                if (ProxyServer.getInstance().getPlayer(uuidNick) != null) {
                                    if (!sender.getUniqueId().equals(uuidNick)) {
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance()
                                                .getPrefixFriend() + "§7Du hast "
                                                + EnderAPI.getInstance().getTeamDatabase().getPrefix(uuidNick)
                                                + EnderAPI.getInstance().getTeamDatabase().getNickedName(uuidNick)
                                                + " §7eine Freundschaftsanfrage §ageschickt"));
                                        ProxyServer.getInstance().getPlayer(uuidNick).sendMessage(EnderAPI.getInstance()
                                                .getMessage("§8§l┃ §5EnderNicker §8» " +
                                                "§7Deinem §5Nick " + "§7wurde eben eine Freundschaftsanfrage von "
                                                        + EnderAPI.getInstance().getPrefix(sender)
                                                + sender.getName() + " §7geschickt"));
                                    } else {
                                        sender.sendMessage(friend.getMessage(friend.getPrefix() + "§7Du kannst dir §ckeine §7Freundschaftsanfrage senden"));
                                    }
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance()
                                            .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7" + name + " §7nimmt §ckeine §7Anfragen an"));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance()
                                        .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7" + name + " §7nimmt §ckeine §7Anfragen an"));
                            }
                        } else {
                            if (EnderAPI.getInstance().getUUID(name) != null) {
                                friend.addRequests(sender, EnderAPI.getInstance().getUUID(name));
                            } else {
                                sender.sendMessage(EnderAPI.getInstance()
                                        .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7" + name + " §7nimmt §ckeine §7Anfragen an"));
                            }
                        }
                    }
                } else if (cmd.equalsIgnoreCase("accept")) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                    if (target != null) {
                        friend.addFriend(sender, target);
                    } else {
                        friend.addFriend(sender, EnderAPI.getInstance().getUUID(name));
                    }
                } else if (cmd.equalsIgnoreCase("jump")) {
                    friend.jump(sender, EnderAPI.getInstance().getUUID(name));
                } else if (cmd.equalsIgnoreCase("remove")) {
                    friend.deleteFriend(sender, EnderAPI.getInstance().getUUID(name));
                } else if (cmd.equalsIgnoreCase("deny")) {
                    friend.denyRequests(sender, EnderAPI.getInstance().getUUID(name));
                } else {
                    sendHelpFirst(sender);
                }
            } else {
                sendHelpFirst(sender);
            }
        }
    }

    private void sendHelpFirst(ProxiedPlayer pp) {
        pp.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Freundeverwaltung §8[§e1§8/§e2§8]"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend add <Name> §7Fügt einen neuen Freund hinzu"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend list §7Zeige eine Liste aller Freunde"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend clear §7Löscht dein Freundesliste"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend remove <Name> §7Entfernt einen Freund"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend jump <Name> §7Auf des Freundes Server springen"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend accept <Name> §7Nimmt eine Anfrage an"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend deny <Name> §7Lehnt eine Anfrage ab"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend help 2 §7Um weitere Befehle zu sehen"));
    }

    private void sendHelpSecond(ProxiedPlayer pp) {
        pp.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Freundeverwaltung §8[§e2§8/§e2§8]"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend requests §7Zeigt alle Anfrangen an"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend acceptall §7Nimmt alle Freundschaftsanfragen an"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend denyall §7Lehnt alle Freundschaftsanfragen ab"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend toggle §7Anfragen erlauben & verbieten"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend togglenotify §7Online/ Offline Nachrichten erlauben verbieten"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend togglemessage §7Private Nachrichten erlauben & verbieten"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/friend togglejump §7Erlaube & verbiete Spielern zu dir zu springen"));
    }

    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();
        if (!(sender instanceof ProxiedPlayer))
            return list;
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 1) {
                if (args[0].startsWith("a")) {
                    list.add("accept");
                    list.add("acceptall");
                    list.add("add");
                } else if (args[0].startsWith("d")) {
                    list.add("deny");
                    list.add("denyall");
                } else if (args[0].startsWith("j")) {
                    list.add("jump");
                } else if (args[0].startsWith("l")) {
                    list.add("list");
                } else if (args[0].startsWith("r")) {
                    list.add("remove");
                    list.add("requests");
                } else if (args[0].startsWith("t")) {
                    list.add("toggle");
                    list.add("togglejump");
                    list.add("togglemessage");
                    list.add("togglenotify");
                } else {
                    list.add("accept");
                    list.add("acceptall");
                    list.add("add");
                    list.add("deny");
                    list.add("denyall");
                    list.add("jump");
                    list.add("list");
                    list.add("remove");
                    list.add("requests");
                    list.add("toggle");
                    list.add("togglejump");
                    list.add("togglemessage");
                    list.add("togglenotify");
                }
                return list;
            }
        }
        return list;
    }
}
