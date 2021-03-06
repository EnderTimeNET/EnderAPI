package net.endertime.enderapi.spigot.utils;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Group {

    EVENTSERVER(EnderAPI.getInstance().getItem(Material.FIREWORK).setDisplayName("§6§lEventServer")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.eventserver",
            Arrays.asList(new String[]{"Event"}), 43, "§6EventServer", "§6§l"),
    AMONGUS(EnderAPI.getInstance().getItem(Material.EMPTY_MAP).setDisplayName("§4§lAmong§f§lUs")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.amongus",
            Arrays.asList(new String[]{"AmongUs"}), 19, "§4Among§fUs", "§4§l"),
    SPEEDUHC(EnderAPI.getInstance().getItem(Material.LEATHER_BOOTS).setDisplayName("§e§lSpeed§6§lUHC")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.speeduhc",
            Arrays.asList(new String[]{"SpeeduHC"}), 23, "§eSpeed§6UHC", "§e§l"),
    SNOWBATTLE(EnderAPI.getInstance().getItem(Material.SNOW_BALL).setDisplayName("§f§lSnowBattle")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.snowbattle",
            Arrays.asList(new String[]{"SnowBattle"}), 11, "§fSnowBattle", "§f§l"),
    LOBBY(EnderAPI.getInstance().getItem(Material.SULPHUR).setDisplayName("§5§lLobby")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.lobby",
            Arrays.asList(new String[]{"Lobby"}), 39, "§5Lobby", "§5§l"),
    KBFFA(EnderAPI.getInstance().getItem(Material.STICK).setDisplayName("§c§lKBFFA")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).setHideEnchantments()
            .getItemStack(), "restart.group.kbffa"
            , Arrays.asList(new String[]{"KBFFA"}), 25, "§cKBFFA", "§c§l"),
    PROXY(EnderAPI.getInstance().getItem(Material.COMMAND).setDisplayName("§4§lProxy")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS)
            .getItemStack(), "restart.group.proxy",
            Arrays.asList(new String[]{"Proxy"}), 49, "§4Proxy", "§4§l"),
    OITC(EnderAPI.getInstance().getItem(Material.ARROW).setDisplayName("§c§lOITC")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.oitc",
            Arrays.asList(new String[]{"OITC"}), 21, "§cOITC", "§c§l"),
    DESTRUCTION(EnderAPI.getInstance().getItem(Material.ANVIL, 1, 2).setDisplayName("§4§lDestruction")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.destruction",
            Arrays.asList(new String[]{"Destruction"}), 13, "§4Destruction", "§4§l"),
    VORBAUEN(EnderAPI.getInstance().getItem(Material.GOLD_PICKAXE).setDisplayName("§6§lVorbauen")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.vorbauen",
            Arrays.asList(new String[]{"Vorbauen"}), 37, "§6Vorbauen", "§6§l"),
    BW(EnderAPI.getInstance().getItem(Material.BED, 1, 0).setDisplayName("§5§lBedWars")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.bedwars",
            Arrays.asList(new String[]{"BW8x1", "BW4x2", "BW4x4"}), 15, "§5BedWars", "§5§l"),
    SILENTLOBBY(EnderAPI.getInstance().getItem(Material.TNT).setDisplayName("§5§lSilentLobby")
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_DESTROYS)
            .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).addItemFlags(ItemFlag.HIDE_PLACED_ON)
            .addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addItemFlags(ItemFlag.HIDE_ENCHANTS).getItemStack(), "restart.group.silentlobby",
            Arrays.asList(new String[]{"SilentLobby"}), 41, "§5SilentLobby", "§5§l");

    private ItemStack itemStack;
    private String permission;
    private List<String> group;
    private int slot;
    private String invTitle;
    private String prefix;

    Group (ItemStack itemStack, String permission, List<String> group, int slot, String invTitle, String prefix) {
        this.itemStack = itemStack;
        this.permission = permission;
        this.group = group;
        this.slot = slot;
        this.invTitle = invTitle;
        this.prefix = prefix;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static List<Group> getGroups() {
        return groups;
    }

    public List<String> getGroup() {
        return group;
    }

    public String getPermission() {
        return permission;
    }

    public static List<Group> groups = new ArrayList<Group>();

    public String getInvTitle() {
        return invTitle;
    }

    public String getPrefix() {
        return prefix;
    }

    public static Group getGroup (int slot) {
        switch (slot) {
            case 11:
                return SNOWBATTLE;
            case 13:
                return DESTRUCTION;
            case 15:
                return BW;
            case 19:
                return AMONGUS;
            case 21:
                return OITC;
            case 23:
                return SPEEDUHC;
            case 25:
                return KBFFA;
            case 37:
                return VORBAUEN;
            case 39:
                return LOBBY;
            case 41:
                return SILENTLOBBY;
            case 43:
                return EVENTSERVER;
            case 49:
                return PROXY;
            default:
                return null;
        }
    }

    public static void fillGroups () {
        groups.add(EVENTSERVER);
        groups.add(AMONGUS);
        groups.add(SNOWBATTLE);
        groups.add(LOBBY);
        groups.add(KBFFA);
        groups.add(SPEEDUHC);
        groups.add(PROXY);
        groups.add(OITC);
        groups.add(DESTRUCTION);
        groups.add(VORBAUEN);
        groups.add(BW);
        groups.add(SILENTLOBBY);
    }

    public static Group getGroup (String invTitle) {
        for (Group group : groups) {
            if (group.getInvTitle().equals(invTitle))
                return group;
        }
        return null;
    }

}
