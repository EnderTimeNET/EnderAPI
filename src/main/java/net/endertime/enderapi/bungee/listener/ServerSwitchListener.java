package net.endertime.enderapi.bungee.listener;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.PlayerParty;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        EnderAPI.getInstance().getFriend().getSettings().updateServer(player.getUniqueId(), player.getServer().getInfo().getName());

        final PlayerParty party;
        if ((EnderAPI.getInstance().getPartyManager().getParty(player) != null)
                && ((party = EnderAPI.getInstance().getPartyManager().getParty(player)).isLeader(player))) {
            if ((party.getLeader().getServer().getInfo().getName().contains("Lobby")))
                return;
            if ((party.getLeader().getServer().getInfo().getName().startsWith("Vorbau")))
                return;
            party.getLeader().sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixParty() + "§7Die Party betritt den Server "
                    + EnderAPI.getInstance().getPrefix(party.getLeader()) + party.getLeader().getServer().getInfo().getName()));
            for (final ProxiedPlayer all : party.getMembers()) {
                all.sendMessage(EnderAPI.getInstance().getMessage(
                        EnderAPI.getInstance().getPrefixParty() + "§7Die Party betritt den Server "
                                + EnderAPI.getInstance().getPrefix(party.getLeader())
                                + party.getLeader().getServer().getInfo().getName()));
                all.connect(party.getLeader().getServer().getInfo());
            }
        }
    }
}
