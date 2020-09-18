package net.endertime.enderapi.spigot.api;

import net.endertime.enderapi.spigot.utils.PlayerParty;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyAPI {

    private static PartyAPI instance = new PartyAPI();

    public static PartyAPI getInstance() {
        return instance;
    }

    public PartyAPI () {
        partyList = new ArrayList<PlayerParty>();
    }

    private List<PlayerParty> partyList;

    public void createParty (UUID uuid, boolean publicParty) {
        PlayerParty party = new PlayerParty(uuid, publicParty);

        getPartyList().add(party);
        party.updateTablist();
    }

    public void deleteParty (UUID uuid) {
        PlayerParty party = getParty(uuid);
        List<UUID> update = new ArrayList<UUID>();
        for (UUID member : party.getPlayers()) {
            update.add(member);
            if (NickAPI.getInstance().isNicked(member)) {
                for (UUID uuids : party.getPlayers()) {
                    if (Bukkit.getPlayer(uuids) != null) {
                        NickAPI.getInstance().getNickedParty().get(member).add(Bukkit.getPlayer(uuids));
                    }
                }
            }
        }
        party.getPlayers().clear();
        getPartyList().remove(party);
        party.updateTablist(update);
    }

    public PlayerParty getParty (UUID uuid) {
        for (PlayerParty party : getPartyList()) {
            if (party.getPlayers().contains(uuid)) {
                return party;
            }
        }
        return null;
    }

    public List<PlayerParty> getPartyList() {
        return partyList;
    }

    public List<PlayerParty> getPublicParties () {
        List<PlayerParty> list = new ArrayList<PlayerParty>();
        for (PlayerParty playerParty : getPartyList()) {
            if (playerParty.isPublicParty())
                list.add(playerParty);
        }
        return list;
    }
}
