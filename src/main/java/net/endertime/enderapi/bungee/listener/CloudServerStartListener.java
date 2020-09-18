package net.endertime.enderapi.bungee.listener;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStartEvent;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.PlayerParty;
import net.endertime.enderapi.bungee.utils.Version;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class CloudServerStartListener {

    @EventListener
    public void onStart (CloudServiceStartEvent event) {
        for (PlayerParty party : EnderAPI.getInstance().getPartyManager().getParties()) {
            ChannelMessage.builder()
                    .channel("enderapi")
                    .message("party")
                    .json(JsonDocument.newDocument().append("command", "create").append("leader", party.getLeader().getUniqueId().toString())
                            .append("publicParty", party.isPublicParty()))
                    .targetService(event.getServiceInfo().getServiceId().getName())
                    .build()
                    .send();
            for (ProxiedPlayer proxiedPlayer : party.getMembers()) {
                ChannelMessage.builder()
                        .channel("enderapi")
                        .message("party")
                        .json(JsonDocument.newDocument().append("command", "add").append("leader", party.getLeader().getUniqueId().toString())
                                .append("uuid", proxiedPlayer.getUniqueId().toString()))
                        .targetService(event.getServiceInfo().getServiceId().getName())
                        .build()
                        .send();
            }
        }

        for (UUID uuid : Version.versions.keySet()) {
            ChannelMessage.builder()
                    .channel("enderapi")
                    .message("version")
                    .json(JsonDocument.newDocument().append("uuid", uuid.toString())
                            .append("versions", Version.versions.get(uuid).getVersion()))
                    .targetService(event.getServiceInfo().getServiceId().getName())
                    .build()
                    .send();
        }
    }
}
