package net.endertime.enderapi.database.enderapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EnderDatabase {

    private DataBaseAPI api = DataBaseAPI.getInstance();

    public DataBaseAPI getAPI() {
        return api;
    }

    private MySQL mysql = getAPI().getMySQL("ENDERDATABASE");

    public MySQL getMysql() {
        return mysql;
    }

    public void createTable() {
        PreparedStatement ps = getAPI().getPreparedStatement("CREATE TABLE IF NOT EXISTS ENDERAPI (UUID VARCHAR(100), NAME VARCHAR(100)," +
                " COINS INT(255), POINTS INT(255), VALUE TEXT(20000), SIGNATURE TEXT(20000))");
        getMysql().runAsyncUpdate(ps);
    }

    public void updateRanksSpigot(UUID uuid, String rank) {
        PreparedStatement ps = new PreparedStatement("UPDATE TEAMSPEAKBOT SET RANKS = ? WHERE UUID = ?");
        ps.setString(1, rank);
        ps.setString(2, uuid.toString());
        getMysql().runAsyncUpdate(ps);
    }

    public boolean isUserExists (UUID uuid) {
        PreparedStatement ps = getAPI().getPreparedStatement("SELECT * FROM ENDERAPI WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return getMysql().isInDatabase(ps);
    }

    public void updateOnJoin (Player player) {
        if (!isUserExists(player.getUniqueId())) {
            createUser(player);
        } else {
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

            GameProfile gameProfile = entityPlayer.getProfile();

            Property property = gameProfile.getProperties().get("textures").iterator().next();

            updateName(player.getUniqueId(), player.getName());
            updateValue(player.getUniqueId(), property.getValue());
            updateSignature(player.getUniqueId(), property.getSignature());
        }
    }

    public void createUser (Player player) {
        if (!isUserExists(player.getUniqueId())) {
            PreparedStatement ps = getAPI().getPreparedStatement("INSERT INTO ENDERAPI (UUID, NAME, COINS, POINTS, VALUE, SIGNATURE) VALUES (?,?,0,0,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());

            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

            GameProfile gameProfile = entityPlayer.getProfile();

            Property property = gameProfile.getProperties().get("textures").iterator().next();

            ps.setString(3, property.getValue());
            ps.setString(4, property.getSignature());

            getMysql().runAsyncUpdate(ps);
        }
    }

    public void createUser (UUID uuid) {
        if (!isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("INSERT INTO ENDERAPI (UUID, NAME, COINS, POINTS, VALUE, SIGNATURE) VALUES (?,?,0,0,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, "");
            ps.setString(3, "");
            ps.setString(4, "");

            getMysql().runAsyncUpdate(ps);
        }
    }

    public int getCoins(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("SELECT COINS FROM ENDERAPI WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"COINS");
        }
        return 0;
    }

    public void updateCoins(UUID uuid, int wert) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("UPDATE ENDERAPI SET COINS = ? WHERE UUID = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public int getPoints(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("SELECT POINTS FROM ENDERAPI WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"POINTS");
        }
        return 0;
    }

    public void updatePoints(UUID uuid, int wert) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("UPDATE ENDERAPI SET POINTS = ? WHERE UUID = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public void updateName(UUID uuid, String name) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("UPDATE ENDERAPI SET NAME = ? WHERE UUID = ?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getName(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("SELECT NAME FROM ENDERAPI WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return getMysql().getString(ps, "NAME");
        }
        return null;
    }

    public void updateSignature(UUID uuid, String signature) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("UPDATE ENDERAPI SET SIGNATURE = ? WHERE UUID = ?");
            ps.setString(1, signature);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getSignature(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("SELECT SIGNATURE FROM ENDERAPI WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return getMysql().getString(ps, "SIGNATURE");
        }
        return null;
    }

    public void updateValue(UUID uuid, String value) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("UPDATE ENDERAPI SET VALUE = ? WHERE UUID = ?");
            ps.setString(1, value);
            ps.setString(2, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public String getValue(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = getAPI().getPreparedStatement("SELECT VALUE FROM ENDERAPI WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return getMysql().getString(ps, "VALUE");
        }
        return null;
    }

    public int countRows() {
        int i = 0;
        PreparedStatement ps = getAPI().getPreparedStatement("SELECT * FROM ENDERAPI");

        ResultSet rs = getMysql().runAsyncQuery(ps);

        try {
            while (rs.next()) {
                i++;
            }
        } catch (Exception localException) {
        } finally {
            mysql.closeConnections(ps);
        }
        return i;
    }

    public boolean isUserExists(String name) {
        PreparedStatement ps = new PreparedStatement("SELECT POINTS FROM ENDERAPI WHERE NAME = ?");
        ps.setString(1, name);
        return mysql.isInDatabase(ps);
    }

    public UUID getUUID(String name) {
        if (isUserExists(name)) {
            PreparedStatement ps = new PreparedStatement("SELECT UUID FROM ENDERAPI WHERE NAME = ?");
            ps.setString(1, name);
            return UUID.fromString(mysql.getString(ps, "UUID"));
        }
        return null;
    }

    public void resetCoins(UUID uuid) {
        if (isUserExists(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE ENDERAPI SET COINS = 0 WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            getMysql().runAsyncUpdate(ps);
        }
    }

    public void a() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM COINS");

        MySQL mySQLNew = new MySQL("COINDATABASE");

        ResultSet rs = mySQLNew.runAsyncQuery(ps);

        try{
            while (rs.next()) {
                try {
                    UUID uuid = UUID.fromString(rs.getString("UUID"));
                    int coins = rs.getInt("COINS");

                    createUser(uuid);
                    updateCoins(uuid, coins);
                } catch (IllegalArgumentException e) {
                }
            }
        } catch (SQLException e) {
        } finally {
            mySQLNew.closeConnections(ps);
        }

        mySQLNew = new MySQL("POINTSDATABASE");

        ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM POINTSDATA");

        rs = mySQLNew.runAsyncQuery(ps);

        try{
            while (rs.next()) {
                try {
                    UUID uuid = UUID.fromString(rs.getString("UUID"));
                    int points = rs.getInt("POINTS");
                    String name = rs.getString("NAME");

                    updatePoints(uuid, points);
                    updateName(uuid, name);
                } catch (IllegalArgumentException e) {
                }
            }
        } catch (SQLException e) {
        } finally {
            mySQLNew.closeConnections(ps);
        }

        mySQLNew = new MySQL("SKULLDATABASE");

        ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM DATA");

        rs = mySQLNew.runAsyncQuery(ps);

        try{
            while (rs.next()) {
                try {
                    UUID uuid = UUID.fromString(rs.getString("UUID"));
                    String signature  = rs.getString("SIGNATURE");
                    String value = rs.getString("VALUE");

                    updateValue(uuid, value);
                    updateSignature(uuid, signature);
                } catch (IllegalArgumentException e) {
                }
            }
        } catch (SQLException e) {
        } finally {
            mySQLNew.closeConnections(ps);
        }
    }

}
