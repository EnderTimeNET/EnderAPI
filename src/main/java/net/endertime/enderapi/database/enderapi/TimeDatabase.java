package net.endertime.enderapi.database.enderapi;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimeDatabase {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public long getTimeByHours(UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TIME FROM ONLINETIME WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            if(rs.next()) {
                return TimeUnit.HOURS.convert(rs.getLong("TIME"), TimeUnit.MILLISECONDS);
            }
        } catch (SQLException e) {
        } finally {
            mysql.closeConnections(ps);
        }
        return 0;
    }

}
