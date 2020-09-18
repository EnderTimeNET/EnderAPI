package net.endertime.enderapi.bungee.utils;

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

    public static State fromString (String state) {
        if (state.equals(""))
            return null;

        if (state.equals("§6Wartelobby")
                || state.equals("§aWartelobby")) {
            return LOBBY;
        } else if (state.equals("§6Ingame")) {
            return INGAME;
        } else if (state.equals("§cRestart")) {
            return RESTART;
        } else if (state.equals("§aonline")) {
            return ONLINE;
        } else if (state.equals("§cBETA")) {
            return BETA;
        } else if (state.equals("§4Wartung")) {
            return MAINTENANCE;
        }

        return null;
    }

}
