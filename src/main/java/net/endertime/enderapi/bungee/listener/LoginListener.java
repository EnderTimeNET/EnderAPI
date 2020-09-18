package net.endertime.enderapi.bungee.listener;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import net.endertime.enderapi.bungee.utils.Version;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

    @EventHandler
    public void onPreLogin (final LoginEvent event) {
        ChannelMessage.builder()
                .channel("enderapi")
                .message("version")
                .json(JsonDocument.newDocument().append("uuid", event.getConnection().getUniqueId().toString())
                        .append("versions", event.getConnection().getVersion()))
                .targetAll()
                .build()
                .send();
        new Version(event.getConnection().getUniqueId(), event.getConnection().getVersion());
    }
}
