package net.endertime.enderapi.spigot.commands;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.Nick;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NickList_Command implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (EnderAPI.getInstance().isInTeam(p)) {
                if (label.equalsIgnoreCase("nicklist")) {
                    if (!Wrapper.getInstance().getServiceId().getName().startsWith("Lobby")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("SilentLobby")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Vorbau")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Team")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Terra")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Event")) {
                        if (args.length == 0) {
                            if (NickAPI.getInstance().getNickedPlayer().values().size() == 0) {
                                p.sendMessage(
                                        EnderAPI.getInstance().getPrefixNick() + "§7Es sind §ckeine §7Spieler genickt");
                            } else {
                                Nick nick = NickAPI.getInstance().getNick(p);

                                if (EnderAPI.getInstance().isInTeam(p)) {
                                    p.sendMessage(EnderAPI.getInstance().getPrefixNick() + "§7Alle genickten §cSpieler§8:");
                                    p.sendMessage("");
                                    for (Nick nicks : NickAPI.getInstance().getNickedPlayer().values())
                                        p.sendMessage("§8● §c" + nicks.getName() + " §8➡ §e" + nicks.getNickedName());
                                    p.sendMessage("");
                                } else {
                                    List<Nick> canSee = new ArrayList<>();

                                    for (Nick nicks : NickAPI.getInstance().getNickedPlayer().values()) {
                                        if (!EnderAPI.getInstance().isInTeam(nick.getPlayer())) {
                                            canSee.add(nicks);
                                        }
                                    }

                                    if (canSee.size() == 0) {
                                        p.sendMessage(
                                                EnderAPI.getInstance().getPrefixNick() + "§7Es sind §ckeine §7Spieler genickt");
                                    } else {
                                        p.sendMessage(EnderAPI.getInstance().getPrefixNick() + "§7Alle genickten §cSpieler§8:");
                                        p.sendMessage("");
                                        for (Nick nicks : canSee)
                                            p.sendMessage("§8● §c" + nicks.getName() + " §8➡ §e" + nicks.getNickedName());
                                        p.sendMessage("");
                                    }
                                }
                            }
                        } else {
                            EnderAPI.getInstance().sendActionBar(p,
                                    EnderAPI.getInstance().getPrefixNick() + "§7Benutze §8/§cnicklist");
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
