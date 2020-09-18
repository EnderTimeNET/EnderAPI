package net.endertime.enderapi.bungee.utils;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.database.friends.FriendSettings;
import net.endertime.enderapi.database.friends.Friends;
import net.endertime.enderapi.database.friends.Requests;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FriendManager {

    private Friends friends = new Friends();
    private Requests requests = new Requests();
    private FriendSettings settings = new FriendSettings();
    private int maxFriends = 100;

    public void addRequests (ProxiedPlayer sender, ProxiedPlayer target) {
        if (!sender.getUniqueId().equals(target.getUniqueId())) {
            if (friends.getFriendsCount(sender.getUniqueId()) <= maxFriends) {
                if (friends.getFriendsCount(target.getUniqueId()) <= maxFriends) {
                    if (!friends.isFriend(sender.getUniqueId(), target.getUniqueId())) {
                        if (!requests.isRequests(sender.getUniqueId(), target.getUniqueId())) {
                            if (!settings.getToggle(target.getUniqueId())) {
                                requests.createRequests(sender.getUniqueId(), target.getUniqueId());
                                sender.sendMessage(getMessage(getPrefix() + "§7Du hast " + getPrefix(target.getUniqueId()) + target.getName() +
                                        " §7eine Freundschaftsanfrage §agesendet"));

                                target.sendMessage(getMessage(getPrefix() + getPrefix(sender.getUniqueId()) + sender.getName() +
                                        " §7möchte dein Freund sein"));

                                BaseComponent[] accept = new ComponentBuilder("§8[§aAnnehmen§8]").event(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, getMessage("§a§lAnnehmen")))
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + sender.getName())).create();

                                BaseComponent[] deny = new ComponentBuilder("§8[§cAblehnen§8]").event(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, getMessage("§c§lAblehnen")))
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + sender.getName())).create();

                                ComponentBuilder cb = new ComponentBuilder("§8§m-------------------------\n")
                                        .event((ClickEvent) null).event((HoverEvent) null).append(accept).append(" ").event((ClickEvent) null)
                                        .event((HoverEvent) null).append(deny);

                                target.sendMessage(cb.create());
                            } else {
                                sender.sendMessage(getMessage(getPrefix() + "§7Der Spieler nimmt §ckeine §7Freundschaftsanfragen an"));
                            }
                        } else {
                            if (requests.isRequestsForCMD(sender.getUniqueId(), target.getUniqueId())) {
                                addFriend(sender, target);
                            } else {
                                sender.sendMessage(getMessage(getPrefix() + "§7Du hast dem Spieler §cbereits §7eine Freundschaftsanfrage §ageschickt"));
                            }
                        }
                    } else {
                        sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cbereits §7mit dem Spieler befreundet"));
                    }
                } else {
                    sender.sendMessage(getMessage(getPrefix() + "§7Der Spieler hat §cbereits §7die maximale Anzahl an Freunden"));
                }
            } else {
                sender.sendMessage(getMessage(getPrefix() + "§7Du hast §cbereits §7die maximale Anzahl an Freunden"));
            }
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du kannst dir §ckeine §7Freundschaftsanfrage senden"));
        }
    }

    public void addRequests (ProxiedPlayer sender, UUID target) {
        if (friends.getFriendsCount(sender.getUniqueId()) <= maxFriends) {
            if (friends.getFriendsCount(target) <= maxFriends) {
                if (!friends.isFriend(sender.getUniqueId(), target)) {
                    if (!requests.isRequests(sender.getUniqueId(), target)) {
                        if (!settings.getToggle(target)) {
                            requests.createRequests(sender.getUniqueId(), target);
                            sender.sendMessage(getMessage(getPrefix() + "§7Du hast " + getPrefix(target) +
                                    EnderAPI.getInstance().getEnderDatabase().getName(target) + " §7eine Freundschaftsanfrage §agesendet"));
                        } else {
                            sender.sendMessage(getMessage(getPrefix() + "§7Der Spieler nimmt §ckeine §7Freundschaftsanfragen an"));
                        }
                    } else {
                        if (requests.isRequestsForCMD(sender.getUniqueId(), target)) {
                            addFriend(sender, target);
                        } else {
                            sender.sendMessage(getMessage(getPrefix() + "§7Du hast dem Spieler §cbereits §7eine Freundschaftsanfrage geschickt"));
                        }
                    }
                } else {
                    sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cbereits §7mit dem Spieler befreundet"));
                }
            } else {
                sender.sendMessage(getMessage(getPrefix() + "§7Der Spieler hat §cbereits §7die maximale Anzahl an Freunden"));
            }
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du hast §cbereits §7die maximale Anzahl an Freunden"));
        }
    }

    public void addFriend (ProxiedPlayer sender, ProxiedPlayer target) {
        if (!friends.isFriend(sender.getUniqueId(), target.getUniqueId())) {
            if (requests.isRequests(sender.getUniqueId(), target.getUniqueId())) {
                if (requests.isRequestsForCMD(sender.getUniqueId(), target.getUniqueId())) {
                    requests.removeRequests(sender.getUniqueId(), target.getUniqueId());
                    friends.createFriend(sender.getUniqueId(), target.getUniqueId());
                    sender.sendMessage(getMessage(getPrefix() + "§7Du hast die Freundschaftsanfrage von "
                            + getPrefix(target.getUniqueId()) + EnderAPI.getInstance().getEnderDatabase().getName(target.getUniqueId()) + " §aangenommen"));
                    target.sendMessage(getMessage(getPrefix() + getPrefix(sender.getUniqueId()) +
                            EnderAPI.getInstance().getEnderDatabase().getName(sender.getUniqueId()) + " §7hat deine Freundschaftsanfrage §aangenommen"));
                } else {
                    sender.sendMessage(getMessage(getPrefix() + "§7Du hast dem Spieler eine Freundschaftsanfrage §cgeschickt"));
                }
            } else {
                sender.sendMessage(getMessage(getPrefix() + "§7Du hast von dem Spieler keine Freundschaftsanfrage erhalten"));
            }
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cbereits §7mit dem Spieler befreundet"));
        }
    }

    public void addFriend (ProxiedPlayer sender, UUID target) {
        if (!friends.isFriend(sender.getUniqueId(), target)) {
            if (requests.isRequests(sender.getUniqueId(), target)) {
                if (requests.isRequestsForCMD(sender.getUniqueId(), target)) {
                    requests.removeRequests(sender.getUniqueId(), target);
                    friends.createFriend(sender.getUniqueId(), target);
                    sender.sendMessage(getMessage(getPrefix() + "§7Du hast die Freundschaftsanfrage von "
                            + getPrefix(target) + EnderAPI.getInstance().getEnderDatabase().getName(target) + " §aangenommen"));
                } else {
                    sender.sendMessage(getMessage(getPrefix() + "§7Du hast dem Spieler eine Freundschaftsanfrage §cgeschickt"));
                }
            } else {
                sender.sendMessage(getMessage(getPrefix() + "§7Du hast von dem Spieler keine Freundschaftsanfrage erhalten"));
            }
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cbereits §7mit dem Spieler befreundet"));
        }
    }

    public void deleteRequests (ProxiedPlayer sender, UUID target) {

    }

    public void denyRequests (ProxiedPlayer sender, UUID target) {
        if (!friends.isFriend(sender.getUniqueId(), target)) {
            if (requests.isRequests(sender.getUniqueId(), target)) {
                if (requests.isRequestsForCMD(sender.getUniqueId(), target)) {
                    requests.removeRequests(sender.getUniqueId(), target);
                    sender.sendMessage(getMessage(getPrefix() + "§7Du hast die Freundschaftsanfrage von "
                            + getPrefix(target) + EnderAPI.getInstance().getEnderDatabase().getName(target) + " §7abgelehnt"));
                } else {
                    sender.sendMessage(getMessage(getPrefix() + "§7Du hast dem Spieler eine Freundschaftsanfrage §cgeschickt"));
                }
            } else {
                sender.sendMessage(getMessage(getPrefix() + "§7Du hast von dem Spieler keine Freundschaftsanfrage erhalten"));
            }
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cbereits §7mit dem Spieler befreundet"));
        }
    }

    public void deleteFriend (ProxiedPlayer sender, UUID target) {
        if (friends.isFriend(sender.getUniqueId(), target)) {
            sender.sendMessage(getMessage(getPrefix() + "§7Du hast " + EnderAPI.getInstance().getPrefix(target) +
                    EnderAPI.getInstance().getName(target) + " §7von " + "deiner Freundesliste §centfernt"));
            friends.removeFriends(sender.getUniqueId(), target);
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cnicht §7mit dem Spieler befreundet"));
        }
    }

    public void jump (ProxiedPlayer sender, UUID uuid) {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
        if (target != null) {
            if (friends.isFriend(sender.getUniqueId(), target.getUniqueId())) {
                if (!settings.getToggleJump(target.getUniqueId())) {
                    ServerInfo serverInfo = target.getServer().getInfo();

                    if (!serverInfo.getName().startsWith("SilentLobby")
                            && !serverInfo.getName().startsWith("Team")
                            && !serverInfo.getName().startsWith("Vorbau")) {
                        sender.connect(serverInfo);
                    } else {
                        sender.sendMessage(getMessage(getPrefix() + "§7Du darfst " + EnderAPI.getInstance().getPrefix(target.getUniqueId()) + target.getName() +
                                " §7nicht nachspringen"));
                    }
                } else {
                    sender.sendMessage(getMessage(getPrefix() + "§7Du darfst " + EnderAPI.getInstance().getPrefix(target.getUniqueId()) + target.getName() +
                            " §7nicht nachspringen"));
                }
            } else {
                sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cnicht §7mit dem Spieler befreundet"));
            }
        } else {
            sender.sendMessage(getMessage(getPrefix() + "§7Du bist §cnicht §7mit dem Spieler befreundet"));
        }
    }

    public BaseComponent[] getMessage (String msg) {
        return EnderAPI.getInstance().getMessage(msg);
    }

    public String getPrefix (){
        return EnderAPI.getInstance().getPrefixFriend();
    }

    public String getPrefix (UUID uuid) {
        return EnderAPI.getInstance().getPrefix(uuid);
    }

    public FriendSettings getSettings() {
        return settings;
    }

    public Friends getFriends() {
        return friends;
    }

    public Requests getRequests() {
        return requests;
    }


}
