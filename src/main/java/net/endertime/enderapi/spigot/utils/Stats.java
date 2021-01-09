package net.endertime.enderapi.spigot.utils;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.MySQL;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public enum Stats {

    ONEVSONE(Arrays.asList(), "§e1vs1", ""),
    SNOWBATTLE(Arrays.asList(), "§fSnowBattle", "SNOWBATTLE"),
    KBFFA(Arrays.asList("ITEMSUSED"), "§cKBFFA", "KBFFA"),
    OITC(Arrays.asList(), "§cOITC", "OITC"),
    DESTRUCTION(Arrays.asList("ITEMSUSED"), "§4Destruction", "DESTRUCTION"),
    BEDWARS(Arrays.asList("BEDSDESTROYED"), "§5BedWars", "BW"),
    SPEEDUHC(Arrays.asList(), "§5SpeedUHC", "SPEEDUHC");

    private List<String> special;
    private MySQL mysql;
    private String gameName;
    private String name;

    Stats(List<String> special, String gameName, String name) {
        this.mysql = DataBaseAPI.getInstance().getMySQL("STATSDATABASE");
        this.special = special;
        this.gameName = gameName;
        this.name = name;
    }

    public List<String> getSpecial() {
        return special;
    }

    public MySQL getMysql() {
        return mysql;
    }

    public String getGameName() {
        return gameName;
    }

    public String getName() {
        return name;
    }

    public static Inventory getInventory(Player player, Stats stats) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6Deine Statistik");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());

        for (int i = 10; i < 17; i++)
            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 10).setDisplayName("§1").getItemStack());

        inventory.setItem(11, EnderAPI.getInstance().getItem(Material.BARRIER).setDisplayName("§c§lStats resetten")
                .setLore(Arrays.asList(new String[]{"", "§7Deine Tokens§8: §a" + EnderAPI.getInstance().getAutoReset()
                        .getTokens(player.getUniqueId()), ""})).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlags(ItemFlag.HIDE_DESTROYS).addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .addItemFlags(ItemFlag.HIDE_PLACED_ON).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE).getItemStack());

        inventory.setItem(13, EnderAPI.getInstance().getSkull(player.getUniqueId()).setDisplayName("§6§lMonatlich")
                .setLore(getMonthly(player.getUniqueId(), stats)).getItemStack());
        inventory.setItem(14, EnderAPI.getInstance().getSkull(player.getUniqueId()).setDisplayName("§6§lAlltime")
                .setLore(getAllTime(player.getUniqueId(), stats)).getItemStack());

        return inventory;
    }

    public static Inventory getInventory(Player player, UUID other, Stats stats) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6Statistik von " + EnderAPI.getInstance().getPrefix(other)
                + EnderAPI.getInstance().getName(other));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());

        for (int i = 10; i < 17; i++)
            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 10).setDisplayName("§1").getItemStack());

        inventory.setItem(11, EnderAPI.getInstance().getSkull(player.getUniqueId()).setDisplayName("§6§lMonatlich")
                .setLore(getMonthly(player.getUniqueId(), stats)).getItemStack());
        inventory.setItem(12, EnderAPI.getInstance().getSkull(player.getUniqueId()).setDisplayName("§6§lAlltime")
                .setLore(getAllTime(player.getUniqueId(), stats)).getItemStack());

        inventory.setItem(14, EnderAPI.getInstance().getSkull(other).setDisplayName("§6§lMonatlich")
                .setLore(getMonthly(other, stats)).getItemStack());
        inventory.setItem(15, EnderAPI.getInstance().getSkull(other).setDisplayName("§6§lAlltime")
                .setLore(getAllTime(other, stats)).getItemStack());

        return inventory;
    }

    public static Inventory getInventoryNick(Player player, UUID otherFake, UUID otherReal, Stats stats) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6Statistik von §3" + EnderAPI.getInstance().getTeamDatabase().getPrefix(otherReal)
                + EnderAPI.getInstance().getTeamDatabase().getNickedName(otherReal));

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());

        for (int i = 10; i < 17; i++)
            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 10).setDisplayName("§1").getItemStack());

        inventory.setItem(11, EnderAPI.getInstance().getSkull(player.getUniqueId()).setDisplayName("§6§lMonatlich")
                .setLore(getMonthly(player.getUniqueId(), stats)).getItemStack());
        inventory.setItem(12, EnderAPI.getInstance().getSkull(player.getUniqueId()).setDisplayName("§6§lAlltime")
                .setLore(getAllTime(player.getUniqueId(), stats)).getItemStack());

        inventory.setItem(14, EnderAPI.getInstance().getSkull(EnderAPI.getInstance().getNickDatabase().getValue(otherFake), "")
                .setDisplayName("§6§lMonatlich")
                .setLore(getMonthlyNick(otherReal, stats)).getItemStack());
        inventory.setItem(15, EnderAPI.getInstance().getSkull(EnderAPI.getInstance().getNickDatabase().getValue(otherFake), "")
                .setDisplayName("§6§lAlltime")
                .setLore(getAllTimeNick(otherReal, stats)).getItemStack());

        return inventory;
    }

    private static List<String> getMonthly(UUID uuid, Stats stats) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§7Name§8: " + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid));
        lore.add("");

        if (stats.equals(SNOWBATTLE) || stats.equals(KBFFA) || stats.equals(OITC) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getKills(uuid, lore, stats, stats.getName() + "_MONTHLY");
        }

        lore.add("§7Punkte§8: §c" + getInt(stats.getMysql(), stats.getName() + "_MONTHLY", "POINTS", uuid));
        lore.add("§7Ranking§8: §c" + getRankingList(stats.getMysql(), stats.getName() + "_MONTHLY", uuid));

        if (stats.equals(SNOWBATTLE) || stats.equals(DESTRUCTION) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getWins(uuid, lore, stats, stats.getName() + "_MONTHLY");
        }

        for (String special : stats.getSpecial()) {
            lore.add("§7" + getString(special) + "§8: §c" + getInt(stats.getMysql(), stats.getName() + "_MONTHLY", special, uuid));
        }

        lore.add("");

        return lore;
    }

    private static List<String> getAllTime(UUID uuid, Stats stats) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§7Name§8: " + EnderAPI.getInstance().getPrefix(uuid) + EnderAPI.getInstance().getName(uuid));
        lore.add("");

        if (stats.equals(SNOWBATTLE) || stats.equals(KBFFA) || stats.equals(OITC) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getKills(uuid, lore, stats, stats.getName() + "_ALLTIME");
        }

        lore.add("§7Punkte§8: §c" + getInt(stats.getMysql(), stats.getName() + "_ALLTIME", "POINTS", uuid));
        lore.add("§7Ranking§8: §c" + getRankingList(stats.getMysql(), stats.getName() + "_ALLTIME", uuid));

        if (stats.equals(SNOWBATTLE) || stats.equals(DESTRUCTION) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getWins(uuid, lore, stats, stats.getName() + "_ALLTIME");
        }

        for (String special : stats.getSpecial()) {
            lore.add("§7" + getString(special) + "§8: §c" + getInt(stats.getMysql(), stats.getName() + "_ALLTIME", special, uuid));
        }

        lore.add("");

        return lore;
    }

    private static List<String> getMonthlyNick(UUID otherReal, Stats stats) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§7Name§8: §3" + EnderAPI.getInstance().getTeamDatabase().getPrefix(otherReal) + EnderAPI.getInstance().getTeamDatabase().getNickedName(otherReal));
        lore.add("");

        if (stats.equals(SNOWBATTLE) || stats.equals(KBFFA) || stats.equals(OITC) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getKills(otherReal, lore, stats, stats.getName() + "_MONTHLY");
        }

        lore.add("§7Punkte§8: §c" + getInt(stats.getMysql(), stats.getName() + "_MONTHLY", "POINTS", otherReal));
        lore.add("§7Ranking§8: §c" + getRankingList(stats.getMysql(), stats.getName() + "_MONTHLY", otherReal));

        if (stats.equals(SNOWBATTLE) || stats.equals(DESTRUCTION) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getWins(otherReal, lore, stats, stats.getName() + "_MONTHLY");
        }

        for (String special : stats.getSpecial()) {
            lore.add("§7" + getString(special) + "§8: §c" + getInt(stats.getMysql(), stats.getName() + "_MONTHLY", special, otherReal));
        }

        lore.add("");

        return lore;
    }

    private static List<String> getAllTimeNick (UUID otherReal, Stats stats) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§7Name§8: §3" + EnderAPI.getInstance().getTeamDatabase().getPrefix(otherReal) + EnderAPI.getInstance().getTeamDatabase().getNickedName(otherReal));
        lore.add("");

        if (stats.equals(SNOWBATTLE) || stats.equals(KBFFA) || stats.equals(OITC) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getKills(otherReal, lore, stats, stats.getName() + "_ALLTIME");
        }

        lore.add("§7Punkte§8: §c" + getInt(stats.getMysql(), stats.getName() + "_ALLTIME", "POINTS", otherReal));
        lore.add("§7Ranking§8: §c" + getRankingList(stats.getMysql(), stats.getName() + "_ALLTIME", otherReal));

        if (stats.equals(SNOWBATTLE) || stats.equals(DESTRUCTION) || stats.equals(BEDWARS) || stats.equals(SPEEDUHC)) {
            lore = getWins(otherReal, lore, stats, stats.getName() + "_ALLTIME");
        }

        for (String special : stats.getSpecial()) {
            lore.add("§7" + getString(special) + "§8: §c" + getInt(stats.getMysql(), stats.getName() + "_ALLTIME", special, otherReal));
        }

        lore.add("");

        return lore;
    }

    private static List<String> getKills(UUID uuid, List<String> lore, Stats stats, String table) {
        float kills = getInt(stats.getMysql(), table, "KILLS", uuid);
        float deaths = getInt(stats.getMysql(), table, "DEATHS", uuid);

        lore.add("§7Kills§8: §c" + (int) kills);
        lore.add("§7Tode§8: §c" + (int) deaths);

        if (deaths <= 0.0F) {
            lore.add("§7K/D§8: §c" + kills);
        } else if (kills <= 0.0F) {
            lore.add("§7K/D§8: §c0");
        } else {
            lore.add("§7K/D§8: §c" + EnderAPI.getInstance().round(kills / deaths, 3));
        }

        return lore;
    }

    private static List<String> getWins (UUID uuid, List<String> lore, Stats stats, String table) {
        float wins = getInt(stats.getMysql(), table, "WINS", uuid);
        float gamesplayed = getInt(stats.getMysql(), table, "GAMESPLAYED", uuid);
        float loses = gamesplayed - wins;

        lore.add("§7Gewonnen§8: §c" + (int) wins);
        lore.add("§7Gespielt§8: §c" + (int) gamesplayed);

        if (loses <= 0.0F) {
            lore.add("§7K/D§8: §c" + wins);
        } else if (wins <= 0.0F) {
            lore.add("§7K/D§8: §c0");
        } else {
            lore.add("§7K/D§8: §c" + EnderAPI.getInstance().round(wins / loses, 3));
        }

        lore.add("§7Siegeschance§8: §c"  + (int)EnderAPI.getInstance().round(wins / gamesplayed * 100.0F, 0) + "§7%");

        return lore;
    }

    private static int getInt(MySQL mysql, String table, String returned, UUID uuid) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT " + returned + " FROM " + table + " WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        return mysql.getInt(ps, returned);
    }

    private static int getRankingList(MySQL mysql, String table, UUID uuid) {
        Map<UUID, Integer> ranking = new HashMap<>();
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM " + table + " ORDER BY POINTS DESC");
        ResultSet rs = mysql.runAsyncQuery(ps);
        try {
            int i = 0;
            while (rs.next()) {
                i++;
                ranking.put(UUID.fromString(rs.getString("UUID")), i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnections(ps);
        }
        if (ranking.get(uuid) != null)
            return ranking.get(uuid);
        return 0;
    }

    private static String getString (String special) {
        switch (special) {
            case "BEDSDESTROYED":
                return "Betten zerstört";
            case "ITEMSUSED":
                return "Items benutzt";
        }
        return "";
    }

    public static Stats getStats () {
        if (Wrapper.getInstance().getServiceId().getName().startsWith("1vs1")) {
            return ONEVSONE;
        } else if (Wrapper.getInstance().getServiceId().getName().startsWith("SnowBattle")) {
            return SNOWBATTLE;
        } else if (Wrapper.getInstance().getServiceId().getName().startsWith("KBFFA")) {
            return KBFFA;
        } else if (Wrapper.getInstance().getServiceId().getName().startsWith("OITC")) {
            return OITC;
        } else if (Wrapper.getInstance().getServiceId().getName().startsWith("Destruction")) {
            return DESTRUCTION;
        } else if (Wrapper.getInstance().getServiceId().getName().startsWith("BW")) {
            return BEDWARS;
        } else if (Wrapper.getInstance().getServiceId().getName().startsWith("SpeedUHC")) {
            return SPEEDUHC;
        } else {
            return null;
        }
    }

    public static List<Stats> getAllStats () {
        List<Stats> list = new ArrayList<>();

        list.add(ONEVSONE);
        list.add(SNOWBATTLE);
        list.add(KBFFA);
        list.add(OITC);
        list.add(DESTRUCTION);
        list.add(BEDWARS);
        list.add(SPEEDUHC);

        return list;
    }

}
