package net.endertime.enderapi.database.enderapi;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.util.UUID;

public class LobbyDatabase {

    private MySQL mySQL = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public MySQL getMySQL() {
        return mySQL;
    }

    public boolean isScoreboard(UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT SCOREBOARD FROM LOBBYSETTINGS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return getMySQL().getBoolean(ps, "SCOREBOARD");
    }
}
