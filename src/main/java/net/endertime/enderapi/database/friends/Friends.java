package net.endertime.enderapi.database.friends;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Friends {

    public static MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public void createTable () {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS FRIENDSREWORK (UUID1 VARCHAR(100), UUID2 VARCHAR(100))");
        mysql.runAsyncUpdate(ps);

        ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS REQUESTSREWORK (UUID1 VARCHAR(100), UUID2 VARCHAR(100))");
        mysql.runAsyncUpdate(ps);

        ps = DataBaseAPI.getInstance().getPreparedStatement("CREATE TABLE IF NOT EXISTS FRIENDSETTINGS (UUID VARCHAR(100), TOGGLE TINYINT(1), TOGGLENOTIFY TINYINT(1)," +
                " TOGGLEMESSAGE TINYINT(1), TOGGLEJUMP TINYINT(1), TOGGLESEEOFFLINE TINYINT(1), PARTYTOGGLE TINYINT(1), ONLINE TINYINT(1), SERVER VARCHAR(100), " +
                "LASTONLINE VARCHAR(100), SPEED TINYINT(1), SILENTLOBBY TINYINT(1), SCOREBOARD TINYINT(1), PLAYER TINYINT(1), ENDER TINYINT(1), FRIENDS TINYINT(1)," +
                " YOUTUBER TINYINT(1), PARTNER TINYINT(1), TEAM TINYINT(1), INV TINYINT(1), HIDE TINYINT(1), TIME TINYINT(1), ENDERPLUS TINYINT(1), STACK TINYINT(1))");
        mysql.runAsyncUpdate(ps);
    }

    public void createFriend (UUID uuid1, UUID uuid2) {
        if (!isFriend(uuid1, uuid2)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO FRIENDSREWORK (UUID1, UUID2) VALUES (?,?)");
            ps.setString(1, uuid1.toString());
            ps.setString(2, uuid2.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean isFriend (UUID uuid1, UUID uuid2) {
        return isFriend1(uuid1, uuid2) || isFriend2(uuid1, uuid2);
    }

    private boolean isFriend1 (UUID uuid1, UUID uuid2) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM FRIENDSREWORK WHERE UUID1 = ? AND UUID2 = ?");
        ps.setString(1, uuid1.toString());
        ps.setString(2, uuid2.toString());
        return mysql.isInDatabase(ps);
    }

    private boolean isFriend2 (UUID uuid1, UUID uuid2) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM FRIENDSREWORK WHERE UUID2 = ? AND UUID1 = ?");
        ps.setString(1, uuid1.toString());
        ps.setString(2, uuid2.toString());
        return mysql.isInDatabase(ps);
    }

    public int getFriendsCount (UUID uuid) {
        return getFriends(uuid).size();
    }

    public List<UUID> getFriends (UUID uuid) {
        List<UUID> list = new ArrayList<UUID>();

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM FRIENDSREWORK WHERE UUID2 = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);

        try {
            while (rs.next()) {
                list.add(UUID.fromString(rs.getString("UUID1")));
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }

        ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID2 FROM FRIENDSREWORK WHERE UUID1 = ?");
        ps.setString(1, uuid.toString());
        rs = mysql.runAsyncQuery(ps);

        try {
            while (rs.next()) {
                list.add(UUID.fromString(rs.getString("UUID2")));
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }

        return list;
    }

    public void removeFriends (UUID uuid1, UUID uuid2) {
        if (isFriend(uuid1, uuid2)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM FRIENDSREWORK WHERE UUID1 = ? AND UUID2 = ?");
            ps.setString(1, uuid1.toString());
            ps.setString(2, uuid2.toString());
            mysql.runAsyncUpdate(ps);
            ps.setString(1, uuid2.toString());
            ps.setString(2, uuid1.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public void übertrag (){
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM FRIENDSREWORK");

        MySQL mySQLNew = new MySQL("FRIENDPARTYDATABASE");
        ResultSet rs = mysql.runAsyncQuery(ps);

        try {
            while (rs.next()) {
                UUID uuid1 = UUID.fromString(rs.getString("UUID1"));
                UUID uuid2 = UUID.fromString(rs.getString("UUID2"));

                removeFriends(uuid1, uuid2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM FRIENDS");

        rs = mySQLNew.runAsyncQuery(ps);

        try {
            while (rs.next()) {
                UUID uuid1 = UUID.fromString(rs.getString("USERUUID"));
                UUID uuid2 = UUID.fromString(rs.getString("FRIENDUUID"));

                createFriend(uuid1, uuid2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
