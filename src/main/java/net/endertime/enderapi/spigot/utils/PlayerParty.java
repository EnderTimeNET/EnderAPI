package net.endertime.enderapi.spigot.utils;

import com.mojang.authlib.GameProfile;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerParty {

    private UUID leader;
    private List<UUID> players;
    private boolean publicParty;

    public PlayerParty(UUID leader, boolean publicParty) {
        this.leader = leader;
        this.publicParty = publicParty;
        this.players = new ArrayList<UUID>();
        getPlayers().add(leader);
    }

    public boolean isLeader(UUID uuid) {
        if (this.leader.equals(uuid)) {
            return true;
        }
        return false;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public UUID getLeader() {
        return this.leader;
    }

    public boolean isPublicParty() {
        return publicParty;
    }

    public void addPlayer(UUID uuid) {
        this.players.add(uuid);

        for (UUID uuids : getPlayers()) {
            if (NickAPI.getInstance().isNicked(uuids)) {
                final Player p = Bukkit.getPlayer(uuid);
                final Player nicked = Bukkit.getPlayer(uuids);
                if (!uuid.equals(uuids)) {
                    if (nicked != null && p != null) {
                        if (!isPublicParty()) {
                            final GameProfile nickedProfile = NickAPI.getInstance().getNick(nicked).getNickedProfile();
                            EnderAPI.getInstance().hidePlayer(p, nicked);

                            Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
                                public void run() {
                                    EnderAPI.getInstance().showPlayer(p, nicked);
                                    EnderAPI.getInstance().removeOnTablist(p, nickedProfile);
                                }
                            }, 2);
                        }
                    }
                }
            }
        }
        updateTablist();
    }

    public void updateTablist () {
        for (UUID uuid : getPlayers()) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                for (UUID uuid1: getPlayers()) {
                    if (Bukkit.getPlayer(uuid1) != null) {
                        Player p1 = Bukkit.getPlayer(uuid1);
                        EnderAPI.getInstance().getScoreboard(p).b(p1);

                        (((CraftPlayer)p).getHandle()).playerConnection.sendPacket(
                                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                                        (((CraftPlayer)p1).getHandle())));
                    }
                }
            }
        }
    }

    public void updateTablist (List<UUID> update) {
        for (UUID uuid : update) {
            if (Bukkit.getPlayer(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                for (UUID uuid1: update) {
                    if (Bukkit.getPlayer(uuid1) != null) {
                        Player p1 = Bukkit.getPlayer(uuid1);
                        EnderAPI.getInstance().getScoreboard(p).b(p1);

                        (((CraftPlayer)p).getHandle()).playerConnection.sendPacket(
                                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                                        (((CraftPlayer)p1).getHandle())));
                    }
                }
            }
        }
    }

    public void removePlayer (UUID uuid) {
        for (Player nicked : NickAPI.getInstance().getNickedPlayer().keySet()) {
            if (getPlayers().contains(nicked.getUniqueId())) {
                if (Bukkit.getPlayer(uuid) != null) {
                    NickAPI.getInstance().getNickedParty().get(nicked.getUniqueId()).add(Bukkit.getPlayer(uuid));
                }
            }
        }
        updateTablist();
        if (Bukkit.getPlayer(uuid) != null) {
            Player p = Bukkit.getPlayer(uuid);
            for (UUID uuid1: getPlayers()) {
                if (Bukkit.getPlayer(uuid1) != null) {
                    Player p1 = Bukkit.getPlayer(uuid1);
                    EnderAPI.getInstance().getScoreboard(p).b(p1);

                    (((CraftPlayer)p).getHandle()).playerConnection.sendPacket(
                            new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                                    (((CraftPlayer)p1).getHandle())));
                }
            }
        }
        this.players.remove(uuid);
    }

}
