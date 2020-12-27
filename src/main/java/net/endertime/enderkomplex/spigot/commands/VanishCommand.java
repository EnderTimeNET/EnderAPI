package net.endertime.enderkomplex.spigot.commands;

import java.util.HashMap;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.GameAPI;
import net.endertime.enderapi.spigot.utils.State;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    public static HashMap<Player, GameMode> vanished = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (label.equalsIgnoreCase("v")) {
            if (p.hasPermission("ek.commands.vanish")) {
                if (args.length == 0) {
                    if(GameAPI.getInstance().getState().equals(State.ONLINE)) {
                        if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
                            if (!vanished.containsKey(p)) {
                                vanished.put(p, p.getGameMode());
                                p.setGameMode(GameMode.ADVENTURE);
                                p.setAllowFlight(true);
                                EnderAPI.getInstance().getVanish().add(p);
                                EnderAPI.getInstance().sendActionBar(p, "§7Du bist nun unsichtbar§8!");
                                EnderAPI.getInstance().playSound(p, Sound.LEVEL_UP, 2);
                                Bukkit.getOnlinePlayers().forEach(all -> {
                                    if (!all.hasPermission("ek.commands.vanish")) {
                                        EnderAPI.getInstance().hidePlayer(all, p);
                                    } else {
                                        (((CraftPlayer) all).getHandle()).playerConnection
                                                .sendPacket(new PacketPlayOutPlayerInfo(
                                                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                                                        (((CraftPlayer) p).getHandle())));
                                    }
                                });
                                if (!Bukkit.getServerName().contains("Lobby"))
                                    EnderAPI.getInstance().broadcastMessage("§8[§4-§8] §7" + p.getName());
                                EnderAPI.getInstance().updateScoreboardGlobally();
                            } else {
                                p.setGameMode(vanished.get(p));
                                vanished.remove(p);
                                EnderAPI.getInstance().getVanish().remove(p);
                                if (!p.getGameMode().equals(GameMode.CREATIVE))
                                    p.setAllowFlight(false);
                                EnderAPI.getInstance().sendActionBar(p, "§7Du bist jetzt wieder sichtbar§8!");
                                EnderAPI.getInstance().playSound(p, Sound.LEVEL_UP, 2);
                                Bukkit.getOnlinePlayers().forEach(all -> {
                                    if (!all.hasPermission("ek.commands.vanish")) {
                                        EnderAPI.getInstance().showPlayer(all, p);
                                    } else {
                                        (((CraftPlayer) all).getHandle()).playerConnection
                                                .sendPacket(new PacketPlayOutPlayerInfo(
                                                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                                                        (((CraftPlayer) p).getHandle())));
                                    }

                                });
                                if (!Bukkit.getServerName().contains("Lobby"))
                                    EnderAPI.getInstance().broadcastMessage("§8[§2+§8] §7" + p.getName());
                                EnderAPI.getInstance().updateScoreboardGlobally();

                                Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
                                            @Override
                                            public void run() {
                                                EnderAPI.getInstance().updateScoreboardGlobally();
                                            }
                                        }, 5);
                            }
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Du kannst jetzt §cnicht §7in Vanish gehen§8!");
                            EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                        }
                    } else {
                        EnderAPI.getInstance().sendActionBar(p, "§7Du kannst jetzt §cnicht §7in Vanish gehen§8!");
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                    }
                } else {
                    EnderAPI.getInstance().sendActionBar(p, "§7Benutze: §8/§cv");
                    EnderAPI.getInstance().playSound(p, Sound.ITEM_BREAK);
                }
            }
        }
        return false;
    }

}
