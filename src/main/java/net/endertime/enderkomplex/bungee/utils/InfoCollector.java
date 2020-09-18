package net.endertime.enderkomplex.bungee.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class InfoCollector implements Listener {

    public enum Version {
        v1_8("§31.8§8: §c", new Integer[] {47, 47}),
        v1_9("§31.9§8: §c", new Integer[] {48, 110}),
        v1_10("§31.10§8: §c", new Integer[] {201, 210}),
        v1_11("§31.11§8: §c", new Integer[] {301, 316}),
        v1_12("§31.12§8: §c", new Integer[] {317, 340});

        private String text;
        private Integer[] nums;

        private Version(String text, Integer[] nums) {
            this.text = text;
            this.nums = nums;
        }

        public String getText() {
            return this.text;
        }

        public Integer[] getNums() {
            return this.nums;
        }
    }

    public static Date lastrestart;
    private static SimpleDateFormat sdf = new SimpleDateFormat("EE dd.MM.yyyy HH:mm:ss");
    private static HashMap<Version, ArrayList<UUID>> versionCount = new HashMap<>();
    private static ArrayList<String> countries = new ArrayList<>();
    public static ArrayList<String> labymod = new ArrayList<>();
    public static ArrayList<String> badlion = new ArrayList<>();
    public static ArrayList<String> forge = new ArrayList<>();
    public static int bans = 0, mutes = 0, reports = 0, chatfilter = 0, tpc = 0, joinmes = 0;
    public static ArrayList<String> ppm = new ArrayList<>();
    public static ArrayList<String> cpm = new ArrayList<>();
    public static ArrayList<String> upc = new ArrayList<>();
    private static HashMap<ProxiedPlayer, String[]> joinstamp = new HashMap<>();
    private static HashMap<String, Long> servers = new HashMap<>();

    @EventHandler
    public void onConnected(ServerConnectedEvent e) {
        ProxiedPlayer pp = e.getPlayer();

        if(joinstamp.containsKey(pp)) {
            String servername = joinstamp.get(pp)[0];
            long differece = System.currentTimeMillis() - Long.valueOf(joinstamp.get(pp)[1]);
            if(!servers.containsKey(servername)) {
                servers.put(servername, 0l);
            }
            servers.put(servername, servers.get(servername) + differece);
        }

        String servername = e.getServer().getInfo().getName().split("-")[0];
        joinstamp.put(e.getPlayer(), new String[]{servername, "" + System.currentTimeMillis()});

    }

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        if(e.getReason().equals(Reason.JOIN_PROXY)) {
            if(e.getPlayer().isForgeUser()) {
                if(!forge.contains(e.getPlayer().getUniqueId().toString())) {
                    forge.add(e.getPlayer().getUniqueId().toString());
                }
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer pp = e.getPlayer();

        if(joinstamp.containsKey(pp)) {
            String servername = joinstamp.get(pp)[0];
            long differece = System.currentTimeMillis() - Long.valueOf(joinstamp.get(pp)[1]);
            if(!servers.containsKey(servername)) {
                servers.put(servername, 0l);
            }
            servers.put(servername, servers.get(servername) + differece);
        }
    }

    public static ArrayList<BaseComponent[]> getTopServers() {
        ArrayList<BaseComponent[]> components = new ArrayList<>();
        HashMap<String, Long> rawData = getMostPopularServers();
        for(Entry<String, Long> entry : rawData.entrySet()) {
            ComponentBuilder cb = new ComponentBuilder("§a" + entry.getKey());
            cb.event(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText(getDurationBreakdown(entry.getValue()))));
            components.add(cb.create());
        }
        return components;
    }

    private static HashMap<String, Long> getMostPopularServers() {
        servers.remove("Lobby");
        servers.remove("SilentLobby");
        int amount = 3;
        if(servers.keySet().size() < 3) {
            amount = servers.keySet().size();
        }
        HashMap<String, Long> top3 = servers.entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder())).limit(amount).collect(Collectors
                        .toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return top3;
    }

    public static BaseComponent[] getLastRestart() {
        return new ComponentBuilder("§e" + sdf.format(lastrestart)).create();
    }

    public static BaseComponent[] getVersionCount(Version v) {
        if(!versionCount.containsKey(v)) {
            versionCount.put(v, new ArrayList<UUID>());
        }
        return new ComponentBuilder(v.getText() + versionCount.get(v).size()).create();
    }

    public static BaseComponent[] getCountries() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(String country : countries) {
            sb.append("§c" + country + "§8, ");
            i++;
            if(i == 5) {
                i = 0;
                sb.append("\n");
            }
        }
        return new ComponentBuilder("§6" + countries.size()).event(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText(sb.toString()))).create();
    }

    private static String getCountry(InetSocketAddress ip) {
        URL url = null;
        try {
            url = new URL("http://api.wipmania.com/" + ip.getAddress());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader stream = null;
        try {
            stream = new BufferedReader(new InputStreamReader(url.openStream()));
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

    @EventHandler (priority = EventPriority.LOW)
    public void onLogin(LoginEvent e) {
        if(!e.isCancelled()) {
            InetSocketAddress ip = e.getConnection().getAddress();
            String country = getCountry(ip);
            UUID uuid = e.getConnection().getUniqueId();
            int version = e.getConnection().getVersion();
            int onlinecount = ProxyServer.getInstance().getOnlineCount() +1;
            if(!upc.contains(uuid.toString())) {
                upc.add(uuid.toString());
            }
            if(!cpm.contains(uuid.toString())) {
                cpm.add(uuid.toString());
            }
            if(!countries.contains(country)) {
                if(!country.equals("XX")) {
                    countries.add(country);
                }
            }
            for(Version v : Version.values()) {
                Integer[] nums = v.getNums();
                if(version >= nums[0] && version <= nums[1]) {
                    if(!versionCount.containsKey(v)) {
                        versionCount.put(v, new ArrayList<UUID>());
                    }
                    if(!versionCount.get(v).contains(uuid)) {
                        versionCount.get(v).add(uuid);
                    }
                    break;
                }
            }
            if(tpc < onlinecount) {
                tpc = onlinecount;
            }
        }
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        String ip = e.getSender().getAddress().getAddress().toString();

        if(e.getTag().equals("LMC")) {
            if(!labymod.contains(ip)) {
                labymod.add(ip);
            }
        } else if(e.getTag().equals("BungeeCord")) {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                if (dataInputStream.readUTF().equals("heartbeat")) {
                    if(!badlion.contains(ip)) {
                        badlion.add(ip);
                    }
                }
            } catch (IOException error) {
            }
        }
    }

    public static void startClearRunnable() {
        ProxyServer.getInstance().getScheduler().schedule(ProxyData.Instance, new Runnable() {

            @Override
            public void run() {
                ppm.clear();
                cpm.clear();

            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        String ip = e.getConnection().getAddress().getAddress().toString();

        if(!ppm.contains(ip)) {
            ppm.add(ip);
        }
    }

    public static String getDurationBreakdown(long millis) {
        if(millis < 0) {
            return "null";
        }

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append("§c" + hours);
        sb.append(" §7h §c");
        sb.append(minutes);
        sb.append(" §7min §c");
        sb.append(seconds);
        sb.append(" §7s");

        return(sb.toString());
    }

}
