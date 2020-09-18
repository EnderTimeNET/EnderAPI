package net.endertime.enderapi.database.databaseapi.mysql;

import com.zaxxer.hikari.HikariDataSource;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQL {

    private final HikariDataSource hikari;

    public MySQL(String database){
        String host = "localhost";
        int port = 3306;
        String username = getString("user");
        String password = getString("password");

        this.hikari = new HikariDataSource();
        getHikari().setIdleTimeout(87000000);
        getHikari().setMinimumIdle(20);
        getHikari().setMaxLifetime(87000000);
        getHikari().setConnectionTimeout(87000000);
        getHikari().setRegisterMbeans(true);
        getHikari().setMaximumPoolSize(10);
        getHikari().setConnectionTestQuery("SELECT 1");
        getHikari().setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        getHikari().addDataSourceProperty("serverName", host);
        getHikari().addDataSourceProperty("port", port);
        getHikari().addDataSourceProperty("databaseName", database);
        getHikari().addDataSourceProperty("user", username);
        getHikari().addDataSourceProperty("password", password);
    }

    public void runAsyncUpdate(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0){
        Connection connection = null;

        PreparedStatement ps = null;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(null, ps, connection);
        }
    }

    public Integer getInt(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        int in = 0;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                in = rs.getInt(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return  in;
    }

    public String getString(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        String s = "";

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                s = rs.getString(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnections(rs, ps, connection);
        }
        return s;
    }

    public Float getFloat(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        float f = 0;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                f = rs.getFloat(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return f;
    }

    public Long getLong(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        long l = 0;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                l = rs.getLong(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return l;
    }

    public Byte getByte(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        byte b = 0;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                b = rs.getByte(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return b;
    }

    public byte[] getBytes(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        byte[] b = null;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                b = rs.getBytes(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return b;
    }

    public Boolean isInDatabase(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return false;
    }

    public Boolean getBoolean(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0, String returned){
        Connection connection = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean b = false;

        try {
            connection = getHikari().getConnection();

            ps = connection.prepareStatement(arg0.getUpdate());
            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }

            rs = ps.executeQuery();

            if(rs.next()){
                b = rs.getBoolean(returned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(rs, ps, connection);
        }
        return b;
    }

    private final Map<net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement, List<Object>> list = new HashMap<>();

    public ResultSet runAsyncQuery(net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0){
        Connection connection;

        PreparedStatement ps;
        ResultSet rs;

        try {
            connection = getHikari().getConnection();
            list.put(arg0, new ArrayList<>());
            list.get(arg0).add(connection);

            ps = connection.prepareStatement(arg0.getUpdate());

            for(int i = 1; i <= arg0.getPreset().keySet().size(); i++) {
                Object object = arg0.getPreset().get(i);
                setObject(i, object, ps);
            }
            list.get(arg0).add(ps);

            rs = ps.executeQuery();
            list.get(arg0).add(rs);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public void closeConnections (net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement arg0) {
        if (list.containsKey(arg0)) {
            Connection connection = (Connection) list.get(arg0).get(0);
            PreparedStatement ps = (PreparedStatement) list.get(arg0).get(1);
            ResultSet rs = (ResultSet) list.get(arg0).get(2);
            try {
                if(!connection.isClosed()) {
                    if(rs != null)
                        rs.close();
                    if(ps != null)
                        ps.close();
                    connection.close();
                }
            } catch (SQLException ignored) {
            } catch (NullPointerException ignored) {
            } finally {
                list.remove(arg0);
            }
        }
    }

    public void closeConnections(ResultSet rs, PreparedStatement ps, Connection connection){
        try {
            if(!connection.isClosed()) {
                if(rs != null)
                    rs.close();
                if(ps != null)
                    ps.close();
                connection.close();
            }
        } catch (SQLException ignored) {
        } catch (NullPointerException ignored) {
        }

    }

    private void setObject(int arg0, Object arg1, PreparedStatement ps) throws  SQLException {
        if (arg1 instanceof Integer) {
            Integer aInteger = (Integer) arg1;

            ps.setInt(arg0, aInteger);
        } else if (arg1 instanceof String) {
            String string = (String) arg1;

            ps.setString(arg0, string);
        } else if (arg1 instanceof Float) {
            Float aFloat = (Float) arg1;

            ps.setFloat(arg0, aFloat);
        } else if (arg1 instanceof Long) {
            Long aLong = (Long) arg1;

            ps.setLong(arg0, aLong);
        } else if (arg1 instanceof Byte) {
            Byte aByte = (Byte) arg1;

            ps.setByte(arg0, aByte);
        } else if (arg1 instanceof Byte[]) {
            byte[] aByte = (byte[]) arg1;

            ps.setBytes(arg0, aByte);
        }
    }

    public void closeConnectionToDatabase(){
        if(getHikari() != null)
            getHikari().close();
    }

    private HikariDataSource getHikari() {
        return hikari;
    }

    private String getString(String arg0) {
        if (DataBaseAPI.getInstance().isBungee()) {
            File file = new File("/home/Datenbankzugang/access.yml");
            Configuration cfg = null;
            try {
                cfg = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return cfg.getString("MySQL." + arg0);
        } else {
            File file = new File("/home/Datenbankzugang/access.yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            return cfg.getString("MySQL." + arg0);
        }
    }

    private Integer getInt(String arg0) {
        File file = new File("/home/Datenbankzugang/access.yml");
        if (DataBaseAPI.getInstance().isBungee()) {
            Configuration cfg = null;
            try {
                cfg = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cfg.getInt("MySQL." + arg0);
        } else {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            return cfg.getInt("MySQL." + arg0);
        }
    }
}