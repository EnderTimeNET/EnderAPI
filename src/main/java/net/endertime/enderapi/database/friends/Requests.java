package net.endertime.enderapi.database.friends;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Requests {

    public static MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public void createRequests (UUID uuid1, UUID uuid2) {
        if (!isRequests(uuid1, uuid2)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO REQUESTSREWORK (UUID1, UUID2) VALUES (?,?)");
            ps.setString(1, uuid1.toString());
            ps.setString(2, uuid2.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean isRequests (UUID uuid1, UUID uuid2) {
        return isRequests1(uuid1, uuid2) || isRequests2(uuid1, uuid2);
    }

    private boolean isRequests1 (UUID uuid1, UUID uuid2) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM REQUESTSREWORK WHERE UUID1 = ? AND UUID2 = ?");
        ps.setString(1, uuid1.toString());
        ps.setString(2, uuid2.toString());
        return mysql.isInDatabase(ps);
    }

    private boolean isRequests2 (UUID uuid1, UUID uuid2) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM REQUESTSREWORK WHERE UUID2 = ? AND UUID1 = ?");
        ps.setString(1, uuid1.toString());
        ps.setString(2, uuid2.toString());
        return mysql.isInDatabase(ps);
    }

    public boolean isRequestsForCMD (UUID uuid1, UUID uuid2) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM REQUESTSREWORK WHERE UUID1 = ? AND UUID2 = ?");
        ps.setString(1, uuid2.toString());
        ps.setString(2, uuid1.toString());
        return mysql.isInDatabase(ps);
    }

    public int getRequestsCount (UUID uuid) {
        return getRequests(uuid).size();
    }

    public List<UUID> getRequests (UUID uuid) {
        List<UUID> list = new ArrayList<UUID>();

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID1 FROM REQUESTSREWORK WHERE UUID2 = ?");
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

        return list;
    }

    public void removeRequests (UUID uuid1, UUID uuid2) {
        if (isRequests(uuid1, uuid2)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM REQUESTSREWORK WHERE UUID1 = ? AND UUID2 = ?");
            ps.setString(1, uuid1.toString());
            ps.setString(2, uuid2.toString());
            mysql.runAsyncUpdate(ps);
            ps.setString(1, uuid2.toString());
            ps.setString(2, uuid1.toString());
            mysql.runAsyncUpdate(ps);
        }
    }
}
