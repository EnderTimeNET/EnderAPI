package net.endertime.enderapi.database.enderapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.util.UUID;

public class NickDatabase {

    private DataBaseAPI api = DataBaseAPI.getInstance();

    public DataBaseAPI getAPI() {
        return api;
    }

    private MySQL mysql = getAPI().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public synchronized int countRowsInTable() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT COUNT(*) FROM NICKS");
        try {
            ResultSet rs = mysql.runAsyncQuery(ps);
            rs.first();
            int numberOfRows = rs.getInt("COUNT(*)");

            return numberOfRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            mysql.closeConnections(ps);
        }
    }

    public String getRandomName () {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM NICKS WHERE STATE = 0 ORDER BY RAND() LIMIT 1");
        try {
            ResultSet rs = mysql.runAsyncQuery(ps);
            rs.first();

            return rs.getString("NAME");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            mysql.closeConnections(ps);
        }
    }

    public GameProfile getProfile(String name) {
        if (isUserExist(name)) {
            UUID uuid = getUUID(name);
            String value = getValue(uuid);
            String signature = getSignature(uuid);
            GameProfile profile = new GameProfile(uuid, name);
            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", value, signature));
            return profile;
        }
        return null;
    }

    public void updateValue(UUID uuid, String value) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE NICKS SET VALUE = ? WHERE UUID = ?");
            ps.setString(1, value);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getValue(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT VALUE FROM NICKS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps, "VALUE");
        }
        return null;
    }

    public void updateSignature(UUID uuid, String signature) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE NICKS SET SIGNATURE = ? WHERE UUID = ?");
            ps.setString(1, signature);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getSignature(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT SIGNATURE FROM NICKS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps,"SIGNATURE");
        }
        return null;
    }

    public void updateName(UUID uuid, String name) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE NICKS SET NAME = ? WHERE UUID = ?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getName(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM NICKS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps, "NAME");
        }
        return null;
    }

    public void updateState(UUID uuid, boolean b) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE NICKS SET STATE = ? WHERE UUID = ?");
            ps.setBoolean(1, b);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public void resetAllNicks() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE NICKS SET STATE = 0 WHERE STATE = 1");
        mysql.runAsyncUpdate(ps);
    }

    public boolean getState(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT STATE FROM NICKS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps, "STATE");
        }
        return true;
    }

    public boolean isUserExist(UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT VALUE FROM NICKS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public boolean isUserExist(String name) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM NICKS WHERE NAME = ?");
        ps.setString(1, name);
        return mysql.isInDatabase(ps);
    }

    public void createUser(UUID uuid, String name, String signature, String value) {
        if (!isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO NICKS (UUID, NAME, SIGNATURE, VALUE, STATE) VALUES(?,?,?,?, 0)");
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.setString(3, signature);
            ps.setString(4, value);
            mysql.runAsyncUpdate(ps);
        }
    }

    public void createTable() {
        PreparedStatement ps = getAPI().getPreparedStatement("CREATE TABLE IF NOT EXISTS NICKS (UUID VARCHAR(100), NAME VARCHAR(20), SIGNATURE TEXT(20000)" +
                ", VALUE TEXT(20000), STATE INT(10))");
        getMysql().runAsyncUpdate(ps);
    }

    public UUID getUUID(String name) {
        if (isUserExist(name)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM NICKS WHERE NAME = ?");
            ps.setString(1, name);
            return UUID.fromString(mysql.getString(ps, "UUID"));
        }
        return null;
    }

    public void deleteNick(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM NICKS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }
}
