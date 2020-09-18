package net.endertime.enderapi.clan.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Member {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public boolean isTagExists (String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MEMBER FROM MEMBER WHERE TAG = ?");
        ps.setString(1, tag);

        return mysql.isInDatabase(ps);
    }

    public boolean isUserExists (UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TAG FROM MEMBER WHERE MEMBER = ?");
        ps.setString(1, uuid.toString());

        return mysql.isInDatabase(ps);
    }

    public void createMember (String tag, UUID uuid, int rang) {
        if (!isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO MEMBER (TAG, MEMBER, RANG, MUTE) " +
                    "VALUES (?, ?, ?, 0)");
            ps.setString(1, tag);
            ps.setString(2, uuid.toString());
            ps.setInt(3, rang);

            mysql.runAsyncUpdate(ps);
        }
    }

    public List<UUID> getMembers (String tag) {
        List<UUID> list = new ArrayList<UUID>();

        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MEMBER FROM MEMBER WHERE TAG = ?");
            ps.setString(1, tag);
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

    public List<UUID> getMembers (String tag, int value) {
        List<UUID> list = new ArrayList<UUID>();
            if (isTagExists(tag)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MEMBER FROM MEMBER WHERE TAG = ? AND RANG = ?");
                ps.setString(1, tag);
                ps.setInt(2, value);

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

    public void updateRang(UUID uuid, int wert) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE MEMBER SET RANG = ? WHERE MEMBER = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());

            mysql.runAsyncUpdate(ps);
        }
    }

    public int getRang(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT RANG FROM MEMBER WHERE MEMBER = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"RANG");
        }
        return 0;
    }

    public UUID getLeader(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MEMBER FROM MEMBER WHERE TAG = ? AND RANG = ?");
            ps.setString(1, tag);
            ps.setInt(2, 1);
            return UUID.fromString(mysql.getString(ps,"MEMBER"));
        }
        return null;
    }

    public void updateMute(UUID uuid, int wert) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE MEMBER SET MUTE = ? WHERE MEMBER = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());

            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getMute(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MUTE FROM MEMBER WHERE MEMBER = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"MUTE");
        }
        return 0;
    }

    public String getTag(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TAG FROM MEMBER WHERE MEMBER = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps,"TAG");
        }
        return null;
    }

    public void deleteMember (String tag, UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM MEMBER WHERE TAG = ? AND MEMBER = ?");
            ps.setString(1, tag);
            ps.setString(2, uuid.toString());

            mysql.runAsyncUpdate(ps);
        }
    }

    public void deleteMembers (String tag) {
        if (isTagExists(tag)) {
            for (UUID uuid : getMembers(tag)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM MEMBER WHERE TAG = ? AND MEMBER = ?");
                ps.setString(1, tag);
                ps.setString(2, uuid.toString());

                mysql.runAsyncUpdate(ps);
            }
        }
    }

    public void renameClan (String tag, String newTag) {
        if (isTagExists(tag)) {
            for (UUID uuid : getMembers(tag)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE MEMBER SET TAG = ? WHERE TAG = ? AND MEMBER = ?");
                ps.setString(1, newTag);
                ps.setString(2, tag);
                ps.setString(3, uuid.toString());
                mysql.runAsyncUpdate(ps);
            }
        }
    }
}
