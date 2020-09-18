package net.endertime.enderapi.spigot.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.utils.Nick;
import net.endertime.enderapi.spigot.utils.PlayerParty;
import net.endertime.enderapi.spigot.utils.ScoreBoardPrefix;
import org.bukkit.entity.Player;

import java.util.*;

public class NickAPI {

    private static NickAPI instance = new NickAPI();

    public static NickAPI getInstance() {
        return instance;
    }

    private Map<Player, Nick> nickedPlayer = new HashMap<>();
    private Map<UUID, List<Player>> nickedParty = new HashMap<>();
    private int nicks;

    public boolean isNicked(Player p) {
        return getNickedPlayer().containsKey(p);
    }

    public boolean isNicked(UUID uuid) {
        for (Player nicked : getNickedPlayer().keySet()) {
            if (nicked.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNickedPerNickedUUID (UUID uuid) {
        for (Nick nick : getNickedPlayer().values()) {
            if (nick.getNickedProfile().getId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Nick getNick (Player player) {
        if (getNickedPlayer().containsKey(player)) {
            return getNickedPlayer().get(player);
        }
        return null;
    }

    public Nick getNick (UUID uuid) {
        for (Player player : getNickedPlayer().keySet()) {
            if (uuid.equals(player.getUniqueId()))
                return getNick(player);
        }
        return null;
    }

    public Nick getNickPerNickedUUID (UUID uuid) {
        for (Nick nick : getNickedPlayer().values()) {
            if (uuid.equals(nick.getNickedProfile().getId()))
                return nick;
        }
        return null;
    }

    public boolean couldSee (Player nicked, Player player) {
        if (!nicked.getUniqueId().equals(player.getUniqueId())) {

            if (!EnderAPI.getInstance().isInTeam(player)) {
                if (!getNickedParty().get(nicked.getUniqueId()).contains(player)) {
                    if (PartyAPI.getInstance().getParty(nicked.getUniqueId()) != null) {
                        PlayerParty playerParty = PartyAPI.getInstance().getParty(nicked.getUniqueId());
                        if (!playerParty.getPlayers().contains(player.getUniqueId()) || playerParty.isPublicParty()) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return  false;
    }

    public boolean isNickAvailable() {
        if (!Wrapper.getInstance().getServiceId().getName().contains("Lobby")
                && !Wrapper.getInstance().getServiceId().getName().startsWith("Vorbau")
                && !Wrapper.getInstance().getServiceId().getName().startsWith("Terra")
                && !Wrapper.getInstance().getServiceId().getName().startsWith("Event")
                && !Wrapper.getInstance().getServiceId().getName().startsWith("Build")
                && !Wrapper.getInstance().getServiceId().getName().startsWith("Content")
                && !Wrapper.getInstance().getServiceId().getName().contains("Dev"))  {
            return true;
        }
        return false;
    }

    public Map<Player, Nick> getNickedPlayer() {
        return nickedPlayer;
    }

    public Map<UUID, List<Player>> getNickedParty() {
        return nickedParty;
    }

    public void registerNickEvent() {
        PacketType[] packets = {PacketType.Play.Server.SCOREBOARD_TEAM, PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN};

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(EnderAPI.getInstance().getPlugin(),
                ListenerPriority.HIGHEST, packets) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
                    StructureModifier<List<PlayerInfoData>> infoDataWrapper = event.getPacket().getPlayerInfoDataLists();
                    if (infoDataWrapper.size() == 0) {
                        return;
                    }

                    List<PlayerInfoData> infoData = infoDataWrapper.getValues().get(0);
                    if (infoData == null || infoData.size() == 0) {
                        return;
                    }

                    PlayerInfoData oldPlayerInfoData = infoData.get(0);
                    WrappedGameProfile oldWrappedGameProfile = oldPlayerInfoData.getProfile();
                    Multimap<String, WrappedSignedProperty> oldProperties = oldWrappedGameProfile.getProperties();
                    Collection<WrappedSignedProperty> oldWrappedSignedPropertyCollection = oldProperties.get("textures");
                    if(oldWrappedSignedPropertyCollection == null || oldWrappedSignedPropertyCollection.size() == 0){
                        return;
                    }

                    UUID oldUUID = oldWrappedGameProfile.getUUID();

                    if (isNicked(oldUUID)) {
                        if (event.getPlayer().getUniqueId().equals(oldUUID)) {
                            GameProfile newGameProfile = getNick(oldUUID).getProfile();

                            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                            WrappedGameProfile newWrappedGameProfile = WrappedGameProfile.fromHandle(newGameProfile);
                            PlayerInfoData newPlayerInfoData = new PlayerInfoData(newWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                    oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + scoreBoardPrefix.getSuffix()));
                            infoData.set(0, newPlayerInfoData);
                        } else if (EnderAPI.getInstance().isInTeam(event.getPlayer())) {
                            GameProfile newGameProfile = getNick(oldUUID).getProfile();

                            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                            WrappedGameProfile newWrappedGameProfile = WrappedGameProfile.fromHandle(newGameProfile);
                            PlayerInfoData newPlayerInfoData = new PlayerInfoData(newWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                    oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + scoreBoardPrefix.getSuffix()));
                            infoData.set(0, newPlayerInfoData);
                        } else if (getNickedParty().get(oldUUID).contains(event.getPlayer())) {
                            GameProfile newGameProfile = getNick(oldUUID).getProfile();

                            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                            WrappedGameProfile newWrappedGameProfile = WrappedGameProfile.fromHandle(newGameProfile);
                            PlayerInfoData newPlayerInfoData = new PlayerInfoData(newWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                    oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + scoreBoardPrefix.getSuffix()));
                            infoData.set(0, newPlayerInfoData);
                        } else if (PartyAPI.getInstance().getParty(oldUUID) != null
                                && PartyAPI.getInstance().getParty(oldUUID).getPlayers().contains(event.getPlayer().getUniqueId())) {
                            if (!PartyAPI.getInstance().getParty(oldUUID).isPublicParty()) {
                                GameProfile newGameProfile = getNick(oldUUID).getProfile();

                                ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                                WrappedGameProfile newWrappedGameProfile = WrappedGameProfile.fromHandle(newGameProfile);
                                PlayerInfoData newPlayerInfoData = new PlayerInfoData(newWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                        oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                        + scoreBoardPrefix.getSuffix()));
                                infoData.set(0, newPlayerInfoData);
                            } else {
                                GameProfile newGameProfile = getNick(oldUUID).getNickedProfile();

                                ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                                WrappedGameProfile newWrappedGameProfile = WrappedGameProfile.fromHandle(newGameProfile);
                                PlayerInfoData newPlayerInfoData = new PlayerInfoData(newWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                        oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                        + scoreBoardPrefix.getSuffix()));
                                infoData.set(0, newPlayerInfoData);
                            }
                        } else {
                            GameProfile newGameProfile = getNick(oldUUID).getNickedProfile();

                            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                            WrappedGameProfile newWrappedGameProfile = WrappedGameProfile.fromHandle(newGameProfile);
                            PlayerInfoData newPlayerInfoData = new PlayerInfoData(newWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                    oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + scoreBoardPrefix.getSuffix()));
                            infoData.set(0, newPlayerInfoData);
                        }
                    } else {
                        if (isNickedPerNickedUUID(oldUUID)) {
                            Nick nick = getNickPerNickedUUID(oldUUID);

                            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(nick.getUniqueId(), event.getPlayer());

                            PlayerInfoData newPlayerInfoData = new PlayerInfoData(WrappedGameProfile.fromHandle(nick.getNickedProfile()),
                                    oldPlayerInfoData.getLatency(), oldPlayerInfoData.getGameMode(),
                                    WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry() + scoreBoardPrefix.getSuffix()));
                            infoData.set(0, newPlayerInfoData);
                        } else {
                            ScoreBoardPrefix scoreBoardPrefix = new ScoreBoardPrefix(oldUUID, event.getPlayer());

                            PlayerInfoData newPlayerInfoData = new PlayerInfoData(oldWrappedGameProfile, oldPlayerInfoData.getLatency(),
                                    oldPlayerInfoData.getGameMode(), WrappedChatComponent.fromText(scoreBoardPrefix.getRang() + scoreBoardPrefix.getEntry()
                                    + scoreBoardPrefix.getSuffix()));
                            infoData.set(0, newPlayerInfoData);
                        }
                    }
                    infoDataWrapper.write(0, infoData);
                } else if (event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
                    StructureModifier<UUID> uuidWrapper = event.getPacket().getUUIDs();

                    if(uuidWrapper.size() == 0){
                        return;
                    }

                    UUID oldUUID = uuidWrapper.read(0);

                    if (isNicked(oldUUID)) {
                        if (event.getPlayer().getUniqueId().equals(oldUUID)
                                || EnderAPI.getInstance().isInTeam(event.getPlayer())
                                || getNickedParty().get(oldUUID).contains(event.getPlayer())) {
                            uuidWrapper.write(0, oldUUID);
                        } else if (PartyAPI.getInstance().getParty(oldUUID) != null) {
                            if (PartyAPI.getInstance().getParty(oldUUID).getPlayers().contains(event.getPlayer().getUniqueId())) {
                                uuidWrapper.write(0, oldUUID);
                            } else {
                                uuidWrapper.write(0, getGameProfile(oldUUID, event.getPlayer()).getId());
                            }
                        } else {
                            uuidWrapper.write(0, getGameProfile(oldUUID, event.getPlayer()).getId());
                        }
                    }
                } else if (event.getPacketType() == PacketType.Play.Server.SCOREBOARD_TEAM) {
                    StructureModifier<UUID> uuidWrapper = event.getPacket().getUUIDs();
                    if(uuidWrapper.size() == 0){
                        return;
                    }

                    UUID oldUUID = uuidWrapper.read(0);

                    if (isNicked(oldUUID)) {
                        if (event.getPlayer().getUniqueId().equals(oldUUID)
                                || EnderAPI.getInstance().isInTeam(event.getPlayer())
                                || getNickedParty().get(oldUUID).contains(event.getPlayer())) {
                            uuidWrapper.write(0, oldUUID);
                        } else if (PartyAPI.getInstance().getParty(oldUUID) != null) {
                            if (PartyAPI.getInstance().getParty(oldUUID).getPlayers().contains(event.getPlayer().getUniqueId())
                                    && !PartyAPI.getInstance().getParty(oldUUID).isPublicParty()) {
                                uuidWrapper.write(0, oldUUID);
                            } else {
                                uuidWrapper.write(0, getGameProfile(oldUUID, event.getPlayer()).getId());
                            }
                        } else {
                            uuidWrapper.write(0, getGameProfile(oldUUID, event.getPlayer()).getId());
                        }
                    }
                }
            }
        });
    }

    public GameProfile getGameProfile(UUID realUUID, Player player) {
        if(!isNicked(realUUID)){
            //Do not change uuid in packet
            return null;
        }
        if (player.getUniqueId().equals(realUUID)) {
            return getNick(realUUID).getProfile();
        }
        if (EnderAPI.getInstance().isInTeam(player)) {
            //Do not change uuid in packet
            return getNick(realUUID).getProfile();
        } else {
            PlayerParty playerParty = PartyAPI.getInstance().getParty(realUUID);
            if (playerParty != null) {
                if (playerParty.getPlayers().contains(player.getUniqueId())) {
                    //Do not change uuid in packet
                    return getNick(realUUID).getProfile();
                }
            }
            if (getNickedParty().get(realUUID).contains(player))
                return getNick(realUUID).getProfile();
        }

        return getNick(realUUID).getNickedProfile();
    }

    public void setNicks(int nicks) {
        this.nicks = nicks;
    }

    public int getNicks() {
        return nicks;
    }
}
