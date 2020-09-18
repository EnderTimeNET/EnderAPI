package net.endertime.enderkomplex.bungee.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.endertime.enderkomplex.bungee.commands.ChatmuteCommand;
import net.endertime.enderkomplex.bungee.container.ChatBlacklist;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.ChatType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.objects.ChatMessage;
import net.endertime.enderkomplex.bungee.objects.ChatNotify;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {

    public static HashMap<ProxiedPlayer, String> lastMessage = new HashMap<>();
    public static HashMap<ProxiedPlayer, List<ChatMessage>> chathistory = new HashMap<>();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e) {
        if(e.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer) e.getSender();

            if(!ChatmuteCommand.isMuted.contains(pp.getServer().getInfo().getName())) {
                if(!pp.hasPermission("teamserver.join")) {
                    if(!Database.hasActiveMute(pp.getUniqueId())) {
                        if(lastMessage.containsKey(pp)) {
                            if(lastMessage.get(pp).equalsIgnoreCase(e.getMessage())) {
                                if(!lastMessage.get(pp).startsWith("/")) {
                                    e.setCancelled(true);
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§cDu wiederholst dich§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                    return;
                                }
                            }
                        }
                        lastMessage.put(pp, e.getMessage());
                        if(!chathistory.containsKey(pp)) {
                            chathistory.put(pp, new ArrayList<>());
                        }
                        if(chathistory.get(pp).size() >= 10) {
                            chathistory.get(pp).remove(0);
                        }
                        if (!e.getMessage().startsWith("/")) {
                            chathistory.get(pp).add(new ChatMessage(e.getMessage(), pp));
                        }
                        String message = e.getMessage().toLowerCase();
                        for(String ad : ChatBlacklist.adblacklist) {
                            if(message.contains(ad)) {
                                for(String whitelistword : ChatBlacklist.whitelist) {
                                    if(message.contains(whitelistword)) {
                                        return;
                                    }
                                }
                                if(message.startsWith("/msg") | message.startsWith("/r")) {
                                    new ChatNotify(ad, e.getMessage(), pp, ChatType.PRIVATCHAT);
                                } else if(message.startsWith("/p")) {
                                    new ChatNotify(ad, e.getMessage(), pp, ChatType.PARTYCHAT);
                                } else if(message.startsWith("/c") || message.startsWith("/clan")) {
                                    new ChatNotify(ad, e.getMessage(), pp, ChatType.CLANCHAT);
                                } else {
                                    new ChatNotify(ad, e.getMessage(), pp, ChatType.SERVERCHAT);
                                }
                                return;
                            }
                        }
                        for(String badword : ChatBlacklist.blacklist) {
                            if(message.contains(badword)) {
                                if(message.startsWith("/msg") | message.startsWith("/r")) {
                                    new ChatNotify(badword, e.getMessage(), pp, ChatType.PRIVATCHAT);
                                } else if(message.startsWith("/p")) {
                                    new ChatNotify(badword, e.getMessage(), pp, ChatType.PARTYCHAT);
                                } else if(message.startsWith("/c")) {
                                    new ChatNotify(badword, e.getMessage(), pp, ChatType.CLANCHAT);
                                } else {
                                    new ChatNotify(badword, e.getMessage(), pp, ChatType.SERVERCHAT);
                                }
                                return;
                            }
                        }
                    } else {
                        long duration = Database.getActiveMuteDuration(pp.getUniqueId());
                        if(duration > 0) {
                            if((Database.getActiveMuteTimestamp(pp.getUniqueId()) + duration) >= System.currentTimeMillis()) {
                                if(!e.getMessage().startsWith("/")) {
                                    e.setCancelled(true);
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§cDu bist noch " +
                                            ProxyHandler.getBanEnd(Database.getActiveMuteTimestamp(pp.getUniqueId()),
                                                    Database.getActiveMuteDuration(pp.getUniqueId())) + " §cgemutet §8[§4" +
                                            Database.getActiveMuteReason(pp.getUniqueId()).getTitle() + "§8]");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                }
                            } else {
                                Database.unmutePlayerExpired(pp.getUniqueId());
                            }
                        } else {
                            if(!e.getMessage().startsWith("/")) {
                                e.setCancelled(true);
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§cDu bist §4§lPERMANENT §cgemutet §8[§4"
                                        + Database.getActiveMuteReason(pp.getUniqueId()).getTitle() + "§8]");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                            }
                        }
                    }
                }
            } else {
                if(!e.getMessage().startsWith("/")) {
                    e.setCancelled(true);
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Der Serverchat wurde vorübergehend gemutet§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            }
        }
    }

}
