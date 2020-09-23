package net.endertime.enderapi.spigot.listener;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.Nick;
import net.endertime.enderapi.spigot.utils.State;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerLoginListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (Wrapper.getInstance().getServiceId().getName().contains("Lobby")) {
            EnderAPI.getInstance().getEnderDatabase().updateOnJoin(player);
        } else if (EnderAPI.getInstance().getVanishUUID().contains(player.getUniqueId())
                || EnderAPI.getInstance().getVanish().contains(player)) {
            EnderAPI.getInstance().getVanish().add(player);
            event.allow();
        } else {
            if(GameAPI.getInstance().getState().equals(State.LOBBY)) {
                if(player.hasPermission("ender.fulljoin")) {
                    if(event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
                        event.allow();
                        List<Player> list = new ArrayList<>();
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!all.hasPermission("ender.fulljoin"))
                                list.add(all);
                        }

                        if (!list.isEmpty()) {
                            Random random = new Random();
                            list.get(random.nextInt(list.size())).kickPlayer("§7Du hast für einen §6Premium Spieler §7Platz gemacht");
                        }
                    }
                }
            } else {
                if(event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
                    event.allow();
                }
            }

            if (NickAPI.getInstance().isNickAvailable()) {
                if (EnderAPI.getInstance().getTeamDatabase().isState(player.getUniqueId())) {
                    Nick nick = new Nick(player);
                    if (NickAPI.getInstance().getNickedPlayer().containsKey(player)) {
                        NickAPI.getInstance().getNickedPlayer().remove(player);
                    }
                    NickAPI.getInstance().getNickedPlayer().put(player, nick);
                    NickAPI.getInstance().getNickedParty().put(player.getUniqueId(), new ArrayList<>());
                }
            }

        }

    }
}
