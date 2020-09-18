package net.endertime.enderapi.clan.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invites {

    private MySQL mysql = Clans.mysql;

    public boolean isTagExists (UUID uuid, String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM INVITES WHERE UUID = ? AND TAG = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, tag);

        return mysql.isInDatabase(ps);
    }

    public boolean isUserExists (UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM INVITES WHERE UUID = ?");
        ps.setString(1, uuid.toString());

        return mysql.isInDatabase(ps);
    }

    public boolean isTagExists (String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM INVITES WHERE TAG = ?");
        ps.setString(1, tag);

        return mysql.isInDatabase(ps);
    }

    public void createUser (UUID uuid, String tag) {
        if (!isTagExists(uuid, tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO INVITES (UUID, TAG) " +
                    "VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public List<String> getInvites (UUID uuid) {
        List<String> list = new ArrayList<String>();

        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TAG FROM INVITES WHERE UUID = ?");
            ps.setString(1, uuid.toString());

            try {
                ResultSet rs = mysql.runAsyncQuery(ps);

                while (rs.next()) {
                    list.add(rs.getString("TAG"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                mysql.closeConnections(ps);
            }
        }
        return list;
    }

    public List<UUID> getInvites (String tag) {
        List<UUID> list = new ArrayList<UUID>();

        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM INVITES WHERE TAG = ?");
            ps.setString(1, tag);

            try {
                ResultSet rs = mysql.runAsyncQuery(ps);

                while (rs.next()) {
                    list.add(UUID.fromString(rs.getString("UUID")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                mysql.closeConnections(ps);
            }
        }
        return list;
    }

    public void deleteInvites (UUID uuid) {
        for (String tag : getInvites(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM INVITES WHERE UUID = ? AND TAG = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
            }
    }

    public void deleteInvite (UUID uuid, String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM INVITES WHERE UUID = ? AND TAG = ?");
        ps.setString(1, uuid.toString());
        ps.setString(2, tag);

        mysql.runAsyncUpdate(ps);
    }

    public void deleteInvites (String tag) {
        for (UUID uuid : getInvites(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM INVITES WHERE UUID = ? AND TAG = ?");
            ps.setString(1, uuid.toString());
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public void renameClan (String tag, String newTag) {
        if (isTagExists(tag)) {
            for (UUID uuid : getInvites(tag)) {
                PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement
                        ("UPDATE INVITES SET TAG = ? WHERE TAG = ? AND UUID = ?");
                ps.setString(1, newTag);
                ps.setString(2, tag);
                ps.setString(3, uuid.toString());

                mysql.runAsyncUpdate(ps);
            }
        }
    }

}
