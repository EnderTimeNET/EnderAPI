package net.endertime.enderapi.bungee.api;

import com.google.gson.JsonObject;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import net.endertime.enderapi.bungee.Bungee;
import net.endertime.enderapi.bungee.api.PermAPI;
import net.endertime.enderapi.bungee.utils.FriendManager;
import net.endertime.enderapi.bungee.utils.PartyManager;
import net.endertime.enderapi.bungee.utils.State;
import net.endertime.enderapi.bungee.utils.Version;
import net.endertime.enderapi.database.enderapi.AutoReset;
import net.endertime.enderapi.database.enderapi.EnderDatabase;
import net.endertime.enderapi.database.enderapi.NickDatabase;
import net.endertime.enderapi.database.enderapi.TeamDatabase;
import net.endertime.enderapi.permission.mysql.RankPermissions;
import net.endertime.enderapi.permission.mysql.Ranks;
import net.endertime.enderapi.permission.mysql.UserPermissions;
import net.endertime.enderapi.permission.mysql.Users;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class EnderAPI {
    private static EnderAPI instance = new EnderAPI();

    public static EnderAPI getInstance() {
        return instance;
    }

    public Bungee getPlugin() {
        return Bungee.getInstance();
    }

    private Ranks ranks = new Ranks();
    private Users users = new Users();
    private UserPermissions userPermissions = new UserPermissions();
    private RankPermissions rankPermissions = new RankPermissions();

    private TeamDatabase teamDatabase = new TeamDatabase();
    private NickDatabase nickDatabase = new NickDatabase();
    private EnderDatabase enderDatabase = new EnderDatabase();
    private List<UUID> badlion = new ArrayList<>();

    public List<UUID> getBadlion() {
        return badlion;
    }

    public TeamDatabase getTeamDatabase() {
        return teamDatabase;
    }

    public NickDatabase getNickDatabase() {
        return nickDatabase;
    }

    public EnderDatabase getEnderDatabase() {
        return enderDatabase;
    }

    public Map<ProxiedPlayer, List<String>> permissions = new HashMap<>();

    public RankPermissions getRankPermissions() {
        return rankPermissions;
    }

    public Ranks getRanks() {
        return ranks;
    }

    public UserPermissions getUserPermissions() {
        return userPermissions;
    }

    public Users getUsers() {
        return users;
    }

    public void addCoins(Player p, int amount) {
        getEnderDatabase().updateCoins(p.getUniqueId(), getEnderDatabase().getCoins(p.getUniqueId()) + (amount * 2));
    }

    public void addPoints(Player p, int amount) {
        if (!p.hasPermission("teamserver.join"))
            getEnderDatabase().updatePoints(p.getUniqueId(), getEnderDatabase().getPoints(p.getUniqueId()) + (amount * 2));
    }

    public boolean removeCoins(Player p, int amount) {
        if (getEnderDatabase().getCoins(p.getUniqueId()) - amount >= 0) {
            getEnderDatabase().updateCoins(p.getUniqueId(), getEnderDatabase().getCoins(p.getUniqueId()) - amount);
            return true;
        }
        return false;
    }

    public boolean removePoints(Player p, int amount) {
        if (!p.hasPermission("teamserver.join")) {
            if (getEnderDatabase().getPoints(p.getUniqueId()) - amount >= 0) {
                getEnderDatabase().updatePoints(p.getUniqueId(), getEnderDatabase().getPoints(p.getUniqueId()) - amount);
                return true;
            }
        }
        return false;
    }

    public int getCoins(UUID uuid) {
        return getEnderDatabase().getCoins(uuid);
    }

    public int getPoints(UUID uuid) {
        return getEnderDatabase().getPoints(uuid);
    }

    public String getRang(UUID uuid) {
        return getRanks().getName(getUsers().getRank(uuid));
    }

    public boolean isInTeam (UUID uuid) {
        String group = getUsers().getRank(uuid);

        if (!group.equals("Owner")
                &&!group.equals("Admin")
                && !group.equals("SrDev")
                && !group.equals("SrMod")
                && !group.equals("SrBuild")
                && !group.equals("Dev")
                && !group.equals("Content")
                && !group.equals("Mod")
                && !group.equals("Build")
                && !group.equals("Sup")
                && !group.equals("Design")
                && !group.equals("JrDev")
                && !group.equals("JrContent")
                && !group.equals("JrBuild")
                && !group.equals("JrSup")
                && !group.equals("JrDesign")) {
            return false;
        }
        return true;
    }

    public UUID getUUID (String name) {
        return getEnderDatabase().getUUID(name);
    }

    public String getName(UUID uuid) {
        return getEnderDatabase().getName(uuid);
    }

    public String getPrefix (UUID uuid) {
        String prefix = PermAPI.getInstance().getRanks().getPrefix(PermAPI.getInstance().getGroup(uuid));
        if (prefix == null)
            return "§7";
        return prefix;
    }

    public String getCompletedPrefix (UUID uuid) {
        return getRanks().getCompletedPrefix(getUsers().getRank(uuid));
    }

    public Version getVersion (UUID uuid) {
        return Version.versions.get(uuid);
    }

    public void sendCurrentPlayingGamemode(ProxiedPlayer player, boolean visible, String gamemodeName) {
        JsonObject object = new JsonObject();
        object.addProperty( "show_gamemode", visible ); // Gamemode visible for everyone
        object.addProperty( "gamemode_name", gamemodeName ); // Name of the current playing gamemode

        // Send to LabyMod using the API
        getPlugin().sendServerMessage( player, "server_gamemode", object );
    }

    public State getState (String server) {
        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(server);

        String state = serviceInfoSnapshot.getProperty(BridgeServiceProperty.STATE).orElse("");

        return State.fromString(state);
    }

    private String noPerm = "§8§l┃ §5EnderTime §8» §7Der Befehl wurde nicht gefunden oder ist gesperrt!";

    private String prefixFriend = "§8§l┃ §5Freunde §8» §7";
    private String prefixParty = "§8§l┃ §5Party §8» §7";
    private String consolePrefix = "§8[§5EnderAPI§8] §7";
    private String prefix = "§8§l┃ §5EnderAPI §8» §7";

    public BaseComponent[] getMessage(String message) {
        return TextComponent.fromLegacyText(message);
    }

    private Map<ProxiedPlayer, ProxiedPlayer> msg = new HashMap<ProxiedPlayer, ProxiedPlayer>();

    private List<ProxiedPlayer> publicParty = new ArrayList<>();

    private FriendManager friend = new FriendManager();
    private PartyManager partyManager = new PartyManager();

    private AutoReset autoReset = new AutoReset();

    public AutoReset getAutoReset() {
        return autoReset;
    }

    public String prefixClan = "§8§l┃ §6Clan §8» ";

    public String getPrefix(ProxiedPlayer pp) {
        String prefix = PermAPI.getInstance().getRanks().getPrefix(PermAPI.getInstance().getGroup(pp.getUniqueId()));
        if (prefix == null)
            return "§7";
        return prefix;
    }

    public Map<ProxiedPlayer, ProxiedPlayer> getMsg() {
        return msg;
    }

    public String getPrefixParty() {
        return prefixParty;
    }

    public String getPrefixFriend() {
        return prefixFriend;
    }

    public String getNoPerm() {
        return noPerm;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getConsolePrefix() {
        return consolePrefix;
    }

    public FriendManager getFriend() {
        return friend;
    }

    public List<ProxiedPlayer> getPublicParty() {
        return publicParty;
    }

    public Map<ProxiedPlayer, List<String>> getPermissions() {
        return permissions;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public String getPrefixClan() {
        return prefixClan;
    }
}
