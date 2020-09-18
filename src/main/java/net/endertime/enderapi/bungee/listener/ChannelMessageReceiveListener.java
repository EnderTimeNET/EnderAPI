package net.endertime.enderapi.bungee.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.bungee.utils.FriendManager;
import net.endertime.enderapi.bungee.utils.PartyManager;
import net.endertime.enderapi.bungee.utils.PlayerParty;
import net.endertime.enderapi.clan.ClanAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class ChannelMessageReceiveListener {

    private FriendManager friend = EnderAPI.getInstance().getFriend();
    private PartyManager partyManager = EnderAPI.getInstance().getPartyManager();

    @EventListener
    public void handleChannelMessage(ChannelMessageReceiveEvent event) {
        if (event.getMessage() != null) {
            if (event.getChannel().equalsIgnoreCase("enderapi")) {
                String command = event.getData().getString("command");
                UUID uuid = UUID.fromString(event.getData().getString("uuid"));
                UUID targetUUID = UUID.fromString(event.getData().getString("target"));
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                if (event.getMessage().equalsIgnoreCase("friend")) {
                    if (command.equalsIgnoreCase("add")) {
                        if (ProxyServer.getInstance().getPlayer(targetUUID) != null) {
                            friend.addRequests(player, ProxyServer.getInstance().getPlayer(targetUUID));
                        }
                    } else if (command.equals("deny")) {
                        friend.denyRequests(player, targetUUID);
                    } else if (command.equals("accept")) {
                        friend.addFriend(player, targetUUID);
                    } else if (command.equals("denyall")) {
                        List<UUID> requests = friend.getRequests().getRequests(player.getUniqueId());

                        for (UUID uuids : requests) {
                            friend.getRequests().removeRequests(player.getUniqueId(), uuids);
                        }

                        if (requests.size() == 0) {
                            player.sendMessage(
                                    EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixFriend()
                                            + "§7Du hast derzeit §ckeine §7Freundschaftsanfragen"));
                        } else if (requests.size() == 1) {
                            player.sendMessage(EnderAPI.getInstance()
                                    .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast eine Freundschaftsanfragen §cabgelehnt"));
                        } else {
                            player.sendMessage(EnderAPI.getInstance()
                                    .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast alle Freundschaftsanfragen §cabgelehnt"));
                        }
                    } else if (command.equals("acceptall")) {
                        List<UUID> requests = friend.getRequests().getRequests(player.getUniqueId());

                        for (UUID uuids : requests) {
                            friend.getRequests().removeRequests(player.getUniqueId(), uuids);
                            friend.getFriends().createFriend(player.getUniqueId(), uuids);
                        }

                        if (requests.size() == 0) {
                            player.sendMessage(
                                    friend.getMessage(friend.getPrefix() + "§7Du hast derzeit §ckeine §7Freundschaftsanfragen"));
                        } else if (requests.size() == 1) {
                            player.sendMessage(EnderAPI.getInstance()
                                    .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast eine Freundschaftsanfragen §aangenommen"));
                        } else {
                            player.sendMessage(EnderAPI.getInstance()
                                    .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast alle Freundschaftsanfragen §aangenommen"));
                        }
                    } else if (command.equals("remove")) {
                        friend.deleteFriend(player, targetUUID);
                    } else if (command.equals("jump")) {
                        friend.jump(player, targetUUID);
                    } else if (command.equals("clear")) {
                        List<UUID> list = friend.getFriends().getFriends(player.getUniqueId());

                        for (UUID uuids : list) {
                            friend.getFriends().removeFriends(player.getUniqueId(), uuids);
                        }

                        player.sendMessage(EnderAPI.getInstance()
                                .getMessage(EnderAPI.getInstance().getPrefixFriend() + "§7Du hast deine komplette §7Freundesliste §agelöscht"));
                    }
                } else if (event.getMessage().equalsIgnoreCase("party")) {
                    if (command.equals("invite")) {
                        if (partyManager.createParty(player, false)) {
                            PlayerParty playerParty = partyManager.getParty(player);
                            if (playerParty.isLeader(player)) {
                                if (player.hasPermission("party.youtuber")) {
                                    if (playerParty.getMembers().size() == 15) {
                                        player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast §cbereits §7eine volle Party"));
                                        return;
                                    }
                                } else if (player.hasPermission("party.ender")) {
                                    if (playerParty.getMembers().size() == 10) {
                                        player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast §cbereits §7eine volle Party"));
                                        return;
                                    }
                                } else {
                                    if (playerParty.getMembers().size() == 8) {
                                        player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du hast §cbereits §7eine volle Party"));
                                        return;
                                    }
                                }

                                try {
                                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
                                    if (partyManager.getParty(target) == null) {
                                        if (friend.getSettings().getPartyToggle(target.getUniqueId()) == 0
                                                || (friend.getSettings().getPartyToggle(target.getUniqueId()) == 1
                                                && friend.getFriends().isFriend(target.getUniqueId(), player.getUniqueId()))) {
                                            if (!partyManager.addInvite(playerParty, target)) {
                                                if (playerParty.getInvites().contains(target)) {
                                                    player.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7wurde §cbereits §7eingeladen"));
                                                } else {
                                                    player.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                            partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7ist §cbereits §7in der Party"));
                                                }
                                            }
                                        } else {
                                            player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                    target.getName() + " §7nimmt §ckeine §7Anfragen an"));
                                        }
                                    } else {
                                        player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                target.getName() + " §7ist §cbereits §7in einer anderen Party"));
                                    }
                                } catch (NullPointerException ex) {
                                    player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                                }
                            } else {
                                player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Du bist §cnicht §7er Partyleiter"));
                            }
                        } else {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
                            PlayerParty playerParty = partyManager.getParty(player);
                            if (target != null) {
                                if (partyManager.getParty(target) == null) {
                                    if (friend.getSettings().getPartyToggle(target.getUniqueId()) == 0
                                            || (friend.getSettings().getPartyToggle(target.getUniqueId()) == 1
                                            && friend.getFriends().isFriend(target.getUniqueId(), player.getUniqueId()))) {
                                        if (!partyManager.addInvite(playerParty, target)) {
                                            if (playerParty.getInvites().contains(target)) {
                                                player.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                        partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7wurde §cbereits §7eingeladen"));
                                            } else {
                                                player.sendMessage(partyManager.getMessage(partyManager.getPrefix() +
                                                        partyManager.getPrefix(target.getUniqueId()) + target.getName() + " §7ist §cbereits §7in der Party"));
                                            }
                                        }
                                    } else {
                                        player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                                target.getName() + " §7nimmt §ckeine §7Anfragen an"));
                                    }
                                } else {
                                    player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + partyManager.getPrefix(target.getUniqueId()) +
                                            target.getName() + " §7ist §cbereits §7in einer anderen Party"));
                                }
                            } else {
                                player.sendMessage(partyManager.getMessage(partyManager.getPrefix() + "§7Der Spieler ist §cnicht §7online"));
                            }
                        }
                    }
                } else if (event.getMessage().equals("clan")) {
                    if (command.equals("invite")) {
                        if (ClanAPI.getInstance().getMember().isUserExists(player.getUniqueId())) {
                            String tag = ClanAPI.getInstance().getMember().getTag(player.getUniqueId());
                            if (ClanAPI.getInstance().getMember().getRang(player.getUniqueId()) == 1 ||
                                    ClanAPI.getInstance().getPermissions().hasPerm(tag, ClanAPI.getInstance().getMember().getRang(player.getUniqueId()),
                                            "clan.invite")) {
                                if (ClanAPI.getInstance().getClans().getMember(tag) > ClanAPI.getInstance().getClan(tag).getAllMembers().size()) {

                                    if (ProxyServer.getInstance().getPlayer(targetUUID) != null) {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);

                                        if (!ClanAPI.getInstance().getMember().isUserExists(targetUUID)) {
                                            if (ClanAPI.getInstance().isToggle(target.getUniqueId())) {
                                                if (!ClanAPI.getInstance().getInvites().isTagExists(targetUUID, tag)) {
                                                    ClanAPI.getInstance().getInvites().createUser(targetUUID, tag);

                                                    target.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                            "§7Du wurdest in den §6Clan " + ClanAPI.getInstance().getClans().getName(tag) +
                                                            " §aeingeladen"));

                                                    TextComponent accept = new TextComponent("§e/clan accept " + ClanAPI.getInstance().getClans()
                                                            .getName(tag) + " §7um dem Clan §abeizutreten");
                                                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " +
                                                            ClanAPI.getInstance().getClans().getName(tag)));
                                                    accept.setHoverEvent(
                                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lAnnehmen").create()));

                                                    TextComponent deny = new TextComponent("§e/clan deny " + ClanAPI.getInstance().getClans().
                                                            getName(tag) + " §7um die Anfrage §cabzulehnen");
                                                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan deny " +
                                                            ClanAPI.getInstance().getClans().getName(tag)));
                                                    deny.setHoverEvent(
                                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§lAblehnen").create()));

                                                    target.sendMessage(accept);
                                                    target.sendMessage(deny);

                                                    player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                            + "§7Du hast " + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance()
                                                            .getSettings().getName(targetUUID) +
                                                            " §7in den §6Clan §aeingeladen"));
                                                } else {
                                                    player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                            "§7Du hast " + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance()
                                                            .getSettings().getName(targetUUID) +
                                                            " §cbereits §7in den §6Clan §7eingeladen"));
                                                }
                                            } else {
                                                player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + EnderAPI.getInstance().getPrefix(targetUUID) + target.getName() + " §7nimmt keine §6Clananfragen §7an"));
                                            }
                                        } else {
                                            if (ClanAPI.getInstance().getMember().getTag(targetUUID).equals(tag)) {
                                                player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance().getSettings().getName(targetUUID)
                                                        + " §7ist §cbereits §7in " +
                                                        "deinen §6Clan"));
                                            } else {
                                                player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance().getSettings().getName(targetUUID)
                                                        + " §7ist §cbereits §7in " +
                                                        "einen anderen §6Clan"));
                                            }
                                        }
                                    } else {
                                        if (ClanAPI.getInstance().getSettings().isUserExists(targetUUID)) {
                                            if (!ClanAPI.getInstance().getMember().isUserExists(targetUUID)) {
                                                if (ClanAPI.getInstance().isToggle(targetUUID)) {
                                                    if (!ClanAPI.getInstance().getInvites().isTagExists(targetUUID, tag)) {
                                                        ClanAPI.getInstance().getInvites().createUser(targetUUID, tag);

                                                        player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                                + "§7Du hast " + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance()
                                                                .getSettings().getName(targetUUID) +
                                                                " §7in den §6Clan §aeingeladen"));
                                                    } else {
                                                        player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                                "§7Du hast " + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance()
                                                                .getSettings().getName(targetUUID) +
                                                                " §cbereits §7in den §6Clan §7eingeladen"));
                                                    }
                                                } else {
                                                    player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                            + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance().getSettings().getName(targetUUID)
                                                            + " §7nimmt keine §6Clananfragen §7an"));
                                                }
                                            } else {
                                                if (ClanAPI.getInstance().getMember().getTag(targetUUID).equals(tag)) {
                                                    player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                            + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance().getSettings().getName(targetUUID)
                                                            + " §7ist §cbereits §7in " +
                                                            "deinen §6Clan"));
                                                } else {
                                                    player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                            + EnderAPI.getInstance().getPrefix(targetUUID) + ClanAPI.getInstance().getSettings().getName(targetUUID)
                                                            + " §7ist §cbereits §7in " +
                                                            "einen anderen §6Clan"));
                                                }
                                            }
                                        } else {
                                            player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7"
                                                    + EnderAPI.getInstance().getName(targetUUID)
                                                    + " §7nimmt keine §6Clananfragen §7an"));
                                        }
                                    }
                                } else {
                                    player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Der §6Clan §7hat" +
                                            " schon die §cmaximale §7Anzahl an " +
                                            "§6Clanmitgliedern"));
                                }
                            } else {
                                player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du hast §ckeine " +
                                        "§7Berechtigung jemanden in den §6Clan §7zu einzuladen"));
                            }
                        } else {
                            player.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist in §ckeinen" +
                                    " §6Clan"));
                        }
                    }
                }
            }
        }
    }
}
