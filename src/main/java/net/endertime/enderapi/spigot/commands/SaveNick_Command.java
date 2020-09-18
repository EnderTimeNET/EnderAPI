package net.endertime.enderapi.spigot.commands;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveNick_Command implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("autonick.use")) {
                if (label.equalsIgnoreCase("savenick")) {
                    if (!Wrapper.getInstance().getServiceId().getName().startsWith("Lobby")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("SilentLobby")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Vorbau")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Team")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Terra")
                            && !Wrapper.getInstance().getServiceId().getName().startsWith("Event")) {
                        if (NickAPI.getInstance().getNickedPlayer().keySet().contains(p)) {
                            if (args.length == 0) {
                                if (EnderAPI.getInstance().getTeamDatabase().isRandom(p.getUniqueId())) {
                                    EnderAPI.getInstance().getTeamDatabase().updateRandom(p.getUniqueId(), false);

                                    p.sendMessage(EnderAPI.getInstance().getPrefixNick() + "§7Du hast den Nick §c" +
                                            NickAPI.getInstance().getNickedPlayer().get(p).getNickedName() + " §7gespeichert");
                                } else {
                                    EnderAPI.getInstance().sendActionBar(p,
                                            EnderAPI.getInstance().getPrefixNick() + "§7Du hast §cbereits §7ein festen Nick");
                                }
                            } else {
                                EnderAPI.getInstance().sendActionBar(p,
                                        EnderAPI.getInstance().getPrefixNick() + "§7Benutze §8/§csavenick");
                            }
                        } else {
                            EnderAPI.getInstance().sendActionBar(p,
                                    EnderAPI.getInstance().getPrefixNick() + "§7Du bist §cnicht §7genickt");
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
