package net.endertime.enderapi.clan;

import net.endertime.enderapi.clan.mysql.*;
import net.endertime.enderapi.clan.utils.Clan;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanAPI {

    public static ClanAPI instance;

    public static ClanAPI getInstance() {
        return instance;
    }

    private boolean bungee;
    private Clans clans = new Clans();
    private ClanSettings settings = new ClanSettings();
    private Invites invites = new Invites();
    private Member member = new Member();
    private Permissions permissions = new Permissions();
    private Ranks ranks = new Ranks();

    private Map<String, Clan> clanMap = new HashMap<>();

    public Clan getClan (String tag) {
        if (clanMap.containsKey(tag)) {
            return clanMap.get(tag);
        } else {
            Clan clan = new Clan(tag);
            clanMap.put(tag, clan);
            return clan;
        }
    }

    public int getCoins (Clan clan) {
        return ClanAPI.getInstance().getClans().getCoins(clan.getTag());
    }

    public int getMaxMember (Clan clan) {
        return ClanAPI.getInstance().getClans().getMember(clan.getTag());
    }

    public int getEXP (Clan clan) {
        return ClanAPI.getInstance().getClans().getExp(clan.getTag());
    }

    public int getLevel (Clan clan) {
        return ClanAPI.getInstance().getClans().getLevel(clan.getTag());
    }

    public boolean isMute (Clan clan) {
        if (ClanAPI.getInstance().getClans().getMute(clan.getTag()) == 1)
            return true;
        else
            return false;
    }

    public boolean isMute (UUID uuid) {
        if (ClanAPI.getInstance().getMember().getMute(uuid) == 1)
            return true;
        else
            return false;
    }

    public boolean isToggle (UUID uuid) {
        if (ClanAPI.getInstance().getSettings().getToggle(uuid) == 1)
            return true;
        else
            return false;
    }

    public boolean isMSG (UUID uuid) {
        if (ClanAPI.getInstance().getSettings().getMSG(uuid) == 1)
            return true;
        else
            return false;
    }

    public String getTag (UUID uuid) {
        return getMember().getTag(uuid);
    }

    public String getTag (String clanName) {
        return getClans().getTag(clanName);
    }

    public boolean isInClan (UUID uuid) {
        return getMember().isUserExists(uuid);
    }

    public boolean isInClan (String name) {
        return isInClan(getSettings().getUUID(name));
    }


    public ClanAPI(boolean bungee) {
        this.bungee = bungee;
    }

    public Clans getClans() {
        return clans;
    }

    public ClanSettings getSettings() {
        return settings;
    }

    public Invites getInvites() {
        return invites;
    }

    public Member getMember() {
        return member;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public Ranks getRanks() {
        return ranks;
    }

    public boolean isBungee() {
        return bungee;
    }
}
