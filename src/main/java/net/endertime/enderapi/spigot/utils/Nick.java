package net.endertime.enderapi.spigot.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Nick {

    private Player player;
    private String name;
    private CraftPlayer craftPlayer;
    private UUID uniqueId;
    private String nickedName;
    private GameProfile nickedProfile;
    private GameProfile profile;
    private String value;
    private String signature;

    public Nick(Player player) {
        this.player = player;
        this.name = player.getName();
        this.craftPlayer = (CraftPlayer) player;
        this.uniqueId = player.getUniqueId();
        this.profile = craftPlayer.getProfile();

        if (EnderAPI.getInstance().getTeamDatabase().isRandom(player.getUniqueId())) {
            this.nickedName = EnderAPI.getInstance().getNickDatabase().getRandomName();
            EnderAPI.getInstance().getNickDatabase().updateState(EnderAPI.getInstance().getNickDatabase().getUUID(getNickedName()), true);
            EnderAPI.getInstance().getTeamDatabase().updateNickedName(player.getUniqueId(), nickedName);
        } else {
            this.nickedName = EnderAPI.getInstance().getTeamDatabase().getNickedName(player.getUniqueId());
        }

        this.nickedProfile = EnderAPI.getInstance().getNickDatabase().getProfile(getNickedName());
        this.value = EnderAPI.getInstance().getNickDatabase().getValue(nickedProfile.getId());
        this.signature = EnderAPI.getInstance().getNickDatabase().getSignature(nickedProfile.getId());


    }

    public GameProfile getBugProfile(boolean b) {
        GameProfile bugProfile;
        if (b) {
            bugProfile = new GameProfile(getUniqueId(), name);
        } else {
            bugProfile = new GameProfile(nickedProfile.getId(), name);
        }
        bugProfile.getProperties().removeAll("textures");
        bugProfile.getProperties().put("textures", new Property("textures", value, signature));
        return bugProfile;
    }

    public Player getPlayer() {
        return player;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public String getNickedName() {
        return nickedName;
    }

    public GameProfile getPlayerProfile() {
        return EnderAPI.getInstance().getTeamDatabase().getProfile(getUniqueId());
    }

    public CraftPlayer getCraftPlayer() {
        return craftPlayer;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public GameProfile getNickedProfile() {
        return nickedProfile;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public String getValue() {
        return value;
    }
}

