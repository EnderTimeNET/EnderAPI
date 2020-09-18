package net.endertime.enderkomplex.bungee.commands;

import java.util.HashMap;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.State;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.utils.InfoCollector;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinmeCommand extends Command implements Listener {

    public JoinmeCommand(String name) {
        super(name);
    }

    private static HashMap<ProxiedPlayer, Integer> tokens = new HashMap<>();
    private static HashMap<String, String> joinmes = new HashMap<>();
    private static HashMap<ProxiedPlayer, String> ids = new HashMap<>();
    private static long lastJoinme = 0l;
    private static long cooldown = 60000l;

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(args.length == 0) {
            String servername = pp.getServer().getInfo().getName();
            if(servername.startsWith("Event") | servername.contains("Lobby")) {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst hier §ckeine §7Joinme senden§8!");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                return;
            }
            if(EnderAPI.getInstance().getState(servername).equals(State.LOBBY) | EnderAPI.getInstance().getState(servername).equals(State.ONLINE)) {
                if(System.currentTimeMillis() - lastJoinme > cooldown) {
                    if(!pp.hasPermission("teamserver.join")) {
                        if(pp.hasPermission("youtuber.joinme")) {
                            if(!tokens.containsKey(pp)) {
                                tokens.put(pp, 9);
                            } else {
                                if(tokens.get(pp) > 0) {
                                    tokens.put(pp, tokens.get(pp) -1);
                                } else {
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §czu wenig §7Joinme-Tokens§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                    return;
                                }
                            }
                        } else if(pp.hasPermission("enderplus.joinme")) {
                            if(!tokens.containsKey(pp)) {
                                tokens.put(pp, 4);
                            } else {
                                if(tokens.get(pp) > 0) {
                                    tokens.put(pp, tokens.get(pp) -1);
                                } else {
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §czu wenig §7Joinme-Tokens§8!");
                                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                    return;
                                }
                            }
                        } else {
                            if(Database.getJoinmeTokens(pp.getUniqueId()) > 0) {
                                Database.removeJoinmeTokens(pp.getUniqueId(), 1);
                            } else {
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast §czu wenig §7Joinme-Tokens§8!");
                                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                                return;
                            }
                        }
                    }
                    lastJoinme = System.currentTimeMillis();
                    String id = "#" + ProxyHandler.randomAlphaNumeric(6);
                    while(joinmes.containsKey(id)) {
                        id = ProxyHandler.randomAlphaNumeric(6);
                    }
                    InfoCollector.joinmes++;
                    joinmes.put(id, servername);
                    ids.put(pp, id);
                    ComponentBuilder cb = new ComponentBuilder("§6§kAAAAAAAAAAAAAAAAAAAAAAAAAA\n\n");
                    cb.append(EnderAPI.getInstance().getCompletedPrefix(pp.getUniqueId()) + pp.getName() + " §7spielt §a" + servername.split("-")[0] + "\n");
                    cb.append("§7Klicke ").append("§6§lHIER")
                            .event(new ClickEvent(Action.RUN_COMMAND, "/joinme " + id))
                            .append("§7 um zu joinen\n");
                    cb.append("\n§6§kAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    BaseComponent[] message = cb.create();
                    pp.sendMessage(message);
                    ProxyServer.getInstance().getPlayers().forEach(online -> {
                        if(!online.getServer().getInfo().getName().equals(servername)) {
                            online.sendMessage(message);
                        }
                    });
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Bitte warte den Cooldown ab§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du kannst jetzt §ckeine §7Joinme senden§8!");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("tokens")) {
                if(pp.hasPermission("teamserver.join")) {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du hast unendlich Joinme§8-§7Tokens§8!");
                } else if(pp.hasPermission("youtuber.joinme")) {
                    if(!tokens.containsKey(pp)) tokens.put(pp, 10);
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dein Joinme§8-§7Token Kontostand§8: §6" + tokens.get(pp));
                } else if(pp.hasPermission("enderplus.joinme")) {
                    if(!tokens.containsKey(pp)) tokens.put(pp, 5);
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dein Joinme§8-§7Token Kontostand§8: §6" + tokens.get(pp));
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dein Joinme§8-§7Token Kontostand§8: §6" +
                            Database.getJoinmeTokens(pp.getUniqueId()));
                }
            } else {
                if(args[0].startsWith("#")) {
                    if(joinmes.containsKey(args[0])) {
                        String servername = joinmes.get(args[0]);
                        if(EnderAPI.getInstance().getState(servername).equals(State.LOBBY) | EnderAPI.getInstance().getState(servername).equals(State.ONLINE)) {
                            pp.connect(ProxyServer.getInstance().getServerInfo(servername));
                        } else {
                            joinmes.remove(args[0]);
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Diese Joinme ist §cabgelaufen§8!");
                            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                        }
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Diese Joinme ist §cabgelaufen§8!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cjoinme §8[<§ctokens§8>]");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            }
        } else {
            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cjoinme §8[<§ctokens§8>]");
            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
        }

    }

    @EventHandler
    public void onConnect(ServerConnectedEvent e) {
        ProxiedPlayer pp = e.getPlayer();

        if(ids.containsKey(pp)) {
            String id = ids.get(pp);
            if(joinmes.containsKey(id)) {
                String servername = joinmes.get(ids.get(pp));
                if(!e.getServer().getInfo().getName().equals(servername)) {
                    joinmes.remove(id);
                }
            }
            ids.remove(pp);
        }
    }

    @EventHandler
    public void onConnect(PlayerDisconnectEvent e) {
        ProxiedPlayer pp = e.getPlayer();

        if(ids.containsKey(pp)) {
            String id = ids.get(pp);
            if(joinmes.containsKey(id)) {
                joinmes.remove(id);
            }
            ids.remove(pp);
        }
    }

}
