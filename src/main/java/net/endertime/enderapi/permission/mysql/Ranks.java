package net.endertime.enderapi.permission.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

public class Ranks {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public void createTable() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS RANKS (RANKS VARCHAR(100), CHILD VARCHAR(100)," +
                " PREFIX VARCHAR(100), COMPLETEDPREFIX VARCHAR(100), TEAM VARCHAR(100), NAME VARCHAR(100))");
        getMysql().runAsyncUpdate(ps);
    }

    public boolean isRankExists (String rank) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM RANKS WHERE RANKS = ?");
        ps.setString(1, rank);
        return getMysql().isInDatabase(ps);
    }

    public void createRank (String rank, String child, String prefix, String completedPrefix, String team, String name) {
        if (!isRankExists(rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO RANKS (RANKS, CHILD, PREFIX, COMPLETEDPREFIX, TEAM, NAME)" +
                    " VALUES (?,?,?,?,?,?)");
            ps.setString(1, rank);
            ps.setString(2, child);
            ps.setString(3, prefix);
            ps.setString(4, completedPrefix);
            ps.setString(5, team);
            ps.setString(6, name);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public void updateChild(String rank, String child) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("UPDATE RANKS SET CHILD = ? WHERE RANKS = ?");
            ps.setString(1, child);
            ps.setString(2, rank);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getChild(String rank) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("SELECT CHILD FROM RANKS WHERE RANKS = ?");
            ps.setString(1, rank);
            return getMysql().getString(ps, "CHILD");
        }
        return null;
    }

    public void updatePrefix(String rank, String prefix) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("UPDATE RANKS SET PREFIX = ? WHERE RANKS = ?");
            ps.setString(1, prefix);
            ps.setString(2, rank);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getPrefix(String rank) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("SELECT PREFIX FROM RANKS WHERE RANKS = ?");
            ps.setString(1, rank);
            return getMysql().getString(ps, "PREFIX");
        }
        return null;
    }

    public void updateCompletedPrefix(String rank, String completedPrefix) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("UPDATE RANKS SET COMPLETEDPREFIX = ? WHERE RANKS = ?");
            ps.setString(1, completedPrefix);
            ps.setString(2, rank);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getCompletedPrefix(String rank) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("SELECT COMPLETEDPREFIX FROM RANKS WHERE RANKS = ?");
            ps.setString(1, rank);
            return getMysql().getString(ps, "COMPLETEDPREFIX");
        }
        return null;
    }

    public void updateName(String rank, String name) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("UPDATE RANKS SET NAME = ? WHERE RANKS = ?");
            ps.setString(1, name);
            ps.setString(2, rank);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getName(String rank) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("SELECT NAME FROM RANKS WHERE RANKS = ?");
            ps.setString(1, rank);
            return getMysql().getString(ps, "NAME");
        }
        return null;
    }

    public void updateTeam(String rank, String team) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("UPDATE RANKS SET TEAM = ? WHERE RANKS = ?");
            ps.setString(1, team);
            ps.setString(2, rank);
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getTeam(String rank) {
        if (isRankExists(rank)) {
            PreparedStatement ps = new PreparedStatement("SELECT TEAM FROM RANKS WHERE RANKS = ?");
            ps.setString(1, rank);
            return getMysql().getString(ps, "TEAM");
        }
        return null;
    }

}
