package net.endertime.enderapi.database.databaseapi;

import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;

import java.util.HashMap;
import java.util.Map;

public class DataBaseAPI {

    private Map<String, MySQL> stringMySQLMap = new HashMap<>();

    public static DataBaseAPI instance;

    public static DataBaseAPI getInstance() {
        return instance;
    }

    private boolean bungee;

    public DataBaseAPI(boolean bungee) {
        this.bungee = bungee;
    }

    public boolean isBungee() {
        return bungee;
    }

    public MySQL getMySQL(String database) {
        if (isDatabaseExists(database))
            return this.stringMySQLMap.get(database);
        return getNewMySQL(database);
    }

    public PreparedStatement getPreparedStatement(String update) {
        return new PreparedStatement(update);
    }

    private boolean isDatabaseExists(String database) {
        if (this.stringMySQLMap.containsKey(database))
            return true;
        return false;
    }

    private MySQL getNewMySQL(String database) {
        MySQL mySQL = new MySQL(database);
        this.stringMySQLMap.put(database, mySQL);
        return mySQL;
    }

}