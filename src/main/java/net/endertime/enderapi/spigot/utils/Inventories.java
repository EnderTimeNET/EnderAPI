package net.endertime.enderapi.spigot.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventories {

    private static List<Inventories> inventories = new ArrayList<Inventories>();

    public static List<Inventories> getInventories() {
        return inventories;
    }

    public static Inventories getInventories (Player player) {
        for (Inventories inventories : getInventories()) {
            if (inventories.getPlayer().getUniqueId().equals(player.getUniqueId()))
                return inventories;
        }
        return new Inventories(player);
    }

    private Player player;
    private List<Inventory> friends;
    private List<Inventory> clans;
    private List<Inventory> parties;
    private List<Inventory> publicParties;
    private int currentFriend;
    private int currentParty;
    private int currentClan;
    private int currentPublicParty;

    public Inventories (Player player) {
        this.player = player;
        this.currentClan = 0;
        this.currentFriend = 0;
        this.currentParty = 0;
        this.currentPublicParty = 0;

        inventories.add(this);
    }

    public List<Inventory> getParties() {
        return parties;
    }

    public List<Inventory> getClans() {
        return clans;
    }

    public List<Inventory> getFriends() {
        return friends;
    }

    public Player getPlayer() {
        return player;
    }

    public void setParties(List<Inventory> parties) {
        this.parties = parties;
    }

    public void setFriends(List<Inventory> friends) {
        this.friends = friends;
    }

    public void setClans(List<Inventory> clans) {
        this.clans = clans;
    }

    public int getCurrentClan() {
        return currentClan;
    }

    public int getCurrentFriend() {
        return currentFriend;
    }

    public int getCurrentParty() {
        return currentParty;
    }

    public void setCurrentClan(int currentClan) {
        this.currentClan = currentClan;
    }

    public void setCurrentFriend(int currentFriend) {
        this.currentFriend = currentFriend;
    }

    public void setCurrentParty(int currentParty) {
        this.currentParty = currentParty;
    }

    public void setPublicParties(List<Inventory> publicParties) {
        this.publicParties = publicParties;
    }

    public List<Inventory> getPublicParties() {
        return publicParties;
    }

    public int getCurrentPublicParty() {
        return currentPublicParty;
    }

    public void setCurrentPublicParty(int currentPublicParty) {
        this.currentPublicParty = currentPublicParty;
    }
}
