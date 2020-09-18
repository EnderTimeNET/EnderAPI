package net.endertime.enderapi.permission.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.util.UUID;

public class Users {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public void createTable() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS USERS (UUID VARCHAR(100), RANKS VARCHAR(100)" +
                ", TIME BIGINT(255), TIMESET BIGINT(255))");
        getMysql().runAsyncUpdate(ps);
    }

    public boolean isUserExists (UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT RANKS FROM USERS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return getMysql().isInDatabase(ps);
    }

    public void createUser (UUID uuid, long time) {
        if (!isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO USERS(UUID, RANKS, TIME, TIMESET) VALUES (?,?,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, "default");
            ps.setLong(3, time);
            ps.setLong(4, System.currentTimeMillis());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public void updateRank(UUID uuid, String rank) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE USERS SET RANKS = ? WHERE UUID = ?");
            ps.setString(1, rank);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getRank(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT RANKS FROM USERS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return getMysql().getString(ps, "RANKS");
        }
        return "default";
    }

    public void updateTime(UUID uuid, long time) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE USERS SET TIME = ? WHERE UUID = ?");
            ps.setLong(1, time);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public long getTime(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT TIME FROM USERS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return getMysql().getLong(ps, "TIME");
        }
        return 0;
    }

    public void updateTimeSet(UUID uuid, long time) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE USERS SET TIMESET = ? WHERE UUID = ?");
            ps.setLong(1, time);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public long getTimeSet(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT TIMESET FROM USERS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return getMysql().getLong(ps, "TIMESET");
        }
        return 0;
    }
}
