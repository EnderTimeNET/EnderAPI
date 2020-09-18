package net.endertime.enderapi.permission.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankPermissions {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public void createTable() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS RANKPERMS (RANKS VARCHAR(100), PERM VARCHAR(100))");
        getMysql().runAsyncUpdate(ps);
    }

    public boolean hasPermission (String rank, String perm) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM RANKPERMS WHERE RANKS = ? AND PERM = ?");
        ps.setString(1, rank);
        ps.setString(2, perm);
        return getMysql().isInDatabase(ps);
    }

    public void addPermission (String rank, String perm) {
        if (!hasPermission(rank, perm)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO RANKPERMS (RANKS, PERM) VALUES (?, ?)");
            ps.setString(1, rank);
            ps.setString(2, perm);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public void removePermission (String rank, String perm) {
        if (hasPermission(rank, perm)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM RANKPERMS WHERE RANKS = ? AND PERM = ?");
            ps.setString(1, rank);
            ps.setString(2, perm);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public List<String> getPerms (String rank) {
        List<String> list = new ArrayList<>();

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM RANKPERMS WHERE RANKS = ?");
        ps.setString(1, rank);

        ResultSet rs = getMysql().runAsyncQuery(ps);

        try {
            while (rs.next()) {
                list.add(rs.getString("PERM"));
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }

        list = getPermsChild(rank, list);

        return list;
    }

    public List<String> getPermsWithOut (String rank) {
        List<String> list = new ArrayList<>();

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM RANKPERMS WHERE RANKS = ?");
        ps.setString(1, rank);

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

    public List<String> getPerms (String rank, List<String> list) {

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM RANKPERMS WHERE RANKS = ?");
        ps.setString(1, rank);

        ResultSet rs = getMysql().runAsyncQuery(ps);

        try {
            while (rs.next()) {
                list.add(rs.getString("PERM"));
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }

        list = getPermsChild(rank, list);

        return list;
    }

    public List<String> getPermsChild (String rank, List<String> list) {
        String child = getChild(rank);
        if (child != null) {

            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM RANKPERMS WHERE RANKS = ?");
            ps.setString(1, child);

            ResultSet rs = getMysql().runAsyncQuery(ps);

            try {
                while (rs.next()) {
                    list.add(rs.getString("PERM"));
                }
            } catch (SQLException e) {
            } finally {
                mysql.closeConnections(ps);
            }

            list = getPermsChild(child, list);
        }
        return list;
    }

    public String getChild(String rank) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("SELECT CHILD FROM RANKS WHERE RANKS = ?");
            ps.setString(1, rank);
            return getMysql().getString(ps, "CHILD");
        }
        return null;
    }

    public boolean isRankExists (String rank) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM RANKS WHERE RANKS = ?");
        ps.setString(1, rank);
        return getMysql().isInDatabase(ps);
    }
}
