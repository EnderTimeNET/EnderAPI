package net.endertime.enderapi.database.enderapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.endertime.enderapi.spigot.api.EnderAPI;

import java.sql.ResultSet;
import java.util.UUID;

public class TeamDatabase {

    private DataBaseAPI api = DataBaseAPI.getInstance();

    public DataBaseAPI getAPI() {
        return api;
    }

    private MySQL mysql = getAPI().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public void deleteTeam(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM TEAMS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public GameProfile getProfile(UUID uuid) {
        if (isUserExist(uuid)) {
            String name = EnderAPI.getInstance().getEnderDatabase().getName(uuid);
            String value = EnderAPI.getInstance().getEnderDatabase().getValue(uuid);
            String signature = EnderAPI.getInstance().getEnderDatabase().getSignature(uuid);
            GameProfile profile = new GameProfile(uuid, name);
            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", value, signature));
            return profile;
        }
        return null;
    }

    public void updateState(UUID uuid, boolean state) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE TEAMS SET STATE = ? WHERE UUID = ?");
            ps.setBoolean(1, state);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean isState(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT STATE FROM TEAMS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps, "STATE");
        }
        return false;
    }

    public void updateRandom(UUID uuid, boolean state) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE TEAMS SET RANDOM = ? WHERE UUID = ?");
            ps.setBoolean(1, state);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean isRandom(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT RANDOM FROM TEAMS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps, "RANDOM");
        }
        return false;
    }

    public void updatePremium(UUID uuid, boolean state) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE TEAMS SET PREMIUM = ? WHERE UUID = ?");
            ps.setBoolean(1, state);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean isPremium(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PREMIUM FROM TEAMS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps, "PREMIUM");
        }
        return false;
    }

    public String getPrefix (UUID uuid) {
        if (isPremium(uuid))
            return "§6";
        return "§7";
    }

    public UUID getUUIDFromNickedName (String name) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM TEAMS WHERE NICKEDNAME = ?");
        ps.setString(1, name);
        return UUID .fromString(mysql.getString(ps, "UUID"));
    }

    public synchronized boolean isNicked (String name) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NICKEDNAME FROM TEAMS");
        try {
            ResultSet rs = mysql.runAsyncQuery(ps);

            while (rs.next()) {
                if (rs.getString("NICKEDNAME").equalsIgnoreCase(name))
                    return true;
            }
        } catch (Exception e) {
        } finally {
            mysql.closeConnections(ps);
        }
        return false;
    }

    public void updateNickedName(UUID uuid, String name) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE TEAMS SET NICKEDNAME = ? WHERE UUID = ?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getNickedName(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NICKEDNAME FROM TEAMS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps, "NICKEDNAME");
        }
        return null;
    }

    public boolean isUserExist(UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM TEAMS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public void createUser(UUID uuid) {
        if (!isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO TEAMS (UUID," +
                    " STATE, NICKEDNAME, RANDOM, PREMIUM) VALUES(?,?,?,?,?)");
            ps.setString(1, uuid.toString());
            ps.setBoolean(2, false);
            ps.setString(3, "");
            ps.setBoolean(4, true);
            ps.setBoolean(5, false);
            mysql.runAsyncUpdate(ps);
        }
    }
}
