package net.endertime.enderapi.database.friends;

import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FriendSettings {

    public static MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");

    public boolean isUserExist(UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLE FROM FRIENDSETTINGS WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.isInDatabase(ps);
    }

    public void createUser(UUID uuid, String service) {
        if (!isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO FRIENDSETTINGS " +
                    "(UUID, TOGGLE, TOGGLENOTIFY, TOGGLEMESSAGE, TOGGLEJUMP, TOGGLESEEOFFLINE, PARTYTOGGLE, ONLINE," +
                    " SERVER, LASTONLINE) VALUES(?,0,0,0,0,0,0,1,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, service);
            ps.setString(3, new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
            mysql.runAsyncUpdate(ps);
        }
    }

    public void updateLastOnline(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET LASTONLINE = ? WHERE UUID = ?");
            ps.setString(1, new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getLastOnline(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT LASTONLINE FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps,"LASTONLINE");
        }
        return "unbekannt";
    }

    public void updateServer(UUID uuid, String server) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET SERVER = ? WHERE UUID = ?");
            ps.setString(1, server);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public String getServer(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT SERVER FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getString(ps,"SERVER");
        }
        return "unbekannt";
    }

    public void updateOnline(UUID uuid, boolean wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET ONLINE = ? WHERE UUID = ?");
            ps.setBoolean(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean getOnline(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT ONLINE FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps,"ONLINE");
        }
        return false;
    }

    public void updatePartyToggle(UUID uuid, int wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET PARTYTOGGLE = ? WHERE UUID = ?");
            ps.setInt(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public int getPartyToggle(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT PARTYTOGGLE FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getInt(ps,"PARTYTOGGLE");
        }
        return 0;
    }

    public void updateToggle(UUID uuid, boolean wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET TOGGLE = ? WHERE UUID = ?");
            ps.setBoolean(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean getToggle(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLE FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps,"TOGGLE");
        }
        return false;
    }

    public void updateToggleOffline(UUID uuid, boolean wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET TOGGLESEEOFFLINE = ? WHERE UUID = ?");
            ps.setBoolean(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean getToggleOffline(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLESEEOFFLINE FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps,"TOGGLESEEOFFLINE");
        }
        return false;
    }

    public void updateToggleNotify(UUID uuid, boolean wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET TOGGLENOTIFY = ? WHERE UUID = ?");
            ps.setBoolean(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean getToggleNotify(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLENOTIFY FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps,"TOGGLENOTIFY");
        }
        return false;
    }

    public void updateToggleMessage(UUID uuid, boolean wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET TOGGLEMESSAGE = ? WHERE UUID = ?");
            ps.setBoolean(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean getToggleMessage(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLEMESSAGE FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps,"TOGGLEMESSAGE");
        }
        return false;
    }

    public void updateToggleJump(UUID uuid, boolean wert) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE FRIENDSETTINGS SET TOGGLEJUMP = ? WHERE UUID = ?");
            ps.setBoolean(1, wert);
            ps.setString(2, uuid.toString());
            mysql.runAsyncUpdate(ps);
        }
    }

    public boolean getToggleJump(UUID uuid) {
        if (isUserExist(uuid)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TOGGLEJUMP FROM FRIENDSETTINGS WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            return mysql.getBoolean(ps,"TOGGLEJUMP");
        }
        return false;
    }

}
