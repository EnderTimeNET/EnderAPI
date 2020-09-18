package net.endertime.enderkomplex.spigot.objects;

import java.util.HashMap;

import net.endertime.enderapi.spigot.Spigot;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.core.ServerHandler;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InfoFeed implements Listener {

    public static HashMap<Player, Integer> leftPerSecond;
    public static HashMap<Player, Integer> rightPerSecond;
    public static HashMap<Player, Double> range;
    public static HashMap<Player, Player> feedReciever;
    public BukkitRunnable seconds;
    public Spigot plugin;

    public InfoFeed(Spigot main) {
        this.plugin = main;
        leftPerSecond = new HashMap<>();
        rightPerSecond = new HashMap<>();
        feedReciever = new HashMap<>();
        range = new HashMap<>();
        main.getServer().getPluginManager().registerEvents(this, main);
        if (seconds == null) {
            seconds = new BukkitRunnable() {
                @Override
                public void run() {
                    feedReciever.keySet().forEach(p -> ServerHandler.sendActionbar(p, "§3" + feedReciever.get(p).getName()
                            + " §8➟ §7L§8: §c" + leftPerSecond.get(feedReciever.get(p)) + " §8━ §7R§8: §c" + rightPerSecond.get(feedReciever.get(p))
                            + " §8┃ §7Range§8: §c" + EnderAPI.getInstance().round(range.get(feedReciever.get(p)), 2) + " §8┃ §7Ping§8: §c"
                            + ((CraftPlayer)p).getHandle().ping + "ms"));
                    leftPerSecond.keySet().forEach(lps -> leftPerSecond.put(lps, 0));
                    rightPerSecond.keySet().forEach(rps -> rightPerSecond.put(rps, 0));
                    range.keySet().forEach(rangep -> range.put(rangep, 0.0));
                }
            };
            seconds.runTaskTimer(plugin, 0, 20);
        }
    }

    public static void setTarget(Player p, Player target) {
        leftPerSecond.put(target, 0);
        rightPerSecond.put(target, 0);
        feedReciever.put(p, target);
        range.put(target, 0.0);
    }

    public static void unsetTargets(Player p) {
        feedReciever.remove(p);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) | e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (leftPerSecond.containsKey(e.getPlayer())) {
                int hits = leftPerSecond.get(e.getPlayer());
                leftPerSecond.put(e.getPlayer(), (hits + 1));
            }
        }
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) | e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (rightPerSecond.containsKey(e.getPlayer())) {
                int hits = rightPerSecond.get(e.getPlayer());
                rightPerSecond.put(e.getPlayer(), (hits + 1));
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
            if(e.getEntity() instanceof Player) {
                if(e.getDamager() instanceof Player) {
                    Player damager = (Player) e.getDamager();
                    Player target = (Player) e.getEntity();
                    Location loc1 = damager.getEyeLocation().clone().subtract(0, damager.getEyeHeight()/2, 0);
                    Location loc2 = target.getEyeLocation().clone().subtract(0, damager.getEyeHeight()/2, 0);
                    range.put((Player)e.getDamager(), (loc1.distance(loc2)) -0.3);
                }
            }
        }
    }

}
