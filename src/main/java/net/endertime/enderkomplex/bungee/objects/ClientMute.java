package net.endertime.enderkomplex.bungee.objects;

import java.util.UUID;

import net.endertime.enderkomplex.bungee.enums.MuteReason;
import net.endertime.enderkomplex.bungee.utils.ChatListener;
import net.endertime.enderkomplex.bungee.utils.InfoCollector;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClientMute {

    String muteID, ip, chatlogid = "null";
    UUID executor, target;
    MuteReason reason;
    int muteamount;
    long duration, timestamp = System.currentTimeMillis();
    boolean chatfilter;

    public ClientMute(ProxiedPlayer executor, MuteReason reason, UUID targetUUID, boolean chatfilter) {
        this.executor = executor.getUniqueId();
        this.target = targetUUID;
        this.reason = reason;
        this.muteamount = Database.getMuteAmount(targetUUID) +1;
        this.duration = reason.getDuration();
        this.chatfilter = chatfilter;
        ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(targetUUID);
        this.muteID = "#EM" + System.currentTimeMillis();
        if(pp != null) {
            this.ip = pp.getAddress().getAddress().toString();
            if(ChatListener.chathistory.containsKey(pp)) {
                this.chatlogid = new Chatlog(executor, pp).getLogID();
            }
        } else {
            if(Database.isIpExistAlts(targetUUID)) {
                this.ip = Database.getIP(targetUUID);
            } else {
                this.ip = null;
            }
        }
        Database.saveMute(this);
        InfoCollector.mutes++;
    }

    public String getID() {
        return this.muteID;
    }

    public String getTargetIP() {
        return this.ip;
    }

    public UUID getExecutor() {
        return this.executor;
    }

    public UUID getTarget() {
        return this.target;
    }

    public MuteReason getReason() {
        return this.reason;
    }

    public int getMuteAmount() {
        return this.muteamount;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isFromChatfilter() {
        return this.chatfilter;
    }

    public String getChatlogID() {
        return this.chatlogid;
    }

}
