package net.endertime.enderkomplex.spigot.commands;

import java.util.ArrayList;
import java.util.HashMap;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.core.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CheckCommand implements CommandExecutor, Listener {

    public static HashMap<Player, Location> loc = new HashMap<>();
    public static HashMap<Player, Location> old = new HashMap<>();
    public static ArrayList<Player> list = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (label.equalsIgnoreCase("check")) {
            if (p.hasPermission("ek.commands.check")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        EnderAPI.getInstance().sendActionBar(p, "§c" + target.getName() + "§7 wird auf Rückstossresistenz geprüft...");
                        EnderAPI.getInstance().playSound(p, Sound.BLOCK_ANVIL_LAND, 0);
                        target.setWalkSpeed(0.0F);
                        old.put(target, target.getLocation());
                        list.add(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {
                            public void run() {
                                target.setWalkSpeed(0.0F);
                                target.setVelocity(target.getLocation().getDirection().multiply(-3.0D));
                            }
                        }, 16L);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {
                            public void run() {
                                loc.put(target, target.getLocation());
                            }
                        }, 8L);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {
                            public void run() {
                                Location newloc = target.getLocation();
                                Location oldloc = loc.get(target);
                                int oldx = (int) oldloc.getX();
                                int oldy = (int) oldloc.getY();
                                int oldz = (int) oldloc.getZ();
                                int newx = (int) newloc.getX();
                                int newy = (int) newloc.getY();
                                int newz = (int) newloc.getZ();
                                if ((oldx == newx) && (oldy == newy) && (oldz == newz)) {
                                    EnderAPI.getInstance().sendActionBar(p, "§7Knockback§8: §4§l✖ §8§l┃ §6§l" + ((CraftPlayer) p).getHandle().ping + "§7§l ms");
                                    EnderAPI.getInstance().playSound(p, Sound.BLOCK_ANVIL_LAND, 0);
                                } else {
                                    EnderAPI.getInstance().sendActionBar(p, "§7§lKnockback§8§l: §2§l✔ §8§l┃ §6§l" + ((CraftPlayer) p).getHandle().ping + "§7§l ms");
                                    EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 2);
                                }
                                target.teleport(old.get(target));
                                target.setWalkSpeed(0.2F);
                                list.remove(p);
                            }
                        }, 24L);
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Dieser Spieler ist §cnicht §7online§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§ccheck §8<§cname§8>");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                }
            }
        }
        return false;
    }

}
