package net.endertime.enderkomplex.spigot.objects;

import net.endertime.enderkomplex.bungee.enums.ReportReason;
import net.endertime.enderkomplex.bungee.enums.ReportStatus;

import java.util.UUID;

public class ReportInfo {

    String reportid, reportedname, signature, value, chatlogid, reporter;
    UUID reported, doneby;
    ReportReason reason;
    long reporttimestamp, donetimestamp;
    ReportStatus status;

    public ReportInfo(String reportid, String reportedname, UUID reporteduuid, ReportReason reason, long reporttime,
                      String reporteruuid, ReportStatus status, String doneby, long donetime, String signature, String value, String chatlogid) {
        this.reportid = reportid;
        this.reportedname = reportedname;
        this.reported = reporteduuid;
        this.reason = reason;
        this.reporttimestamp = reporttime;
        this.reporter = reporteruuid;
        this.status = status;
        if(doneby != null) {
            if(!doneby.equals("")) {
                this.doneby = UUID.fromString(doneby);
            }
        }
        this.donetimestamp = donetime;
        this.signature = signature;
        this.value = value;
        this.chatlogid = chatlogid;
    }

    public String getID() {
        return this.reportid;
    }

    public String getReportedName() {
        return this.reportedname;
    }

    public String getSignature() {
        return this.reportid;
    }

    public String getValue() {
        return this.reportid;
    }

    public UUID getReported() {
        return this.reported;
    }

    public String getReporter() {
        return this.reporter;
    }

    public UUID getDoneBy() {
        return this.doneby;
    }

    public ReportReason getReason() {
        return this.reason;
    }

    public long getReportTimestamp() {
        return this.reporttimestamp;
    }

    public long getDoneTimestamp() {
        return this.donetimestamp;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public String getChatlogID() {
        return this.chatlogid;
    }
}