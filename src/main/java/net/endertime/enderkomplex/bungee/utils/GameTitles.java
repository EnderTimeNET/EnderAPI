package net.endertime.enderkomplex.bungee.utils;

import net.endertime.enderkomplex.mysql.Database;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GameTitles implements Listener {

    @EventHandler
    public void onJoin(ServerConnectedEvent e) {
        ProxiedPlayer pp = e.getPlayer();
        String servername = e.getServer().getInfo().getName();

        String[] titles = Database.getTitles(servername);
        if(!titles[0].equals("null")) {
            String subtitle = titles[0].replace('&', '§');
            if(!titles[1].equals("null")) {
                String title = titles[1].replace('&', '§');
                ProxyServer.getInstance().createTitle().title(TextComponent.fromLegacyText(title)).
                        subTitle(TextComponent.fromLegacyText(subtitle)).fadeIn(5).stay(40).fadeOut(5).send(pp);
            } else {
                ProxyServer.getInstance().createTitle().title(TextComponent.fromLegacyText("")).
                        subTitle(TextComponent.fromLegacyText(subtitle)).fadeIn(5).stay(40).fadeOut(5).send(pp);
            }
        } else {
            if(!titles[1].equals("null")) {
                String title = titles[1].replace('&', '§');
                ProxyServer.getInstance().createTitle().title(TextComponent.fromLegacyText(title)).
                        subTitle(TextComponent.fromLegacyText("")).fadeIn(5).stay(40).fadeOut(5).send(pp);
            }
        }
    }

}
