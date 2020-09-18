package net.endertime.enderapi.clan.mysql;

import net.endertime.enderapi.clan.ClanAPI;
import net.endertime.enderapi.clan.utils.Rank;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ranks {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public boolean isTagExists (String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANRANKS WHERE TAG = ?");
        ps.setString(1, tag);

        return mysql.isInDatabase(ps);
    }

    public boolean isRankExists (String tag, String rank) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANRANKS WHERE TAG = ? AND NAME = ?");
        ps.setString(1, tag);
        ps.setString(2, rank);

        return mysql.isInDatabase(ps);
    }

    public boolean isRankExists (String tag, int value) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANRANKS WHERE TAG = ? AND VALUE = ?");
        ps.setString(1, tag);
        ps.setInt(2, value);

        return mysql.isInDatabase(ps);
    }

    public void creatRank (String tag, String rank, int value, String prefix, int itemID) {
        if (!isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO CLANRANKS (TAG, NAME, VALUE, PREFIX, ITEMID) " +
                    "VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, tag);
            ps.setString(2, rank);
            ps.setInt(3, value);
            ps.setString(4, prefix);
            ps.setInt(5, itemID);

            mysql.runAsyncUpdate(ps);
        }
    }

    public String getRank (String tag, int value) {
        if (isRankExists(tag, value)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANRANKS WHERE TAG = ? AND VALUE = ?");
            ps.setString(1, tag);
            ps.setInt(2, value);

            return mysql.getString(ps,"NAME");
        }
        return null;
    }

    public List<Rank> getRanks (String tag) {
        List<Rank> list = new ArrayList<Rank>();

        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANRANKS WHERE TAG = ? ORDER BY VALUE ASC");
            ps.setString(1, tag);

            try {
                ResultSet rs = mysql.runAsyncQuery(ps);
                while (rs.next()) {
                    list.add(new Rank(tag, rs.getString("NAME")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                mysql.closeConnections(ps);
            }
        }

        return list;
    }

    public List<UUID> getPlayers (Rank rank) {
        List<UUID> list = new ArrayList<UUID>();

        if (isRankExists(rank.getTag(), rank.getRank())) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MEMBER FROM MEMBER WHERE TAG = ? AND RANG = ?");
            ps.setString(1, rank.getTag());
            ps.setInt(2, rank.getValue());
            try {

                ResultSet rs = mysql.runAsyncQuery(ps);
                while (rs.next()) {
                    list.add(UUID.fromString(rs.getString("MEMBER")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                mysql.closeConnections(ps);
            }
        }

        return list;
    }

    public void updateItemID(String tag, String rank, int wert) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANRANKS SET ITEMID = ? WHERE TAG = ? AND NAME = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);
            ps.setString(3, rank);
            mysql.runAsyncUpdate(ps);
        }
    }

    public int getItemId (String tag, String rank) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT ITEMID FROM CLANRANKS WHERE TAG = ? AND NAME = ?");
            ps.setString(1, tag);
            ps.setString(2, rank);

            return mysql.getInt(ps,"ITEMID");
        }
        return 0;
    }

    public String getPrefix (String tag, String rank) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PREFIX FROM CLANRANKS WHERE TAG = ? AND NAME = ?");
            ps.setString(1, tag);
            ps.setString(2, rank);

            return mysql.getString(ps,"PREFIX");
        }
        return null;
    }

    public void updatePrefix(String tag, String rank, String wert) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANRANKS SET PREFIX = ? WHERE TAG = ? AND NAME = ?");
            ps.setString(1, wert);
            ps.setString(2, tag);
            ps.setString(3, rank);
            mysql.runAsyncUpdate(ps);
        }
    }

    public int getValue (String tag, String rank) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT VALUE FROM CLANRANKS WHERE TAG = ? AND NAME = ?");
            ps.setString(1, tag);
            ps.setString(2, rank);

            return mysql.getInt(ps,"VALUE");
        }
        return 0;
    }

    public void updateValue(String tag, String rank, int wert) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANRANKS SET VALUE = ? WHERE TAG = ? AND NAME = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);
            ps.setString(3, rank);
            mysql.runAsyncUpdate(ps);
        }
    }

    public void deleteRank (String tag, String rank) {
        if (isRankExists(tag, rank)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM CLANRANKS WHERE TAG = ? AND NAME = ?");
            ps.setString(1, tag);
            ps.setString(2, rank);
            mysql.runAsyncUpdate(ps);
            ClanAPI.getInstance().getPermissions().deletePerms(tag, getValue(tag, rank));
        }
    }

    public void deleteRanks (String tag) {
        if (isTagExists(tag)) {
            for (Rank rank : getRanks(tag)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM CLANRANKS WHERE TAG = ? AND NAME = ?");
                ps.setString(1, tag);
                ps.setString(2, rank.getRank());
                mysql.runAsyncUpdate(ps);
                ClanAPI.getInstance().getPermissions().deletePerms(tag, rank.getValue());
            }
        }
    }

    public void renameClan (String tag, String newTag) {
        if (isTagExists(tag)) {
            for (Rank rank : getRanks(tag)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANRANKS SET TAG = ? WHERE TAG = ? AND NAME = ?");
                ps.setString(1, newTag);
                ps.setString(2, tag);
                ps.setString(3, rank.getRank());
                mysql.runAsyncUpdate(ps);
                ClanAPI.getInstance().getPermissions().renameClan(tag, newTag, rank.getValue());
            }
        }
    }
}
