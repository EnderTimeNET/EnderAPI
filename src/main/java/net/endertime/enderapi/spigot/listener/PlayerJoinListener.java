package net.endertime.enderapi.spigot.listener;

import com.mojang.authlib.GameProfile;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.ScoreBoard;
import net.endertime.enderapi.spigot.utils.State;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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

        if (!EnderAPI.getInstance().getVanishUUID().contains(player.getUniqueId())
                && !EnderAPI.getInstance().getVanish().contains(player)) {
            fixLowerVersionBug(player);
            setScoreboard(player);

            if (!Wrapper.getInstance().getServiceId().getName().contains("Lobby")) {
                if (GameAPI.getInstance().getState().equals(State.INGAME)) {
                    if (NickAPI.getInstance().isNicked(player)) {
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
                    if (NickAPI.getInstance().isNicked(player)) {
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
                setNick(player);
            } else {
                event.setJoinMessage(null);
                if (!EnderAPI.getInstance().getBadlion().contains(player.getUniqueId()))
                    badlion(player);
            }

            for (Player vanished : EnderAPI.getInstance().getVanish()) {
                EnderAPI.getInstance().getScoreboard(vanished).b(player);
            }
        } else {
            event.setJoinMessage(null);
            EnderAPI.getInstance().getScoreboard(player).b();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("ek.commands.vanish")) {
                    EnderAPI.getInstance().hidePlayer(onlinePlayer, player);
                }
            }
        }
    }

    private void setNick(final Player player) {
        if (NickAPI.getInstance().isNickAvailable()) {
            if (EnderAPI.getInstance().getTeamDatabase().isState(player.getUniqueId())) {
                MinecraftServer minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
                WorldServer worldServer = ((CraftWorld)player.getWorld()).getHandle();
                final GameProfile nickedProfile = NickAPI.getInstance().getNick(player).getNickedProfile();

                final EntityPlayer entityPlayer = new EntityPlayer(minecraftServer, worldServer, nickedProfile
                        , new PlayerInteractManager(worldServer));

                final PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo
                        (PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
                final PacketPlayOutPlayerInfo playerInfoAddPacket = new PacketPlayOutPlayerInfo
                        (PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, (((CraftPlayer)player).getHandle()));
                final PacketPlayOutNamedEntitySpawn entitySpawnPacket = new PacketPlayOutNamedEntitySpawn(
                        ((CraftPlayer)player).getHandle());

                Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(),
                        () -> EnderAPI.getInstance().removeOnTablist(player, nickedProfile), 1);

                Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), () -> {
                    for (final Player all : Bukkit.getOnlinePlayers()) {
                        if (!player.getUniqueId().equals(all.getUniqueId())) {
                            if (NickAPI.getInstance().getNickedPlayer().containsKey(all)) {
                                (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(packetPlayOutPlayerInfo);
                                (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(playerInfoAddPacket);
                                (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(entitySpawnPacket);
                            }
                            if (!NickAPI.getInstance().couldSee(player, all)) {
                                EnderAPI.getInstance().removeOnTablist(player, NickAPI.getInstance().getNick(player).getBugProfile(true));
                                EnderAPI.getInstance().removeOnTablist(player, NickAPI.getInstance().getNick(player).getBugProfile(false));
                            }
                        } else {
                            (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(playerInfoAddPacket);
                        }
                    }
                }, 1);
            }
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
    }

    private void fixLowerVersionBug(final Player player) {
        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), () -> {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (!all.getUniqueId().equals(player.getUniqueId())) {
                    EnderAPI.getInstance().hidePlayer(all, player);
                    EnderAPI.getInstance().hidePlayer(player, all);
                }
            }
        }, 10);

        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), () -> {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (!all.getUniqueId().equals(player.getUniqueId())) {
                    if (EnderAPI.getInstance().isVanished(all)) {
                        if (player.hasPermission("ek.commands.vanish")) {
                            EnderAPI.getInstance().showPlayer(player, all);
                        }
                        EnderAPI.getInstance().showPlayer(all, player);
                    } else if (EnderAPI.getInstance().isVanished(player)) {
                        if (player.hasPermission("ek.commands.vanish")) {
                            EnderAPI.getInstance().showPlayer(all, player);
                        }
                        EnderAPI.getInstance().showPlayer(player, all);
                    } else {
                        EnderAPI.getInstance().showPlayer(all, player);
                        EnderAPI.getInstance().showPlayer(player, all);
                    }
                }
            }
        }, 15);
    }

    private void badlion(Player player) {
        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), () -> {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            try {
                dataOutputStream.writeUTF("heartbeat");
            } catch (IOException ignored) {
                return;
            }

            player.sendPluginMessage(EnderAPI.getInstance().getPlugin(), "BungeeCord", byteArrayOutputStream.toByteArray());
        }, 20L);

    }
}
