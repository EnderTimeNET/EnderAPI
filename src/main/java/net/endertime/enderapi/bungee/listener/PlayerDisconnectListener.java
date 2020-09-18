package net.endertime.enderapi.bungee.listener;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.PlayerParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (EnderAPI.getInstance().getPartyManager().getParty(player) != null) {
            PlayerParty party = EnderAPI.getInstance().getPartyManager().getParty(player);
            if (party.isLeader(player)) {
                EnderAPI.getInstance().getPartyManager().deleteParty(player);
            } else {
                if (EnderAPI.getInstance().getPartyManager().removePlayer(party, player)) {
                    for (ProxiedPlayer member : party.getMembers()) {
                        member.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty()
                                + EnderAPI.getInstance().getPrefix(player) + player.getName()
                                + " §7hat die Party §cverlassen"));
                    }

                    party.getLeader().sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty()
                            + EnderAPI.getInstance().getPrefix(player) + player.getName()
                            + " §7hat die Party §cverlassen"));

                    EnderAPI.getInstance().getPartyManager().start(party);
                }
            }
        }
    }
}
