package net.endertime.enderapi.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.endertime.enderapi.clan.ClanAPI;
import net.endertime.enderapi.database.databaseapi.DataBaseAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.api.PermAPI;
import net.endertime.enderapi.spigot.commands.*;
import net.endertime.enderapi.spigot.listener.*;
import net.endertime.enderapi.spigot.listener.VanishListener;
import net.endertime.enderkomplex.spigot.commands.*;
import net.endertime.enderkomplex.spigot.core.ServerData;
import net.endertime.enderkomplex.spigot.objects.InfoFeed;
import net.endertime.enderkomplex.spigot.utils.*;
import net.labymod.serverapi.Addon;
import net.labymod.serverapi.LabyModAPI;
import net.labymod.serverapi.LabyModConfig;
import net.labymod.serverapi.Permission;
import net.labymod.serverapi.bukkit.BukkitLabyModConfig;
import net.labymod.serverapi.bukkit.event.LabyModPlayerJoinEvent;
import net.labymod.serverapi.bukkit.event.MessageReceiveEvent;
import net.labymod.serverapi.bukkit.event.MessageSendEvent;
import net.labymod.serverapi.bukkit.event.PermissionsSendEvent;
import net.labymod.serverapi.bukkit.listener.PlayerJoinListener;
import net.labymod.serverapi.bukkit.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Spigot extends JavaPlugin {

    private static Spigot plugin = null;

    public static Spigot getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        ServerData.Instance = this;
        DataBaseAPI.instance = new DataBaseAPI(false);
        PermAPI.instance = new PermAPI();
        ClanAPI.instance = new ClanAPI(false);

        labySetup();

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "enderkomplex", new VanishListener());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BadlionListener());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "enderkomplex", new ChannelListener());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "enderkomplex");

        registerCommands();
        registerEvents(this.getServer().getPluginManager());

        if (Wrapper.getInstance().getServiceId().getName().startsWith("1vs1")
                || Wrapper.getInstance().getServiceId().getName().startsWith("SnowBattle")
                || Wrapper.getInstance().getServiceId().getName().startsWith("KBFFA")
                || Wrapper.getInstance().getServiceId().getName().startsWith("OITC")
                || Wrapper.getInstance().getServiceId().getName().startsWith("Destruction")
                || Wrapper.getInstance().getServiceId().getName().startsWith("BW")
                || Wrapper.getInstance().getServiceId().getName().startsWith("Gameserver")) {
            getCommand("stats").setExecutor(new Stats_Command());
            this.getServer().getPluginManager().registerEvents(new Stats_Command(), this);
        }

        if (NickAPI.getInstance().isNickAvailable()) {
            NickAPI.getInstance().registerNickEvent();
        }

        clearRecipes();
        registerProtocolSoundBlocker();

        if (CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName("Proxy-1").getAddress().getPort() == 25565) {
            BossbarHandler.startUpdater();
            AFKTimer.startTimer();
            new InfoFeed(this);
            registerNicks();
        }
    }

    private void registerNicks() {
        NickAPI.getInstance().setNicks(EnderAPI.getInstance().getNickDatabase().countRowsInTable());
    }

    private void clearRecipes(){
        if (!Wrapper.getInstance().getServiceId().getName().startsWith("Terra")
                && !Wrapper.getInstance().getServiceId().getName().startsWith("SpeedUHC")) {
            Bukkit.clearRecipes();
        }
    }

    private void registerEvents(final PluginManager pm) {
        pm.registerEvents(new AsyncPlayerChatListener(), this);
        pm.registerEvents(new net.endertime.enderapi.spigot.listener.PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);


        if (CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName("Proxy-1").getAddress().getPort() == 25565) {
            pm.registerEvents(new BadlionListener(), this);
            pm.registerEvents(new Restart_Command(), this);
            pm.registerEvents(new PlayerChatTabCompleteListener(), this);
            pm.registerEvents(new PlayerLoginListener(), this);
            pm.registerEvents(new TabCompleteListener(), this);
            pm.registerEvents(new net.endertime.enderkomplex.spigot.utils.VanishListener(), this);


            pm.registerEvents(new CheckCommand(), this);
            pm.registerEvents(new ReportListener(), this);
            pm.registerEvents(new AltsMenu(), this);
            pm.registerEvents(new PlayerInfoMenu(), this);
            pm.registerEvents(new QuitListener(), this);
            pm.registerEvents(new net.endertime.enderkomplex.spigot.utils.VanishListener(), this);
            pm.registerEvents(new ReportsMenu(), this);
            pm.registerEvents(new NotifyMenu(), this);
            pm.registerEvents(new EditMenu(), this);
            pm.registerEvents(new ApplyMenu(), this);
            pm.registerEvents(new AFKTimer(), this);
            pm.registerEvents(new NewsBossbar(), this);
        }
    }

    private void registerCommands() {
        getCommand("addcoins").setExecutor(new AddCoins_Command());
        getCommand("removecoins").setExecutor(new RemoveCoins_Command());
        getCommand("resetcoins").setExecutor(new ResetCoins_Command());
        getCommand("perm").setExecutor(new Perm_Command());
        getCommand("rank").setExecutor(new Rank_Command());
        getCommand("reset").setExecutor(new Reset_Command());
        /*getCommand("checknick").setExecutor(new CheckNick_Command());
        getCommand("loadskins").setExecutor(new LoadSkins_Command());
        getCommand("deleteoffuuid").setExecutor(new OfflineUUIDDelete_Command());*/


        if (CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName("Proxy-1").getAddress().getPort() == 25565) {
            getCommand("rs").setExecutor(new Restart_Command());
            getCommand("unnick").setExecutor(new Unnick_Command());
            getCommand("nicklist").setExecutor(new NickList_Command());
            getCommand("savenick").setExecutor(new SaveNick_Command());



            getCommand("check").setExecutor(new CheckCommand());
            getCommand("report").setExecutor(new ReportCommand());
            getCommand("pi").setExecutor(new PlayerInfoCommand());
            getCommand("if").setExecutor(new InfoFeedCommand());
            getCommand("v").setExecutor(new VanishCommand());
            getCommand("reports").setExecutor(new ReportsCommand());
            getCommand("notifys").setExecutor(new NotifysCommand());
            getCommand("ec").setExecutor(new EnderchestCommand());
            getCommand("invsee").setExecutor(new InvseeCommand());
            getCommand("tp").setExecutor(new TeleportCommand());
            getCommand("em").setExecutor(new EditMuteCommand());
            getCommand("eb").setExecutor(new EditBanCommand());
            getCommand("apply").setExecutor(new ApplyCommand());
            getCommand("verify").setExecutor(new VerifyCommand());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
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

    private PacketUtils packetUtils;

    public LabyModAPI getApi() {
        return this.api;
    }

    public PacketUtils getPacketUtils() {
        return this.packetUtils;
    }

    public void labySetup() {
        this.packetUtils = new PacketUtils();
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();
        this.labyModConfig = new BukkitLabyModConfig(new File(this.getDataFolder(), "config.yml"));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        EnderAPI.getInstance().getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this, "LABYMOD", new PluginMessageListener() {
            public void onPluginMessageReceived(String channel, final Player player, byte[] bytes) {
                ByteBuf buf = Unpooled.wrappedBuffer(bytes);
                try {
                    final String version = Spigot.this.api.readString(buf, 32767);
                    Bukkit.getScheduler().runTask((Plugin)Spigot.this, new Runnable() {
                        public void run() {
                            if (!player.isOnline())
                                return;
                            Bukkit.getPluginManager().callEvent((Event)new LabyModPlayerJoinEvent(player, version, false,
                                    0, new ArrayList()));
                        }
                    });
                } catch (RuntimeException runtimeException) {}
            }
        });
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "LMC", new PluginMessageListener() {
            public void onPluginMessageReceived(String channel, final Player player, byte[] bytes) {
                ByteBuf buf = Unpooled.wrappedBuffer(bytes);
                try {
                    final String messageKey = Spigot.this.api.readString(buf, 32767);
                    String messageContents = Spigot.this.api.readString(buf, 32767);
                    final JsonElement jsonMessage = Spigot.jsonParser.parse(messageContents);
                    Bukkit.getScheduler().runTask((Plugin)Spigot.this, new Runnable() {
                        public void run() {
                            if (!player.isOnline())
                                return;
                            if (messageKey.equals("INFO") && jsonMessage.isJsonObject()) {
                                JsonObject jsonObject = jsonMessage.getAsJsonObject();
                                String version = (jsonObject.has("version") && jsonObject.get("version").isJsonPrimitive() &&
                                        jsonObject.get("version").getAsJsonPrimitive().isString()) ? jsonObject.get("version").getAsString() : "Unknown";
                                boolean chunkCachingEnabled = false;
                                int chunkCachingVersion = 0;
                                if (jsonObject.has("ccp") && jsonObject.get("ccp").isJsonObject()) {
                                    JsonObject chunkCachingObject = jsonObject.get("ccp").getAsJsonObject();
                                    if (chunkCachingObject.has("enabled"))
                                        chunkCachingEnabled = chunkCachingObject.get("enabled").getAsBoolean();
                                    if (chunkCachingObject.has("version"))
                                        chunkCachingVersion = chunkCachingObject.get("version").getAsInt();
                                }
                                Bukkit.getPluginManager().callEvent((Event)new LabyModPlayerJoinEvent(player, version, chunkCachingEnabled, chunkCachingVersion,
                                        Addon.getAddons(jsonObject)));
                                return;
                            }
                            Bukkit.getPluginManager().callEvent((Event)new MessageReceiveEvent(player, messageKey, jsonMessage));
                        }
                    });
                } catch (RuntimeException runtimeException) {}
            }
        });
    }

    public void sendPermissions(Player player) {
        Map<Permission, Boolean> modifiedPermissions = new HashMap<>();
        modifiedPermissions.putAll(labyModConfig.permissions);
        PermissionsSendEvent sendEvent = new PermissionsSendEvent(player, modifiedPermissions, false);
        Bukkit.getPluginManager().callEvent((Event)sendEvent);
        if (!sendEvent.isCancelled() && sendEvent.getPermissions().size() > 0)
            this.packetUtils.sendPacket(player, this.packetUtils.getPluginMessagePacket("LMC", this.api.getBytesToSend(modifiedPermissions)));
    }

    public void sendServerMessage(Player player, String messageKey, JsonElement messageContents) {
        messageContents = cloneJson(messageContents);
        MessageSendEvent sendEvent = new MessageSendEvent(player, messageKey, messageContents, false);
        Bukkit.getPluginManager().callEvent((Event)sendEvent);
        if (!sendEvent.isCancelled())
            this.packetUtils.sendPacket(player, this.packetUtils.getPluginMessagePacket("LMC", this.api.getBytesToSend(messageKey, messageContents.toString())));
    }

    public JsonElement cloneJson(JsonElement cloneElement) {
        try {
            return jsonParser.parse(cloneElement.toString());
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void registerProtocolSoundBlocker() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL,
                PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                if(e.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                    if(e.getPacket().getSoundEffects().getValues().contains(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE)
                            || e.getPacket().getSoundEffects().getValues().contains(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK)) {
                        e.setCancelled(true);
                    }
                }
            }
        });
    }
}
