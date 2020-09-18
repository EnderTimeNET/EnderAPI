package net.endertime.enderapi.clan.mysql;

import net.endertime.enderapi.clan.ClanAPI;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Clans {

    private MySQL mysql = DataBaseAPI.getInstance().getMySQL("ENDERDATABASE");
    private int member = 10;

    public MySQL getMysql() {
        return mysql;
    }

    public boolean isTagExists (String tag) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANS WHERE TAG = ?");
        ps.setString(1, tag);

        return mysql.isInDatabase(ps);
    }

    public List<String> getClans () {
        List<String> list = new ArrayList<String>();

        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM CLANS");
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

        return list;
    }

    public boolean isNameExists (String name) {
        if (name != null) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TAG FROM CLANS WHERE NAME = ?");
            ps.setString(1, name);

            return mysql.isInDatabase(ps);
        }
        return false;
    }

    public void updateName(String tag, String name) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET NAME = ? WHERE TAG = ?");
            ps.setString(1, name);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public String getNameName(String name) {
        if (isNameExists(name)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANS WHERE NAME = ?");
            ps.setString(1, name);

            return mysql.getString(ps,"NAME");
        }
        return null;
    }

    public String getName(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT NAME FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getString(ps,"NAME");
        }
        return null;
    }

    public void updateTag(String tag, String name) {
        if (isNameExists(name)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET TAG = ? WHERE NAME = ?");
            ps.setString(1, tag);
            ps.setString(2, name);

            mysql.runAsyncUpdate(ps);
        }
    }

    public String getTag(String name) {
        if (isNameExists(name)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT TAG FROM CLANS WHERE NAME = ?");
            ps.setString(1, name);

            return mysql.getString(ps,"TAG");
        }
        return null;
    }

    public void updateCoins(String tag, int wert) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET COINS = ? WHERE TAG = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getCoins(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT COINS FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getInt(ps,"COINS");
        }
        return 0;
    }

    public void updateMute(String tag, int wert) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET MUTE = ? WHERE TAG = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getMute(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MUTE FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getInt(ps,"MUTE");
        }
        return 0;
    }

    public void updateLevel(String tag, int wert) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET LEVEL = ? WHERE TAG = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getLevel(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT LEVEL FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getInt(ps,"LEVEL");
        }
        return 0;
    }

    public void updateExp(String tag, int wert) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET EXP = ? WHERE TAG = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public  int getExp(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT EXP FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getInt(ps,"EXP");
        }
        return 0;
    }

    public void updateMember(String tag, int wert) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET MEMBER = ? WHERE TAG = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public int getMember(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT MEMBER FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getInt(ps,"MEMBER");
        }
        return 0;
    }

    public void updateSystem(String tag, int wert) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("UPDATE CLANS SET RANKS = ? WHERE TAG = ?");
            ps.setInt(1, wert);
            ps.setString(2, tag);

            mysql.runAsyncUpdate(ps);
        }
    }

    public int getSystem(String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT RANKS FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            return mysql.getInt(ps,"RANKS");
        }
        return 0;
    }

    public void deleteClan (String tag) {
        if (isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("DELETE FROM CLANS WHERE TAG = ?");
            ps.setString(1, tag);

            mysql.runAsyncUpdate(ps);
            ClanAPI.getInstance().getRanks().deleteRanks(tag);
        }
    }

    public void createClan (String tag, String name) {
        if (!isTagExists(tag)) {
            PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("INSERT INTO CLANS (TAG, NAME, MEMBER, EXP, LEVEL, MUTE, COINS, RANKS) " +
                    "VALUES (?, ?, ?, 0, 0, 0, 0, 0)");
            ps.setString(1, tag);
            ps.setString(2, name);
            ps.setInt(3, member);

            mysql.runAsyncUpdate(ps);
        }
    }


}
