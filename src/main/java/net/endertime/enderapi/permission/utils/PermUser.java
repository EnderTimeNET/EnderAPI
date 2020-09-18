package net.endertime.enderapi.permission.utils;

import net.endertime.enderapi.spigot.api.PermAPI;

import java.util.UUID;

public class PermUser {

    private UUID uuid;

    public PermUser(UUID uuid) {
        this.uuid = uuid;
    }

    public void addPermission(String permission) {
        PermAPI.getInstance().addPermission(uuid, permission);
    }

    public void removePermission(String permission) {
        PermAPI.getInstance().removePermission(uuid, permission);
    }

    public String getGroup () {
        return PermAPI.getInstance().getGroup(uuid);
    }

    public void setGroup (String group, long time) {
        PermAPI.getInstance().setGroup(uuid, group, time);
    }

    public boolean hasPermission (String permission) {
        return PermAPI.getInstance().hasPermission(uuid, permission);
    }
}
