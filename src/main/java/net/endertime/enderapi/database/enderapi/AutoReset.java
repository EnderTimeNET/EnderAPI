package net.endertime.enderapi.database.enderapi;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AutoReset {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("STATSDATABASE");

    public void updateLastAutoReset(String taskname) {
        PreparedStatement ps = new PreparedStatement("UPDATE AUTORESET SET TIMESTAMP = ? WHERE TASKNAME = ?");
        ps.setLong(1, System.currentTimeMillis());
        ps.setString(2, taskname);
        mysql.runAsyncUpdate(ps);
    }

    public String getLastAutoReset(String taskname) {
        PreparedStatement ps = new PreparedStatement("SELECT TIMESTAMP FROM AUTORESET WHERE TASKNAME = ?");
        ps.setString(1, taskname);
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date(mysql.getLong(ps, "TIMESTAMP")));
    }

    public MySQL getMysql() {
        return mysql;
    }

    public void createTablesIfNotExists() {
        PreparedStatement ps = new PreparedStatement("CREATE TABLE IF NOT EXISTS TOKENS (UUID VARCHAR(100), AMOUNT INT(255))");
        mysql.runAsyncUpdate(ps);
    }

    public boolean isUserExist(UUID uuid) {
        PreparedStatement ps = new PreparedStatement("SELECT AMOUNT FROM TOKENS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public void createUser(UUID uuid) {
        if (!isUserExist(uuid)) {
            PreparedStatement ps = new PreparedStatement("INSERT INTO TOKENS (UUID, AMOUNT) VALUES(?,0)");
            ps.setString(1, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public Integer getTokens(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = new PreparedStatement("SELECT AMOUNT FROM TOKENS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps, "AMOUNT");
        }
        return 0;
    }

    public void updateTokens(UUID uuid, int value) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = new PreparedStatement("UPDATE TOKENS SET AMOUNT = ? WHERE UUID = ?");
            ps.setInt(1, value);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }
}
