package net.endertime.enderapi.spigot.listener;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.State;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public class PlayerQuitListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (!Wrapper.getInstance().getServiceId().getName().contains("Lobby")) {
            if (GameAPI.getInstance().getState().equals(State.INGAME)) {
                if (EnderAPI.getInstance().isVanished(player)) {
                    event.setQuitMessage(null);
                } else if (NickAPI.getInstance().isNicked(player)) {
                    event.setQuitMessage(null);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                            if (NickAPI.getInstance().couldSee(player, all)) {
                                all.sendMessage("§8[§4-§8] §7" + NickAPI.getInstance().getNick(player).getNickedName());
                                EnderAPI.getInstance().removeOnTablist(all, NickAPI.getInstance().getNick(player).getNickedProfile());
                            } else {
                                all.sendMessage("§8[§4-§8] §7" + NickAPI.getInstance().getNick(player).getName());
                            }
                        }
                    }
                } else {
                    event.setQuitMessage(null);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                            all.sendMessage("§8[§4-§8] §7" + player.getName());
                        }
                    }
                }
            } else {
                if (EnderAPI.getInstance().isVanished(player)) {
                    event.setQuitMessage(null);
                } else if (NickAPI.getInstance().isNicked(player)) {
                    event.setQuitMessage(null);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (NickAPI.getInstance().couldSee(player, all)) {
                            all.sendMessage("§8[§4-§8] §7" + NickAPI.getInstance().getNick(player).getNickedName());
                            EnderAPI.getInstance().removeOnTablist(all, NickAPI.getInstance().getNick(player).getNickedProfile());
                        } else {
                            all.sendMessage("§8[§4-§8] §7" + NickAPI.getInstance().getNick(player).getName());
                        }
                    }
                } else {
                    event.setQuitMessage("§8[§4-§8] §7" + player.getName());
                }
            }
        } else {
            event.setQuitMessage(null);
        }

        if (EnderAPI.getInstance().getVanish().contains(player)) {
            EnderAPI.getInstance().getVanish().remove(player);
        }

        if (EnderAPI.getInstance().getVanishUUID().contains(player.getUniqueId())) {
            EnderAPI.getInstance().getVanishUUID().remove(player.getUniqueId());
        }

        if (EnderAPI.getInstance().getBadlion().contains(player.getUniqueId())) {
            EnderAPI.getInstance().getBadlion().remove(player.getUniqueId());
        }

        if (EnderAPI.getInstance().getNoActionbar().contains(player)) {
            EnderAPI.getInstance().getNoActionbar().remove(player);
        }

        if (NickAPI.getInstance().getNickedPlayer().containsKey(player)) {
            NickAPI.getInstance().getNickedPlayer().remove(player);
        }

        for (Collection<Player> list : NickAPI.getInstance().getNickedParty().values()) {
            if (list.contains(player))
                list.remove(player);
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (!player.getUniqueId().equals(all.getUniqueId())) {
                EnderAPI.getInstance().hidePlayer(all, player);
            }
        }
    }
}
