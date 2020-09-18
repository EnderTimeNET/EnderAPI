package net.endertime.enderkomplex.bungee.commands;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HelpCommand extends Command {

    public HelpCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(args.length == 0) {
            ComponentBuilder cb = new ComponentBuilder("§0\n");
            cb.append("§7Folgende Befehle können nützlich sein:\n");
            cb.append("§8➟ §c/friend").event(new ClickEvent(Action.SUGGEST_COMMAND, "/friend")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/party").event(new ClickEvent(Action.SUGGEST_COMMAND, "/party")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/register").event(new ClickEvent(Action.SUGGEST_COMMAND, "/register")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/verify").event(new ClickEvent(Action.SUGGEST_COMMAND, "/verify")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/clan").event(new ClickEvent(Action.SUGGEST_COMMAND, "/clan")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/hub").event(new ClickEvent(Action.SUGGEST_COMMAND, "/hub")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/report").event(new ClickEvent(Action.SUGGEST_COMMAND, "/report")).append("\n").event((ClickEvent)null);
            cb.append("§8➟ §c/link").event(new ClickEvent(Action.SUGGEST_COMMAND, "/link")).append("\n").event((ClickEvent)null);
            pp.sendMessage(cb.create());
        } else {
            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§chelp");
            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
        }

    }

}
