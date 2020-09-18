package net.endertime.enderapi.bungee.listener;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.clan.ClanAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        EnderAPI.getInstance().getFriend().getSettings().createUser(player.getUniqueId(), player.getServer().getInfo().getName());

        if (EnderAPI.getInstance().getNickDatabase().isUserExist(player.getUniqueId())) {
            String nickName = EnderAPI.getInstance().getNickDatabase().getName(player.getUniqueId());
            if (EnderAPI.getInstance().getTeamDatabase().isNicked(nickName)) {
                UUID uuidNick = EnderAPI.getInstance().getTeamDatabase().getUUIDFromNickedName(nickName);
                ProxiedPlayer nicked = ProxyServer.getInstance().getPlayer(uuidNick);
                if (nicked != null) {
                    if (EnderAPI.getInstance().getTeamDatabase().isState(uuidNick)) {
                        ChannelMessage.builder()
                                .channel("enderapi")
                                .message("nicksystem")
                                .json(JsonDocument.newDocument().append("uuid", nicked.getUniqueId().toString()))
                                .targetService(nicked.getServer().getInfo().getName())
                                .build()
                                .send();
                    }
                }


                EnderAPI.getInstance().getTeamDatabase().updateRandom(uuidNick, true);
                String nickedName = EnderAPI.getInstance().getTeamDatabase().getNickedName(uuidNick);
                EnderAPI.getInstance().getNickDatabase().updateState(EnderAPI.getInstance().getNickDatabase().getUUID(nickedName), false);
                EnderAPI.getInstance().getTeamDatabase().updateNickedName(uuidNick, "");
            }

            EnderAPI.getInstance().getNickDatabase().deleteNick(player.getUniqueId());
        }

        ClanAPI.getInstance().getSettings().createUser(player.getUniqueId(), player.getName());
    }
}
