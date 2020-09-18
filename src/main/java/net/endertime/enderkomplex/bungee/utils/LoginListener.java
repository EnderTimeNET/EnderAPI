package net.endertime.enderkomplex.bungee.utils;

import java.net.InetAddress;
import java.util.UUID;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginListener implements Listener {

    @EventHandler (priority = EventPriority.HIGH)
    public void onLogin(LoginEvent e) {
        if(!e.isCancelled()) {
            if (e.getConnection().getVersion() > 340 | e.getConnection().getVersion() < 47) {
                e.setCancelled(true);
                e.setCancelReason(TextComponent.fromLegacyText("§7Deine Client-Version ist §cnicht §7zugelassen!"));
                return;
            }
            InetAddress ip = e.getConnection().getAddress().getAddress();
            UUID uuid = e.getConnection().getUniqueId();
            Database.updateLastSeen(uuid);
            Database.updateIP(uuid, ip);
            if(Database.isEverBeenBanned(uuid)) {
                if(Database.hasActiveBan(uuid)) {
                    long duration = Database.getActiveBanDuration(uuid);
                    if(duration > 0) {
                        if((Database.getActiveBanTimestamp(uuid) + duration) >= System.currentTimeMillis()) {
                            e.setCancelReason(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                    + "\n"
                                    + "§7Grund§8: §c" + Database.getActiveBanReason(uuid).getTitle() + "\n"
                                    + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(uuid) + "\n"
                                    + "\n"
                                    + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen " +
                                    "Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                            e.setCancelled(true);
                            return;
                        } else {
                            Database.unbanPlayerExpired(uuid);
                        }
                    } else {
                        e.setCancelReason(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                + "\n"
                                + "§7Grund§8: §c" + Database.getActiveBanReason(uuid).getTitle() + "\n"
                                + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(uuid) + "\n"
                                + "\n"
                                + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen " +
                                "Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                        e.setCancelled(true);
                        return;
                    }
                } else {
                    if(Database.hasActiveIpBan(ip.toString())) {
                        UUID bannedUUID = Database.getBanedUUID(ip);
                        if(Database.getBanReason(bannedUUID).isIpBan()) {
                            e.setCancelReason(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                    + "\n"
                                    + "§7Grund§8: §c" + Database.getActiveBanReason(bannedUUID).getTitle() + "\n"
                                    + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(bannedUUID) + "\n"
                                    + "\n"
                                    + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen " +
                                    "Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            } else {
                if(Database.hasActiveIpBan(ip.toString())) {
                    UUID bannedUUID = Database.getBanedUUID(ip);
                    if(Database.getBanReason(bannedUUID).isIpBan()) {
                        e.setCancelReason(TextComponent.fromLegacyText("§7§lDu wurdest vom §5§lEnderTime-Netzwerk §7§lverbannt!\n"
                                + "\n"
                                + "§7Grund§8: §c" + Database.getActiveBanReason(bannedUUID).getTitle() + "\n"
                                + "§7Dauer§8: §c" + ProxyHandler.getBanEnd(bannedUUID) + "\n"
                                + "\n"
                                + "§7Falls du diesen Bann ungerechtfertigt finden solltest§8, §7kannst du einen " +
                                "Entbannungsantrag in unserem Forum unter §3https://forum.endertime.net/ §7stellen"));
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLogin(ServerConnectEvent e) {
        if(e.getReason().equals(Reason.JOIN_PROXY)) {
            Database.createUserJoinme(e.getPlayer().getUniqueId());
            if(ProxyServer.getInstance().getOnlineCount() >= 1) {
                InetAddress ip = e.getPlayer().getAddress().getAddress();
                ProxyServer.getInstance().getPlayers().forEach(all -> {
                    if(all.getAddress().getAddress().toString().equals(ip.toString())) {
                        if(!all.getUniqueId().toString().equals(e.getPlayer().getUniqueId().toString())) {
                            ProxyServer.getInstance().getPlayers().forEach(team -> {
                                if(team.hasPermission("teamserver.join")) {
                                    if(Database.existsInNotifySettings(team.getUniqueId())) {
                                        if(Database.getNotifySetting(team.getUniqueId(), NotifyType.CONNECTION)) {
                                            ProxyHandler.sendPluginMessage(team, PluginMessage.SEND_ACTIONBAR,
                                                    "§4§l⚠ §7§lVerbindungsgleichheit§8§l: §c§l" + all.getName() +
                                                            " §8§l& §c§l" + e.getPlayer().getName() + " §4§l⚠");
                                        }
                                    } else {
                                        ProxyHandler.sendPluginMessage(team, PluginMessage.SEND_ACTIONBAR,
                                                "§4§l⚠ §7§lVerbindungsgleichheit§8§l: §c§l" + all.getName() +
                                                        " §8§l& §c§l" + e.getPlayer().getName() + " §4§l⚠");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

}
