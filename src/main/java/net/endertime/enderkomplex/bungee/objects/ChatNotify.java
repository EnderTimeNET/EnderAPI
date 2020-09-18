package net.endertime.enderkomplex.bungee.objects;

import java.util.HashMap;
import java.util.UUID;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.ChatType;
import net.endertime.enderkomplex.bungee.enums.MuteReason;
import net.endertime.enderkomplex.bungee.enums.NotifyType;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.endertime.enderkomplex.bungee.utils.InfoCollector;
import net.endertime.enderkomplex.mysql.Database;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatNotify {

    public static HashMap<String, ChatNotify> notifys = new HashMap<>();

    String word, sentence;
    ProxiedPlayer sender;
    ChatType chattype;
    String uuid;

    public ChatNotify(String word, String sentence, ProxiedPlayer sender, ChatType chattype) {
        this.word = word;
        this.sentence = sentence;
        this.sender = sender;
        this.chattype = chattype;
        this.uuid = "#CN" + UUID.randomUUID();
        notifys.put(this.uuid, this);
        String servername = sender.getServer().getInfo().getName();
        BaseComponent[] CC = new ComponentBuilder("§9§lCC")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§cBetroffener Chat wird geleert")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cc " + servername))
                .create();
        BaseComponent[] LV = new ComponentBuilder("§c§lLV")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + sender.getName() + "§7 für §4"
                        + MuteReason.CHAT1.getTitle() + " §7muten")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + this.uuid + " 1"))
                .create();
        BaseComponent[] MV = new ComponentBuilder("§c§lMV")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + sender.getName() + "§7 für §4"
                        + MuteReason.CHAT2.getTitle() + " §7muten")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + this.uuid + " 2"))
                .create();
        BaseComponent[] SV = new ComponentBuilder("§c§lSV")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§3" + sender.getName() + "§7 für §4"
                        + MuteReason.CHAT3.getTitle() + " §7muten")))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + this.uuid + " 3"))
                .create();
        BaseComponent[] wc = new ComponentBuilder("§c" + word)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7" + sentence.replaceAll(word, "§c" + word + "§7"))))
                .create();
        BaseComponent[] server = new ComponentBuilder("§a" + servername)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Betrete den Server §a" + servername)))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jump " + this.sender.getServer().getInfo().getName()))
                .create();
        BaseComponent[] player = new ComponentBuilder("§3" + sender.getName())
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Öffne die PlayerInfo von §3" + sender.getName())))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pi " + sender.getName()))
                .create();

        ProxyServer.getInstance().getPlayers().forEach(all -> {
            if(all.hasPermission("ek.notify.chatfilter")) {
                if(Database.existsInNotifySettings(all.getUniqueId())) {
                    if(Database.getNotifySetting(all.getUniqueId(), NotifyType.CHATFILTER)) {
                        ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                        ComponentBuilder cb = new ComponentBuilder("§8§m     §4§l ⚠ §c§lChatfilter §c§lVerdacht §4§l⚠ §8§m     \n")
                                .append("§7Client§8: ").append(player).append(" §7auf ").event((HoverEvent) null).event((ClickEvent) null)
                                .append(server).append("\n")
                                .append("§7Wort§8: ").event((HoverEvent) null).event((ClickEvent) null).append(wc).append(" §8➟ §6" +
                                        chattype.getTitle() + "\n");
                        if(chattype.equals(ChatType.CLANCHAT) | chattype.equals(ChatType.PARTYCHAT) | chattype.equals(ChatType.PRIVATCHAT)) {
                            cb.append("§7Aktionen§8: ").event((HoverEvent) null).event((ClickEvent) null).append("§8[")
                                    .append(LV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null)
                                    .append(MV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null)
                                    .append(SV).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                        } else {
                            cb.append("§7Aktionen§8: ").event((HoverEvent) null).event((ClickEvent) null).append("§8[")
                                    .append(CC).append("§8] [").event((HoverEvent) null).event((ClickEvent) null)
                                    .append(LV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null).
                                    append(MV).append("§8] [").event((HoverEvent) null).event((ClickEvent) null)
                                    .append(SV).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                        }
                        cb.append("§8§m                                                ");
                        all.sendMessage(cb.create());
                    }
                } else {
                    ProxyHandler.sendPluginMessage(all, PluginMessage.PLAY_NOTIFY_SOUND, null);
                    ComponentBuilder cb = new ComponentBuilder("§8§m     §4§l ⚠ §c§lChatfilter §c§lVerdacht §4§l⚠ §8§m     \n")
                            .append("§7Client§8: ").append(player).append(" §7auf ").append(server).append("\n")
                            .append("§7Wort§8: ").append(wc).append(" §8➟ §6" + chattype.getTitle() + "\n");
                    if(chattype.equals(ChatType.CLANCHAT) | chattype.equals(ChatType.PARTYCHAT) | chattype.equals(ChatType.PRIVATCHAT)) {
                        cb.append("§7Aktionen§8: ").append("§8[").append(LV).append("§8] [").event((HoverEvent) null)
                                .event((ClickEvent) null).append(MV).append("§8] [").event((HoverEvent) null)
                                .event((ClickEvent) null).append(SV).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                    } else {
                        cb.append("§7Aktionen§8: ").append("§8[").append(CC).append("§8] [").event((HoverEvent) null)
                                .event((ClickEvent) null).append(LV).append("§8] [").event((HoverEvent) null)
                                .event((ClickEvent) null).append(MV).append("§8] [").event((HoverEvent) null)
                                .event((ClickEvent) null).append(SV).append("§8]\n").event((HoverEvent) null).event((ClickEvent) null);
                    }
                    cb.append("§8§m                                                ");
                    all.sendMessage(cb.create());
                }
            }
        });
        InfoCollector.chatfilter++;
    }

    public String getWord() {
        return this.word;
    }

    public String getSentence() {
        return this.sentence;
    }

    public ProxiedPlayer getSender() {
        return this.sender;
    }

    public ChatType getChatType() {
        return this.chattype;
    }
}
