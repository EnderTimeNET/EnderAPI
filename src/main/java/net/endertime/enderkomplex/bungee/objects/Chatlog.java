package net.endertime.enderkomplex.bungee.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.utils.ChatListener;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Chatlog {

    String logID, creatorname, loggedname;
    UUID creator, target;
    Date date;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    public Chatlog(ProxiedPlayer pp, ProxiedPlayer target) {
        this.logID = ProxyHandler.randomAlphaNumeric(6);
        if(ChatListener.chathistory.containsKey(pp)) this.messages.addAll(ChatListener.chathistory.get(pp));
        if(ChatListener.chathistory.containsKey(target)) this.messages.addAll(ChatListener.chathistory.get(target));
        this.creator = pp.getUniqueId();
        this.date = new Date();
        this.target = target.getUniqueId();
        this.loggedname = target.getName();
        this.creatorname = pp.getName();
        Database.saveChatlog(this);
    }

    public String getLogID() {
        return this.logID;
    }

    public UUID getCreatorUUID() {
        return this.creator;
    }

    public String getCreatorName() {
        return this.creatorname;
    }

    public UUID getTargetUUID() {
        return this.target;
    }

    public String getTargetName() {
        return this.loggedname;
    }

    public ArrayList<ChatMessage> getMessages() {
        return this.messages;
    }

    public Date getTimestamp() {
        return this.date;
    }

    public String getLink() {
        return ProxyData.ChatlogLink + this.logID;
    }

}
