package net.endertime.enderapi.database.databaseapi.mysql;

import java.util.HashMap;
import java.util.Map;

public class PreparedStatement {

    private String update;
    private Map<Integer, Object> preset;

    public PreparedStatement(String update){
        this.update = update;
        this.preset = new HashMap<Integer, Object>();
    }

    public void setInt(int arg0, int arg1) {
        getPreset().put(arg0, arg1);
    }

    public void setFloat(int arg0, float arg1) {
        getPreset().put(arg0, arg1);
    }

    public void setString(int arg0, String arg1) {
        getPreset().put(arg0, arg1);
    }

    public void setBoolean(int arg0, boolean arg1) {
        if(arg1)
            getPreset().put(arg0, 1);
        else
            getPreset().put(arg0, 0);

    }

    public void setLong(int arg0, long arg1) {
        getPreset().put(arg0, arg1);
    }

    public void setByte(int arg0, byte arg1) {
        getPreset().put(arg0, arg1);
    }

    public void setBytes(int arg0, byte[] arg1) {
        getPreset().put(arg0, arg1);
    }

    public Map<Integer, Object> getPreset() {
        return preset;
    }

    public String getUpdate() {
        return update;
    }

    public void clearPreparedStatement () {
        getPreset().clear();
    }
}
