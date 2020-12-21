package net.endertime.enderapi.spigot.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class CreateSkulls {

    public static ItemStack createSkull(String name) {

        UUID uuid = EnderAPI.getInstance().getEnderDatabase().getUUID(name);

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(uuid, EnderAPI.getInstance().getEnderDatabase().getName(uuid));

        profile.getProperties().put("textures", new Property("textures", EnderAPI.getInstance().getEnderDatabase().getValue(uuid)));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException error) {
            error.printStackTrace();
        } catch (IllegalAccessException error) {
            error.printStackTrace();
        } catch ( NoSuchFieldException error) {
            error.printStackTrace();
        } catch ( SecurityException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack createSkull(UUID uuid) {

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(uuid, EnderAPI.getInstance().getEnderDatabase().getName(uuid));

        profile.getProperties().put("textures", new Property("textures", EnderAPI.getInstance().getEnderDatabase().getValue(uuid)));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException error) {
            error.printStackTrace();
        } catch (IllegalAccessException error) {
            error.printStackTrace();
        } catch ( NoSuchFieldException error) {
            error.printStackTrace();
        } catch ( SecurityException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack createSkull(String value, String name) {

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setDisplayName("§7" + name);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException error) {
            error.printStackTrace();
        } catch (IllegalAccessException error) {
            error.printStackTrace();
        } catch ( NoSuchFieldException error) {
            error.printStackTrace();
        } catch ( SecurityException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack createSkullValue(String value) {

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException error) {
            error.printStackTrace();
        } catch (IllegalAccessException error) {
            error.printStackTrace();
        } catch ( NoSuchFieldException error) {
            error.printStackTrace();
        } catch ( SecurityException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static Class<?> tileEntityClass;
    public static Class<?> blockPositionClass;

    static {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            tileEntityClass = Class.forName("net.minecraft.server." + version + ".TileEntitySkull");

            blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean setBlock(Location loc, String signature, String value) {
        return setBlock(loc.getBlock(), signature, value);
    }

    public static boolean setBlock(Block block, String signature, String value) {
        if ((block.getType() != Material.SKULL) && (block.getType() != Material.SKULL_ITEM)) {
            block.setType(Material.SKULL);
        }
        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle", new Class[0]).invoke(block.getWorld(),
                    new Object[0]);
            Object tileEntity = null;

            Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", new Class[] { blockPositionClass });

            tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld, new Object[] {

                    getBlockPositionFor(block.getX(), block.getY(), block.getZ()) }));

            tileEntityClass.getMethod("setGameProfile", new Class[] { GameProfile.class }).invoke(tileEntity,
                    new Object[] { getProfile(signature, value) });

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static GameProfile getProfile(String signature, String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Property property = new Property("textures", value, signature);
        profile.getProperties().put("textures", property);
        return profile;
    }

    private static Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;
        try {
            Constructor<?> cons = blockPositionClass
                    .getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE });

            blockPosition = cons
                    .newInstance(new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z) });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockPosition;
    }

}
