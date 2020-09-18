package net.endertime.enderapi.bungee.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerParty {

    private ProxiedPlayer leader;
    private List<ProxiedPlayer> members;
    private List<ProxiedPlayer> invites;
    private boolean publicParty;

    public PlayerParty(ProxiedPlayer leader, boolean publicParty) {
        this.leader = leader;
        this.publicParty = publicParty;
        this.members = new ArrayList<ProxiedPlayer>();
        this.invites = new ArrayList<ProxiedPlayer>();
    }

    public void setLeader(ProxiedPlayer leader) {
        this.leader = leader;
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }

    public List<ProxiedPlayer> getMembers() {
        return members;
    }

    public List<ProxiedPlayer> getInvites() {
        return invites;
    }

    public boolean inParty (ProxiedPlayer p) {
        if (getMembers().contains(p)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLeader (ProxiedPlayer p) {
        return getLeader().equals(p);
    }

    public boolean isPublicParty() {
        return publicParty;
    }
}
