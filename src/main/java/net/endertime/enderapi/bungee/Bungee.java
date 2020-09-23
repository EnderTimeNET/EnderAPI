package net.endertime.enderapi.bungee;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.commands.*;
import net.endertime.enderapi.bungee.listener.LoginListener;
import net.endertime.enderapi.clan.ClanAPI;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.database.databaseapi.mysql.PreparedStatement;
import net.endertime.enderapi.bungee.api.PermAPI;
import net.endertime.enderkomplex.bungee.commands.*;
import net.endertime.enderapi.bungee.listener.*;
import net.endertime.enderkomplex.bungee.container.ChatBlacklist;
import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.utils.*;
import net.endertime.enderkomplex.mysql.Database;
import net.labymod.serverapi.LabyModAPI;
import net.labymod.serverapi.LabyModConfig;
import net.labymod.serverapi.Permission;
import net.labymod.serverapi.bungee.BungeecordLabyModConfig;
import net.labymod.serverapi.bungee.event.MessageSendEvent;
import net.labymod.serverapi.bungee.event.PermissionsSendEvent;
import net.labymod.serverapi.bungee.listener.PlayerJoinListener;
import net.labymod.serverapi.bungee.listener.PluginMessageListener;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.PluginMessage;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bungee extends Plugin {

    private static Bungee instance = null;

    public static Bungee getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ProxyData.Instance = this;
        DataBaseAPI.instance = new DataBaseAPI(true);
        ClanAPI.instance = new ClanAPI(true);
        PermAPI.instance = new PermAPI();

        register(getProxy().getPluginManager());

        getProxy().registerChannel("enderkomplex");

        if (CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName("Proxy-1").getAddress().getPort() == 25565) {
            doAllOffline();
            resetStats();
            checkRanks();
        }
    }

    @Override
    public void onDisable() {
    }

    private void register(PluginManager pm) {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        this.labyModConfig = new BungeecordLabyModConfig(new File(getDataFolder(), "config.yml"));
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
        getProxy().getPluginManager().registerListener(this, new PluginMessageListener());


        if (CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName("Proxy-1").getAddress().getPort() == 25565) {
            ChatBlacklist.fillArrayList();

            if(new SimpleDateFormat("HH").format(new Date()).equals("03")) {
                Database.deleteAllUselessApplys();
            }

            InfoCollector.lastrestart = new Date();
            InfoCollector.startClearRunnable();

            CloudNetDriver.getInstance().getEventManager().registerListener(new ChannelMessageReceiveListener());
            CloudNetDriver.getInstance().getEventManager().registerListener(new CloudServerStartListener());

            pm.registerListener(this, new LoginListener());
            pm.registerListener(this, new PlayerDisconnectListener());
            pm.registerListener(this, new PostLoginListener());
            pm.registerListener(this, new ServerSwitchListener());

            pm.registerListener(this, new ChatListener());
            pm.registerListener(this, new net.endertime.enderkomplex.bungee.utils.ChannelListener());
            pm.registerListener(this, new net.endertime.enderkomplex.bungee.utils.LoginListener());
            pm.registerListener(this, new DisconnectListener());
            pm.registerListener(this, new InfoCollector());
            pm.registerListener(this, new TeamchatCommand("tc"));
            pm.registerListener(this, new JoinmeCommand("joinme"));
            pm.registerListener(this, new CommandHider());
            pm.registerListener(this, new ShortCommandListener());
            pm.registerListener(this, new OnlineTime());
            pm.registerListener(this, new JoinSpammer());
            pm.registerListener(this, new GameTitles());

            pm.registerCommand(this, new Friend_Command("f", EnderAPI.getInstance().getFriend()));
            pm.registerCommand(this, new Friend_Command("friend", EnderAPI.getInstance().getFriend()));
            pm.registerCommand(this, new MSG_Command(EnderAPI.getInstance().getFriend()));
            pm.registerCommand(this, new Reply_Command("r"));
            pm.registerCommand(this, new Reply_Command("reply"));
            pm.registerCommand(this, new Party_Command("p", EnderAPI.getInstance().getPartyManager()));
            pm.registerCommand(this, new Party_Command("party", EnderAPI.getInstance().getPartyManager()));
            pm.registerCommand(this, new Clan_Command("c"));
            pm.registerCommand(this, new Clan_Command("clan"));

            pm.registerCommand(this, new BanCommand("ban"));
            pm.registerCommand(this, new MuteCommand("mute"));
            pm.registerCommand(this, new UnbanCommand("unban"));
            pm.registerCommand(this, new UnmuteCommand("unmute"));
            pm.registerCommand(this, new ChatclearCommand("cc"));
            pm.registerCommand(this, new JumpCommand("jump"));
            pm.registerCommand(this, new ShowSkinCommand("showskin"));
            pm.registerCommand(this, new VjumpCommand("vjump"));
            pm.registerCommand(this, new ClosereportCommand("closereport"));
            pm.registerCommand(this, new RejectreportCommand("rejectreport"));
            pm.registerCommand(this, new KickCommand("kick"));
            pm.registerCommand(this, new ChatmuteCommand("chatmute"));
            pm.registerCommand(this, new ChatlogCommand("chatlog"));
            pm.registerCommand(this, new InfoCommand("info"));
            pm.registerCommand(this, new TeamchatCommand("tc"));
            pm.registerCommand(this, new PingCommand("ping"));
            pm.registerCommand(this, new BroadcastCommand("bc"));
            pm.registerCommand(this, new JoinmeCommand("joinme"));
            pm.registerCommand(this, new HelpCommand("help"));
        }
    }

    private void resetStats() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        if(sdf.format(now).startsWith("01.")) {
            if(!EnderAPI.getInstance().getAutoReset().getLastAutoReset("SnowBattle").equals(sdf.format(now))) {
                reset("BW");
                reset("DESTRUCTION");
                reset("KBFFA");
                reset("SNOWBAATTLE");
                reset("SPEEDUHC");
                reset("OITC");
            }
        }
    }

    private void reset(String service) {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("TRUNCATE TABLE " + service + "_MONTHLY");
        EnderAPI.getInstance().getAutoReset().getMysql().runAsyncUpdate(ps);
        EnderAPI.getInstance().getAutoReset().updateLastAutoReset(service);
    }

    private void doAllOffline() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT UUID FROM FRIENDSETTINGS WHERE ONLINE = ?");
        ps.setInt(1, 1);
        try {
            ResultSet rs = EnderAPI.getInstance().getFriend().getSettings().mysql.runAsyncQuery(ps);
            while (rs.next()) {
                EnderAPI.getInstance().getFriend().getSettings().updateOnline(UUID.fromString(rs.getString("UUID")), false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            EnderAPI.getInstance().getFriend().getSettings().mysql.closeConnections(ps);
        }
    }

    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    private static final JsonParser jsonParser = new JsonParser();

    private LabyModConfig labyModConfig;

    public LabyModConfig getLabyModConfig() {
        return this.labyModConfig;
    }

    private LabyModAPI api = new LabyModAPI();

    public LabyModAPI getApi() {
        return this.api;
    }

    public void sendPermissions(ProxiedPlayer player) {
        Map<Permission, Boolean> modifiedPermissions = new HashMap<>( labyModConfig.permissions );

        // Calling the Bukkit event
        PermissionsSendEvent sendEvent = new PermissionsSendEvent( player, modifiedPermissions, false );
        getProxy().getPluginManager().callEvent( sendEvent );

        // Sending the packet
        if ( !sendEvent.isCancelled() )
            player.unsafe().sendPacket( new PluginMessage( "LMC", api.getBytesToSend( modifiedPermissions ), false ) );
    }

    public void sendServerMessage(ProxiedPlayer player, String messageKey, JsonElement messageContents) {
        messageContents = cloneJson(messageContents);
        MessageSendEvent sendEvent = new MessageSendEvent(player, messageKey, messageContents, false);
        getProxy().getPluginManager().callEvent((Event) sendEvent);
        if (!sendEvent.isCancelled())
            player.unsafe().sendPacket((DefinedPacket) new PluginMessage("LMC",
                    this.api.getBytesToSend(messageKey, messageContents.toString()), false));
    }

    public JsonElement cloneJson(JsonElement cloneElement) {
        try {
            return jsonParser.parse(cloneElement.toString());
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void checkRanks() {
        PreparedStatement ps = DataBaseAPI.getInstance().getPreparedStatement("SELECT * FROM USERS WHERE TIME != -1");

        ResultSet rs = EnderAPI.getInstance().getUsers().getMysql().runAsyncQuery(ps);

        long current = java.lang.System.currentTimeMillis();
        try {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("UUID"));
                long timeset = rs.getLong("TIMESET");
                long time = rs.getLong("TIME");

                if (time + timeset < current) {
                    PermAPI.getInstance().getUsers().updateTime(uuid, -1);
                    PermAPI.getInstance().getUsers().updateRank(uuid, "default");
                }
            }
        } catch (SQLException ignored) {
        } finally {
            EnderAPI.getInstance().getUsers().getMysql().closeConnections(ps);
        }
    }
}
