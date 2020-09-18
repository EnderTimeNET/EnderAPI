package net.endertime.enderapi.permission;

import net.endertime.enderapi.permission.listener.PermissionCheckListener;
import net.endertime.enderapi.permission.listener.PlayerLoginListener;
import net.endertime.enderapi.permission.listener.PostLoginListener;
import net.endertime.enderapi.permission.mysql.RankPermissions;
import net.endertime.enderapi.permission.mysql.Ranks;
import net.endertime.enderapi.permission.mysql.UserPermissions;
import net.endertime.enderapi.permission.mysql.Users;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermAPI {

    public static PermAPI instance;

    public static PermAPI getInstance() {
        return instance;
    }

    private boolean bungee;
    private boolean start;

    public PermAPI(boolean bungee) {
        this.bungee = bungee;
        start = false;
    }

    private Ranks ranks = new Ranks();
    private Users users = new Users();
    private UserPermissions userPermissions = new UserPermissions();
    private RankPermissions rankPermissions = new RankPermissions();
    private Map<UUID, List<String>> permissions = new HashMap<>();

    public void addPermission(UUID uuid, String permission) {
        getUserPermissions().addPermission(uuid, permission);
        if (!permissions.get(uuid).contains(permission)) {
            permissions.get(uuid).add(permission);
        }
    }

    public void removePermission(UUID uuid, String permission) {
        getUserPermissions().removePermission(uuid, permission);
        if (permissions.get(uuid).contains(permission)) {
            permissions.get(uuid).remove(permission);
        }
    }

    public Map<UUID, List<String>> getPermissions() {
        return permissions;
    }

    public Ranks getRanks() {
        return ranks;
    }

    public RankPermissions getRankPermissions() {
        return rankPermissions;
    }

    public UserPermissions getUserPermissions() {
        return userPermissions;
    }

    public Users getUsers() {
        return users;
    }public boolean hasPermission (UUID uuid, String permission) {
        if (permissions.get(uuid).contains("*")) {
            return true;
        } else if (permissions.get(uuid).contains("-" + permission)) {
            return false;
        } else if (permissions.get(uuid).contains(permission)) {
            return true;
        } else {
            String[] split = permission.split("\\.");
            String newPerm = "";
            for (int i = 0; i < split.length - 1; i++) {
                newPerm = newPerm + split[i] + ".";
                if (permissions.get(uuid).contains(newPerm + "*"))
                    return true;
            }
        }

        return false;
    }

    public boolean inGroup(UUID uuid, String group) {
        return getGroup(uuid).equals(group);
    }

    public String getGroup(UUID uuid) {
        return getUsers().getRank(uuid);
    }

    public void setGroup (UUID uuid, String group, long time) {
        getUsers().updateRank(uuid, group);
        getUsers().updateTime(uuid, time);
        getUsers().updateTimeSet(uuid, java.lang.System.currentTimeMillis());
    }

    public void addTimeToGroup (UUID uuid, long time) {
        getUsers().updateTime(uuid, getUsers().getTime(uuid) + time);
    }

    public boolean isBungee() {
        return bungee;
    }

    public void registerListener() {
        if (!start) {
            start = true;
            if (isBungee()) {
                ProxyServer.getInstance().getPluginManager().registerListener(net.endertime.enderapi.bungee.api.
                        EnderAPI.getInstance().getPlugin(), new PermissionCheckListener());
                ProxyServer.getInstance().getPluginManager().registerListener(net.endertime.enderapi.bungee.api.
                        EnderAPI.getInstance().getPlugin(), new PostLoginListener());
            } else {
                EnderAPI.getInstance().getPlugin().getServer().getPluginManager().registerEvents(new PlayerLoginListener(), EnderAPI.getInstance().getPlugin());
            }
        }
    }
}
