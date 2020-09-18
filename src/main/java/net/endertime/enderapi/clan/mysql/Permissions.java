package net.endertime.enderapi.clan.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Permissions {

    private MySQL mysql = Clans.mysql;

    public boolean isTagExists (String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT RANK FROM PERMISSIONS WHERE TAG = ?");
        ps.setString(1, tag);

        return mysql.isInDatabase(ps);
    }

    public boolean isRankExists (String tag, int rank) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM PERMISSIONS WHERE TAG = ? AND RANK = ?");
        ps.setString(1, tag);
        ps.setInt(2, rank);

        return mysql.isInDatabase(ps);
    }

    public void creatRank (String tag, int rank, String perm) {
        if (!isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO PERMISSIONS (TAG, RANK, PERM) " +
                    "VALUES (?, ?, ?)");
            ps.setString(1, tag);
            ps.setInt(2, rank);
            ps.setString(3, perm);
            mysql.runAsyncUpdate(ps);
        }
    }

    public List<String> getPerms (String tag, int rank) {
        List<String> list = new ArrayList<String>();

        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM PERMISSIONS TAG = ? AND RANK = ?");
            ps.setString(1, tag);
            ps.setInt(2, rank);
            try {

                ResultSet rs = mysql.runAsyncQuery(ps);
                while (rs.next()) {
                    list.add(rs.getString("PERM"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                mysql.closeConnections(ps);
            }
        }

        return list;
    }

    public boolean hasPerm (String tag, int rank, String perm) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PERM FROM PERMISSIONS WHERE TAG = ? AND RANK = ?");
            ps.setString(1, tag);
            ps.setInt(2, rank);

            return perm.equals(mysql.getString(ps, "PERM"));
        }
        return false;
    }

    public void deletePerm (String tag, int rank, String perm) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM PERMISSIONS WHERE TAG = ? AND RANK = ? AND PERM = ?");
            ps.setString(1, tag);
            ps.setInt(2, rank);
            ps.setString(3, perm);

            mysql.runAsyncUpdate(ps);
        }
    }

    public void deletePerms (String tag, int rank) {
        if (isRankExists(tag, rank)) {
            for (String perm : getPerms(tag, rank)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM PERMISSIONS WHERE TAG = ? AND RANK = ? AND PERM = ?");
                ps.setString(1, tag);
                ps.setInt(2, rank);
                ps.setString(3, perm);

                mysql.runAsyncUpdate(ps);
            }
        }
    }

    public void renameClan (String tag, String newTag, int rank) {
        if (isTagExists(tag)) {
            for (String perm : getPerms(tag, rank)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE PERMISSIONS SET TAG = ? WHERE TAG = ? AND RANK = ? AND PERM = ?");
                ps.setString(1, newTag);
                ps.setString(2, tag);
                ps.setInt(3, rank);
                ps.setString(4, perm);

                mysql.runAsyncUpdate(ps);
            }
        }
    }
}
