package net.endertime.enderapi.spigot.commands;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.gameapi.UnnickEvent;
import net.endertime.enderapi.spigot.utils.Nick;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Unnick_Command implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            final Player p = (Player) sender;
            if (p.hasPermission("autonick.use")) {
                if (label.equalsIgnoreCase("unnick")) {
                    if (!Wrapper.getInstance().getServiceId().getName().startsWith("Lobby")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("SilentLobby")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Vorbau")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Team")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Terra")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Event")) {
                        if (args.length == 0) {
                            if (NickAPI.getInstance().getNickedPlayer().containsKey(p)) {
                                final Nick nick = NickAPI.getInstance().getNickedPlayer().get(p);

                                if (EnderAPI.getInstance().getTeamDatabase().isRandom(p.getUniqueId())) {
                                    EnderAPI.getInstance().getNickDatabase().
                                            updateState(EnderAPI.getInstance().getNickDatabase().getUUID(nick.getNickedName()), false);
                                    EnderAPI.getInstance().getTeamDatabase().
                                            updateNickedName(EnderAPI.getInstance().getNickDatabase().getUUID(nick.getNickedName()), "");
                                }

                                NickAPI.getInstance().getNickedPlayer().remove(p);

                                EnderAPI.getInstance().removeOnTablist(p, nick.getNickedProfile());

                                for (final Player all :Bukkit.getOnlinePlayers()) {
                                    if (!all.getUniqueId().equals(p.getUniqueId())) {
                                        EnderAPI.getInstance().hidePlayer(all, p);

                                        Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
                                            public void run() {
                                                EnderAPI.getInstance().showPlayer(all, p);
                                                EnderAPI.getInstance().removeOnTablist(all, nick.getNickedProfile());
                                            }
                                        }, 2);
                                    }
                                    EnderAPI.getInstance().getScoreboard(all).b(p);
                                }

                                //EnderAPI.getInstance().updateScoreboardGlobally();

                                Bukkit.getPluginManager().callEvent(new UnnickEvent());

                                EnderAPI.getInstance().sendActionBar(p,
                                        "§7Du bist nun nicht mehr als §e" + nick.getNickedName() + " §7genickt");

                                p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5F, 2.0F);
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1, true, false));
                            } else {
                                EnderAPI.getInstance().sendActionBar(p, "§7Du bist §cnicht §7genickt");
                            }
                        } else {
                            EnderAPI.getInstance().sendActionBar(p, "§7Benutze §8/§cunnick");
                        }
                    } else {
                        p.sendMessage(EnderAPI.getInstance().getNoPerm());
                    }
                }
            } else {
                p.sendMessage(EnderAPI.getInstance().getNoPerm());
            }
        }
        return false;
    }

}
