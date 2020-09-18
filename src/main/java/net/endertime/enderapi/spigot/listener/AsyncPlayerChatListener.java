package net.endertime.enderapi.spigot.listener;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.utils.ScoreBoardPrefix;
import net.endertime.enderapi.spigot.utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    public static String gobalPrefix = "§8[§5Global§8] ";
    public static String diePrefix = "§8[§4✘§8] ";

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (EnderAPI.getInstance().getVanish().contains(p)) {
            if (!e.getMessage().startsWith("/"))
                e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        for (Player all : Bukkit.getOnlinePlayers()) {
            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(p.getUniqueId(), all);
            if (p.getGameMode().equals(GameMode.SPECTATOR)) {
                if (GameAPI.getInstance().isSpectatorDiePrefixChat()) {
                    if (GameAPI.getInstance().isHideSpectatorChat()) {
                        if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                            all.sendMessage(diePrefix + scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + " §8» §7" + EnderAPI.getInstance().replaceChat(p, all, e.getMessage()));
                        }
                    } else {
                        all.sendMessage(diePrefix + scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry() + " §8» §7" + EnderAPI.getInstance()
                                .replaceChat(p, all, e.getMessage()));
                    }
                } else {
                    if (GameAPI.getInstance().isHideSpectatorChat()) {
                        if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                            all.sendMessage(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + " §8» §7" + EnderAPI.getInstance().replaceChat(p, all, e.getMessage()));
                        }
                    } else {
                        all.sendMessage(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry() + " §8» §7" + EnderAPI.getInstance()
                                .replaceChat(p, all, e.getMessage()));
                    }
                }
            } else {
                if (Team.isInTeam(p)) {
                    Team team = Team.getTeam(p);

                    if (GameAPI.getInstance().isGlobalChat()) {
                        if (e.getMessage().startsWith("@a ") || e.getMessage().startsWith("@all ")
                                || e.getMessage().startsWith("@global ") ||e.getMessage().startsWith("@a")
                                || e.getMessage().startsWith("@all") || e.getMessage().startsWith("@global")) {
                            String msg = e.getMessage();
                            msg = replaceGlobal(msg, "@a ", "");
                            msg = replaceGlobal(msg, "@global ", "");
                            msg = replaceGlobal(msg, "@all ", "");
                            msg = replaceGlobal(msg, "@a", "");
                            msg = replaceGlobal(msg, "@global", "");
                            msg = replaceGlobal(msg, "@all", "");

                            all.sendMessage(gobalPrefix + team.getPrefix() +
                                    scoreBoardPrefix.getEntry() + " §8» §7" + EnderAPI.getInstance().replaceChat(p, all, msg));
                        } else {
                            if (team.getMembers().contains(all)) {
                                all.sendMessage(team.getPrefix() + scoreBoardPrefix.getEntry() + " §8» §7" +
                                        EnderAPI.getInstance().replaceChat(p, all, e.getMessage()));
                            }
                        }
                    } else {
                        all.sendMessage(team.getPrefix() + scoreBoardPrefix.getEntry() + " §8» §7" +
                                EnderAPI.getInstance().replaceChat(p, all, e.getMessage()));
                    }
                } else {
                    all.sendMessage(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry() +
                            " §8» §7" + EnderAPI.getInstance().replaceChat(p, all, e.getMessage()));
                }
            }
        }
    }

    public String replaceGlobal (String arg1, String search, String rename) {
        return EnderAPI.getInstance().getString(arg1, search, rename);
    }
}
