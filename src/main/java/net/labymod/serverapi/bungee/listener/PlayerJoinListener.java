package net.labymod.serverapi.bungee.listener;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent event) {
        EnderAPI.getInstance().getPlugin().sendPermissions(event.getPlayer());
    }
}
