package net.endertime.enderapi.clan.utils;

import net.endertime.enderapi.clan.ClanAPI;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class Clan {

    private String tag;
    private String name;
    private List<Rank> ranks;
    private List<UUID> members;
    private List<UUID> allMembers;
    private UUID leader;

    public Clan(String tag) {
        this.tag = tag;
        this.name = ClanAPI.getInstance().getClans().getName(tag);
        this.ranks = ClanAPI.getInstance().getRanks().getRanks(tag);
        this.leader = ClanAPI.getInstance().getMember().getLeader(this.tag);
        this.members = ClanAPI.getInstance().getMember().getMembers(this.tag, 0);
        this.allMembers = ClanAPI.getInstance().getMember().getMembers(this.tag);
    }

    public Rank getRank (String rank) {
        for (Rank ranks : this.ranks) {
            if (ranks.getRank().equals(rank)) {
                return ranks;
            }
        }
        return null;
    }

    public UUID getLeader() {
        return leader;
    }

    public String getTag() {
        return tag;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public boolean isPlayerOnline (UUID uuid) {
        if (ClanAPI.getInstance().isBungee()) {
            if (ProxyServer.getInstance().getPlayer(uuid) != null)
                return true;
            return false;
        } else {
            if (Bukkit.getPlayer(uuid) != null)
                return true;
            return false;
        }
    }

    public List<UUID> getAllMembers() {
        return allMembers;
    }

    public void update() {
        this.leader = ClanAPI.getInstance().getMember().getLeader(this.tag);
        this.members = ClanAPI.getInstance().getMember().getMembers(this.tag, 0);
        this.allMembers = ClanAPI.getInstance().getMember().getMembers(this.tag);
    }
}
