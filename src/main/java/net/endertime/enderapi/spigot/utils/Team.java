package net.endertime.enderapi.spigot.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    public static List<Team> teams = new ArrayList<Team>();

    private ArrayList<Player> members;
    private String name;
    private String prefix;
    private int id;
    private int maxMembers;

    public Team(String name, String prefix, int maxMembers) {
        this.id = teams.size();
        this.name = name;
        this.prefix = prefix;
        this.members = new ArrayList<Player>();
        if (!teams.contains(this))
            teams.add(this);
        this.maxMembers = maxMembers;
    }

    public int getId() {
        return id;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public ArrayList<Player> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public static boolean isInTeam (Player player) {
        for (Team teams : teams) {
            if (teams.getMembers().contains(player)) {
                return true;
            }
        }
        return false;
    }

    public static Team getTeam (Player player) {
        for (Team teams : teams) {
            if (teams.getMembers().contains(player)) {
                return teams;
            }
        }
        return null;
    }

    public static boolean isInTeam (UUID uuid) {
        for (Team team : teams) {
            for (Player member : team.getMembers()) {
                if (member.getUniqueId().equals(uuid))
                    return true;
            }
        }
        return false;
    }

    public static Team getTeam (UUID uuid) {
        for (Team team : teams) {
            for (Player member : team.getMembers()) {
                if (member.getUniqueId().equals(uuid))
                    return team;
            }
        }
        return null;
    }
}
