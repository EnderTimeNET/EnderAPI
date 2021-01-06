package net.endertime.enderkomplex.mysql;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.endertime.enderapi.spigot.api.PermAPI;
import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.enums.*;
import net.endertime.enderkomplex.bungee.objects.*;
import net.endertime.enderkomplex.spigot.objects.Report;
import net.endertime.enderkomplex.spigot.objects.ReportInfo;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Database {

    public static MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public static boolean isEverBeenBanned(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT BANED_UUID FROM BANDATA WHERE BANED_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static boolean isEverBeenMuted(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT MUTED_UUID FROM MUTEDATA WHERE MUTED_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static boolean isEverBeenReported(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORTED_UUID FROM REPORTDATA WHERE REPORTED_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static boolean hasActiveBan(UUID banneduuid) {
        if(!isEverBeenBanned(banneduuid)) return false;
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
        ps.setString(1, banneduuid.toString());
        if(mysql.getInt(ps, "COUNT(*)") >= 1) {
            return true;
        }
        return false;
    }

    public static boolean hasActiveBan(String bannedip) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_IP = ? " +
                "AND BAN_STATUS = 1");
        ps.setString(1, bannedip);
        if(mysql.getInt(ps, "COUNT(*)") >= 1) {
            return true;
        }
        return false;
    }

    public static boolean hasActiveIpBan(String bannedip) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_IP = ? " +
                "AND BAN_STATUS = 1 AND (BAN_REASON = 'ADMIN_BAN' OR BAN_REASON = 'BANNUMGEHUNG')");
        ps.setString(1, bannedip);
        if(mysql.getInt(ps, "COUNT(*)") >= 1) {
            return true;
        }
        return false;
    }

    public static boolean isBannumgehung(String bannedip) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_IP = ? " +
                "AND BAN_STATUS = 1 AND BAN_REASON = ?");
        ps.setString(1, bannedip);
        ps.setString(2, BanReason.BANNUMGEHUNG.toString());
        if(mysql.getInt(ps, "COUNT(*)") >= 1) {
            return true;
        }
        return false;
    }

    public static boolean isAdminban(String bannedip) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_IP = ? " +
                "AND BAN_STATUS = 1 AND BAN_REASON = ?");
        ps.setString(1, bannedip);
        ps.setString(2, BanReason.ADMIN_BAN.toString());
        if(mysql.getInt(ps, "COUNT(*)") >= 1) {
            return true;
        }
        return false;
    }

    public static boolean hasActiveMute(UUID muteduuid) {
        if(!isEverBeenMuted(muteduuid)) return false;
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
        ps.setString(1, muteduuid.toString());
        if(mysql.getInt(ps, "COUNT(*)") >= 1) {
            return true;
        }
        return false;
    }

    public static boolean hasActiveReport(UUID reporteduuid) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORT_STATUS FROM REPORTDATA WHERE REPORTED_UUID = ?" +
                " AND REPORT_STATUS = 2 OR REPORT_STATUS = 1");
        ps.setString(1, reporteduuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static boolean isChatlogExist(String logID) {
        PreparedStatement ps = new PreparedStatement("SELECT LOG_ID FROM CHATLOGDATA WHERE LOG_ID = ?");
        ps.setString(1, logID);
        return mysql.isInDatabase(ps);
    }

    public static boolean isIpExistBan(UUID uuid) {
        PreparedStatement ps1 = new PreparedStatement("SELECT BANED_IP FROM BANDATA WHERE BANED_UUID = ?");
        ps1.setString(1, uuid.toString());
        if(mysql.isInDatabase(ps1) && !mysql.getString(ps1, "BANED_IP").equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isIpExistMute(UUID uuid) {
        PreparedStatement ps2 = new PreparedStatement("SELECT MUTED_IP FROM MUTEDATA WHERE MUTED_UUID = ?");
        ps2.setString(1, uuid.toString());
        if(mysql.isInDatabase(ps2) && !mysql.getString(ps2, "MUTED_IP").equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isIpExistAlts(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT IP FROM ALTDATA WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static boolean isBanIdExist(String banID) {
        PreparedStatement ps = new PreparedStatement("SELECT BAN_ID FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banID);
        return mysql.isInDatabase(ps);
    }

    public static boolean isMuteIdExist(String muteID) {
        PreparedStatement ps = new PreparedStatement("SELECT MUTE_ID FROM BANDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteID);
        return mysql.isInDatabase(ps);
    }

    public static boolean isReportIdExist(String reportID) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORT_ID FROM REPORTDATA WHERE REPORT_ID = ?");
        ps.setString(1, reportID);
        return mysql.isInDatabase(ps);
    }

    public static boolean hasEverMadeChatlog(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT CREATOR_UUID FROM CHATLOGDATA WHERE CREATOR_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static boolean hasEverMadeReport(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORTER_UUID FROM REPORTDATA WHERE REPORTER_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static int getBanAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getExcusedBanAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BANED_UUID = ? " +
                "AND UNBAN_REASON = 'FALSEBAN' OR UNBAN_REASON = 'TECH_DIFFI'");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getExcusedMuteAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTED_UUID = ? " +
                "AND UNMUTE_REASON = 'FALSEMUTE' OR UNMUTE_REASON = 'TECH_DIFFI'");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getMuteAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTED_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static String getIP(UUID uuid) {
        if(isIpExistBan(uuid)) {
            PreparedStatement ps1 = new PreparedStatement("SELECT BANED_IP FROM BANDATA WHERE BANED_UUID = ?");
            ps1.setString(1, uuid.toString());
            return mysql.getString(ps1, "BANED_IP");
        } else if(isIpExistMute(uuid)) {
            PreparedStatement ps2 = new PreparedStatement("SELECT MUTED_IP FROM MUTEDATA WHERE MUTED_UUID = ?");
            ps2.setString(1, uuid.toString());
            return mysql.getString(ps2, "MUTED_IP");
        } else if(isIpExistAlts(uuid)) {
            PreparedStatement ps3 = new PreparedStatement("SELECT IP FROM ALTDATA WHERE UUID = ?");
            ps3.setString(1, uuid.toString());
            return mysql.getString(ps3, "IP");
        }
        return null;
    }

    public static String getIPFromBan(UUID uuid) {
        if(isIpExistBan(uuid)) {
            PreparedStatement ps1 = new PreparedStatement("SELECT BANED_IP FROM BANDATA WHERE BANED_UUID = ?");
            ps1.setString(1, uuid.toString());
            return mysql.getString(ps1, "BANED_IP");
        }
        return null;
    }

    public static String getIPFromMute(UUID uuid) {
        if(isIpExistMute(uuid)) {
            PreparedStatement ps2 = new PreparedStatement("SELECT MUTED_IP FROM MUTEDATA WHERE MUTED_UUID = ?");
            ps2.setString(1, uuid.toString());
            return mysql.getString(ps2, "MUTED_IP");
        }
        return null;
    }

    public static String getIPFromAlts(UUID uuid) {
        if(isIpExistAlts(uuid)) {
            PreparedStatement ps3 = new PreparedStatement("SELECT IP FROM ALTDATA WHERE UUID = ?");
            ps3.setString(1, uuid.toString());
            return mysql.getString(ps3, "IP");
        }
        return null;
    }

    public static String getActiveBanID(UUID uuid) {
        if(isEverBeenBanned(uuid) && hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT BAN_ID FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps, "BAN_ID");
        }
        return null;
    }

    public static String getActiveMuteID(UUID uuid) {
        if(isEverBeenMuted(uuid) && hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT MUTE_ID FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps, "MUTE_ID");
        }
        return null;
    }

    public static UUID getActiveBanExecutor(UUID uuid) {
        if(isEverBeenBanned(uuid) && hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT BAN_EXECUTOR FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, uuid.toString());
            return UUID.fromString(mysql.getString(ps, "BAN_EXECUTOR"));
        }
        return null;
    }

    public static UUID getActiveMuteExecutor(UUID uuid) {
        if(isEverBeenMuted(uuid) && hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT MUTE_EXECUTOR FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setString(1, uuid.toString());
            return UUID.fromString(mysql.getString(ps, "MUTE_EXECUTOR"));
        }
        return null;
    }

    public static BanReason getActiveBanReason(UUID uuid) {
        if(isEverBeenBanned(uuid) && hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT BAN_REASON FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, uuid.toString());
            return BanReason.valueOf(mysql.getString(ps, "BAN_REASON"));
        }
        return null;
    }

    public static MuteReason getActiveMuteReason(UUID uuid) {
        if(isEverBeenMuted(uuid) && hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT MUTE_REASON FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setString(1, uuid.toString());
            return MuteReason.valueOf(mysql.getString(ps, "MUTE_REASON"));
        }
        return null;
    }

    public static long getActiveBanDuration(UUID uuid) {
        if(isEverBeenBanned(uuid) && hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT BAN_DURATION FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, uuid.toString());
            return mysql.getLong(ps, "BAN_DURATION");
        }
        return 0;
    }

    public static long getActiveMuteDuration(UUID uuid) {
        if(isEverBeenMuted(uuid) && hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT MUTE_DURATION FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setString(1, uuid.toString());
            return mysql.getLong(ps, "MUTE_DURATION");
        }
        return 0;
    }

    public static long getActiveMuteTimestamp(UUID uuid) {
        if(isEverBeenMuted(uuid) && hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT MUTE_TIMESTAMP FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setString(1, uuid.toString());
            return mysql.getLong(ps, "MUTE_TIMESTAMP");
        }
        return 0;
    }

    public static long getActiveBanTimestamp(UUID uuid) {
        if(isEverBeenBanned(uuid) && hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT BAN_TIMESTAMP FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, uuid.toString());
            return mysql.getLong(ps, "BAN_TIMESTAMP");
        }
        return 0;
    }

    public static int getAltAmount(String ip) {
        int alts = 0;
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM ALTDATA WHERE IP = ?");
        ps.setString(1, ip);
        alts += mysql.getInt(ps, "COUNT(*)");
        if(alts > 0) alts--;
        return alts;
    }

    public static ArrayList<UUID> getAlts(String ip) {
        ArrayList<UUID> alts = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM ALTDATA WHERE IP = ?");
        ps.setString(1, ip);
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                alts.add(UUID.fromString(rs.getString("UUID")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return alts;
    }

    public static Date getLastSeen(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT LASTSEEN FROM ALTDATA WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return new Date(mysql.getLong(ps, "LASTSEEN"));
    }

    public static UUID getBanedUUID(InetAddress ip) {
        PreparedStatement ps = new PreparedStatement("SELECT BANED_UUID FROM BANDATA WHERE BANED_IP = ?" +
                " AND BAN_STATUS = 1  AND (BAN_REASON = 'BANNUMGEHUNG' OR BAN_REASON = 'ADMIN_BAN') LIMIT 1");
        ps.setString(1, ip.toString());
        return UUID.fromString(mysql.getString(ps, "BANED_UUID"));
    }

    public static BanReason getBanReason(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT BAN_REASON FROM BANDATA WHERE BANED_UUID = ? AND BAN_STATUS = 1");
        ps.setString(1, uuid.toString());
        return BanReason.valueOf(mysql.getString(ps, "BAN_REASON"));
    }

    public static void updateIP(UUID uuid, InetAddress ip) {
        String ipString = ip.toString();
        if(isIpExistBan(uuid)) {
            if(!getIPFromBan(uuid).equals(ipString)) {
                PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET BANED_IP = ? WHERE BANED_UUID = ?");
                ps.setString(1, ipString);
                ps.setString(2, uuid.toString());
                mysql.runAsyncUpdate(ps);
            }
        }
        if(isIpExistMute(uuid)) {
            if(!getIPFromMute(uuid).equals(ipString)) {
                PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET MUTED_IP = ? WHERE MUTED_UUID = ?");
                ps.setString(1, ipString);
                ps.setString(2, uuid.toString());
                mysql.runAsyncUpdate(ps);
            }
        }
        if(isIpExistAlts(uuid)) {
            if(!getIPFromAlts(uuid).equals(ipString)) {
                PreparedStatement ps = new PreparedStatement("UPDATE ALTDATA SET IP = ? WHERE UUID = ?");
                ps.setString(1, ipString);
                ps.setString(2, uuid.toString());
                mysql.runAsyncUpdate(ps);
            }
        } else {
            PreparedStatement ps = new PreparedStatement("INSERT INTO ALTDATA(UUID, IP, LASTSEEN) VALUES(?, ?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, ipString);
            ps.setLong(3, System.currentTimeMillis());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateLastSeen(UUID uuid) {
        if(isIpExistAlts(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE ALTDATA SET LASTSEEN = ? WHERE UUID = ?");
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void saveBan(ClientBan ban) {
        PreparedStatement ps = new PreparedStatement("INSERT INTO BANDATA(BANED_NAME, BANED_UUID," +
                " BANED_IP, BAN_REASON, BAN_EXECUTOR, BAN_AMOUNT, BAN_ID, BAN_STATUS, BAN_TIMESTAMP, BAN_DURATION," +
                " UNBAN_REASON, UNBAN_UUID, UNBAN_TIMESTAMP, REDUCER_UUID, REDUCED_TIMESTAMP, ORIGINAL_DURATION," +
                " REPLAY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, EnderAPI.getInstance().getName(ban.getTarget()));
        ps.setString(2, ban.getTarget().toString());
        ps.setString(3, ban.getTargetIP());
        ps.setString(4, ban.getReason().toString());
        ps.setString(5, ban.getExecutor().toString());
        ps.setInt(6, ban.getBanAmount());
        ps.setString(7, ban.getID());
        ps.setBoolean(8, true);
        ps.setLong(9, ban.getTimestamp());
        ps.setLong(10, ban.getDuration());
        ps.setString(11, "");
        ps.setString(12, "");
        ps.setLong(13, 0);
        ps.setString(14, "");
        ps.setLong(15, 0);
        ps.setLong(16, ban.getDuration());
        ps.setString(17, "");
        mysql.runAsyncUpdate(ps);
    }

    public static void saveMute(ClientMute mute) {
        PreparedStatement ps = new PreparedStatement("INSERT INTO MUTEDATA(MUTED_NAME, MUTED_UUID, " +
                "MUTED_IP, MUTE_REASON, MUTE_EXECUTOR, MUTE_AMOUNT, MUTE_ID, MUTE_STATUS, MUTE_TIMESTAMP, " +
                "MUTE_DURATION, UNMUTE_REASON, UNMUTE_UUID, UNMUTE_TIMESTAMP, REDUCER_UUID, REDUCED_TIMESTAMP," +
                " ORIGINAL_DURATION, CHATLOG, CHATFILTER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, EnderAPI.getInstance().getName(mute.getTarget()));
        ps.setString(2, mute.getTarget().toString());
        ps.setString(3, mute.getTargetIP());
        ps.setString(4, mute.getReason().toString());
        ps.setString(5, mute.getExecutor().toString());
        ps.setInt(6, mute.getMuteAmount());
        ps.setString(7, mute.getID());
        ps.setBoolean(8, true);
        ps.setLong(9, mute.getTimestamp());
        ps.setLong(10, mute.getDuration());
        ps.setString(11, "");
        ps.setString(12, "");
        ps.setLong(13, 0);
        ps.setString(14, "");
        ps.setLong(15, 0);
        ps.setLong(16, mute.getDuration());
        ps.setString(17, mute.getChatlogID());
        ps.setBoolean(18, mute.isFromChatfilter());
        mysql.runAsyncUpdate(ps);
    }

    public static void saveReport(Report report) {
        PreparedStatement ps = new PreparedStatement("INSERT INTO REPORTDATA(REPORT_ID, REPORTED_NAME," +
                " REPORTED_UUID, REPORT_REASON, REPORT_TIMESTAMP, REPORTER_UUID, REPORT_STATUS, DONE_BY, DONE_TIMESTAMP," +
                " SIGNATURE, VALUE, CHATLOG, REPLAY) VALUES(?,?,?,?," + System.currentTimeMillis() + ",?,?,?,0,?,?,?,?)");
        ps.setString(1, report.getID());
        ps.setString(2, report.getReported().getName());
        ps.setString(3, report.getReported().getUniqueId().toString());
        ps.setString(4, report.getReason().toString());
        ps.setString(5, report.getSenderUUID());
        ps.setInt(6, report.getStatus().getID());
        ps.setString(7, "");
        ps.setString(8, report.getSkinSignature());
        ps.setString(9, report.getSkinValue());
        ps.setString(10, "null");
        ps.setString(11, "");
        mysql.runAsyncUpdate(ps);
    }

    public static void saveChatlog(Chatlog chatlog) {
        SimpleDateFormat datef = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
        for(ChatMessage cm : chatlog.getMessages()) {
            PreparedStatement ps = new PreparedStatement("INSERT INTO CHATLOGDATA(LOG_ID, LOGGED_UUID, LOGGED_NAME," +
                    " CREATOR_UUID, CREATOR_NAME, SERVER, WRITER_NAME, MESSAGE, MESSAGE_TIME, LOG_TIME, LOG_DATE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, chatlog.getLogID());
            ps.setString(2, chatlog.getTargetUUID().toString());
            ps.setString(3, chatlog.getTargetName());
            ps.setString(4, chatlog.getCreatorUUID().toString());
            ps.setString(5, chatlog.getCreatorName());
            ps.setString(6, cm.getServerName());
            ps.setString(7, cm.getSender());
            ps.setString(8, cm.getMessage());
            ps.setString(9, timef.format(new Date(cm.getTimestamp())));
            ps.setString(10, timef.format(chatlog.getTimestamp()));
            ps.setString(11, datef.format(chatlog.getTimestamp()));
            mysql.runAsyncUpdate(ps);
        }
    }

    public static String getValue(String reportid) {
        if(isReportIdExist(reportid)) {
            PreparedStatement ps = new PreparedStatement("SELECT VALUE FROM REPORTDATA WHERE REPORT_ID = ?");
            ps.setString(1, reportid);
            return mysql.getString(ps, "VALUE");
        }
        return null;
    }

    public static String getSignature(String reportid) {
        if(isReportIdExist(reportid)) {
            PreparedStatement ps = new PreparedStatement("SELECT SIGNATURE FROM REPORTDATA WHERE REPORT_ID = ?");
            ps.setString(1, reportid);
            return mysql.getString(ps, "SIGNATURE");
        }
        return null;
    }

    public static void unbanPlayer(UUID uuid, UnbanReason reason, UUID unbaner) {
        PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET UNBAN_REASON = ?, BAN_STATUS = 0, " +
                "UNBAN_UUID = ?, UNBAN_TIMESTAMP = ? WHERE BANED_UUID = ? AND BAN_STATUS = 1");
        ps.setString(1, reason.toString());
        ps.setString(2, unbaner.toString());
        ps.setLong(3, System.currentTimeMillis());
        ps.setString(4, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static void unbanPlayerExpired(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET UNBAN_REASON = ?, BAN_STATUS = 0, " +
                "UNBAN_UUID = ?, UNBAN_TIMESTAMP = ? WHERE BANED_UUID = ? AND BAN_STATUS = 1");
        ps.setString(1, UnbanReason.EXPIRED.toString());
        ps.setString(2, uuid.toString());
        ps.setLong(3, System.currentTimeMillis());
        ps.setString(4, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static void unmutePlayer(UUID uuid, UnmuteReason reason, UUID unmuter) {
        PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET UNMUTE_REASON = ?, MUTE_STATUS = 0," +
                " UNMUTE_UUID = ?, UNMUTE_TIMESTAMP = ? WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
        ps.setString(1, reason.toString());
        ps.setString(2, unmuter.toString());
        ps.setLong(3, System.currentTimeMillis());
        ps.setString(4, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static void unmutePlayerExpired(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET UNMUTE_REASON = ?, MUTE_STATUS = 0, " +
                "UNMUTE_UUID = ?, UNMUTE_TIMESTAMP = ? WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
        ps.setString(1, UnmuteReason.EXPIRED.toString());
        ps.setString(2, uuid.toString());
        ps.setLong(3, System.currentTimeMillis());
        ps.setString(4, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static int getUnbanAmount(UUID uuid, UnbanReason ubr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE UNBAN_UUID = ?" +
                " AND UNBAN_REASON = '" + ubr.toString() + "'");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getUnmuteAmount(UUID uuid, UnmuteReason umr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE UNMUTE_UUID = ?" +
                " AND UNMUTE_REASON = '" + umr.toString() + "'");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getCreatedChatlogsCount(UUID uuid) {
        ArrayList<String> logids = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM CHATLOGDATA WHERE CREATOR_UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                if(logids.isEmpty()) {
                    logids.add(rs.getString("LOG_ID"));
                } else {
                    if(!logids.get(logids.size() -1).equals(rs.getString("LOG_ID"))) {
                        logids.add(rs.getString("LOG_ID"));
                    }
                }
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return logids.size();
    }

    public static int getCreatedReportsCount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE REPORTER_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getMutedChatfiltersCount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTE_EXECUTOR = ? AND CHATFILTER = 1");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getMutedAmount(UUID uuid, MuteReason mr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTE_EXECUTOR = ? AND MUTE_REASON = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, mr.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getMutedAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTE_EXECUTOR = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getBanedAmount(UUID uuid, BanReason br) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BAN_EXECUTOR = ? AND BAN_REASON = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, br.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getBanedAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BAN_EXECUTOR = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportsBaned(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTSTATS WHERE UUID = ? AND BANED = 1");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportsClosed(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTSTATS WHERE UUID = ? AND CLOSED = 1");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportsRejected(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTSTATS WHERE UUID = ? AND REJECTED = 1");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportsFinished(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTSTATS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportsFinished(UUID uuid, ReportReason rr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTSTATS WHERE UUID = ? AND REASON = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, rr.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportsGot(UUID uuid, ReportReason rr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE REPORTED_UUID = ? AND REPORT_REASON = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, rr.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getUnmutedAmount(UUID uuid, UnmuteReason mr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE MUTE_EXECUTOR = ? AND UNMUTE_REASON = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, mr.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getUnmutedAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM MUTEDATA WHERE UNMUTE_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getUnbanedAmount(UUID uuid, UnbanReason ubr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE BAN_EXECUTOR = ? AND UNBAN_REASON = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, ubr.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getMessagesCount(String chatlogid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM CHATLOGDATA WHERE LOG_ID = ?");
        ps.setString(1, chatlogid);
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static String getLastInactiveBanID(UUID uuid) {
        int bans = getBanAmount(uuid);
        PreparedStatement ps = new PreparedStatement("SELECT BAN_ID FROM BANDATA WHERE BANED_UUID = ? AND BAN_AMOUNT = ?");
        ps.setString(1, uuid.toString());
        ps.setInt(2, bans);
        return mysql.getString(ps, "BAN_ID");
    }

    public static String getLastInactiveMuteID(UUID uuid) {
        int bans = getMuteAmount(uuid);
        PreparedStatement ps = new PreparedStatement("SELECT MUTE_ID FROM MUTEDATA WHERE MUTED_UUID = ? AND MUTE_AMOUNT = ?");
        ps.setString(1, uuid.toString());
        ps.setInt(2, bans);
        return mysql.getString(ps, "MUTE_ID");
    }

    public static int getUnbanedAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM BANDATA WHERE UNBAN_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static UUID getBanExecutor(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT BAN_EXECUTOR FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return UUID.fromString(mysql.getString(ps, "BAN_EXECUTOR"));
    }

    public static BanReason getBanReason(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT BAN_REASON FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return BanReason.valueOf(mysql.getString(ps, "BAN_REASON"));
    }

    public static UUID getMuteExecutor(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT MUTE_EXECUTOR FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return UUID.fromString(mysql.getString(ps, "MUTE_EXECUTOR"));
    }

    public static UUID getMutedPlayer(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT MUTED_UUID FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return UUID.fromString(mysql.getString(ps, "MUTED_UUID"));
    }

    public static UUID getBanedPlayer(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT BANED_UUID FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return UUID.fromString(mysql.getString(ps, "BANED_UUID"));
    }

    public static UUID getReportedPlayer(String reportid) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORTED_UUID FROM REPORTDATA WHERE REPORT_ID = ?");
        ps.setString(1, reportid);
        return UUID.fromString(mysql.getString(ps, "REPORTED_UUID"));
    }

    public static MuteReason getMuteReason(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT MUTE_REASON FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return MuteReason.valueOf(mysql.getString(ps, "MUTE_REASON"));
    }

    public static UUID getUnmuteExecutor(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNMUTE_UUID FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return UUID.fromString(mysql.getString(ps, "UNMUTE_UUID"));
    }

    public static UnmuteReason getUnmuteReason(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNMUTE_REASON FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return UnmuteReason.valueOf(mysql.getString(ps, "UNMUTE_REASON"));
    }

    public static UUID getUnbanExecutor(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNBAN_UUID FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return UUID.fromString(mysql.getString(ps, "UNBAN_UUID"));
    }

    public static UnbanReason getUnbanReason(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNBAN_REASON FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return UnbanReason.valueOf(mysql.getString(ps, "UNBAN_REASON"));
    }

    public static boolean isUnbanned(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNBAN_REASON FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        if(mysql.getString(ps, "UNBAN_REASON").equals(" ")) {
            return false;
        }
        return true;
    }

    public static boolean isUnmuted(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNMUTE_REASON FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, banid);
        if(mysql.getString(ps, "UNMUTE_REASON").equals(" ")) {
            return false;
        }
        return true;
    }

    public static long getBanTimestamp(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT BAN_TIMESTAMP FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return mysql.getLong(ps, "BAN_TIMESTAMP");
    }

    public static long getUnbanTimestamp(String banid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNBAN_TIMESTAMP FROM BANDATA WHERE BAN_ID = ?");
        ps.setString(1, banid);
        return mysql.getLong(ps, "UNBAN_TIMESTAMP");
    }

    public static long getMuteTimestamp(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT MUTE_TIMESTAMP FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return mysql.getLong(ps, "MUTE_TIMESTAMP");
    }

    public static long getUnmuteTimestamp(String muteid) {
        PreparedStatement ps = new PreparedStatement("SELECT UNMUTE_TIMESTAMP FROM MUTEDATA WHERE MUTE_ID = ?");
        ps.setString(1, muteid);
        return mysql.getLong(ps, "UNMUTE_TIMESTAMP");
    }

    public static ArrayList<BanInfo> getAllBans(UUID uuid) {
        ArrayList<BanInfo> bans = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM BANDATA WHERE BANED_UUID = ? ORDER BY BAN_AMOUNT");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                bans.add(new BanInfo(rs.getString("BANED_NAME"), UUID.fromString(rs.getString("BANED_UUID")),
                        rs.getString("BANED_IP"), BanReason.valueOf(rs.getString("BAN_REASON")),
                        UUID.fromString(rs.getString("BAN_EXECUTOR")), rs.getInt("BAN_AMOUNT"),
                        rs.getString("BAN_ID"), rs.getLong("BAN_TIMESTAMP"),
                        rs.getLong("BAN_DURATION"), rs.getString("UNBAN_REASON"),
                        rs.getString("UNBAN_UUID"), rs.getLong("UNBAN_TIMESTAMP"),
                        rs.getBoolean("BAN_STATUS")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return bans;
    }

    public static ArrayList<BanInfo> getAllHandledBans(UUID uuid) {
        ArrayList<BanInfo> bans = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM BANDATA WHERE BAN_EXECUTOR = ? ORDER BY BAN_AMOUNT");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                bans.add(new BanInfo(rs.getString("BANED_NAME"), UUID.fromString(rs.getString("BANED_UUID")),
                        rs.getString("BANED_IP"), BanReason.valueOf(rs.getString("BAN_REASON")),
                        UUID.fromString(rs.getString("BAN_EXECUTOR")), rs.getInt("BAN_AMOUNT"),
                        rs.getString("BAN_ID"), rs.getLong("BAN_TIMESTAMP"), rs.getLong("BAN_DURATION"),
                        rs.getString("UNBAN_REASON"), rs.getString("UNBAN_UUID"),
                        rs.getLong("UNBAN_TIMESTAMP"), rs.getBoolean("BAN_STATUS")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return bans;
    }

    public static ArrayList<MuteInfo> getAllMutes(UUID uuid) {
        ArrayList<MuteInfo> mutes = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM MUTEDATA WHERE MUTED_UUID = ? ORDER BY MUTE_AMOUNT");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                mutes.add(new MuteInfo(rs.getString("MUTED_NAME"), UUID.fromString(rs.getString("MUTED_UUID")),
                        rs.getString("MUTED_IP"),MuteReason.valueOf(rs.getString("MUTE_REASON")),
                        UUID.fromString(rs.getString("MUTE_EXECUTOR")), rs.getInt("MUTE_AMOUNT"),
                        rs.getString("MUTE_ID"), rs.getLong("MUTE_TIMESTAMP"), rs.getLong("MUTE_DURATION"),
                        rs.getString("UNMUTE_REASON"), rs.getString("UNMUTE_UUID"), rs.getLong("UNMUTE_TIMESTAMP"),
                        rs.getString("CHATLOG"), rs.getBoolean("MUTE_STATUS")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return mutes;
    }

    public static ArrayList<MuteInfo> getAllHandledMutes(UUID uuid) {
        ArrayList<MuteInfo> mutes = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM MUTEDATA WHERE MUTE_EXECUTOR = ? ORDER BY MUTE_AMOUNT");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                mutes.add(new MuteInfo(rs.getString("MUTED_NAME"), UUID.fromString(rs.getString("MUTED_UUID")),
                        rs.getString("MUTED_IP"),MuteReason.valueOf(rs.getString("MUTE_REASON")),
                        UUID.fromString(rs.getString("MUTE_EXECUTOR")), rs.getInt("MUTE_AMOUNT"),
                        rs.getString("MUTE_ID"), rs.getLong("MUTE_TIMESTAMP"), rs.getLong("MUTE_DURATION"),
                        rs.getString("UNMUTE_REASON"), rs.getString("UNMUTE_UUID"), rs.getLong("UNMUTE_TIMESTAMP"),
                        rs.getString("CHATLOG"), rs.getBoolean("MUTE_STATUS")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return mutes;
    }

    public static ChatlogInfo getChatlogInfo(String logid) {
        PreparedStatement ps = new PreparedStatement("SELECT * FROM CHATLOGDATA WHERE LOG_ID = ?");
        ps.setString(1, logid);
        return new ChatlogInfo(mysql.getString(ps, "LOG_ID"), mysql.getString(ps, "LOG_TIME"),
                mysql.getString(ps, "LOG_DATE"), mysql.getString(ps, "LOGGED_NAME"));
    }

    public static ArrayList<ReportInfo> getAllReports(UUID uuid) {
        ArrayList<ReportInfo> reports = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM REPORTDATA WHERE REPORTER_UUID = ? ORDER BY REPORT_TIMESTAMP");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                reports.add(new ReportInfo(rs.getString("REPORT_ID"), rs.getString("REPORTED_NAME"),
                        UUID.fromString(rs.getString("REPORTED_UUID")), ReportReason.valueOf(rs.getString("REPORT_REASON")),
                        rs.getLong("REPORT_TIMESTAMP"), rs.getString("REPORTER_UUID"),
                        ReportStatus.translate(rs.getInt("REPORT_STATUS")), rs.getString("DONE_BY"),
                        rs.getLong("DONE_TIMESTAMP"),
                        rs.getString("SIGNATURE"),
                        rs.getString("VALUE"),
                        rs.getString("CHATLOG")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return reports;
    }

    public static ArrayList<ReportInfo> getAllHandledReports(UUID uuid) {
        ArrayList<ReportInfo> reports = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM REPORTDATA WHERE DONE_BY = ? ORDER BY REPORT_TIMESTAMP");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                reports.add(new ReportInfo(rs.getString("REPORT_ID"), rs.getString("REPORTED_NAME"),
                        UUID.fromString(rs.getString("REPORTED_UUID")), ReportReason.valueOf(rs.getString("REPORT_REASON")),
                        rs.getLong("REPORT_TIMESTAMP"), rs.getString("REPORTER_UUID"),
                        ReportStatus.translate(rs.getInt("REPORT_STATUS")), rs.getString("DONE_BY"),
                        rs.getLong("DONE_TIMESTAMP"), rs.getString("SIGNATURE"), rs.getString("VALUE"),
                        rs.getString("CHATLOG")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return reports;
    }

    public static ArrayList<ReportInfo> getOpenReports(ReportReason rr) {
        ArrayList<ReportInfo> reports = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT * FROM REPORTDATA WHERE REPORT_REASON = ? AND REPORT_STATUS = 2");
        ps.setString(1, rr.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                reports.add(new ReportInfo(rs.getString("REPORT_ID"), rs.getString("REPORTED_NAME"),
                        UUID.fromString(rs.getString("REPORTED_UUID")), ReportReason.valueOf(rs.getString("REPORT_REASON")),
                        rs.getLong("REPORT_TIMESTAMP"), rs.getString("REPORTER_UUID"),
                        ReportStatus.translate(rs.getInt("REPORT_STATUS")), rs.getString("DONE_BY"),
                        rs.getLong("DONE_TIMESTAMP"), rs.getString("SIGNATURE"), rs.getString("VALUE"),
                        rs.getString("CHATLOG")));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return reports;
    }

    public static String getRandomOpenReportID(ReportReason rpr) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORT_ID FROM REPORTDATA WHERE" +
                " REPORT_REASON = ? AND REPORT_STATUS = 2 ORDER BY RAND() LIMIT 1");
        ps.setString(1, rpr.toString());
        return mysql.getString(ps, "REPORT_ID");
    }

    public static int getReportCount(ReportReason rpr) {
        int reports = 0;
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE REPORT_REASON = ? AND REPORT_STATUS = 2");
        ps.setString(1, rpr.toString());
        reports = mysql.getInt(ps, "COUNT(*)");
        if(reports == 0) reports = 1;
        if(reports > 64) reports = 64;
        return reports;
    }

    public static int getRealReportCount(ReportReason rpr) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE" +
                " REPORT_REASON = ? AND REPORT_STATUS = 2");
        ps.setString(1, rpr.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static void updateReportStatus(String reportid, ReportStatus rs) {
        PreparedStatement ps = new PreparedStatement("UPDATE REPORTDATA SET REPORT_STATUS = ? WHERE REPORT_ID = ?");
        ps.setInt(1, rs.getID());
        ps.setString(2, reportid);
        mysql.runAsyncUpdate(ps);
    }

    public static void updateReportTimestamp(String reportid) {
        PreparedStatement ps = new PreparedStatement("UPDATE REPORTDATA SET DONE_TIMESTAMP = ? WHERE REPORT_ID = ?");
        ps.setLong(1, System.currentTimeMillis());
        ps.setString(2, reportid);
        mysql.runAsyncUpdate(ps);
    }

    public static void updateReportWorker(String reportid, UUID uuid) {
        PreparedStatement ps = new PreparedStatement("UPDATE REPORTDATA SET DONE_BY = ? WHERE REPORT_ID = ?");
        if(uuid != null) {
            ps.setString(1, uuid.toString());
        } else {
            ps.setString(1, "");
        }
        ps.setString(2, reportid);
        mysql.runAsyncUpdate(ps);
    }

    public static boolean hasOpenReport(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT DONE_BY FROM REPORTDATA WHERE DONE_BY = ? AND REPORT_STATUS = 1");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static ReportInfo getReportInfo(String reportid) {
        PreparedStatement ps = new PreparedStatement("SELECT * FROM REPORTDATA WHERE REPORT_ID = ?");
        ps.setString(1, reportid);
        return new ReportInfo(reportid, mysql.getString(ps, "REPORTED_NAME"), UUID.fromString(mysql.getString(ps, "REPORTED_UUID")),
                ReportReason.valueOf(mysql.getString(ps, "REPORT_REASON")), mysql.getLong(ps, "REPORT_TIMESTAMP"),
                mysql.getString(ps ,"REPORTER_UUID"), ReportStatus.translate(mysql.getInt(ps, "REPORT_STATUS")),
                mysql.getString(ps, "DONE_BY"), mysql.getLong(ps ,"DONE_TIMESTAMP"), mysql.getString(ps, "SIGNATURE"),
                mysql.getString(ps, "VALUE"), mysql.getString(ps, "CHATLOG"));
    }

    public static ReportStatus getReportStatus(String reportid) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORT_STATUS FROM REPORTDATA WHERE REPORT_ID = ?");
        ps.setString(1, reportid);
        return ReportStatus.translate(mysql.getInt(ps, "REPORT_STATUS"));
    }

    public static String getReportWorkerUUID(String reportid) {
        PreparedStatement ps = new PreparedStatement("SELECT DONE_BY FROM REPORTDATA WHERE REPORT_ID = ?");
        ps.setString(1, reportid);
        return mysql.getString(ps, "DONE_BY");
    }

    public static UUID getreportedUUID(String reportid) {
        PreparedStatement ps = new PreparedStatement("SELECT REPORTED_UUID FROM REPORTDATA WHERE REPORT_ID = ?");
        ps.setString(1, reportid);
        return UUID.fromString(mysql.getString(ps, "REPORTED_UUID"));
    }

    public static int getOpenReportCount() {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE REPORT_STATUS = 2");
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static void createNewNotifySettings(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("INSERT INTO NOTIFYDATA(UUID, REPORTS," +
                " CONNECTION, BANSYSTEM, CHATFILTER) VALUES(?,1,1,1,1)");
        ps.setString(1, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static boolean existsInNotifySettings(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT * FROM NOTIFYDATA WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static void updateNotifySettings(UUID uuid, NotifyType nt, boolean active) {
        if(existsInNotifySettings(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE NOTIFYDATA" +
                    " SET " + nt.toString() + " = ? WHERE UUID = ?");
            ps.setBoolean(1, active);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static boolean getNotifySetting(UUID uuid, NotifyType nt) {
        PreparedStatement ps = new PreparedStatement("SELECT " + nt.toString() + " FROM " +
                "NOTIFYDATA WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getBoolean(ps, nt.toString());
    }

    public static void updateActiveMuteChatlogID(UUID uuid, String chatlogid) {
        if(hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET CHATLOG = ?" +
                    " WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setString(1, chatlogid);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateActiveBanReplayID(UUID uuid, String replayid) {
        if(hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET REPLAY = ?" +
                    " WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, replayid);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateActiveBanDuration(UUID uuid, long newtime) {
        if(hasActiveBan(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET BAN_DURATION = ?" +
                    " WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setLong(1, newtime);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateActiveMuteDuration(UUID uuid, long newtime) {
        if(hasActiveMute(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET MUTE_DURATION = ? " +
                    "WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setLong(1, newtime);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static long getActiveOriginalMuteDuration(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT ORIGINAL_DURATION FROM MUTEDATA " +
                "WHERE MUTED_UUID = ? AND MUTE_STATUS = 1");
        ps.setString(1, uuid.toString());
        return mysql.getLong(ps, "ORIGINAL_DURATION");
    }

    public static long getActiveOriginalBanDuration(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT ORIGINAL_DURATION FROM BANDATA" +
                " WHERE BANED_UUID = ? AND BAN_STATUS = 1");
        ps.setString(1, uuid.toString());
        return mysql.getLong(ps, "ORIGINAL_DURATION");
    }

    public static void updateActiveBanReducer(UUID baned, UUID reducer) {
        if(hasActiveBan(baned)) {
            PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET REDUCER_UUID = ? " +
                    "WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setString(1, reducer.toString());
            ps.setString(2, baned.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateActiveBanReduceTimestamp(UUID baned) {
        if(hasActiveBan(baned)) {
            PreparedStatement ps = new PreparedStatement("UPDATE BANDATA SET REDUCED_TIMESTAMP = ?" +
                    " WHERE BANED_UUID = ? AND BAN_STATUS = 1");
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, baned.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateActiveMuteReducer(UUID muted, UUID reducer) {
        if(hasActiveMute(muted)) {
            PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET REDUCER_UUID = ? WHERE MUTED_UUID = ? " +
                    "AND MUTE_STATUS = 1");
            ps.setString(1, reducer.toString());
            ps.setString(2, muted.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateActiveMuteReduceTimestamp(UUID muted) {
        if(hasActiveMute(muted)) {
            PreparedStatement ps = new PreparedStatement("UPDATE MUTEDATA SET REDUCED_TIMESTAMP = ? WHERE " +
                    "MUTED_UUID = ? AND MUTE_STATUS = 1");
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, muted.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void updateReportChatlogID(String reportid, String logid) {
        if(isReportIdExist(reportid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE REPORTDATA SET CHATLOG = ? WHERE REPORT_ID = ?");
            ps.setString(1, logid);
            ps.setString(2, reportid);
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void deleteReport(String reportid) {
        if(isReportIdExist(reportid)) {
            PreparedStatement ps = new PreparedStatement("DELETE FROM REPORTDATA WHERE REPORT_ID = ?");
            ps.setString(1, reportid);
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void createReportStats(UUID uuid, String reportid, boolean baned, boolean closed, boolean rejected,
                                         ReportReason rr) {
        PreparedStatement ps = new PreparedStatement("INSERT INTO REPORTSTATS(UUID, REPORTID, BANED, CLOSED," +
                " REJECTED, REASON) VALUES(?,?,?,?,?,?)");
        ps.setString(1, uuid.toString());
        ps.setString(2, reportid);
        ps.setBoolean(3, baned);
        ps.setBoolean(4, closed);
        ps.setBoolean(5, rejected);
        ps.setString(6, rr.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static ReportReason getReportReason(String reportid) {
        if(isReportIdExist(reportid)) {
            PreparedStatement ps = new PreparedStatement("SELECT REPORT_REASON FROM REPORTDATA WHERE REPORT_ID = ?");
            ps.setString(1, reportid);
            return ReportReason.valueOf(mysql.getString(ps, "REPORT_REASON"));
        }
        return null;
    }

    public static int getRejectAmount(String reportid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTSTATS WHERE REPORTID = ? AND REJECTED = 1");
        ps.setString(1, reportid);
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportAmount(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE REPORTED_UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static int getReportAmountAntiCheat(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT COUNT(*) FROM REPORTDATA WHERE REPORTED_UUID = ? " +
                "AND REPORTER_UUID = 'AntiCheat'");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, "COUNT(*)");
    }

    public static String getReportDesision(String reportid) {
        if(getReportStatus(reportid).equals(ReportStatus.FINISHED)) {
            PreparedStatement ps = new PreparedStatement("SELECT * FROM REPORTSTATS WHERE REPORTID = ? AND REJECTED != 1");
            ps.setString(1, reportid);
            if(mysql.getBoolean(ps, "BANED")) {
                return "§2Angenommen";
            } else if(mysql.getBoolean(ps, "CLOSED")) {
                return "§4Abgelehnt";
            }
        }
        return "§4ERROR";
    }

    public static void deleteAllOpenReports(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("DELETE FROM REPORTDATA WHERE REPORTED_UUID = ? AND REPORT_STATUS = 2");
        ps.setString(1, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static ArrayList<String> getAllWorkingReports(UUID targetUUID) {
        ArrayList<String> reportids = new ArrayList<>();
        PreparedStatement ps = new PreparedStatement("SELECT REPORT_ID FROM REPORTDATA WHERE REPORTED_UUID = ? AND REPORT_STATUS = 1");
        ps.setString(1, targetUUID.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            while(rs.next()) {
                reportids.add(rs.getString("REPORT_ID"));
            }
            rs.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        mysql.closeConnections(ps);
        return reportids;
    }

    public static void addApplyID(String id, int tsnumber, UUID uniqueId) {
        PreparedStatement ps = new PreparedStatement("INSERT INTO APPLYDATA(APPLY_ID, UUID, APPLY_RANK, STATUS, " +
                "ACCEPTED, TIMESTAMP, TS_NUMBER) VALUES(?,?,?,?,?,?,?)");
        ps.setString(1, id);
        ps.setString(2, uniqueId.toString());
        ps.setString(3, "");
        ps.setInt(4, 2);
        ps.setBoolean(5, false);
        ps.setLong(6, System.currentTimeMillis());
        ps.setInt(7, tsnumber);
        mysql.runAsyncUpdate(ps);
    }

    public static void deleteAllUselessApplys() {
        PreparedStatement ps = new PreparedStatement("DELETE FROM APPLYDATA WHERE TIMESTAMP < ? AND STATUS = 2");
        ps.setLong(1, (System.currentTimeMillis() - 2592000000l));
        mysql.runAsyncUpdate(ps);
    }

    public static void deleteAllApplys(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("DELETE FROM APPLYDATA WHERE UUID = ? AND STATUS = 2");
        ps.setString(1, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static void updateApplyStatus(int tsid, int status, boolean accepted) {
        PreparedStatement ps = new PreparedStatement("UPDATE APPLYDATA SET STATUS = ?, ACCEPTED = ?, TIMESTAMP = ?" +
                " WHERE TS_NUMBER = ?");
        ps.setInt(1, status);
        ps.setBoolean(2, accepted);
        ps.setLong(3, System.currentTimeMillis());
        ps.setInt(4, tsid);
        mysql.runAsyncUpdate(ps);
    }

    public static boolean isTS_NumberExist(int tsnumber) {
        PreparedStatement ps = new PreparedStatement("SELECT TS_NUMBER FROM APPLYDATA WHERE TS_NUMBER = ?");
        ps.setInt(1, tsnumber);
        return mysql.isInDatabase(ps);
    }

    public static boolean isOpenApplyExist(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT APPLY_ID FROM APPLYDATA WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static String getOpenApplyID(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT APPLY_ID FROM APPLYDATA WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getString(ps, "APPLY_ID");
    }

    public static boolean getApplyphaseStatus(String rank) {
        PreparedStatement ps = new PreparedStatement("SELECT OPEN FROM APPLYPHASE WHERE RANKS = ?");
        ps.setString(1, rank);
        return mysql.getBoolean(ps, "OPEN");
    }

    public static void updateApplyphaseStatus(String rank, boolean open) {
        PreparedStatement ps = new PreparedStatement("UPDATE APPLYPHASE SET OPEN = ? WHERE RANKS = ?");
        ps.setBoolean(1, open);
        ps.setString(2, rank);
        mysql.runAsyncUpdate(ps);
    }

    public static int getApplyStatus(int tsnumber) {
        PreparedStatement ps = new PreparedStatement("SELECT STATUS FROM APPLYDATA WHERE TS_NUMBER = ?");
        ps.setInt(1, tsnumber);
        return mysql.getInt(ps, "STATUS");
    }

    public static boolean isApplyIdExist(String id) {
        PreparedStatement ps = new PreparedStatement("SELECT APPLY_ID FROM APPLYDATA WHERE APPLY_ID = ?");
        ps.setString(1, id);
        return mysql.isInDatabase(ps);
    }

    public static String getApplyRank(int tsid) {
        PreparedStatement ps = new PreparedStatement("SELECT APPLY_RANK FROM APPLYDATA WHERE TS_NUMBER = ?");
        ps.setInt(1, tsid);
        return mysql.getString(ps, "APPLY_RANK");
    }

    public static String getApplyUUID(int tsid) {
        PreparedStatement ps = new PreparedStatement("SELECT UUID FROM APPLYDATA WHERE TS_NUMBER = ?");
        ps.setInt(1, tsid);
        return mysql.getString(ps, "UUID");
    }

    public static boolean isUserExistJoinme(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT UUID FROM JOINMETOKENS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static int getJoinmeTokens(UUID uuid) {
        if(!isUserExistJoinme(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT AMOUNT FROM JOINMETOKENS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps, "AMOUNT");
        }
        return 0;
    }

    public static void removeJoinmeTokens(UUID uuid, int amount) {
        if(!isUserExistJoinme(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE JOINMETOKENS SET AMOUNT = ? WHERE UUID = ?");
            ps.setInt(1, getJoinmeTokens(uuid) -amount);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void addJoinmeTokens(UUID uuid, int amount) {
        if(!isUserExistJoinme(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE JOINMETOKENS SET AMOUNT = ? WHERE UUID = ?");
            ps.setInt(1, getJoinmeTokens(uuid) +amount);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void createUserJoinme(UUID uuid) {
        if(!isUserExistJoinme(uuid)) {
            PreparedStatement ps = new PreparedStatement("INSERT INTO JOINMETOKENS(UUID, AMOUNT) VALUES(?, ?)");
            ps.setString(1, uuid.toString());
            ps.setInt(2, 0);
            mysql.runAsyncUpdate(ps);
        }
    }

    public static boolean isUserExistTime(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT UUID FROM ONLINETIME WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static void createUserTime(UUID uuid) {
        if (!isUserExistTime(uuid)) {
            PreparedStatement ps = new PreparedStatement("INSERT INTO ONLINETIME (UUID, LASTSAVE, TIME) VALUES(?,?,0)");
            ps.setString(1, uuid.toString());
            ps.setLong(2, System.currentTimeMillis());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static long getLastSavedTimestamp(UUID uuid) {
        if (isUserExistTime(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT LASTSAVE FROM ONLINETIME WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getLong(ps, "LASTSAVE");
        }
        return 0;
    }

    public static long getTime(UUID uuid) {
        if (isUserExistTime(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT TIME FROM ONLINETIME WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getLong(ps, "TIME");
        }
        return 0;
    }

    public static void saveTime(UUID uuid) {
        if (isUserExistTime(uuid)) {
            long lasttimestamp = getLastSavedTimestamp(uuid);
            long difference = System.currentTimeMillis() - lasttimestamp;
            long newtime = getTime(uuid) + difference;
            PreparedStatement ps = new PreparedStatement("UPDATE ONLINETIME SET TIME = ?, LASTSAVE = ? WHERE UUID = ?");
            ps.setLong(1, newtime);
            ps.setLong(2, System.currentTimeMillis());
            ps.setString(3, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static void setLastSave(UUID uuid) {
        if (isUserExistTime(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE ONLINETIME SET LASTSAVE = ? WHERE UUID = ?");
            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static boolean isUserExistLocations(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT UUID FROM LOBBYSETTINGS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static void updateLastLocationToSpawnLocation(UUID uuid) {
        if(isUserExistLocations(uuid)) {
            String locationstring = ProxyData.Spawn[0] + "/" + ProxyData.Spawn[1] + "/" + ProxyData.Spawn[2] + "/" +
                    ProxyData.Spawn[3] + "/" + ProxyData.Spawn[4];
            PreparedStatement ps = new PreparedStatement("UPDATE LOBBYSETTINGS SET LOCATION = ? WHERE UUID = ?");
            ps.setString(1, locationstring);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public static String getBossbarNews(String servername) {
        servername = servername.split("-")[0];
        if(servername.startsWith("BW")) servername = "BW";
        if(servername.startsWith("Silent")) servername = "Lobby";
        PreparedStatement ps = new PreparedStatement("SELECT TEXT FROM BOSSBARNEWS WHERE GROUPNAME = ?");
        ps.setString(1, servername);
        return mysql.getString(ps, "TEXT");
    }

    public static String[] getTitles(String servername) {
        servername = servername.split("-")[0];
        if(servername.startsWith("BW")) servername = "BW";
        if(servername.startsWith("Silent")) servername = "Lobby";
        PreparedStatement ps = new PreparedStatement("SELECT * FROM GAMETITLES WHERE GROUPNAME = ?");
        ps.setString(1, servername);
        return new String[] {mysql.getString(ps, "SUBTITLE"), mysql.getString(ps, "TITLE")};
    }

    public static boolean isVerified(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT VERIFIED FROM TEAMSPEAKBOT WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getBoolean(ps, "VERIFIED");
    }

    public static boolean isUUIDExistVerify(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT UUID FROM TEAMSPEAKBOT WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public static void verify(UUID uuid) {
        String rank = getRank(uuid);
        PreparedStatement ps = new PreparedStatement("INSERT INTO TEAMSPEAKBOT(UUID, RANKS, VERIFIED) VALUES (?, ?, 0)");
        ps.setString(1, uuid.toString());
        ps.setString(2, rank);
        mysql.runAsyncUpdate(ps);
    }

    public static void updateRanksSpigot(UUID uuid) {
        String rank = getRank(uuid);
        PreparedStatement ps = new PreparedStatement("UPDATE TEAMSPEAKBOT SET RANKS = ? WHERE UUID = ?");
        ps.setString(1, rank);
        ps.setString(2, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    public static void updateRanksSpigot(UUID uuid, String rank) {
        PreparedStatement ps = new PreparedStatement("UPDATE TEAMSPEAKBOT SET RANKS = ? WHERE UUID = ?");
        ps.setString(1, rank);
        ps.setString(2, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

    private static String getRank(UUID uuid) {
        String rank = PermAPI.getInstance().getGroup(uuid);
        if(rank.equalsIgnoreCase("default")) rank = "Spieler";
        if(rank.equalsIgnoreCase("JrMod")) rank = "Mod";
        if(rank.equalsIgnoreCase("JrDev")) rank = "Dev";
        if(rank.equalsIgnoreCase("JrBuild")) rank = "Build";
        if(rank.equalsIgnoreCase("JrContent")) rank = "Content";
        if(rank.equalsIgnoreCase("JrDesign")) rank = "Design";
        return rank;
    }

    public static void unverify(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("DELETE FROM TEAMSPEAKBOT WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        mysql.runAsyncUpdate(ps);
    }

}