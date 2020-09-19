package net.endertime.enderapi.spigot.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.api.PartyAPI;
import net.endertime.enderapi.spigot.utils.Nick;
import net.endertime.enderapi.spigot.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class ChannelMessageReceiveListener {

    @EventListener
    public void handleChannelMessage(ChannelMessageReceiveEvent event) {
        if (event.getMessage() != null) {
            if (event.getChannel().equalsIgnoreCase("enderapi")) {
                if (event.getMessage().equalsIgnoreCase("tablist")) {
                    EnderAPI.getInstance().updateScoreboardGlobally();
                } else if (event.getMessage().equalsIgnoreCase("party")) {
                    if (!Wrapper.getInstance().getServiceId().getName().startsWith("Vorbau")) {
                        String command = event.getData().getString("command");
                        UUID leader = UUID.fromString(event.getData().getString("leader"));
                        if (command.equals("create")) {
                            boolean publicParty = event.getData().getBoolean("publicParty");

                            PartyAPI.getInstance().createParty(leader, publicParty);
                        } else if (command.equals("add")) {
                            UUID uuid = UUID.fromString(event.getData().getString("uuid"));

                            PartyAPI.getInstance().getParty(leader).addPlayer(uuid);
                        } else if (command.equals("remove")) {
                            UUID uuid = UUID.fromString(event.getData().getString("uuid"));

                            PartyAPI.getInstance().getParty(leader).removePlayer(uuid);
                        } else if (command.equals("leader")) {
                            UUID uuid = UUID.fromString(event.getData().getString("uuid"));

                            PartyAPI.getInstance().getParty(leader).setLeader(uuid);
                        } else if (command.equals("delete")) {
                            PartyAPI.getInstance().deleteParty(leader);
                        }
                    }
                } else if (event.getMessage().equalsIgnoreCase("nicklist")) {
                    UUID uuid = UUID.fromString(event.getData().getString("uuid"));

                    final Player target = Bukkit.getPlayer(uuid);
                    if (target != null) {
                        if (NickAPI.getInstance().getNickedPlayer().containsKey(target)) {
                            final Nick nick = NickAPI.getInstance().getNickedPlayer().get(target);

                            NickAPI.getInstance().getNickedPlayer().remove(target);

                            if (EnderAPI.getInstance().getTeamDatabase().isRandom(target.getUniqueId())) {
                                EnderAPI.getInstance().getNickDatabase().updateState(EnderAPI.getInstance()
                                        .getNickDatabase().getUUID(nick.getNickedName()), false);
                            }
                            EnderAPI.getInstance().getTeamDatabase().updateNickedName(EnderAPI.getInstance()
                                    .getNickDatabase().getUUID(nick.getNickedName()), "");

                            EnderAPI.getInstance().removeOnTablist(target, nick.getNickedProfile());

                            for (final Player all : Bukkit.getOnlinePlayers()) {
                                if (!all.getUniqueId().equals(target.getUniqueId())) {
                                    EnderAPI.getInstance().hidePlayer(all, target);

                                    Bukkit.getScheduler().runTaskLater(EnderAPI.getInstance().getPlugin(), new Runnable() {
                                        public void run() {
                                            EnderAPI.getInstance().showPlayer(all, target);
                                            EnderAPI.getInstance().removeOnTablist(all, nick.getNickedProfile());
                                        }
                                    }, 2);
                                }
                            }

                            EnderAPI.getInstance().updateScoreboardGlobally();

                            EnderAPI.getInstance().updateScoreboardGlobally();

                            target.sendMessage("§7Ein Spieler mit deinem §5Nicknamen §7ist §cgejoint§7, um Bugs zu vermeiden wurdest du §centnickt");

                            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 2.0F);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
                        }
                    }
                } else if (event.getMessage().equalsIgnoreCase("version")) {
                    UUID uuid = UUID.fromString(event.getData().getString("uuid"));
                    int version = event.getData().getInt("versions");

                    new Version(uuid, version);
                }
            } else if (event.getChannel().equalsIgnoreCase("enderkomplex")) {
                if (event.getMessage().equalsIgnoreCase("pluginmessage")) {
                    String action = event.getData().getString("action");
                    UUID uuid = UUID.fromString(event.getData().getString("uuid"));
                    if (action.equals("SET_VANISH")) {
                        EnderAPI.getInstance().getVanishUUID().add(uuid);
                    }
                }
            }
        }
    }
}
