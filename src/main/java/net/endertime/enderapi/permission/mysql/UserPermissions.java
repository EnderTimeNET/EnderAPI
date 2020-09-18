package net.endertime.enderapi.permission.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserPermissions {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public void createTable() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS USERPERMS (UUID VARCHAR(100), PERM VARCHAR(100))");
        getMysql().runAsyncUpdate(ps);
    }

    public boolean hasPermission (UUID uuid, String perm) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM USERPERMS WHERE UUID = ? AND PERM = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, perm);
        return getMysql().isInDatabase(ps);
    }

    public void addPermission (UUID uuid, String perm) {
        if (!hasPermission(uuid, perm)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO USERPERMS (UUID, PERM) VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, perm);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public void removePermission (UUID uuid, String perm) {
        if (hasPermission(uuid, perm)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM USERPERMS WHERE UUID = ? AND PERM = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, perm);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public List<String> getPerms (UUID uuid) {
        List<String> list = new ArrayList<>();

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM USERPERMS WHERE UUID = ?");
        ps.setString(1, uuid.toString());

        ResultSet rs = getMysql().runAsyncQuery(ps);

        try {
            while (rs.next()) {
                list.add(rs.getString("PERM"));
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }
        return list;
    }

    public List<String> getPerms (UUID uuid, List<String> list) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM USERPERMS WHERE UUID = ?");
        ps.setString(1, uuid.toString());

        ResultSet rs = getMysql().runAsyncQuery(ps);

        try {
            while (rs.next()) {
                list.add(rs.getString("PERM"));
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }
        return list;
    }
}
