package net.endertime.enderkomplex.spigot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.utils.State;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.ServerData;
import net.endertime.enderkomplex.spigot.core.ServerHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.minecraft.server.v1_12_R1.EntityArmorStand;

public class AFKTimer implements Listener {

    private static HashMap<Player, Long> afkTime = new HashMap<>();
    private static HashMap<Player, Long> lastTime = new HashMap<>();
    private static HashMap<Player, Location> lastLocation = new HashMap<>();
    private static HashMap<Player, EntityArmorStand> holograms = new HashMap<>();
    private static ArrayList<Player> afkplayers = new ArrayList<>();

    public static void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ServerData.Instance, new Runnable() {

            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(online -> {
                    if(!online.getGameMode().equals(GameMode.SPECTATOR)) {
                        if(!EnderAPI.getInstance().isVanished(online.getUniqueId())) {
                            if(lastLocation.containsKey(online)) {
                                if(online.getLocation().equals(lastLocation.get(online))) {
                                    if(!afkTime.containsKey(online)) {
                                        afkTime.put(online, System.currentTimeMillis());
                                    } else {
                                        long afktime = System.currentTimeMillis() - afkTime.get(online);
                                        if(afktime >= 60000) {
                                            EntityArmorStand eas = null;
                                            if(!holograms.containsKey(online)) {
                                                eas = ServerHandler.createPacketHologram("§8× §e§oAFK seit einer Minute §8×",
                                                        online.getEyeLocation().add(0, 0.9, 0));
                                                holograms.put(online, eas);
                                            } else {
                                                eas = holograms.get(online);
                                            }
                                            if(afktime >= 3600000) {
                                                if(afktime >= 7200000) {
                                                    //2h oder mehr
                                                    long hours = TimeUnit.MILLISECONDS.toHours(afktime);
                                                    if(lastTime.get(online) != hours) {
                                                        eas.setCustomName("§8× §e§oAFK seit über " + TimeUnit.MILLISECONDS.toHours(afktime) + " Stunden §8×");
                                                        ServerHandler.destroyHologram(eas);
                                                        ServerHandler.showHologram(eas);
                                                        lastTime.put(online, hours);
                                                    }
                                                } else {
                                                    //1h
                                                    if(lastTime.get(online) != 1l) {
                                                        eas.setCustomName("§8× §e§oAFK seit über einer Stunde §8×");
                                                        ServerHandler.destroyHologram(eas);
                                                        ServerHandler.showHologram(eas);
                                                        lastTime.put(online, 1l);
                                                    }
                                                }
                                            } else {
                                                if(afktime >= 120000) {
                                                    //2min oder mehr
                                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(afktime);
                                                    if(lastTime.get(online) != minutes) {
                                                        eas.setCustomName("§8× §e§oAFK seit " + minutes + " Minuten §8×");
                                                        ServerHandler.destroyHologram(eas);
                                                        ServerHandler.showHologram(eas);
                                                        lastTime.put(online, minutes);
                                                    }
                                                    if (minutes >= 5) {
                                                        if (!Wrapper.getInstance().getServiceId().getName().contains("Lobby")
                                                                && !Wrapper.getInstance().getServiceId().getName().startsWith("Terra")
                                                                && !Wrapper.getInstance().getServiceId().getName().startsWith("Event")
                                                                && !Wrapper.getInstance().getServiceId().getName().startsWith("Vorbauen")
                                                                && GameAPI.getInstance().getState().equals(State.ONLINE)) {

                                                            online.kickPlayer(null);
                                                        }
                                                    }
                                                } else {
                                                    //1min
                                                    if(!lastTime.containsKey(online)) {
                                                        eas.setCustomName("§8× §e§oAFK seit einer Minute §8×");
                                                        lastTime.put(online, 1l);
                                                        if(!afkplayers.contains(online)) {
                                                            afkplayers.add(online);
                                                            Database.saveTime(online.getUniqueId());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                lastLocation.put(online, online.getLocation());
                            }
                        }
                    }
                });
            }
        }, 20, 20*5);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(ServerData.Instance, new Runnable() {

            @Override
            public void run() {
                ArrayList<Player> temp = new ArrayList<>();
                temp.addAll(lastLocation.keySet());
                temp.forEach(ll -> {
                    if(ll.isOnline()) {
                        if(!ll.getLocation().equals(lastLocation.get(ll))) {
                            lastLocation.remove(ll);
                            afkTime.remove(ll);
                            if(holograms.containsKey(ll)) {
                                ServerHandler.destroyHologram(holograms.get(ll));
                                holograms.remove(ll);
                            }
                            if(afkplayers.contains(ll)) {
                                Database.setLastSave(ll.getUniqueId());
                            }
                            afkplayers.remove(ll);
                            lastTime.remove(ll);
                        }
                    }
                });
                temp.clear();
                temp = null;
            }
        }, 10, 10);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        holograms.values().forEach(holo -> ServerHandler.showHologram(p, holo));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
            if(!EnderAPI.getInstance().isVanished(p)) {
                if(lastLocation.containsKey(p)) {
                    if(p.getLocation().equals(lastLocation.get(p))) {
                        lastLocation.remove(p);
                        afkTime.remove(p);
                        if(holograms.containsKey(p)) {
                            ServerHandler.destroyHologram(holograms.get(p));
                            holograms.remove(p);
                        }
                        if(afkplayers.contains(p)) {
                            Database.setLastSave(p.getUniqueId());
                        }
                        afkplayers.remove(p);
                        lastTime.remove(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
            if(!EnderAPI.getInstance().isVanished(p)) {
                if(lastLocation.containsKey(p)) {
                    if(p.getLocation().equals(lastLocation.get(p))) {
                        lastLocation.remove(p);
                        afkTime.remove(p);
                        if(holograms.containsKey(p)) {
                            ServerHandler.destroyHologram(holograms.get(p));
                            holograms.remove(p);
                        }
                        if(afkplayers.contains(p)) {
                            Database.setLastSave(p.getUniqueId());
                        }
                        afkplayers.remove(p);
                        lastTime.remove(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();

        if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
            if(!EnderAPI.getInstance().isVanished(p)) {
                if(lastLocation.containsKey(p)) {
                    if(p.getLocation().equals(lastLocation.get(p))) {
                        lastLocation.remove(p);
                        afkTime.remove(p);
                        if(holograms.containsKey(p)) {
                            ServerHandler.destroyHologram(holograms.get(p));
                            holograms.remove(p);
                        }
                        if(afkplayers.contains(p)) {
                            Database.setLastSave(p.getUniqueId());
                        }
                        afkplayers.remove(p);
                        lastTime.remove(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = (Player) e.getPlayer();

        afkplayers.forEach(afk -> {
            if(e.getMessage().contains(afk.getName())) {
                EnderAPI.getInstance().sendActionBar(p, "§c" + afk.getName() + " §7ist aktuell AFK und atwortet evtl§8. §7nicht§8!");
            }
        });

        if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
            if(!EnderAPI.getInstance().isVanished(p)) {
                if(lastLocation.containsKey(p)) {
                    if(p.getLocation().equals(lastLocation.get(p))) {
                        lastLocation.remove(p);
                        afkTime.remove(p);
                        if(holograms.containsKey(p)) {
                            ServerHandler.destroyHologram(holograms.get(p));
                            holograms.remove(p);
                        }
                        if(afkplayers.contains(p)) {
                            Database.setLastSave(p.getUniqueId());
                        }
                        afkplayers.remove(p);
                        lastTime.remove(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = (Player) e.getPlayer();

        if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
            if(!EnderAPI.getInstance().isVanished(p)) {
                if(lastLocation.containsKey(p)) {
                    if(p.getLocation().equals(lastLocation.get(p))) {
                        lastLocation.remove(p);
                        afkTime.remove(p);
                        if(holograms.containsKey(p)) {
                            ServerHandler.destroyHologram(holograms.get(p));
                            holograms.remove(p);
                        }
                        if(afkplayers.contains(p)) {
                            Database.setLastSave(p.getUniqueId());
                        }
                        afkplayers.remove(p);
                        lastTime.remove(p);
                    }
                }
            }
        }
    }

}
