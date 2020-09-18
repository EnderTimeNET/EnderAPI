package net.endertime.enderapi.permission.utils;

import net.endertime.enderapi.permission.PermAPI;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

public class CustomPermissibleBase extends PermissibleBase {

    private final Player player;

    public CustomPermissibleBase(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(String inName) {
        if (PermAPI.getInstance().getPermissions().get(player.getUniqueId()) != null) {
            return PermAPI.getInstance().hasPermission(player.getUniqueId(), inName);
        }
        return super.hasPermission(inName);
    }
}
