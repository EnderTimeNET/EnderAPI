package net.endertime.enderapi.bungee.commands;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderapi.clan.ClanAPI;
import net.endertime.enderapi.clan.utils.Clan;
import net.endertime.enderapi.clan.utils.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clan_Command extends Command implements TabExecutor {

    private ClanAPI clan = ClanAPI.getInstance();

    public Clan_Command(String name) {
        super(name);
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
            if (args.length > 0) {
                String cmd = args[0].toLowerCase();
                if (args.length == 1)  {
                    if (cmd.equals("help")) {
                        sendHelpFirst(sender);
                    } else if (cmd.equals("toggle")) {
                        if (clan.isToggle(sender.getUniqueId())) {
                            clan.getSettings().updateToggle(sender.getUniqueId(), 0);
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du hast die §6Clananfragen §cdeaktiviert"));
                        } else {
                            clan.getSettings().updateToggle(sender.getUniqueId(), 1);
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du hast die §6Clananfragen §aaktiviert"));
                        }
                    } else if (cmd.equals("togglemessage") || cmd.equals("togglemsg")) {
                        if (clan.isMSG(sender.getUniqueId())) {
                            clan.getSettings().updateMSG(sender.getUniqueId(), 0);
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du hast die §6Clannachrichten §cdeaktiviert"));
                        } else {
                            clan.getSettings().updateMSG(sender.getUniqueId(), 1);
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du hast die §6Clannachrichten §aaktiviert"));
                        }
                    } else if (cmd.equals("invites")) {
                        if (!this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            List<String> list = clan.getInvites().getInvites(sender.getUniqueId());
                            if (list.size() > 0) {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast §e" + list.size() + " §7Clananfrage(n)"));

                                for (String tag : list) {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                            + "§7-§e" + clan.getClans().getName(tag)));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast §ckeine §6Clananfragen"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du bist §cbereits §7in einem §6Clan"));
                        }
                    } else if (cmd.equals("leave")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (this.clan.getMember().getRang(sender.getUniqueId()) != 1) {
                                Clan clan = this.clan.getClan(this.clan.getMember().getTag(sender.getUniqueId()));
                                this.clan.getMember().deleteMember(this.clan.getMember().getTag(sender.getUniqueId()), sender.getUniqueId());
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast den §6Clan §cverlassen"));
                                ChannelMessage.builder()
                                        .channel("enderapi")
                                        .message("tablist")
                                        .targetService(sender.getServer().getInfo().getName())
                                        .build()
                                        .send();

                                clan.update();
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du kannst deinen eigenen §6Clan §cnicht §7verlassen"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist §7in §ckeinem §6Clan"));
                        }
                    } else if (cmd.equals("info")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            String tag = this.clan.getMember().getTag(sender.getUniqueId());
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§6Claninformationen"));
                            sender.sendMessage(EnderAPI.getInstance().getMessage("§6Clanname§8: §7" + clan.getClans().getName(tag)));
                            sender.sendMessage(EnderAPI.getInstance().getMessage("§6Clantag§8: §7" + tag));
                            sender.sendMessage(EnderAPI.getInstance().getMessage(""));

                            Clan clan = this.clan.getClan(tag);
                            String on = "§c●";

                            for (Rank rank : clan.getRanks()) {
                                sender.sendMessage(EnderAPI.getInstance().getMessage("§6" + rank.getRank() + "§8[§7" + rank.getPlayers().size() + "§8]"));
                                for (UUID uuid : rank.getPlayers()) {
                                    if (clan.isPlayerOnline(uuid))
                                        on = "§a●";
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(on + " " + EnderAPI.getInstance().getPrefix(uuid)
                                            + this.clan.getSettings().getName(uuid)));
                                    on = "§c●";
                                }
                            }

                            sender.sendMessage(EnderAPI.getInstance().getMessage("§6Members§8[§7" + clan.getMembers().size() + "§8]"));

                            for (UUID uuid : clan.getMembers()) {
                                if (clan.isPlayerOnline(uuid))
                                    on = "§a●";
                                sender.sendMessage(EnderAPI.getInstance().getMessage(on + " " + EnderAPI.getInstance().getPrefix(uuid)
                                        + this.clan.getSettings().getName(uuid)));
                                on = "§c●";
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist §7in §ckeinen §6Clan"));
                        }
                    }/* else if (cmd.equals("party")) {
                        String tag = this.clan.getMember().getTag(sender.getUniqueId());
                    if (this.clan.getMember().getRang(sender.getUniqueId()) == 1 ||this.clan.getMember().getRang(sender.getUniqueId()) == 2 ||
                            EnderAPI.getInstance().getPermissions().hasPerm(tag, this.clan.getMember().getRang(sender.getUniqueId()), "clan.party")) {
                           TODO:
                            Hier Clan-Party hinzufügen

                    } else {
                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du hast §ckeine §eRechte §7dazu"));
                    }
                    } else if (cmd.equals("ranks")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (this.clan.getMember().getRang(sender.getUniqueId()) == 1) {
                                sendToServer("ranks", sender.getUniqueId().toString(), "");
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                 + "§7Du hast §ckeine §7Berechtigung die §6Ränge eines " +
                                        "§cSpielers §7zu bearbeiten"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist in §ckeinem §6Clan"));
                        }
                    }*/ else if (cmd.equals("delete")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (this.clan.getMember().getRang(sender.getUniqueId()) == 1) {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Willst du dein §6Clan §7wirklich §clöschen"));
                                TextComponent accept = new TextComponent("§7Klicke §7hier §7um §7den §6Clan §7zu §clöschen");
                                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan delete accept " + sender.getUniqueId()));
                                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§lLöschen").create()));

                                TextComponent deny = new TextComponent("§7Klicke §7hier §7um §7den §6Clan §7zu §abehalten");
                                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan delete deny " + sender.getUniqueId()));
                                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lBehalten").create()));

                                sender.sendMessage(accept);
                                sender.sendMessage(deny);
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du bist §cnicht §7der Leader des §6Clans"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist in §ckeinen §6Clan"));
                        }
                    } else {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (args.length > 0) {
                                String msg = "";
                                for (int i = 0; i < args.length; i++)
                                    msg = msg + args[i] + " ";

                                Clan clan = this.clan.getClan(this.clan.getMember().getTag(sender.getUniqueId()));

                                for (UUID uuid : clan.getAllMembers()) {
                                    if (clan.isPlayerOnline(uuid)) {
                                        if (this.clan.isMSG(uuid)) {
                                            ProxyServer.getInstance().getPlayer(uuid).sendMessage(EnderAPI.getInstance()
                                                    .getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                    EnderAPI.getInstance().getPrefix(sender) + sender.getName() + " §7" + msg));
                                        }
                                    }
                                }
                            }
                        } else {
                            sendHelpFirst(sender);
                        }
                    }
                } else if (args.length == 2) {
                    if (cmd.equals("help")) {
                        if (args[1].equals("2")) {
                            sendHelpSecond(sender);
                        } else if (args[1].equals("3")) {
                            sendHelpThird(sender);
                        } else {
                            sendHelpFirst(sender);
                        }
                    } else if (cmd.equals("invite")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            String tag = this.clan.getMember().getTag(sender.getUniqueId());
                            if (this.clan.getMember().getRang(sender.getUniqueId()) == 1 ||
                                    ClanAPI.getInstance().getPermissions().hasPerm(tag, this.clan.getMember()
                                            .getRang(sender.getUniqueId()), "clan.invite")) {
                                if (clan.getClans().getMember(tag) > clan.getClan(tag).getAllMembers().size()) {
                                    String name = args[1];

                                    if (ProxyServer.getInstance().getPlayer(name) != null) {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                                        UUID uuid = target.getUniqueId();

                                        if (!this.clan.getMember().isUserExists(uuid)) {
                                            if (this.clan.isToggle(target.getUniqueId())) {
                                                clan.getInvites().createUser(uuid, tag);

                                                target.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + "§7Du wurdest in den §6Clan " + clan.getClans().getName(tag) +
                                                        " §aeingeladen"));

                                                TextComponent accept = new TextComponent("§e/clan accept " + clan.getClans().getName(tag)
                                                        + " §7um dem Clan §abeizutreten");
                                                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept "
                                                        + clan.getClans().getName(tag)));
                                                accept.setHoverEvent(
                                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lAnnehmen").create()));

                                                TextComponent deny = new TextComponent("§e/clan deny " + clan.getClans().getName(tag)
                                                        + " §7um die Anfrage §cabzulehnen");
                                                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan deny "
                                                        + clan.getClans().getName(tag)));
                                                deny.setHoverEvent(
                                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§lAblehnen").create()));

                                                target.sendMessage(accept);
                                                target.sendMessage(deny);

                                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + "§7Du hast " + EnderAPI.getInstance().getPrefix(uuid) + clan.getSettings().getName(uuid) +
                                                        " §7in den §6Clan §aeingeladen"));
                                            } else {
                                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + EnderAPI.getInstance().getPrefix(uuid) + target.getName() + " §7nimmt keine §6Clananfragen §7an"));
                                            }
                                        } else {
                                            if (this.clan.getMember().getTag(uuid).equals(tag)) {
                                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + EnderAPI.getInstance().getPrefix(uuid) + clan.getSettings().getName(uuid) + " §7ist §cbereits §7in " +
                                                        "deinen §6Clan"));
                                            } else {
                                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                        + EnderAPI.getInstance().getPrefix(uuid) + clan.getSettings().getName(uuid) + " §7ist §cbereits §7in " +
                                                        "einen anderen §6Clan"));
                                            }
                                        }
                                    } else {
                                        if (clan.getSettings().isNameExists(name)) {
                                            UUID uuid = clan.getSettings().getUUID(name);

                                            if (!this.clan.getMember().isUserExists(uuid)) {
                                                if (this.clan.isToggle(uuid)) {
                                                    clan.getInvites().createUser(uuid, tag);

                                                    sender.sendMessage(EnderAPI.getInstance()
                                                            .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                                    + "§7Du hast " + EnderAPI.getInstance()
                                                                    .getPrefix(uuid) + clan.getSettings().getName(uuid) +
                                                            " §7in den §6Clan §aeingeladen"));
                                                } else {
                                                    sender.sendMessage(EnderAPI.getInstance()
                                                            .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                                    + EnderAPI.getInstance().getPrefix(uuid)
                                                                    + clan.getSettings().getName(uuid) + " §7nimmt keine §6Clananfragen §7an"));
                                                }
                                            } else {
                                                if (this.clan.getMember().getTag(uuid).equals(tag)) {
                                                    sender.sendMessage(EnderAPI.getInstance()
                                                            .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                                    + EnderAPI.getInstance().getPrefix(uuid)
                                                                    + clan.getSettings().getName(uuid) + " §7ist §cbereits §7in " +
                                                            "deinen §6Clan"));
                                                } else {
                                                    sender.sendMessage(EnderAPI.getInstance()
                                                            .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                                    + EnderAPI.getInstance().getPrefix(uuid)
                                                                    + clan.getSettings().getName(uuid) + " §7ist §cbereits §7in " +
                                                            "einen anderen §6Clan"));
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(EnderAPI.getInstance()
                                                    .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                            + "§7" + name + " §7nimmt keine §6Clananfragen §7an"));
                                        }
                                    }
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance()
                                            .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                    + "§7Der §6Clan §7hat schon die §cmaximale §7Anzahl an " +
                                            "§6Clanmitgliedern"));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance()
                                        .getMessage(EnderAPI.getInstance().getPrefixClan()
                                                + "§7Du hast §ckeine §7Berechtigung jemanden in den §6Clan §7zu einzuladen"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance()
                                    .getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist in §ckeinen §6Clan"));
                        }
                    } else if (cmd.equals("kick")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            String tag = this.clan.getMember().getTag(sender.getUniqueId());
                            if (this.clan.getMember().getRang(sender.getUniqueId()) == 1 ||
                                    clan.getPermissions().hasPerm(tag, this.clan.getMember()
                                            .getRang(sender.getUniqueId()), "clan.kick")) {
                                String name = args[1];
                                UUID uuid = clan.getSettings().getUUID(name);
                                if (uuid != null) {
                                    if (this.clan.getMember().getTag(uuid).equals(this.clan.getMember().getTag(sender.getUniqueId()))) {
                                        this.clan.getMember().deleteMember(tag, uuid);

                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du hast "
                                                + EnderAPI.getInstance().getPrefix(uuid)
                                                + clan.getSettings().getName(uuid) + " §7aus den " + "§6Clan §cgekickt"));

                                        Clan clan = this.clan.getClan(tag);
                                        clan.update();

                                        ProxiedPlayer kicked = ProxyServer.getInstance().getPlayer(uuid);
                                        if (kicked != null) {
                                            kicked.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                    + "§7Du wurdest aus dem §6Clan §7gekickt"));
                                            ChannelMessage.builder()
                                                    .channel("enderapi")
                                                    .message("tablist")
                                                    .targetService(kicked.getServer().getInfo().getName())
                                                    .build()
                                                    .send();
                                        }

                                        ChannelMessage.builder()
                                                .channel("enderapi")
                                                .message("tablist")
                                                .targetService(sender.getServer().getInfo().getName())
                                                .build()
                                                .send();
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                + EnderAPI.getInstance().getPrefix(uuid) +
                                                clan.getSettings().getName(uuid) + " §7ist §cnicht §7in deinem §6Clan"));
                                    }
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7" +
                                            name + " §7ist §cnicht §7in deinem §6Clan"));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast §ckeine §7Berechtigung jemanden aus den §6Clan §7zu kicken"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist in §ckeinen §6Clan"));
                        }
                    } else if (cmd.equals("accept")) {
                        if (!this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            String name = args[1];

                            if (clan.getClans().isNameExists(name)) {
                                String tag = clan.getClans().getTag(name);
                                if (clan.getInvites().isTagExists(sender.getUniqueId(), tag)) {
                                    Clan clan = this.clan.getClan(tag);

                                    clan.getAllMembers().add(sender.getUniqueId());
                                    clan.getMembers().add(sender.getUniqueId());

                                    this.clan.getMember().createMember(tag, sender.getUniqueId(), 0);
                                    this.clan.getInvites().deleteInvites(sender.getUniqueId());

                                    for (UUID uuid : clan.getAllMembers()) {
                                        if (clan.isPlayerOnline(uuid)) {
                                            if (this.clan.isMSG(uuid)) {
                                                ProxyServer.getInstance().getPlayer(uuid).sendMessage(EnderAPI.getInstance()
                                                        .getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                        EnderAPI.getInstance().getPrefix(sender) + sender.getName() + " §7ist dem §6Clan §7beigetreten"));
                                            }
                                        }
                                    }

                                    clan.update();

                                    ChannelMessage.builder()
                                            .channel("enderapi")
                                            .message("tablist")
                                            .targetService(sender.getServer().getInfo().getName())
                                            .build()
                                            .send();
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                            + "§7Du hast §ckeine §7Einladung des §6Clans"));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast §ckeine §7Einladung des §6Clans"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du bist §cbereits §7in einen §6Clan"));
                        }
                    } else if (cmd.equals("deny")) {
                        if (!this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            String name = args[1];

                            if (clan.getClans().isNameExists(name)) {
                                String tag = clan.getClans().getTag(name);
                                if (clan.getInvites().isTagExists(sender.getUniqueId(), tag)) {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                            + "§7Du hast die §6Clananfrage §cabgelehnt"));

                                    clan.getInvites().deleteInvite(sender.getUniqueId(), tag);
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                            + "§7Du hast §ckeine §7Einladung des §6Clans"));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast §ckeine §7Einladung des §6Clans"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du bist §cbereits §7in einem §6Clan"));
                        }
                    } else if (cmd.equals("rank")) {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            String tag = this.clan.getMember().getTag(sender.getUniqueId());
                            if (this.clan.getMember().getRang(sender.getUniqueId()) == 1 ||
                                    clan.getPermissions().hasPerm(tag, this.clan.getMember().getRang(sender.getUniqueId()), "clan.edit")) {
                                String name = args[1];
                                if (clan.getSettings().isNameExists(name)) {
                                    UUID uuid = clan.getSettings().getUUID(name);
                                    if (this.clan.getMember().isUserExists(uuid) && this.clan.getMember().getTag(uuid).equals(tag)) {
                                        if (!uuid.equals(sender.getUniqueId())) {
                                            ChannelMessage.builder()
                                                    .channel("enderapi")
                                                    .message("tablist")
                                                    .targetService(sender.getServer().getInfo().getName())
                                                    .build()
                                                    .send();
                                        } else {
                                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                    + "§7Du kannst dein §6Clanrang §cnicht §7verwalten"));
                                        }
                                    } else {
                                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                                + EnderAPI.getInstance().getPrefix(uuid)
                                                + clan.getSettings().getName(uuid) + " ist §cnicht" +
                                                " §7in deinen §6Clan"));
                                    }
                                } else {
                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7" + name + " ist §cnicht" +
                                            " §7in deinen §6Clan"));
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast §ckeine §7Berechtigung die Ränge eines " +
                                        "§6Spielers §7zu bearbeiten"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Du bist in §ckeinen §6Clan"));
                        }
                    } else {
                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (args.length > 0) {
                                String msg = "";
                                for (int i = 0; i < args.length; i++)
                                    msg = msg + args[i] + " ";

                                Clan clan = this.clan.getClan(this.clan.getMember().getTag(sender.getUniqueId()));

                                for (UUID uuid : clan.getAllMembers()) {
                                    if (clan.isPlayerOnline(uuid)) {
                                        if (this.clan.isMSG(uuid)) {
                                            ProxyServer.getInstance().getPlayer(uuid).sendMessage(EnderAPI.getInstance()
                                                    .getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                    EnderAPI.getInstance().getPrefix(sender) + sender.getName() + " §7" + msg));
                                        }
                                    }
                                }
                            }
                        } else {
                            sendHelpFirst(sender);
                        }
                    }
                } else if (args.length == 3) {
                    if (cmd.equals("create")) {
                        String tag = args[1].toUpperCase();
                        String name = args[2];

                        if (!this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (isAvailable(tag, name, sender)) {
                                clan.getClans().createClan(tag, name);
                                this.clan.getMember().createMember(tag, sender.getUniqueId(), 1);
                                clan.getRanks().creatRank(tag, "Leader", 1, "§4", 276);
                                clan.getRanks().creatRank(tag, "Mod", 2, "§c", 267);

                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast den §6Clan " + name + " §7mit dem §6Tag " +
                                        tag + " §aerstellt"));

                                ChannelMessage.builder()
                                        .channel("enderapi")
                                        .message("tablist")
                                        .targetService(sender.getServer().getInfo().getName())
                                        .build()
                                        .send();
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du bist §cbereits §7in einem §6Clan"));
                        }
                    } else if (cmd.equals("rename")) {
                        String tag = args[1].toUpperCase();
                        String name = args[2];

                        if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                            if (this.clan.getMember().getRang(sender.getUniqueId()) == 1) {
                                if (isAvailable(tag, name, sender)) {
                                    String oldTag = this.clan.getMember().getTag(sender.getUniqueId());

                                    clan.getRanks().renameClan(oldTag, tag);
                                    this.clan.getMember().renameClan(oldTag, tag);
                                    clan.getInvites().renameClan(oldTag, tag);

                                    clan.getClans().updateName(oldTag, name);
                                    clan.getClans().updateTag(tag, name);

                                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                            + "§7Du hast den §6Clan §7zu §6" + clan.getClans().getName(tag) +
                                            " §7mit dem §6Tag " + tag + " §aumbenannt"));

                                    ChannelMessage.builder()
                                            .channel("enderapi")
                                            .message("tablist")
                                            .targetService(sender.getServer().getInfo().getName())
                                            .build()
                                            .send();
                                }
                            } else {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du bist §cnicht §7der §6Leader §7des §6Clans"));
                            }
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Du bist in §ckeinen §6Clan"));
                        }
                    } else if (cmd.equals("delete")) {
                        String call = args[1];
                        String uuid = args[2];

                        if (uuid.equals(sender.getUniqueId().toString())) {
                            if (call.equals("accept")) {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast dein §6Clan §cgelöscht"));

                                String tag = this.clan.getMember().getTag(sender.getUniqueId());

                                this.clan.getMember().deleteMembers(tag);
                                clan.getClans().deleteClan(tag);
                                clan.getInvites().deleteInvites(tag);
                                clan.getRanks().deleteRanks(tag);

                                ChannelMessage.builder()
                                        .channel("enderapi")
                                        .message("tablist")
                                        .targetService(sender.getServer().getInfo().getName())
                                        .build()
                                        .send();
                            } else if (call.equals("deny")) {
                                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                        + "§7Du hast dein §6Clan §abehalten"));
                            } else {
                                sendHelpFirst(sender);
                            }
                        } else {
                            sendHelpFirst(sender);
                        }
                    } else {
                        sendHelpFirst(sender);
                    }
                } else {
                    if (this.clan.getMember().isUserExists(sender.getUniqueId())) {
                        if (args.length > 0) {
                            String msg = "";
                            for (int i = 0; i < args.length; i++)
                                msg = msg + args[i] + " ";

                            Clan clan = this.clan.getClan(this.clan.getMember().getTag(sender.getUniqueId()));

                            for (UUID uuid : clan.getAllMembers()) {
                                if (clan.isPlayerOnline(uuid)) {
                                    if (this.clan.isMSG(uuid)) {
                                        ProxyServer.getInstance().getPlayer(uuid).sendMessage(EnderAPI.getInstance()
                                                .getMessage(EnderAPI.getInstance().getPrefixClan() +
                                                EnderAPI.getInstance().getPrefix(sender) + sender.getName() + " §7" + msg));
                                    }
                                }
                            }
                        }
                    } else {
                        sendHelpFirst(sender);
                    }
                }
            } else {
                sendHelpFirst(sender);
            }
        }
    }

    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();
        if (!(sender instanceof ProxiedPlayer))
            return list;
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 1) {
                if (args[0].startsWith("a")) {
                    list.add("accept");
                } else if (args[0].startsWith("c")) {
                    list.add("create");
                } else if (args[0].startsWith("d")) {
                    list.add("delete");
                    list.add("deny");
                } else if (args[0].startsWith("i")) {
                    list.add("info");
                    list.add("invite");
                    list.add("invites");
                } else if (args[0].startsWith("l")) {
                    list.add("leave");
                } else if (args[0].startsWith("r")) {
                    list.add("rank");
                    list.add("rename");
                } else if (args[0].startsWith("t")) {
                    list.add("toggle");
                    list.add("togglemsg");
                    list.add("toggletag");
                } else if (args[0].startsWith("k")) {
                    list.add("kick");
                } else {
                    list.add("accept");
                    list.add("create");
                    list.add("delete");
                    list.add("deny");
                    list.add("info");
                    list.add("invite");
                    list.add("invites");
                    list.add("kick");
                    list.add("leave");
                    list.add("rank");
                    list.add("rename");
                    list.add("toggle");
                    list.add("togglemsg");
                    list.add("toggletag");
                }
                return list;
            }
        }
        return list;
    }

    private void sendHelpFirst (ProxiedPlayer proxiedPlayer) {
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Clanverwaltung§8 [§e1§8/§e3§8]"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan toggle §7Anfragen erlauben & verbieten"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan create <Tag> <Name> §7Erstellt ein Clan"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan invite <Name> §7Lädt ein Spieler ein"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan leave §7Verlässt ein Clan"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan info §7Zeigt dir die Claninfo an"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/c <Nachricht> §7Sendet eine Nachricht"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan help 2 §7Um weitere Befehle zu sehen"));
    }

    private void sendHelpSecond (ProxiedPlayer proxiedPlayer) {
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Clanverwaltung§8 [§e2§8/§e3§8]"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan togglemsg §7Clannachrichten erlauben & verbieten"));
        //proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan party §7Lädt alle in deine Party ein"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan invites §7Listet alle Claneinladungen auf"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan delete §7Löscht dein Clan"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan accept <Name> §7Nimmt eine Claneinladung an"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan deny <Name> §7Lehnt eine Claneinladung ab"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan help 3 §7Um weitere Befehle zu sehen"));
    }

    private void sendHelpThird (ProxiedPlayer proxiedPlayer) {
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan() + "§7Clanverwaltung§8 [§e3§8/§e3§8]"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan rename <Tag> <Name> §7Verändert den Clantag und Clannamen"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan rank <Name> §7Bestimmt die Rangordnung des Spielers"));
        //proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan ranks §7Bestimme dir Rangordnung aller Ränge"));
        //proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan party §7Lädt alle in deine Party ein"));
        proxiedPlayer.sendMessage(EnderAPI.getInstance().getMessage("§e/clan kick <Name> §7Kickt den Spieler"));
    }

    private boolean isAvailable (String tag, String name, ProxiedPlayer sender) {
        if (!this.clan.getMember().isUserExists(sender.getUniqueId())) {
            if (!clan.getClans().isTagExists(tag)) {
                if (isTagAvailable(tag)) {
                    if (!clan.getClans().isNameExists(clan.getClans().getNameName(name))
                            || clan.getClans().getName(this.clan.getMember().getTag(sender.getUniqueId())).equals(name)) {
                        if (name.length() < 15) {
                            return true;
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Der §6Name §7darf §cnur §7aus maximal 14 Zeichen bestehen"));
                        }
                    } else {
                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                + "§7Dieser §6Name §cexistiert §7bereits"));
                    }
                } else {
                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                            + "§7Der §6Tag §7darf §cnur §7aus 2-4 Zeichen bestehen"));
                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                            + "§aErlaubte Zeichen sind von A-Z und 0-9"));
                }
            } else {
                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                        + "§7Dieser §6Tag §cexistiert §7bereits"));
            }
        } else {
            if (!clan.getClans().isTagExists(tag) || this.clan.getMember().getTag(sender.getUniqueId()).equals(tag)) {
                if (isTagAvailable(tag)) {
                    if (!clan.getClans().isNameExists(clan.getClans().getNameName(name))
                            || clan.getClans().getName(this.clan.getMember().getTag(sender.getUniqueId())).equals(name)) {
                        if (name.length() < 15) {
                            return true;
                        } else {
                            sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                    + "§7Der §6Name §7darf §cnur §7aus maximal 14 Zeichen bestehen"));
                        }
                    } else {
                        sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                                + "§7Dieser §6Name §cexistiert §7bereits"));
                    }
                } else {
                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                            + "§7Der §6Tag §7darf §cnur §7aus 2-4 Zeichen bestehen"));
                    sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                            + "§aErlaubte Zeichen sind von A-Z, 0-9, # und _"));
                }
            } else {
                sender.sendMessage(EnderAPI.getInstance().getMessage(EnderAPI.getInstance().getPrefixClan()
                        + "§7Dieser §6Tag §cexistiert §7bereits"));
            }
        }
        return false;
    }

    private boolean isTagAvailable (String tag) {
        boolean b = true;
        if (1 < tag.length() && tag.length() < 5) {
            for (char c : tag.toCharArray()) {
                if ((64 < c && c < 90) || (47 < c && c < 58) || '#' == c ||'_' == c) {
                    b = b && true;
                } else {
                    b = false;
                }
            }
        } else {
            b = false;
        }
        return b;
    }
}
