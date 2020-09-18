package net.endertime.enderkomplex.bungee.utils;

import java.util.UUID;

import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class OnlineTime implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    public void onLogin(LoginEvent e) {
        if(!e.isCancelled()) {
            UUID uuid = e.getConnection().getUniqueId();

            if(!Database.isUserExistTime(uuid)) {
                Database.createUserTime(uuid);
            } else {
                Database.setLastSave(uuid);
            }
        }
    }

    @EventHandler
    public void onConnect(ServerConnectedEvent e) {

        Database.saveTime(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {

        Database.saveTime(e.getPlayer().getUniqueId());
    }

}
