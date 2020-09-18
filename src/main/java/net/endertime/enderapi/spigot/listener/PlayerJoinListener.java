package net.endertime.enderapi.spigot.listener;

import com.mojang.authlib.GameProfile;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.ScoreBoard;
import net.endertime.enderapi.spigot.utils.State;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerJoinListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {

            public void run() {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                try {
                    dataOutputStream.writeUTF("heartbeat");
                } catch (IOException ignored) {
                    return;
                }

                player.sendPluginMessage(EnderAPI.getInstance().getPlugin(), "BungeeCord", byteArrayOutputStream.toByteArray());
            }
        }, 20L);

        fixLowerVersionBug(player);
        setScoreboard(player);

        if (!Wrapper.getInstance().getServiceId().getName().contains("Lobby")) {
            if (!GameAPI.getInstance().getState().equals(State.INGAME)) {
                if (EnderAPI.getInstance().isVanished(player)) {
                    event.setJoinMessage(null);
                    EnderAPI.getInstance().getScoreboard(player).b();
                } else if (NickAPI.getInstance().isNicked(player)) {
                    event.setJoinMessage(null);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                            if (NickAPI.getInstance().couldSee(player, all)) {
                                all.sendMessage("§8[§2+§8] §7" + NickAPI.getInstance().getNick(player).getNickedName());
                            } else {
                                all.sendMessage("§8[§2+§8] §7" + NickAPI.getInstance().getNick(player).getName());
                            }
                        }
                    }
                } else {
                    event.setJoinMessage(null);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                            all.sendMessage("§8[§2+§8] §7" + player.getName());
                        }
                    }
                }
            } else {
                if (EnderAPI.getInstance().isVanished(player)) {
                    event.setJoinMessage(null);
                    EnderAPI.getInstance().getScoreboard(player).b();
                } else if (NickAPI.getInstance().isNicked(player)) {
                    event.setJoinMessage(null);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (NickAPI.getInstance().couldSee(player, all)) {
                            all.sendMessage("§8[§2+§8] §7" + NickAPI.getInstance().getNick(player).getNickedName());
                        } else {
                            all.sendMessage("§8[§2+§8] §7" + NickAPI.getInstance().getNick(player).getName());
                        }
                    }
                } else {
                    event.setJoinMessage("§8[§2+§8] §7" + player.getName());
                }
            }
        } else {
            event.setJoinMessage(null);
        }

        for (Player vanished : EnderAPI.getInstance().getVanish()) {
            EnderAPI.getInstance().getScoreboard(vanished).b(player);
        }

        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(
                            new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                                    (((CraftPlayer)player).getHandle())));
                }
            }
        }, 60);

        setNick(player);
    }

    private void setNick(final Player player) {
        if (NickAPI.getInstance().isNickAvailable()) {
            MinecraftServer minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
            WorldServer worldServer = ((CraftWorld)player.getWorld()).getHandle();
            final GameProfile nickedProfile = NickAPI.getInstance().getNick(player).getNickedProfile();

            final EntityPlayer entityPlayer = new EntityPlayer(minecraftServer, worldServer, nickedProfile
                    , new PlayerInteractManager(worldServer));

            final PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo
                    (PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{entityPlayer});
            final PacketPlayOutPlayerInfo playerInfoAddPacket = new PacketPlayOutPlayerInfo
                    (PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, (((CraftPlayer)player).getHandle()));
            final PacketPlayOutNamedEntitySpawn entitySpawnPacket = new PacketPlayOutNamedEntitySpawn(
                    ((CraftPlayer)player).getHandle());

            Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
                public void run() {
                    EnderAPI.getInstance().removeOnTablist(player, nickedProfile);
                }
            }, 1);

            Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (final Player nicked : NickAPI.getInstance().getNickedPlayer().keySet()) {
                        if (!player.getUniqueId().equals(nicked.getUniqueId())) {
                            (((CraftPlayer)nicked).getHandle()).playerConnection.sendPacket(packetPlayOutPlayerInfo);
                            (((CraftPlayer)nicked).getHandle()).playerConnection.sendPacket(playerInfoAddPacket);
                            (((CraftPlayer)nicked).getHandle()).playerConnection.sendPacket(entitySpawnPacket);
                        }
                    }
                    for (final Player all : Bukkit.getOnlinePlayers()) {
                        if (!player.getUniqueId().equals(all.getUniqueId())) {
                            if (!NickAPI.getInstance().couldSee(player, all)) {
                                EnderAPI.getInstance().removeOnTablist(player, NickAPI.getInstance().getNick(player).getBugProfile(true));
                                EnderAPI.getInstance().removeOnTablist(player, NickAPI.getInstance().getNick(player).getBugProfile(false));
                            }
                        }
                    }
                }
            }, 1);
        }
    }

    private void setScoreboard(final Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                if (!EnderAPI.getInstance().isVanished(onlinePlayer)) {
                    ScoreBoard scoreBoard = EnderAPI.getInstance().getScoreboard(onlinePlayer);

                    scoreBoard.b();
                }
            } else {
                if (!EnderAPI.getInstance().isVanished(onlinePlayer)) {
                    ScoreBoard scoreBoard = EnderAPI.getInstance().getScoreboard(onlinePlayer);

                    scoreBoard.b(player);
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
            public void run() {
                EnderAPI.getInstance().getScoreboard(player).b(player);
            }
        }, 3);
    }

    private void fixLowerVersionBug(final Player player) {
        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.getUniqueId().equals(player.getUniqueId())) {
                        EnderAPI.getInstance().hidePlayer(all, player);
                        EnderAPI.getInstance().hidePlayer(player, all);
                    }
                }
            }
        }, 10);

        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.getUniqueId().equals(player.getUniqueId())) {
                        if (EnderAPI.getInstance().isVanished(all)) {
                            if (EnderAPI.getInstance().isInTeam(player)) {
                                EnderAPI.getInstance().showPlayer(player, all);
                            }
                            EnderAPI.getInstance().showPlayer(all, player);
                        } else if (EnderAPI.getInstance().isVanished(player)) {
                            if (EnderAPI.getInstance().isInTeam(all)) {
                                EnderAPI.getInstance().showPlayer(all, player);
                            }
                            EnderAPI.getInstance().showPlayer(player, all);
                        } else {
                            EnderAPI.getInstance().showPlayer(all, player);
                            EnderAPI.getInstance().showPlayer(player, all);
                        }
                    }
                }
            }
        }, 15);
    }
}
