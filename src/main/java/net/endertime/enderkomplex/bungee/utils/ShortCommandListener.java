package net.endertime.enderkomplex.bungee.utils;

import net.endertime.enderapi.bungee.api.EnderAPI;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ShortCommandListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e) {
        if(e.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            if(e.getMessage().equalsIgnoreCase("/pl") | e.getMessage().equalsIgnoreCase("/plugins")) {
                if(!p.hasPermission("admin.seeplugins")) {
                    e.setCancelled(true);
                    TextComponent tc = new TextComponent("§8➟ §cZu den Anforderungen");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://endertime.net/apply"));
                    p.sendMessage(TextComponent.fromLegacyText("§0"));
                    p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                    p.sendMessage(TextComponent.fromLegacyText("§1"));
                    p.sendMessage(TextComponent.fromLegacyText("§7Unsere Plugins und Systeme werden von unserem Developerteam " +
                            "selbst entwickelt. Wenn du Interesse hast in dem Team mitzuwirken, schaue dir die Anforderungen in" +
                            " unserem Forum an und bewerbe dich ggf. anschließend"));
                    p.sendMessage(TextComponent.fromLegacyText("§2"));
                    p.sendMessage(tc);
                    p.sendMessage(TextComponent.fromLegacyText("§3"));
                    p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                }
            }
            if(e.getMessage().equalsIgnoreCase("/ts") | e.getMessage().equalsIgnoreCase("/teamspeak")) {
                e.setCancelled(true);
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Unsere TeamSpeak Adresse"));
                p.sendMessage(TextComponent.fromLegacyText("§8➟ §9ts.endertime.net"));
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/website") | e.getMessage().equalsIgnoreCase("/webseite")) {
                e.setCancelled(true);
                TextComponent tc = new TextComponent("§8➟ §chttps://endertime.net");
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://endertime.net"));
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Unsere Website-Adresse"));
                p.sendMessage(tc);
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/forum")) {
                e.setCancelled(true);
                TextComponent tc = new TextComponent("§8➟ §chttps://forum.endertime.net");
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://forum.endertime.net"));
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Unsere Forum-Adresse"));
                p.sendMessage(tc);
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/premium") | e.getMessage().equalsIgnoreCase("/prem")) {
                e.setCancelled(true);
                TextComponent tc = new TextComponent("§8➟ §chttps://forum.endertime.net/threads/r%C3%A4nge-und-ihre-rechte.53/#post-64");
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://forum.endertime.net/threads/r%C3%A4nge-und-ihre-rechte.53/#post-64"));
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Informationen über Premiumränge"));
                p.sendMessage(tc);
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/youtube") | e.getMessage().equalsIgnoreCase("/youtuber")
                    | e.getMessage().equalsIgnoreCase("/yt")) {
                e.setCancelled(true);
                TextComponent tc = new TextComponent("§8➟ §cZu den Anforderungen");
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://endertime.net/apply"));
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Infos über den YouTuber/Ender+ Rang"));
                p.sendMessage(tc);
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/coins") | e.getMessage().equalsIgnoreCase("/money")) {
                e.setCancelled(true);
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Deine EnderCoins"));
                p.sendMessage(TextComponent.fromLegacyText("§8➟ §6" + EnderAPI.getInstance().getCoins(p.getUniqueId())));
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/points") | e.getMessage().equalsIgnoreCase("/punkte")) {
                e.setCancelled(true);
                p.sendMessage(TextComponent.fromLegacyText("§0"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                p.sendMessage(TextComponent.fromLegacyText("§1"));
                p.sendMessage(TextComponent.fromLegacyText("§7Deine Skill-Punkte"));
                p.sendMessage(TextComponent.fromLegacyText("§8➟ §4" + EnderAPI.getInstance().getPoints(p.getUniqueId())));
                p.sendMessage(TextComponent.fromLegacyText("§2"));
                p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
            }
            if(e.getMessage().equalsIgnoreCase("/shop") | e.getMessage().equalsIgnoreCase("/store")) {
                if(!p.getServer().getInfo().getName().contains("Skyblock")) {
                    e.setCancelled(true);
                    TextComponent tc = new TextComponent("§8➟ §chttps://shop.endertime.net");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://shop.endertime.net"));
                    p.sendMessage(TextComponent.fromLegacyText("§0"));
                    p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                    p.sendMessage(TextComponent.fromLegacyText("§1"));
                    p.sendMessage(TextComponent.fromLegacyText("§7Unser Online-Shop"));
                    p.sendMessage(tc);
                    p.sendMessage(TextComponent.fromLegacyText("§2"));
                    p.sendMessage(TextComponent.fromLegacyText("§8§m                           "));
                }
            }
        }
    }

}
