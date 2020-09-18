package net.endertime.enderkomplex.bungee.utils;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinSpammer implements Listener {

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        if (e.getReason().equals(Reason.JOIN_PROXY)) {
            for (int i = 0; i < 250; i++) {
                e.getPlayer().sendMessage(TextComponent.fromLegacyText("§c "));
            }
        }
    }

}
