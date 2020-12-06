package net.endertime.enderapi.spigot.gameapi;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.Nick;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SpectatorListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(p.getGameMode().equals(GameMode.SPECTATOR)) {
                if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                    String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                    boolean b = false;
                    for (Nick nick : NickAPI.getInstance().getNickedPlayer().values()) {
                        if (nick.getNickedName().equals(name)) {
                            p.teleport(nick.getPlayer());
                            b = true;
                        } else if (nick.getName().equals(name)) {
                            p.teleport(nick.getPlayer());
                            b = true;
                        }
                    }
                    if (!b) {
                        p.teleport(Bukkit.getPlayer(EnderAPI.getInstance().getUUID(name)));
                    }
                    e.setCancelled(true);
                    p.playSound(p.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, (float) 0.5, 1);
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onUnnick (UnnickEvent event) {
        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.getGameMode().equals(GameMode.SPECTATOR)) {
                        GameAPI.getInstance().setSpectator(all);
                    }
                }
            }
        }, 2);
    }
}
