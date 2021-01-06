package net.endertime.enderapi.bungee.commands;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.api.PermAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class Perm_Command extends Command {

    public Perm_Command(String name) {
        super(name);
    }

    public void execute(CommandSender commandSender, String[] args) {
            if (commandSender instanceof ProxiedPlayer) {
                ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                if (sender.hasPermission("*")) {
                    if (args.length < 3) {
                        if (args.length < 2) {
                            sendHelp(commandSender);
                        } else {
                            String arg1 = args[0];
                            String name = args[1];
                            if (arg1.equalsIgnoreCase("user")) {
                                UUID uuid = EnderAPI.getInstance().getUUID(name);
                                if (uuid != null) {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix() +
                                            "§7Permission von §c" + EnderAPI.getInstance().getName(uuid)));

                                    for (String s : PermAPI.getInstance().getUserPermissions().getPerms(uuid))
                                        sender.sendMessage(EnderAPI.getInstance().getMessage("§8-> §7" + s));
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                            + "§c" + name + " §7war noch §cnie §7auf dem Netzwerk"));
                                }
                            } else if ( arg1.equalsIgnoreCase("group")) {
                                if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                            + "§7Permission von §c" + name));

                                    for (String s : PermAPI.getInstance().getRankPermissions().getPerms(name))
                                        sender.sendMessage(EnderAPI.getInstance().getMessage("§8-> §7" + s));

                                    sender.sendMessage(EnderAPI.getInstance().getMessage(""));
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(""));
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(""));
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(""));

                                    for (String s : PermAPI.getInstance().getRankPermissions().getPermsWithOut(name))
                                        sender.sendMessage(EnderAPI.getInstance().getMessage("§8-> §7" + s));
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                            + "Der Rang §c" + name + " §7existiert §cnicht"));
                                }
                            } else {
                                sendHelp(commandSender);
                            }
                        }
                    } else {
                        String arg1 = args[0];
                        String name = args[1];
                        if (arg1.equalsIgnoreCase("user")) {
                            UUID uuid = EnderAPI.getInstance().getUUID(name);
                            if (uuid != null) {
                                String arg2 = args[2];
                                if (arg2.equalsIgnoreCase("add")) {
                                    String permission = args[3];
                                    if (args.length == 4) {
                                        PermAPI.getInstance().getUserPermissions().addPermission(uuid, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                + "§7§Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                                + " §7die Permission " + "§c" + permission + " §7gegeben"));
                                    } else {
                                        sendHelp(commandSender);
                                    }
                                } else if (arg2.equalsIgnoreCase("remove")) {
                                    String permission = args[3];
                                    if (args.length == 4) {
                                        PermAPI.getInstance().getUserPermissions().removePermission(uuid, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                + "Du hast §c" + EnderAPI.getInstance().getName(uuid) +
                                                " §7die Permission " + "§c" + permission + " §7entfernt"));
                                    } else {
                                        sendHelp(commandSender);
                                    }
                                } else if (arg2.equalsIgnoreCase("set")) {
                                    if (!PermAPI.getInstance().getUsers().isUserExists(uuid)) {
                                        PermAPI.getInstance().getUsers().createUser(uuid, -1);
                                    }
                                    String rank = args[3];
                                    if (args.length == 4) {
                                        if (PermAPI.getInstance().getRanks().isRankExists(rank)) {
                                            PermAPI.getInstance().getUsers().updateRank(uuid, rank);
                                            PermAPI.getInstance().getUsers().updateTime(uuid, -1);
                                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                    + "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                                    + " §7die Gruppe " + "§c" + rank + " §7gegeben"));
                                        } else {
                                            sendHelp(commandSender);
                                        }
                                    } else {
                                        sendHelp(commandSender);
                                    }
                                } else {
                                    sendHelp(commandSender);
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                        + "§c" + name + " §7war noch §cnie §7auf dem Netzwerk"));
                            }
                        } else if (arg1.equalsIgnoreCase("group")) {
                            String arg3 = args[2];
                            if (args.length == 3) {
                                if (arg3.equalsIgnoreCase("create")) {
                                    if (!PermAPI.getInstance().getRanks().isRankExists(name)) {
                                        PermAPI.getInstance().getRanks().createRank(name, "", "", "", "", "");
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix() +
                                                "§7Du hast den Rang §c" + name + " §7erstellt"));
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix() +
                                                "Der Rang §c" + name + " §7existiert §cschon"));
                                    }
                                } else {
                                    sendHelp(commandSender);
                                }
                            } else if (args.length == 4) {
                                String permission = args[3];
                                if (arg3.equalsIgnoreCase("add")) {
                                    if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                                        PermAPI.getInstance().getRankPermissions().addPermission(name, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                + "Du hast §c" + name
                                                + " §7die Permission " + "§c" + permission + " §7gegeben"));
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                + "Der Rang §c" + name + " §7existiert §cnicht"));
                                    }
                                } else if (arg3.equalsIgnoreCase("remove")) {
                                    if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                                        PermAPI.getInstance().getRankPermissions().removePermission(name, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                + "Du hast §c" + name +
                                                " §7die Permission " + "§c" + permission + " §7entfernt"));
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix()
                                                + "Der Rang §c" + name + " §7existiert §cnicht"));
                                    }
                                } else {
                                    sendHelp(commandSender);
                                }
                            } else {
                                sendHelp(commandSender);
                            }
                        } else {
                            sendHelp(commandSender);
                        }
                    }
                } else {
                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getNoPerm()));
                }
            } else {
                if (args.length >= 4) {
                    String arg1 = args[0];
                    if (arg1.equalsIgnoreCase("user")) {
                        UUID uuid = UUID.fromString(args[1]);
                        if (uuid != null) {
                            String arg2 = args[2];
                            if (arg2.equalsIgnoreCase("set")) {
                                String rank = args[3];
                                if (PermAPI.getInstance().getRanks().isRankExists(rank)) {
                                    long time = Long.parseLong(args[4]);
                                    PermAPI.getInstance().setGroup(uuid, rank, time);
                                    commandSender.sendMessage(EnderAPI.getInstance().getPrefix() +
                                            "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                            + " §7die Gruppe " + "§c" + rank + " §7gegeben");
                                }
                            } else if (arg2.equalsIgnoreCase("add")) {
                                String permission = args[3];
                                if (args.length == 4) {
                                    PermAPI.getInstance().getUserPermissions().addPermission(uuid, permission);
                                    commandSender.sendMessage(EnderAPI.getInstance().getPrefix() +
                                            "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                            + " §7die Permission " + "§c" + permission + " §7gegeben");
                                }
                            }
                        }
                    }
                } else {
                    sendHelp(commandSender);
                }
            }
    }

    private void sendHelp (CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefix() + "§7Permissionverwaltung §8[§e1§8/§e1§8]"));
            sender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm user <name> add/remove <permission>"));
            sender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm user <name> set <rank>"));
            sender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm user <name>"));
            sender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm group <name>"));
            sender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm group <name> create"));
            sender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm group <name> add/remove <permission>"));
        } else {
            commandSender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getConsolePrefix()
                    + "§7Permissionverwaltung §8[§e1§8/§e1§8]"));
            commandSender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm user <name> add <permission>"));
            commandSender.sendMessage(EnderAPI.getInstance().getMessage("§e/perm user <uuid> set <rank> <millis>"));
        }
    }
}
