package net.endertime.enderkomplex.bungee.utils;

import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class CommandHider implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e) {
        if(e.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            if(!p.hasPermission("teamserver.join")) {
                if(e.getMessage().toLowerCase().startsWith("/bukkit")
                        | e.getMessage().toLowerCase().startsWith("/minecraft")
                        | e.getMessage().toLowerCase().startsWith("/me")
                        | e.getMessage().toLowerCase().startsWith("/?")
                        | e.getMessage().toLowerCase().startsWith("/reload")
                        | e.getMessage().toLowerCase().startsWith("/timings")
                        | e.getMessage().toLowerCase().startsWith("/version")
                        | e.getMessage().toLowerCase().startsWith("/ban")
                        | e.getMessage().toLowerCase().startsWith("/ban-ip")
                        | e.getMessage().toLowerCase().startsWith("/banlist")
                        | e.getMessage().toLowerCase().startsWith("/clear")
                        | e.getMessage().toLowerCase().startsWith("/debug")
                        | e.getMessage().toLowerCase().startsWith("/defaultgamemode")
                        | e.getMessage().toLowerCase().startsWith("/deop")
                        | e.getMessage().toLowerCase().startsWith("/difficulty")
                        | e.getMessage().toLowerCase().startsWith("/effect")
                        | e.getMessage().toLowerCase().startsWith("/enchant")
                        | e.getMessage().toLowerCase().startsWith("/gamemrule")
                        | e.getMessage().toLowerCase().startsWith("/give")
                        | e.getMessage().toLowerCase().startsWith("/kick")
                        | e.getMessage().toLowerCase().startsWith("/kill")
                        | e.getMessage().toLowerCase().startsWith("/list")
                        | e.getMessage().toLowerCase().startsWith("/pardon")
                        | e.getMessage().toLowerCase().startsWith("/pardon-ip")
                        | e.getMessage().toLowerCase().startsWith("/playsound")
                        | e.getMessage().toLowerCase().startsWith("/save-all")
                        | e.getMessage().toLowerCase().startsWith("/save-off")
                        | e.getMessage().toLowerCase().startsWith("/save-on")
                        | e.getMessage().toLowerCase().startsWith("/say")
                        | e.getMessage().toLowerCase().startsWith("/scoreboard")
                        | e.getMessage().toLowerCase().startsWith("/seed")
                        | e.getMessage().toLowerCase().startsWith("/setblock")
                        | e.getMessage().toLowerCase().startsWith("/fill")
                        | e.getMessage().toLowerCase().startsWith("/setidletimeout")
                        | e.getMessage().toLowerCase().startsWith("/setworldspawn")
                        | e.getMessage().toLowerCase().startsWith("/spawnpoint")
                        | e.getMessage().toLowerCase().startsWith("/spreadplayers")
                        | e.getMessage().toLowerCase().startsWith("/stop")
                        | e.getMessage().toLowerCase().startsWith("/summon")
                        | e.getMessage().toLowerCase().startsWith("/tellraw")
                        | e.getMessage().toLowerCase().startsWith("/testfor")
                        | e.getMessage().toLowerCase().startsWith("/testforblock")
                        | e.getMessage().toLowerCase().startsWith("/time")
                        | e.getMessage().toLowerCase().startsWith("/toggledownfall")
                        | e.getMessage().toLowerCase().startsWith("/tp ")
                        | e.getMessage().toLowerCase().startsWith("/weather")
                        | e.getMessage().toLowerCase().startsWith("/whitelist")
                        | e.getMessage().toLowerCase().startsWith("/gamemode")
                        | e.getMessage().toLowerCase().startsWith("/server")
                        | e.getMessage().toLowerCase().startsWith("/bungee")
                        | e.getMessage().toLowerCase().startsWith("/bungeecord")
                        | e.getMessage().toLowerCase().startsWith("/cloud")
                        | e.getMessage().toLowerCase().startsWith("/op")
                        | e.getMessage().toLowerCase().startsWith("/ip")
                        | e.getMessage().toLowerCase().startsWith("/pex")
                        | e.getMessage().toLowerCase().startsWith("/perms")
                        | e.getMessage().toLowerCase().startsWith("/permissions")
                        | e.getMessage().toLowerCase().startsWith("/spartan")
                        | e.getMessage().toLowerCase().startsWith("/trigger")
                        | e.getMessage().toLowerCase().startsWith("/exploitfixer")
                        | e.getMessage().toLowerCase().startsWith("/ef")
                        | e.getMessage().toLowerCase().startsWith("/aegis")
                        | e.getMessage().toLowerCase().startsWith("/flamecord")
                        | e.getMessage().toLowerCase().startsWith("/fawe")
                        | e.getMessage().toLowerCase().startsWith("/permissionseex")
                        | e.getMessage().toLowerCase().startsWith("/about")) {
                    e.setCancelled(true);
                    p.sendMessage(TextComponent.fromLegacyText(ProxyData.ChatPrefix + "§7Dieser §7Befehl §7wurde §7nicht §7gefunden §7oder §7ist §7gesperrt!"));
                    return;
                }
                if(!p.getServer().getInfo().getName().contains("Vorbauen")) {
                    if(e.getMessage().startsWith("//")) {
                        e.setCancelled(true);
                        p.sendMessage(TextComponent.fromLegacyText(ProxyData.ChatPrefix + "§7Dieser §7Befehl §7wurde §7nicht §7gefunden §7oder §7ist §7gesperrt!"));
                    }
                } else {
                    if(e.getMessage().startsWith("/plot") | e.getMessage().startsWith("/p2")) {
                        if(!p.hasPermission("plots.auto")) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}
