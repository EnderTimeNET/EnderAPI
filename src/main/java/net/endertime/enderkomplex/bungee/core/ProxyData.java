package net.endertime.enderkomplex.bungee.core;

import java.security.SecureRandom;

import net.md_5.bungee.api.plugin.Plugin;

public class ProxyData {

    public static SecureRandom random = new SecureRandom();

    public static Plugin Instance;

    public static String ChatlogLink = "https://enderti.me/chatlog/?id=";
    public static String ApplyLink = "https://endertime.net/apply?id=";

    public static String ChatPrefix = "§8§l┃ §5EnderTime §8» §7";

    public static Double[] Spawn = new Double[] {0.5, 100.0, 0.5, 0.0, 0.0};

}
