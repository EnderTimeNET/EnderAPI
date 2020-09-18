package net.endertime.enderapi.bungee.commands;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.PartyManager;
import net.endertime.enderapi.bungee.utils.PlayerParty;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Party_Command extends Command implements TabExecutor {

    private PartyManager partyManager;

    public Party_Command(String name, PartyManager partyManager) {
        super(name);
        this.partyManager = partyManager;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
            if (args.length == 0) {
                sendHelpFirst(sender);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (sender.hasPermission("party.publicparty")) {
                        if (partyManager.createParty(sender, true)) {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast eine öffentliche Party §aerstellt"));
                        } else {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cbereits §7in einer Party"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Man kann §cerst §7ab YouTuber eine öffentliche Party erstellen"));
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    PlayerParty playerParty = partyManager.getParty(sender);
                    if (playerParty != null) {
                        if (!playerParty.isPublicParty() || playerParty.isLeader(sender)) {
                            String list = "";
                            for (ProxiedPlayer players : playerParty.getMembers()) {
                                if (list.equals("")) {
                                    list = EnderAPI.getInstance().getPrefix(players) + players.getName();
                                } else {
                                    list = list + "§7, " + EnderAPI.getInstance().getPrefix(players) + players.getName();
                                }
                            }

                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Liste der Party"));
                            sender.sendMessage(partyManager.getMessage("§7Leader: " + partyManager.getPrefix(playerParty.getLeader().getUniqueId()) +
                                    playerParty.getLeader().getName()));
                            sender.sendMessage(partyManager.getMessage("§7Mitglieder: " + list));
                        } else {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist in der öffentlichen Party von " +
                                    partyManager.getPrefix(playerParty.getLeader().getUniqueId()) + playerParty.getLeader().getName()));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist in §ckeiner §7Party"));
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    PlayerParty playerParty = partyManager.getParty(sender);
                    if (playerParty != null) {
                        if (partyManager.removePlayer(playerParty, sender)) {
                            if (!playerParty.isPublicParty()) {
                                for (ProxiedPlayer member : playerParty.getMembers()) {
                                    member.sendMessage(partyManager.getMessage(partyManager.getPrefix()
                                            + partyManager.getPrefix(sender.getUniqueId()) +
                                            sender.getName() + " §7hat die Party §cverlassen"));
                                }
                            }
                            playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix()
                                    + partyManager.getPrefix(sender.getUniqueId()) +
                                    sender.getName() + " §7hat die Party §cverlassen"));

                            partyManager.start(playerParty);

                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast die Party §cverlassen"));
                        } else {
                            partyManager.deleteParty(sender);
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast die Party §caufgelöst"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist in §ckeiner §7Party"));
                    }
                } else if (args[0].equalsIgnoreCase("toggle")) {
                    int i = EnderAPI.getInstance().getFriend().getSettings().getPartyToggle(sender.getUniqueId());

                    if (i == 0) {
                        EnderAPI.getInstance().getFriend().getSettings().updatePartyToggle(sender.getUniqueId(), 1);

                        sender.sendMessage(
                                EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty()
                                        + "§7Du bekommst nun §cnur noch §7von Freunden eine Partyanfrage"));
                    } else if (i == 1){
                        EnderAPI.getInstance().getFriend().getSettings().updatePartyToggle(sender.getUniqueId(), 2);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty()
                                + "§7Du bekommst nun §ckeine §7Partyanfragen mehr"));
                    } else {
                        EnderAPI.getInstance().getFriend().getSettings().updatePartyToggle(sender.getUniqueId(), 0);

                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty()
                                + "§7Du bekommst nun §awieder §7Partyanfragen"));
                    }
                } else {
                    PlayerParty playerParty = partyManager.getParty(sender);
                    if (playerParty != null) {
                        sendPartyChat(sender, playerParty, args);
                    } else {
                        sendHelpFirst(sender);
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join")) {
                    if (partyManager.getParty(sender) == null) {
                        try {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                            PlayerParty playerParty = partyManager.getParty(target);
                            if (playerParty != null) {
                                if (playerParty.isPublicParty()) {
                                    if (partyManager.addPlayer(playerParty, sender)) {
                                        playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                partyManager.getPrefix(sender.getUniqueId()) + sender.getName() + " §7ist der Party §abeigetreten"));
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist der Party von " +
                                                partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §abeigetreten"));
                                    } else {
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cbereits §7in einer Party"));
                                    }
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                            target.getName() + " §7hat keine öffentliche Party"));
                                }
                            } else {
                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                        target.getName() + " §7hat keine öffentliche Party"));
                            }
                        } catch (NullPointerException e) {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cbereits §7in einer Party"));
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (partyManager.createParty(sender, false)) {
                        final PlayerParty playerParty = partyManager.getParty(sender);
                        if (playerParty.isLeader(sender)) {

                            try {
                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                                if (!target.getUniqueId().equals(sender.getUniqueId())) {
                                    if (partyManager.getParty(target) == null) {
                                        if (EnderAPI.getInstance().getFriend().getSettings().getPartyToggle(target.getUniqueId()) == 0
                                                || (EnderAPI.getInstance().getFriend().getSettings().getPartyToggle(target.getUniqueId()) == 1
                                                && EnderAPI.getInstance().getFriend().getFriends().isFriend(target.getUniqueId(), sender.getUniqueId()))) {
                                            if (!partyManager.addInvite(playerParty, target)) {
                                                if (playerParty.getInvites().contains(target)) {
                                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7wurde §cbereits §7eingeladen"));
                                                } else {
                                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7ist §cbereits §7in der Party"));
                                                }
                                            }
                                        } else {
                                            partyManager.deleteParty(sender);
                                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                    target.getName() + " §7nimmt §ckeine §7Anfragen an"));
                                        }
                                    } else {
                                        partyManager.deleteParty(sender);
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                target.getName() + " §7ist §cbereits §7in einer anderen Party"));
                                    }
                                } else {
                                    partyManager.deleteParty(sender);
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du kannst dich §cnicht §7selbst einladen"));
                                }
                            } catch (NullPointerException e) {
                                if (EnderAPI.getInstance().getTeamDatabase().isNicked(args[1])) {
                                    final UUID uuidNick = EnderAPI.getInstance().getTeamDatabase().getUUIDFromNickedName(args[1]);
                                    if (EnderAPI.getInstance().getTeamDatabase().isState(uuidNick)) {
                                        try {
                                            if (!sender.getUniqueId().equals(uuidNick)) {
                                                playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast "
                                                        + EnderAPI.getInstance().getTeamDatabase().getPrefix(uuidNick)
                                                        + EnderAPI.getInstance().getTeamDatabase().getNickedName(uuidNick) + " §7in die Party §aeingeladen"));

                                                ProxyServer.getInstance().getScheduler().schedule(EnderAPI.getInstance().getPlugin(), new Runnable() {
                                                    public void run() {
                                                        playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix()
                                                                + EnderAPI.getInstance().getTeamDatabase().getPrefix(uuidNick)
                                                                + EnderAPI.getInstance().getTeamDatabase().getNickedName(uuidNick)
                                                                + " §7hat die Einladung §cnicht §7angenommen"));
                                                        partyManager.start(playerParty);
                                                    }
                                                }, 1L, TimeUnit.MINUTES);

                                                ProxyServer.getInstance().getPlayer(uuidNick).sendMessage(partyManager.getMessage("§8§l┃ §5EnderNicker §8» " +
                                                        "§7Deinem §5Nick §7wurde eben eine Partyanfrage von " + partyManager.getPrefix(sender.getUniqueId()) +
                                                        sender.getName() + " §7geschickt"));
                                            } else {
                                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7" +
                                                        "Du kannst dich §cnicht §7selbst einladen"));
                                            }
                                        } catch (NullPointerException ex) {
                                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                                        }
                                    }
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                                }
                            }
                        } else {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cnicht §7der Partyleiter"));
                        }
                    } else {
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                        final PlayerParty playerParty = partyManager.getParty(sender);
                        if (playerParty.isLeader(sender)) {
                            if (target != null) {
                                if (!target.getUniqueId().equals(sender.getUniqueId())) {
                                    if (partyManager.getParty(target) == null) {
                                        if (EnderAPI.getInstance().getFriend().getSettings().getPartyToggle(target.getUniqueId()) == 0
                                                || (EnderAPI.getInstance().getFriend().getSettings().getPartyToggle(target.getUniqueId()) == 1
                                                && EnderAPI.getInstance().getFriend().getFriends().isFriend(target.getUniqueId(), sender.getUniqueId()))) {
                                            if (!partyManager.addInvite(playerParty, target)) {
                                                if (playerParty.getInvites().contains(target)) {
                                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7wurde §cbereits §7eingeladen"));
                                                } else {
                                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7ist §cbereits §7in der Party"));
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                    target.getName() + " §7nimmt §ckeine §7Anfragen an"));
                                        }
                                    } else {
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                target.getName() + " §7ist §cbereits §7in einer anderen Party"));
                                    }
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du kannst dich §cnicht §7selbst einladen"));
                                }
                            } else {
                                if (EnderAPI.getInstance().getTeamDatabase().isNicked(args[1])) {
                                    final UUID uuidNick = EnderAPI.getInstance().getTeamDatabase().getUUIDFromNickedName(args[1]);
                                    if (EnderAPI.getInstance().getTeamDatabase().isState(uuidNick)) {
                                        try {
                                            if (!sender.getUniqueId().equals(uuidNick)) {
                                                playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast "
                                                        + EnderAPI.getInstance().getTeamDatabase().getPrefix(uuidNick)
                                                        + EnderAPI.getInstance().getTeamDatabase().getNickedName(uuidNick) + " §7in die Party §aeingeladen"));

                                                ProxyServer.getInstance().getScheduler().schedule(EnderAPI.getInstance().getPlugin(), new Runnable() {
                                                    public void run() {
                                                        playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix()
                                                                + EnderAPI.getInstance().getTeamDatabase().getPrefix(uuidNick)
                                                                + EnderAPI.getInstance().getTeamDatabase().getNickedName(uuidNick)
                                                                + " §7hat die Einladung §cnicht §7angenommen"));
                                                        partyManager.start(playerParty);
                                                    }
                                                }, 1L, TimeUnit.MINUTES);

                                                ProxyServer.getInstance().getPlayer(uuidNick).sendMessage(partyManager.getMessage("§8§l┃ §5EnderNicker §8» " +
                                                        "§7Deinem §5Nick §7wurde eben eine Partyanfrage von " + partyManager.getPrefix(sender.getUniqueId()) +
                                                        sender.getName() + " §7geschickt"));
                                            } else {
                                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7" +
                                                        "Du kannst dich §cnicht §7selbst einladen"));
                                            }
                                        } catch (NullPointerException ex) {
                                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                                        }
                                    }
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                                }
                            }
                        } else {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cnicht §7der Partyleiter"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (partyManager.getParty(sender) == null) {
                        try {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                            PlayerParty playerParty = partyManager.getParty(target);
                            if (playerParty != null) {
                                if (partyManager.addPlayer(playerParty, sender)) {
                                    for (ProxiedPlayer member : playerParty.getMembers()) {
                                        member.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(sender.getUniqueId()) +
                                                sender.getName() + " §7ist der Party §abeigetreten"));
                                    }
                                    playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                            partyManager.getPrefix(sender.getUniqueId()) + sender.getName() + " §7ist der Party §abeigetreten"));
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                            target.getName() + " §7hat dich §cnicht §7eingeladen"));
                                }
                            } else {
                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                        target.getName() + " §7hat §ckeine §7Party"));
                            }
                        } catch (NullPointerException e) {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist nicht online"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cbereits §7in einer Party"));
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {
                    if (partyManager.getParty(sender) == null) {
                        try {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                            PlayerParty playerParty = partyManager.getParty(target);
                            if (playerParty != null) {
                                if (partyManager.removeInvite(playerParty, sender)) {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast die Partyanfrage von " +
                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §cabgelehnt"));
                                    partyManager.start(playerParty);
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                            target.getName() + " §7hat dich §cnicht §7eingeladen"));
                                }
                            } else {
                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                        target.getName() + " §7hat §ckeine §7Party"));
                            }
                        } catch (NullPointerException e) {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist nicht online"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cbereits §7in einer Party"));
                    }
                } else if (args[0].equalsIgnoreCase("kick")) {
                    PlayerParty playerParty = partyManager.getParty(sender);
                    if (playerParty != null) {
                        if (playerParty.isLeader(sender)) {
                            try {
                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                                if (!target.getUniqueId().equals(sender.getUniqueId())) {
                                    if (partyManager.removePlayer(playerParty, target)) {
                                        partyManager.start(playerParty);
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix()+ "§7Du hast " +
                                                partyManager.getPrefix(target.getUniqueId()) + target.getName()
                                                + " §7aus der Party §cgekickt"));
                                        for (ProxiedPlayer member : playerParty.getMembers()) {
                                            member.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId())
                                                    + target.getName() + " §7hat die Party §cverlassen"));
                                        }
                                    } else {
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                target.getName() + " §7ist §cnicht §7in deiner Party"));
                                    }
                                } else {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du kannst dich §cnicht §7selbst kicken"));
                                }
                            } catch (NullPointerException e) {
                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                            }
                        } else {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cnicht §7der Partyleiter"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist ein §ckeiner §7Party"));
                    }
                } else if (args[0].equalsIgnoreCase("promote")) {
                    PlayerParty playerParty = partyManager.getParty(sender);
                    if (playerParty != null) {
                        if (playerParty.isLeader(sender)) {
                            if (!playerParty.isPublicParty()) {
                                try {
                                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                                    if (!sender.getUniqueId().equals(target.getUniqueId())) {
                                        if (playerParty.inParty(target)) {
                                            partyManager.promote(playerParty, target);
                                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast " +
                                                    partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7zum Partyleiter §abefördet"));
                                            for (ProxiedPlayer member : playerParty.getMembers()) {
                                                member.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                        partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7ist nun der Partyleiter"));
                                            }
                                        } else {
                                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                    target.getName() + " §7ist §cnicht §7in deiner Party"));
                                        }
                                    } else {
                                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cbereits §7der Partyleiter"));
                                    }
                                } catch (NullPointerException e) {
                                    sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                                }
                            } else {
                                sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du kannst in einer öffentlichen Party " +
                                        "§cnicht §7den Partyleiter wechseln"));
                            }
                        } else {
                            sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cnicht §7der Partyleiter"));
                        }
                    } else {
                        sender.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist in §ckeiner §7Party"));
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (args[1].equalsIgnoreCase("1")) {
                        sendHelpFirst(sender);
                    } else if (args[1].equalsIgnoreCase("2")) {
                        sendHelpSecond(sender);
                    } else {
                        sendHelpFirst(sender);
                    }
                } else {
                    PlayerParty playerParty = partyManager.getParty(sender);
                    if (playerParty != null) {
                        sendPartyChat(sender, playerParty, args);
                    } else {
                        sendHelpFirst(sender);
                    }
                }
            } else {
                PlayerParty playerParty = partyManager.getParty(sender);
                if (playerParty != null) {
                    sendPartyChat(sender, playerParty, args);
                } else {
                    sendHelpFirst(sender);
                }
            }
        }
    }

    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 1) {
                list.add("accept");
                list.add("create");
                list.add("deny");
                list.add("invite");
                list.add("join");
                list.add("kick");
                list.add("leave");
                list.add("list");
                list.add("promote");

                return list;
            }
        }
        list.clear();
        return list;
    }

    private void sendHelpFirst(ProxiedPlayer pp) {
        pp.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty() + "§7Partyverwaltung §8[§e1§8/§e2§8]"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party invite <Name> §7Lädt Spieler in die Party ein"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party accept <Name> §7Nimmt die Anfrage an"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party deny <Name> §7Lehnt eine Anfrage ab"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party join <Name> §7Joine eine öffentlich Party"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party <Nachricht> §7Schreibe im Party Chat"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party list §7Listet alle Party Mitglieder auf"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party help 2 §7Um weitere Befehle zu sehen"));
    }

    private void sendHelpSecond(ProxiedPlayer pp) {
        pp.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty() + "§7Partyverwaltung §8[§e2§8/§e2§8]"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party promote <Name> §7Gib die Partyleitung ab"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party leave §7Verlässt die Party"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party kick <Name> §7Kickt einen Spieler aus der Party"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party toggle §7Anfragen erlauben & verbieten"));
        pp.sendMessage(EnderAPI.getInstance().getMessage("§e/party create §7Erstelle eine öffentliche Party"));
    }

    private void sendPartyChat (ProxiedPlayer sender, PlayerParty playerParty, String[] args) {
        String msg = "";
        for (String s : args)
            msg = msg + s + " ";

        for (ProxiedPlayer member : playerParty.getMembers()) {
            member.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(sender.getUniqueId()) + sender.getName() +
                    "§8: §7" + msg));
        }
        playerParty.getLeader().sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(sender.getUniqueId()) +
                sender.getName() + "§8: §7" + msg));
    }
}
