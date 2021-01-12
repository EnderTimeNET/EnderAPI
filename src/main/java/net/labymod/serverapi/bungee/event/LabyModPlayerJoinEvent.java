package net.labymod.serverapi.bungee.event;

import net.labymod.serverapi.Addon;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * Class created by qlow | Jan
 */
public class LabyModPlayerJoinEvent extends Event {
    private ProxiedPlayer player;

    private String modVersion;

    private boolean chunkCachingEnabled;

    private int chunkCachingVersion;

    private List<Addon> addons;

    @ConstructorProperties({"player", "modVersion", "chunkCachingEnabled", "chunkCachingVersion", "addons"})
    public LabyModPlayerJoinEvent(ProxiedPlayer player, String modVersion, boolean chunkCachingEnabled, int chunkCachingVersion, List<Addon> addons) {
        this.player = player;
        this.modVersion = modVersion;
        this.chunkCachingEnabled = chunkCachingEnabled;
        this.chunkCachingVersion = chunkCachingVersion;
        this.addons = addons;
    }

    public LabyModPlayerJoinEvent() {}

    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    public String getModVersion() {
        return this.modVersion;
    }

    public boolean isChunkCachingEnabled() {
        return this.chunkCachingEnabled;
    }

    public int getChunkCachingVersion() {
        return this.chunkCachingVersion;
    }

    public List<Addon> getAddons() {
        return this.addons;
    }
}
