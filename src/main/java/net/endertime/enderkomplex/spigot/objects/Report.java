package net.endertime.enderkomplex.spigot.objects;

import java.util.UUID;

import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.enums.ReportReason;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.ServerHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_12_R1.EntityPlayer;

public class Report {

    String senderuuid;
    Player reported;
    ReportReason reason;
    ReportStatus status = ReportStatus.OPEN;
    String servername, reportid, signature = "", value = "";
    long timestamp = System.currentTimeMillis();

    public Report(Player reported, String senderuuid, ReportReason reason) {
        this.reported = reported;
        this.senderuuid = senderuuid;
        this.reason = reason;
        this.servername = Bukkit.getServerName();
        if(reason.equals(ReportReason.SKIN)) {
            EntityPlayer playerNMS = ((CraftPlayer) reported).getHandle();
            GameProfile profile = playerNMS.getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            this.signature = property.getSignature();
            this.value = property.getValue();
        }
        String reportid = "#ER" + System.currentTimeMillis();
        this.reportid = reportid;
        Database.saveReport(this);
        if(!senderuuid.equalsIgnoreCase("AntiCheat")) {
            ServerHandler.sendPluginMessage(Bukkit.getPlayer(UUID.fromString(senderuuid)),
                    PluginMessage.SEND_REPORT_NOTIFY, reported.getUniqueId().toString(), this.reportid, this.reason.toString());
        }
    }

    public String getSenderUUID() {
        return this.senderuuid;
    }

    public Player getReported() {
        return this.reported;
    }

    public ReportReason getReason() {
        return this.reason;
    }

    public String getID() {
        return this.reportid;
    }

    public String getGameProfile() {
        return this.reportid;
    }

    public String getSkinValue() {
        return this.value;
    }

    public String getSkinSignature() {
        return this.signature;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public ReportStatus getStatus() {
        return this.status;
    }
}
