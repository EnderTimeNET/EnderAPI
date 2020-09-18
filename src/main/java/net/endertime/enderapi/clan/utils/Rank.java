package net.endertime.enderapi.clan.utils;

import net.endertime.enderapi.clan.ClanAPI;

import java.util.List;
import java.util.UUID;

public class Rank {

    private String tag;
    private String rank;
    private String prefix;
    private int itemID;
    private int value;
    private List<UUID> players;

    public Rank(String tag, String rank) {
        this.tag = tag;
        this.rank = rank;
        this.prefix = ClanAPI.getInstance().getRanks().getPrefix(tag, rank);
        this.itemID = ClanAPI.getInstance().getRanks().getItemId(tag, rank);
        this.value = ClanAPI.getInstance().getRanks().getValue(tag, rank);
        this.players = ClanAPI.getInstance().getRanks().getPlayers(this);
    }

    public String getTag() {
        return tag;
    }

    public int getValue() {
        return value;
    }

    public String getRank() {
        return rank;
    }

    public boolean hasPerm (String perm) {
        return ClanAPI.getInstance().getPermissions().hasPerm(tag, value, perm);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getItemID() {
        return itemID;
    }
}
