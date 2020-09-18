package net.endertime.enderkomplex.bungee.objects;

import net.endertime.enderkomplex.bungee.enums.BanReason;
import net.endertime.enderkomplex.bungee.enums.UnbanReason;

import java.util.UUID;

public class BanInfo {

    String banedname, banedip, banid;
    UUID baneduuid, banexecutor, unbanuuid;
    int banamount;
    boolean isActive;
    long bantimestamp, banduration, unbantimestamp;
    BanReason banreason;
    UnbanReason unbanreason;

    public BanInfo(String banedname, UUID baneduuid, String banedip, BanReason banreason, UUID banexecutor,
                   int banamount, String banid, long bantimestamp, long banduration, String unbanreason, String unbanuuid, long unbantimestamp, boolean isActive) {
        this.banedname = banedname;
        this.baneduuid = baneduuid;
        this.banedip= banedip;
        this.banreason = banreason;
        this.banexecutor = banexecutor;
        this.banamount = banamount;
        this.banid = banid;
        this.bantimestamp = bantimestamp;
        this.banduration = banduration;
        if(!unbanreason.equals("")) this.unbanreason = UnbanReason.valueOf(unbanreason);
        if(!unbanuuid.equals("")) this.unbanuuid = UUID.fromString(unbanuuid);
        this.unbantimestamp = unbantimestamp;
        this.isActive = isActive;
    }

    public String getID() {
        return this.banid;
    }

    public String getBanedIP() {
        return this.banedip;
    }

    public String getBanedName() {
        return this.banedname;
    }

    public UUID getBanedUUID() {
        return this.baneduuid;
    }

    public UUID getBanExecutor() {
        return this.banexecutor;
    }

    public UUID getUnbanUUID() {
        return this.unbanuuid;
    }

    public int getBanAmount() {
        return this.banamount;
    }

    public long getBanTimestamp() {
        return this.bantimestamp;
    }

    public long getBanDuration() {
        return this.banduration;
    }

    public long getUnbanTimestamp() {
        return this.unbantimestamp;
    }

    public BanReason getBanReason() {
        return this.banreason;
    }

    public UnbanReason getUnbanReason() {
        return this.unbanreason;
    }

    public boolean isActive() {
        return this.isActive;
    }

}