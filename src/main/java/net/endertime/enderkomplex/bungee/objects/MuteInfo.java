package net.endertime.enderkomplex.bungee.objects;

import net.endertime.enderkomplex.bungee.enums.MuteReason;
import net.endertime.enderkomplex.bungee.enums.UnmuteReason;

import java.util.UUID;


public class MuteInfo {

    String mutedname, mutedip, muteid, chatlogid;
    UUID muteduuid, muteexecutor, unmuteuuid;
    boolean isActive;
    int muteamount;
    long mutetimestamp, muteduration, unmutetimestamp;
    MuteReason mutereason;
    UnmuteReason unmutereason;

    public MuteInfo(String mutedname, UUID muteduuid, String mutedip, MuteReason mutereason, UUID muteexecutor,
                    int muteamount, String muteid, long mutetimestamp, long muteduration, String unmutereason,
                    String unmuteuuid, long unmutetimestamp, String chatlogid, boolean isActive) {
        this.mutedname = mutedname;
        this.muteduuid = muteduuid;
        this.mutedip= mutedip;
        this.mutereason = mutereason;
        this.muteexecutor = muteexecutor;
        this.muteamount = muteamount;
        this.muteid = muteid;
        this.mutetimestamp = mutetimestamp;
        this.muteduration = muteduration;
        if(!unmutereason.equals("")) this.unmutereason = UnmuteReason.valueOf(unmutereason);
        if(!unmuteuuid.equals("")) this.unmuteuuid = UUID.fromString(unmuteuuid);
        this.unmutetimestamp = unmutetimestamp;
        this.chatlogid = chatlogid;
        this.isActive = isActive;
    }

    public String getID() {
        return this.muteid;
    }

    public String getMutedIP() {
        return this.mutedip;
    }

    public String getChatlogID() {
        return this.chatlogid;
    }

    public String getMutedName() {
        return this.mutedname;
    }

    public UUID getMutedUUID() {
        return this.muteduuid;
    }

    public UUID getMuteExecutor() {
        return this.muteexecutor;
    }

    public UUID getUnmuteUUID() {
        return this.unmuteuuid;
    }

    public int getMuteAmount() {
        return this.muteamount;
    }

    public long getMuteTimestamp() {
        return this.mutetimestamp;
    }

    public long getMuteDuration() {
        return this.muteduration;
    }

    public long getUnmuteTimestamp() {
        return this.unmutetimestamp;
    }

    public MuteReason getMuteReason() {
        return this.mutereason;
    }

    public UnmuteReason getUnmuteReason() {
        return this.unmutereason;
    }

    public boolean isActive() {
        return this.isActive;
    }

}
