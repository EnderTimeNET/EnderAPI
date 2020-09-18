package net.endertime.enderkomplex.bungee.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxyHandler {

    public static void sendPluginMessage(ProxiedPlayer player, PluginMessage action, String additionalText) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);

        try {
            output.writeUTF("pluginmessage");
            output.writeUTF(player.getName());
            output.writeUTF(action.toString());

            if(additionalText == null) {
                output.writeUTF("");
            } else {
                output.writeUTF(additionalText.toString());
            }

            player.getServer().getInfo().sendData("enderkomplex", stream.toByteArray());
        } catch (Exception e) {}
    }

    public static String getBanEnd(long timestamp, long duration) {
        if(duration == -1l) return "§4§lPERMANENT";
        long uhrzeit = System.currentTimeMillis();
        long end = timestamp + duration;

        long millis = end - uhrzeit;

        long sekunden = 0L;
        long minuten = 0L;
        long stunden = 0L;
        long tage = 0L;
        while (millis > 1000L)
        {
            millis -= 1000L;
            sekunden += 1L;
        }
        while (sekunden > 60L)
        {
            sekunden -= 60L;
            minuten += 1L;
        }
        while (minuten > 60L)
        {
            minuten -= 60L;
            stunden += 1L;
        }
        while (stunden > 24L)
        {
            stunden -= 24L;
            tage += 1L;
        }
        if(tage != 0){
            return "§a" + tage + "§7d §a" + stunden + "§7h §a" + minuten + "§7min";
        } else if(tage == 0 && stunden != 0){
            return "§a" + stunden + "§7h §a" + minuten + "§7min §a" + sekunden + "§7s";
        } else if(tage == 0 && stunden == 0 && minuten != 0){
            return "§a" + minuten + "§7min§a" + sekunden + "§7s";
        } else if(tage == 0 && stunden == 0 && minuten == 0 && sekunden != 0) {
            return "§a" + sekunden + "§7s";
        } else {
            return "§4Fehler in der Berechnung!";
        }
    }

    public static String getBanEnd(UUID uuid) {
        long duration = Database.getActiveBanDuration(uuid);
        if(duration == -1l) return "§4§lPERMANENT";
        long uhrzeit = System.currentTimeMillis();
        long end = Database.getActiveBanTimestamp(uuid) + duration;

        long millis = end - uhrzeit;

        long sekunden = 0L;
        long minuten = 0L;
        long stunden = 0L;
        long tage = 0L;
        while (millis > 1000L)
        {
            millis -= 1000L;
            sekunden += 1L;
        }
        while (sekunden > 60L)
        {
            sekunden -= 60L;
            minuten += 1L;
        }
        while (minuten > 60L)
        {
            minuten -= 60L;
            stunden += 1L;
        }
        while (stunden > 24L)
        {
            stunden -= 24L;
            tage += 1L;
        }
        if(tage != 0){
            return "§a" + tage + "§7d §a" + stunden + "§7h §a" + minuten + "§7min";
        } else if(tage == 0 && stunden != 0){
            return "§a" + stunden + "§7h §a" + minuten + "§7min §a" + sekunden + "§7s";
        } else if(tage == 0 && stunden == 0 && minuten != 0){
            return "§a" + minuten + "§7min§a" + sekunden + "§7s";
        } else if(tage == 0 && stunden == 0 && minuten == 0 && sekunden != 0) {
            return "§a" + sekunden + "§7s";
        } else {
            return "§4Fehler in der Berechnung!";
        }
    }

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String getCountry(InetSocketAddress ip) {
        URL url = null;
        try {
            url = new URL("http://api.wipmania.com/" + ip.getAddress());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader stream = null;
        try {
            stream = new BufferedReader(new InputStreamReader(
                    url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder entirePage = new StringBuilder();
        String inputLine;
        try {
            while ((inputLine = stream.readLine()) != null)
                entirePage.append(inputLine);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entirePage.toString();
    }

    public static String argsToString(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

}
