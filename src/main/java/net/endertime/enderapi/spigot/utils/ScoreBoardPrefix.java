package net.endertime.enderapi.spigot.utils;

import net.endertime.enderapi.clan.ClanAPI;
import net.endertime.enderapi.permission.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.api.PartyAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ScoreBoardPrefix {

    private String rang;
    private String suffix;
    private String labySuffix;
    private String team;
    private String entry;

    public ScoreBoardPrefix(UUID uuid, Player scoreBoard) {
        if (ClanAPI.getInstance().getMember().isUserExists(uuid)) {
            if (ClanAPI.getInstance().getClans().isTagExists(ClanAPI.getInstance().getMember().getTag(uuid))) {
                this.suffix = " §8[§6" + ClanAPI.getInstance().getMember().getTag(uuid) + "§8]";
                if (ClanAPI.getInstance().getMember().getRang(uuid) == 0) {
                    this.labySuffix = "§6" + ClanAPI.getInstance().getClans().getName(ClanAPI.getInstance().getMember().getTag(uuid)) + " §8× §7Member";
                } else if (ClanAPI.getInstance().getMember().getRang(uuid) == 1) {
                    this.labySuffix = "§6" + ClanAPI.getInstance().getClans().getName(ClanAPI.getInstance().getMember().getTag(uuid)) + " §8× §4Leader";
                } else if (ClanAPI.getInstance().getMember().getRang(uuid) == 2) {
                    this.labySuffix = "§6" + ClanAPI.getInstance().getClans().getName(ClanAPI.getInstance().getMember().getTag(uuid)) + " §8× §cMod";
                }
            }
        } else {
            this.suffix = "";
            this.labySuffix = "";
        }
        if (EnderAPI.getInstance().isVanished(Bukkit.getPlayer(uuid))) {
            this.entry = EnderAPI.getInstance().getName(uuid);
            this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
            this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                    + EnderAPI.getInstance().getName(uuid);
            this.suffix = " §8[§1V§8]";
        } else {
            if (Team.isInTeam(uuid)) {
                Team team = Team.getTeam(uuid);
                if (NickAPI.getInstance().isNicked(uuid)) {
                    if (uuid.equals(scoreBoard.getUniqueId())) {  //scoreBoard equals p
                        if (PartyAPI.getInstance().getParty(uuid) != null
                                && !PartyAPI.getInstance().getParty(uuid).isPublicParty()) {
                            this.rang = team.getPrefix();
                            this.team = "00" + team.getId() + NickAPI.getInstance().getNick(uuid).getName();
                            this.suffix = " §8[§5Party§8]";
                        } else {
                            this.rang = team.getPrefix();
                            this.team = "00" + team.getId() + NickAPI.getInstance().getNick(uuid).getName();
                            this.suffix = " §8[§5N§8]";
                        }
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else if (PartyAPI.getInstance().getParty(uuid) != null
                            && PartyAPI.getInstance().getParty(uuid).getPlayers().contains(scoreBoard.getUniqueId())
                            && !PartyAPI.getInstance().getParty(uuid).isPublicParty()) {  //scoreBoard in Party mit p
                        this.rang = team.getPrefix();
                        this.team = "00" + team.getId() + NickAPI.getInstance().getNick(uuid).getName();
                        this.suffix = " §8[§5Party§8]";
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else if (EnderAPI.getInstance().isInTeam(scoreBoard)) {  //scoreBoard ist im Team
                        this.rang = team.getPrefix();
                        this.team = "00" + team.getId() + NickAPI.getInstance().getNick(uuid).getName();
                        this.suffix = " §8[§5N§8]";
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else if (NickAPI.getInstance().getNickedParty().get(uuid).contains(scoreBoard)) {  //scoreBoard war Party mit p
                        this.rang = team.getPrefix();
                        this.team = "00" + team.getId() + NickAPI.getInstance().getNick(uuid).getName();
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else {    //Spieler ohne NickSee Rechte
                        this.rang = team.getPrefix();
                        this.team = "00" + team.getId() + NickAPI.getInstance().getNick(uuid).getNickedName();
                        this.entry = NickAPI.getInstance().getNick(uuid).getNickedName();
                        this.suffix = "";
                    }
                } else {
                    if (PartyAPI.getInstance().getParty(uuid) != null
                            && PartyAPI.getInstance().getParty(uuid).getPlayers().contains(scoreBoard.getUniqueId())
                            && !PartyAPI.getInstance().getParty(uuid).isPublicParty()) {
                        this.suffix = " §8[§5Party§8]";
                    }
                    this.rang = team.getPrefix();
                    this.team = "00" + team.getId() + EnderAPI.getInstance().getName(uuid);
                    this.entry = EnderAPI.getInstance().getName(uuid);
                }
            } else {
                if (NickAPI.getInstance().isNicked(uuid)) {
                    if (uuid.equals(scoreBoard.getUniqueId())) {  //scoreBoard equals p
                        if (PartyAPI.getInstance().getParty(uuid) != null
                                && !PartyAPI.getInstance().getParty(uuid).isPublicParty()) {
                            this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
                            this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                                    + NickAPI.getInstance().getNick(uuid).getName();
                            this.suffix = " §8[§5Party§8]";
                        } else {
                            this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
                            this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                                    + NickAPI.getInstance().getNick(uuid).getName();
                            this.suffix = " §8[§5N§8]";
                        }
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else if (PartyAPI.getInstance().getParty(uuid) != null
                            && PartyAPI.getInstance().getParty(uuid).getPlayers().contains(scoreBoard.getUniqueId())
                            && !PartyAPI.getInstance().getParty(uuid).isPublicParty()) {  //scoreBoard in Party mit p
                        this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
                        this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                                + NickAPI.getInstance().getNick(uuid).getName();
                        this.suffix = " §8[§5Party§8]";
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else if (EnderAPI.getInstance().isInTeam(scoreBoard)) {  //scoreBoard ist im Team
                        this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
                        this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                                + NickAPI.getInstance().getNick(uuid).getName();
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                        this.suffix = " §8[§5N§8]";
                    } else if (NickAPI.getInstance().getNickedParty().get(uuid).contains(scoreBoard)) {  //scoreBoard war Party mit p
                        this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
                        this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                                + NickAPI.getInstance().getNick(uuid).getName();
                        this.entry = NickAPI.getInstance().getNick(uuid).getName();
                    } else {    //Spieler ohne NickSee Rechte
                        if (EnderAPI.getInstance().getTeamDatabase().isPremium(uuid)) {
                            this.rang = "§6Ender §8┃ §6";
                            this.team = PermAPI.getInstance().getRanks().getTeam("Ender") + NickAPI.getInstance().getNick(uuid).getNickedName();
                        } else {
                            this.rang = "§7";
                            this.team = PermAPI.getInstance().getRanks().getTeam("default") + NickAPI.getInstance().getNick(uuid).getNickedName();
                        }
                        this.entry = NickAPI.getInstance().getNick(uuid).getNickedName();
                        this.suffix = "";
                    }
                } else {
                    if (PartyAPI.getInstance().getParty(uuid) != null
                            && PartyAPI.getInstance().getParty(uuid).getPlayers().contains(scoreBoard.getUniqueId())
                            && !PartyAPI.getInstance().getParty(uuid).isPublicParty()) {
                        this.suffix = " §8[§5Party§8]";
                    }
                    this.rang = PermAPI.getInstance().getRanks().getCompletedPrefix(PermAPI.getInstance().getUsers().getRank(uuid));
                    this.team = PermAPI.getInstance().getRanks().getTeam(PermAPI.getInstance().getUsers().getRank(uuid))
                            + EnderAPI.getInstance().getName(uuid);
                    this.entry = EnderAPI.getInstance().getName(uuid);
                }
            }
        }

        if (this.team.length() > 16) {
            this.team = (this.team).substring(0, 16);
        }

        if (EnderAPI.getInstance().getBadlion().contains(uuid)) {
            suffix = " ✔" + suffix;
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public String getLabySuffix() {
        return labySuffix;
    }

    public String getTeam() {
        return team;
    }

    public String getRang() {
        return rang;
    }

    public String getEntry() {
        return entry;
    }
}