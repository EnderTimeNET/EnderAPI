package net.endertime.enderkomplex.bungee.objects;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatMessage {

    long timestamp;
    String servername, sender, text;

    public ChatMessage(String text, ProxiedPlayer sender) {
        this.text = text;
        this.timestamp = System.currentTimeMillis();
        this.sender = sender.getName();
        this.servername = sender.getServer().getInfo().getName();
    }

    public String getMessage() {
        return this.text;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getSender() {
        return this.sender;
    }

    public String getServerName() {
        return this.servername;
    }

}
