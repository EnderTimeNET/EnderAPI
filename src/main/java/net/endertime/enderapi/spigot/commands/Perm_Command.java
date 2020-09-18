package net.endertime.enderapi.spigot.commands;

import net.endertime.enderapi.spigot.api.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Perm_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("perm")) {
            if (commandSender instanceof Player) {
                Player sender = (Player) commandSender;
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
                                    sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Permission von §c" + EnderAPI.getInstance().getName(uuid));

                                    for (String s : PermAPI.getInstance().getUserPermissions().getPerms(uuid))
                                        sender.sendMessage("§8-> §7" + s);
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§c" + name + " §7war noch §cnie §7auf dem Netzwerk");
                                }
                            } else if ( arg1.equalsIgnoreCase("group")) {
                                if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                                    sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Permission von §c" + name);

                                    for (String s : PermAPI.getInstance().getRankPermissions().getPerms(name))
                                        sender.sendMessage("§8-> §7" + s);

                                    sender.sendMessage("");
                                    sender.sendMessage("");
                                    sender.sendMessage("");
                                    sender.sendMessage("");

                                    for (String s : PermAPI.getInstance().getRankPermissions().getPermsWithOut(name))
                                        sender.sendMessage("§8-> §7" + s);
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Der Rang §c" + name + " §7existiert §cnicht");
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
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                                + " §7die Permission " + "§c" + permission + " §7gegeben");
                                    } else {
                                        sendHelp(commandSender);
                                    }
                                } else if (arg2.equalsIgnoreCase("remove")) {
                                    String permission = args[3];
                                    if (args.length == 4) {
                                        PermAPI.getInstance().getUserPermissions().removePermission(uuid, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + EnderAPI.getInstance().getName(uuid) +
                                                " §7die Permission " + "§c" + permission + " §7entfernt");
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
                                            sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                                    + " §7die Gruppe " + "§c" + rank + " §7gegeben");
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
                                sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§c" + name + " §7war noch §cnie §7auf dem Netzwerk");
                            }
                        } else if (arg1.equalsIgnoreCase("group")) {
                            String arg3 = args[2];
                            if (args.length == 3) {
                                if (arg3.equalsIgnoreCase("create")) {
                                    if (!PermAPI.getInstance().getRanks().isRankExists(name)) {
                                        PermAPI.getInstance().getRanks().createRank(name, "", "", "", "", "");
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Du hast den Rang §c" + name + " §7erstellt");
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Der Rang §c" + name + " §7existiert §cschon");
                                    }
                                } else {
                                    sendHelp(commandSender);
                                }
                            } else if (args.length == 4) {
                                String permission = args[3];
                                if (arg3.equalsIgnoreCase("add")) {
                                    if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                                        PermAPI.getInstance().getRankPermissions().addPermission(name, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + name
                                                + " §7die Permission " + "§c" + permission + " §7gegeben");
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Der Rang §c" + name + " §7existiert §cnicht");
                                    }
                                } else if (arg3.equalsIgnoreCase("remove")) {
                                    if (PermAPI.getInstance().getRanks().isRankExists(name)) {
                                        PermAPI.getInstance().getRankPermissions().removePermission(name, permission);
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + name +
                                                " §7die Permission " + "§c" + permission + " §7entfernt");
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getPrefix() + "Der Rang §c" + name + " §7existiert §cnicht");
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
                    sender.sendMessage(EnderAPI.getInstance().getNoPerm());
                }
            } else {
                if (args.length >= 4) {
                    String arg1 = args[0];
                    String name = args[1];
                    if (arg1.equalsIgnoreCase("user")) {
                        UUID uuid = EnderAPI.getInstance().getUUID(name);
                        if (uuid != null) {
                            String arg2 = args[2];
                            if (arg2.equalsIgnoreCase("set")) {
                                String rank = args[3];
                                if (PermAPI.getInstance().getRanks().isRankExists(rank)) {
                                    long time = Long.parseLong(args[4]);
                                    PermAPI.getInstance().setGroup(uuid, rank, time);
                                    commandSender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                            + " §7die Gruppe " + "§c" + rank + " §7gegeben");
                                }
                            } else if (arg2.equalsIgnoreCase("add")) {
                                String permission = args[3];
                                if (args.length == 4) {
                                    PermAPI.getInstance().getUserPermissions().addPermission(uuid, permission);
                                    commandSender.sendMessage(EnderAPI.getInstance().getPrefix() + "Du hast §c" + EnderAPI.getInstance().getName(uuid)
                                            + " §7die Permission " + "§c" + permission + " §7gegeben");
                                }
                            }
                        } else {
                            commandSender.sendMessage(EnderAPI.getInstance().getPrefix() + "§c" + name + " §7war noch §cnie §7auf dem Netzwerk");
                        }
                    }
                } else {
                    sendHelp(commandSender);
                }
            }
        } else {
            commandSender.sendMessage(EnderAPI.getInstance().getNoPerm());
        }
        return false;
    }

    private void sendHelp (CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            sender.sendMessage(EnderAPI.getInstance().getPrefix() + "§7Permissionverwaltung §8[§e1§8/§e1§8]");
            sender.sendMessage("§e/perm user <name> add/remove <permission>");
            sender.sendMessage("§e/perm user <name> set <rank>");
            sender.sendMessage("§e/perm user <name>");
            sender.sendMessage("§e/perm group <name>");
            sender.sendMessage("§e/perm group <name> create");
            sender.sendMessage("§e/perm group <name> add/remove <permission>");
        } else {
            commandSender.sendMessage(EnderAPI.getInstance().getConsolePrefix() + "§7Permissionverwaltung §8[§e1§8/§e1§8]");
            commandSender.sendMessage("§e/perm user <name> add <permission>");
            commandSender.sendMessage("§e/perm user <name> set <rank> <millis>");
        }
    }
}
