package net.endertime.enderapi.spigot.utils;

import org.bukkit.Bukkit;

public enum State {

    LOBBY(),
    INGAME(),
    RESTART(),
    ONLINE(),
    MAINTENANCE(),
    BETA();

    public static String toString (State state) {
        if (state == null)
            return "";

        switch (state) {
            case LOBBY:
                if (Bukkit.getMaxPlayers() == Bukkit.getOnlinePlayers().size()) {
                    return "§6Wartelobby";
                } else {
                    return "§aWartelobby";
                }
            case INGAME:
                return  "§6Ingame";
            case RESTART:
                return "§cRestart";
            case ONLINE:
                return "§aonline";
            case BETA:
                return "§cBETA";
            case MAINTENANCE:
                return "§4Wartung";
            default:
                return "";
        }
    }

}
