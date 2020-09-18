package net.endertime.enderapi.bungee.utils;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PartyManager {

    private List<PlayerParty> parties = new ArrayList<PlayerParty>();

    public PlayerParty getParty (ProxiedPlayer p) {
        for (PlayerParty party : parties) {
            if (party.inParty(p) || party.isLeader(p)) {
                return party;
            }
        }
        return null;
    }

    public List<PlayerParty> getParties() {
        return parties;
    }

    public boolean createParty (ProxiedPlayer p, boolean publicParty) {
        if (getParty(p) == null) {
            parties.add(new PlayerParty(p, publicParty));
            ChannelMessage.builder()
                    .channel("enderapi")
                    .message("party")
                    .json(JsonDocument.newDocument().append("command", "create").append("leader", p.getUniqueId().toString())
                            .append("publicParty", publicParty))
                    .targetAll()
                    .build()
                    .send();
            return true;
        }
        return false;
    }

    public void promote (PlayerParty party, ProxiedPlayer p) {
        party.getMembers().remove(p);
        party.getMembers().add(party.getLeader());
        ChannelMessage.builder()
                .channel("enderapi")
                .message("party")
                .json(JsonDocument.newDocument().append("command", "leader").append("leader", party.getLeader().getUniqueId().toString())
                        .append("uuid", p.getUniqueId().toString()))
                .targetAll()
                .build()
                .send();
        party.setLeader(p);
    }

    public boolean deleteParty (ProxiedPlayer p) {
        if (getParty(p) != null) {
            if (getParty(p).isLeader(p)) {
                Iterator<ProxiedPlayer> iterator = getParty(p).getMembers().iterator();

                while (iterator.hasNext()) {
                    ProxiedPlayer member = iterator.next();
                    member.sendMessage(getMessage(getPrefix() + "§7Die Party wurde §caufgelöst"));
                    iterator.remove();
                }

                ChannelMessage.builder()
                        .channel("enderapi")
                        .message("party")
                        .json(JsonDocument.newDocument().append("command", "add").append("delete", p.getUniqueId().toString()))
                        .targetAll()
                        .build()
                        .send();
                parties.remove(getParty(p));
                return true;
            }
        }
        return false;
    }

    public boolean addPlayer (PlayerParty party, ProxiedPlayer p) {
        if (!party.inParty(p) && party.getInvites().contains(p)) {
            party.getMembers().add(p);
            party.getInvites().remove(p);
            ChannelMessage.builder()
                    .channel("enderapi")
                    .message("party")
                    .json(JsonDocument.newDocument().append("command", "add").append("leader", party.getLeader().getUniqueId().toString())
                            .append("uuid", p.getUniqueId().toString()))
                    .targetAll()
                    .build()
                    .send();
            return true;
        } else if (!party.inParty(p) && party.isPublicParty()) {
            party.getMembers().add(p);
            ChannelMessage.builder()
                    .channel("enderapi")
                    .message("party")
                    .json(JsonDocument.newDocument().append("command", "add").append("leader", party.getLeader().getUniqueId().toString())
                            .append("uuid", p.getUniqueId().toString()))
                    .targetAll()
                    .build()
                    .send();
            return true;
        }
        return false;
    }

    public boolean removePlayer (PlayerParty party, ProxiedPlayer p) {
        if (party.inParty(p)) {
            party.getMembers().remove(p);
            ChannelMessage.builder()
                    .channel("enderapi")
                    .message("party")
                    .json(JsonDocument.newDocument().append("command", "remove").append("leader", party.getLeader().getUniqueId().toString())
                            .append("uuid", p.getUniqueId().toString()))
                    .targetAll()
                    .build()
                    .send();
            return true;
        }
        return false;
    }

    public boolean addInvite (final PlayerParty party, final ProxiedPlayer p) {
        if (!party.inParty(p) && !party.getInvites().contains(p)) {
            party.getInvites().add(p);
            party.getLeader().sendMessage(getMessage(getPrefix() + "§7Du hast " + getPrefix(p.getUniqueId()) + p.getName()
                    + " §7in die Party §aeingeladen"));

            p.sendMessage(getMessage(getPrefix() + getPrefix(party.getLeader().getUniqueId()) + party.getLeader().getName() +
                    " §7hat dich in seine Party §aeingeladen"));


            BaseComponent[] accept = new ComponentBuilder("§8[§aAnnehmen§8]").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getMessage("§a§lAnnehmen")))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + party.getLeader().getName())).create();

            BaseComponent[] deny = new ComponentBuilder("§8[§cAblehnen§8]").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getMessage("§c§lAblehnen")))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + party.getLeader().getName())).create();

            ComponentBuilder cb = new ComponentBuilder("§8§m-------------------------\n").event((ClickEvent) null).event((HoverEvent) null)
                    .append(accept).append(" ").event((ClickEvent) null).event((HoverEvent) null).append(deny);

            p.sendMessage(cb.create());

            ProxyServer.getInstance().getScheduler().schedule(EnderAPI.getInstance().getPlugin(), new Runnable() {
                public void run() {
                    if (removeInvite(party, p)) {
                        p.sendMessage(getMessage(getPrefix() + "§7Deine Einladung ist §cabgelaufen"));
                        party.getLeader().sendMessage(getMessage(getPrefix() + getPrefix(p.getUniqueId()) + p.getName() + " §7hat die Einladung" +
                                " §cnicht §7angenommen"));
                        start(party);
                    }
                }
            }, 1L, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    public boolean removeInvite (PlayerParty party, ProxiedPlayer p) {
        if (!party.inParty(p) && party.getInvites().contains(p)) {
            party.getInvites().remove(p);
            return true;
        }
        return false;
    }

    public void start (final PlayerParty party) {
        if (party != null && party.getMembers().size() == 0 && party.getInvites().size() == 0 && !party.isPublicParty()) {
            deleteParty(party.getLeader());
            party.getLeader().sendMessage(getMessage(getPrefix() + "§7Die Party wird wegen §czu wenig §7Mitgliedern aufgelöst"));
        }
    }

    public BaseComponent[] getMessage (String message) {
        return EnderAPI.getInstance().getMessage(message);
    }

    public String getPrefix () {
        return EnderAPI.getInstance().getPrefixParty();
    }

    public String getPrefix (UUID uuid) {
        return EnderAPI.getInstance().getPrefix(uuid);
    }
}
