package net.endertime.enderapi.clan.mysql;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.util.UUID;

public class ClanSettings {

    private MySQL mysql = Clans.mysql;

    public boolean isUserExists (UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM SETTINGS WHERE UUID = ?");
        ps.setString(1, uuid.toString());

        return mysql.isInDatabase(ps);
    }

    public boolean isNameExists (String name) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM SETTINGS WHERE NAME = ?");
        ps.setString(1, name);

        return mysql.isInDatabase(ps);
    }

    public void createUser (UUID uuid, String name) {
        if (!isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO SETTINGS (UUID, NAME, TOGGLE, MSG, TAG) " +
                    "VALUES (?, ?, 1, 1, 1)");
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            mysql.runAsyncUpdate(ps);
        }
    }

    public void updateToggle(UUID uuid, int wert) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE SETTINGS SET TOGGLE = ? WHERE UUID = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getToggle(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLE FROM SETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"TOGGLE");
        }
        return 0;
    }

    public void updateName(UUID uuid, String name) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE SETTINGS SET NAME = ? WHERE UUID = ?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getName(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM SETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps,"NAME");
        }
        return null;
    }

    public UUID getUUID(String name) {
        if (isNameExists(name)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM SETTINGS WHERE NAME = ?");
            ps.setString(1, name);
            return UUID.fromString(mysql.getString(ps,"UUID"));
        }
        return null;
    }

    public void updateMSG(UUID uuid, int wert) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE SETTINGS SET MSG = ? WHERE UUID = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getMSG(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MSG FROM SETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"MSG");
        }
        return 0;
    }

}
