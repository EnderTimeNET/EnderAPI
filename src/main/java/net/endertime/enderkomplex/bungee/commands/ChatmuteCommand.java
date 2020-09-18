package net.endertime.enderkomplex.bungee.commands;

import java.util.ArrayList;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ChatmuteCommand extends Command implements TabExecutor {

    public ChatmuteCommand(String name) {
        super(name);
    }

    public static ArrayList<String> isMuted = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.chatmute")) {
            if(args.length == 0) {
                if(isMuted.contains(pp.getServer().getInfo().getName())) {
                    isMuted.remove(pp.getServer().getInfo().getName());
                    pp.getServer().getInfo().getPlayers().forEach(all -> {
                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7wurde von §c" + pp.getName() + "§7 freigegeben§8!");
                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                    });
                    ProxyServer.getInstance().getPlayers().forEach(online -> {
                        if(!online.getUniqueId().toString().equals(pp.getUniqueId().toString())) {
                            if(!online.getServer().getInfo().getName().equals(pp.getServer().getInfo().getName())) {
                                if(online.hasPermission("teamserver.join")) {
                                    if(Database.existsInNotifySettings(online.getUniqueId())) {
                                        if(Database.getNotifySetting(online.getUniqueId(), NotifyType.BANSYSTEM)) {
                                            ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 freigegeben§8!");
                                            ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                        }
                                    } else {
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 freigegeben§8!");
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    isMuted.add(pp.getServer().getInfo().getName());
                    pp.getServer().getInfo().getPlayers().forEach(all -> {
                        ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7wurde von §c" + pp.getName() + "§7 gemutet§8!");
                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                    });
                    ProxyServer.getInstance().getPlayers().forEach(online -> {
                        if(!online.getUniqueId().toString().equals(pp.getUniqueId().toString())) {
                            if(!online.getServer().getInfo().getName().equals(pp.getServer().getInfo().getName())) {
                                if(online.hasPermission("teamserver.join")) {
                                    if(Database.existsInNotifySettings(online.getUniqueId())) {
                                        if(Database.getNotifySetting(online.getUniqueId(), NotifyType.BANSYSTEM)) {
                                            ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 gemutet§8!");
                                            ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                        }
                                    } else {
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 gemutet§8!");
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                }
                            }
                        }
                    });
                }
            } else if(args.length == 1) {
                String servername = args[0];
                if(ProxyServer.getInstance().getServers().containsKey(servername)) {
                    if(isMuted.contains(servername)) {
                        isMuted.remove(servername);
                        ProxyServer.getInstance().getServerInfo(servername).getPlayers().forEach(all -> {
                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7wurde von §c" + pp.getName() + "§7 freigegeben§8!");
                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                        });
                        ProxyServer.getInstance().getPlayers().forEach(online -> {
                            if(online.hasPermission("teamserver.join")) {
                                if(Database.existsInNotifySettings(online.getUniqueId())) {
                                    if(Database.getNotifySetting(online.getUniqueId(), NotifyType.BANSYSTEM)) {
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 freigegeben§8!");
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                } else {
                                    ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 freigegeben§8!");
                                    ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                }
                            }
                        });
                    } else {
                        isMuted.add(servername);
                        ProxyServer.getInstance().getServerInfo(servername).getPlayers().forEach(all -> {
                            ProxyHandler.sendPluginMessage(all, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7wurde von §c" + pp.getName() + "§7 gemutet§8!");
                            ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                        });
                        ProxyServer.getInstance().getPlayers().forEach(online -> {
                            if(online.hasPermission("teamserver.join")) {
                                if(Database.existsInNotifySettings(online.getUniqueId())) {
                                    if(Database.getNotifySetting(online.getUniqueId(), NotifyType.BANSYSTEM)) {
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 gemutet§8!");
                                        ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                    }
                                } else {
                                    ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der §6Serverchat §7von §3" + pp.getServer().getInfo().getName() + "§7wurde von §c" + pp.getName() + "§7 gemutet§8!");
                                    ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                                }
                            }
                        });
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Server ist aktuell §cnicht §7online!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cchatmute §8<§cserver§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        ArrayList<String> matches = new ArrayList<>();
        matches.clear();

        if(p.hasPermission("ek.commands.chatmute")) {
            if(args.length == 1) {
                ProxyServer.getInstance().getServers().keySet().forEach(server -> {
                    matches.add(server);
                });
            }
        }
        return matches;
    }

}
