package net.endertime.enderapi.spigot.api;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.utils.State;

public class GameAPI {

    public static GameAPI instance = new GameAPI();

    public static GameAPI getInstance() {
        return instance;
    }

    private boolean hideSpectatorChat = false; //Is true only Spectator see Chat
    private boolean spectatorDiePrefixChat = false; //Is true the Chat got a X
    private boolean globalChat = false;
    private State state = State.ONLINE;

    public void setState(State state) {
        BukkitCloudNetHelper.setState(State.toString(state));
        Wrapper.getInstance().publishServiceInfoUpdate();
        this.state = state;
    }

    public void setMOTD (String motd) {
        BukkitCloudNetHelper.setApiMotd(motd);
        Wrapper.getInstance().publishServiceInfoUpdate();
    }

    public void setGlobalChat(boolean globalChat) {
        this.globalChat = globalChat;
    }

    public void setSpectatorDiePrefixChat(boolean spectatorDiePrefixChat) {
        this.spectatorDiePrefixChat = spectatorDiePrefixChat;
    }

    public void setHideSpectatorChat(boolean hideSpectatorChat) {
        this.hideSpectatorChat = hideSpectatorChat;
    }

    public State getState() {
        return state;
    }

    public boolean isGlobalChat() {
        return globalChat;
    }

    public boolean isSpectatorDiePrefixChat() {
        return spectatorDiePrefixChat;
    }

    public boolean isHideSpectatorChat() {
        return hideSpectatorChat;
    }
}
