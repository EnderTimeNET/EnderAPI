package net.endertime.enderkomplex.spigot.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import net.endertime.enderkomplex.bungee.enums.BanReason;
import net.endertime.enderkomplex.bungee.enums.MuteReason;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

public class ServerHandler {

    public static void createNPC(Player p, String reportid) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) p.getWorld()).getHandle();
        GameProfile gp = new GameProfile(UUID.randomUUID(), "§7§kxxxxxxxxxx");
        gp.getProperties().put("textures", new Property("textures", Database.getValue(reportid), Database.getSignature(reportid)));
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gp, new PlayerInteractManager(nmsWorld));
        npc.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        DataWatcher watcher = new DataWatcher(null);
        watcher.register(new DataWatcherObject<>(13, DataWatcherRegistry.a), (byte) 127);
        connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), watcher, true));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) ((p.getLocation().getYaw() * 256.0F) / 360.0F)));
        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {

            @Override
            public void run() {
                connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));

            }
        }, 5);
        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {

            @Override
            public void run() {
                connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));

            }
        }, 20*10);
    }

    public static void crashClient(Player p) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(EnumParticle.PORTAL,
                true, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Integer.MAX_VALUE, new int[] { Integer.MAX_VALUE }));
    }

    public static void sendPluginMessage(Player player, PluginMessage action, String targetuuid, String reportid, String reason) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);

        try {
            output.writeUTF("pluginmessage");
            output.writeUTF(player.getUniqueId().toString());
            output.writeUTF(action.toString());
            output.writeUTF(targetuuid);
            output.writeUTF(reportid);
            output.writeUTF(reason);

            player.sendPluginMessage(ServerData.Instance, "enderkomplex", stream.toByteArray());
        } catch (Exception e) {}
    }

    public static void sendReportIdToBungee(Player player, String reportid) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);

        try {
            output.writeUTF("pluginmessage");
            output.writeUTF(player.getUniqueId().toString());
            output.writeUTF(PluginMessage.WORKING_REPORT_ID.toString());
            output.writeUTF(reportid);

            player.sendPluginMessage(ServerData.Instance, "enderkomplex", stream.toByteArray());
        } catch (Exception e) {}
    }

    public static void sendActionbar(Player p, String message) {
        CraftPlayer player = (CraftPlayer) p;
        IChatBaseComponent ibc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, ibc, 200, 3000, 900);
        player.getHandle().playerConnection.sendPacket(packet);
    }

    public static Material getMaterial(BanReason br) {
        switch(br) {
            case ADMIN_BAN:
                return Material.NETHER_STAR;
            case AFK_RAFMING:
                return Material.COBBLESTONE;
            case BANNUMGEHUNG:
                return Material.SHIELD;
            case BAUWERKE:
                return Material.BRICK;
            case BUGUSING:
                return Material.DEAD_BUSH;
            case CLIENTMODS:
                return Material.IRON_SWORD;
            case REPORTABUSE:
                return Material.FLINT_AND_STEEL;
            case SKIN_NAME:
                return Material.NAME_TAG;
            case STATS_BOOSTING:
                return Material.SIGN;
            case TEAMING:
                return Material.RABBIT_FOOT;
            case TROLLING:
                return Material.TNT;
        }
        return null;
    }

    public static Material getMaterial(MuteReason mr) {
        switch(mr) {
            case CHAT1:
                return Material.PAPER;
            case CHAT2:
                return Material.MAP;
            case CHAT3:
                return Material.BOOK;
            case MUTE_UMGEHUNG:
                return Material.BOOK_AND_QUILL;
        }
        return null;
    }

    public static void showHologram(Player p, EntityArmorStand holo) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(holo);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void showHologram(EntityArmorStand holo) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(holo);
        Bukkit.getOnlinePlayers().forEach(online -> ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet));
    }

    public static void destroyHologram(EntityArmorStand holo) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(holo.getId());
        Bukkit.getOnlinePlayers().forEach(online -> ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet));
    }

    public static EntityArmorStand createPacketHologram(String text, Location loc) {
        EntityArmorStand entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY() -2, loc.getZ());
        entity.setCustomName(text);
        entity.setCustomNameVisible(true);
        entity.setInvisible(true);
        entity.setInvulnerable(true);
        entity.setNoGravity(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
        Bukkit.getOnlinePlayers().forEach(online -> ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet));
        return entity;
    }

}
