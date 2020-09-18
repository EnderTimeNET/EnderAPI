package net.labymod.serverapi.bukkit.event;

import net.labymod.serverapi.Addon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.beans.ConstructorProperties;
import java.util.List;

public class LabyModPlayerJoinEvent extends Event {
    @ConstructorProperties({"player", "modVersion", "chunkCachingEnabled", "chunkCachingVersion", "addons"})
    public LabyModPlayerJoinEvent(Player player, String modVersion, boolean chunkCachingEnabled, int chunkCachingVersion, List<Addon> addons) {
        this.player = player;
        this.modVersion = modVersion;
        this.chunkCachingEnabled = chunkCachingEnabled;
        this.chunkCachingVersion = chunkCachingVersion;
        this.addons = addons;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private static final HandlerList handlerList = new HandlerList();

    private Player player;

    private String modVersion;

    private boolean chunkCachingEnabled;

    private int chunkCachingVersion;

    private List<Addon> addons;

    public Player getPlayer() {
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

    public HandlerList getHandlers() {
        return handlerList;
    }
}
