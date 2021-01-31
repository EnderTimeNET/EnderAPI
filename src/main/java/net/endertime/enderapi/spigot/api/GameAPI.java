package net.endertime.enderapi.spigot.api;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.endertime.enderapi.spigot.gameapi.MapSetupEvent;
import net.endertime.enderapi.spigot.gameapi.StartGameEvent;
import net.endertime.enderapi.spigot.utils.State;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class GameAPI {

    public static GameAPI instance = new GameAPI();

    public static GameAPI getInstance() {
        return instance;
    }

    private boolean hideSpectatorChat = false; //Is true only Spectator see Chat
    private boolean spectatorDiePrefixChat = false; //Is true the Chat got a X
    private boolean globalChat = false;
    private State state = State.ONLINE;

    public void setState(State state) {
        BukkitCloudNetHelper.setState(State.toString(state));
        Wrapper.getInstance().publishServiceInfoUpdate();
        this.state = state;
    }

    public void setMOTD (String motd) {
        BukkitCloudNetHelper.setMotd(motd);
        Wrapper.getInstance().publishServiceInfoUpdate();
    }

    public void setGlobalChat(boolean globalChat) {
        this.globalChat = globalChat;
    }

    public void setSpectatorDiePrefixChat(boolean spectatorDiePrefixChat) {
        this.spectatorDiePrefixChat = spectatorDiePrefixChat;
    }

    public void setHideSpectatorChat(boolean hideSpectatorChat) {
        this.hideSpectatorChat = hideSpectatorChat;
    }

    public State getState() {
        return state;
    }

    public boolean isGlobalChat() {
        return globalChat;
    }

    public boolean isSpectatorDiePrefixChat() {
        return spectatorDiePrefixChat;
    }

    public boolean isHideSpectatorChat() {
        return hideSpectatorChat;
    }

    public void setSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);

        for (PotionEffect pe : player.getActivePotionEffects())
            player.removePotionEffect(pe.getType());

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        for (int i = 0; i < 9; i++)
            player.getInventory().setItem(i, EnderAPI.getInstance().getItem(Material.BARRIER).setDisplayName("§1").getItemStack());

        for (int i = 9; i < 18; i++)
            player.getInventory().setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());

        player.getInventory().setItem(18, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());
        player.getInventory().setItem(27, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());
        player.getInventory().setItem(26, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());
        player.getInventory().setItem(35, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§1").getItemStack());


        for (Player all : Bukkit.getOnlinePlayers()) {
            if (!all.getGameMode().equals(GameMode.SPECTATOR)) {
                if (!EnderAPI.getInstance().isVanished(all)) {
                    player.getInventory().addItem(EnderAPI.getInstance().getSkull(player, all.getUniqueId())
                            .setLore(Arrays.asList(new String[] {"§7Klicke um dich ", "§7zu teleportieren"})).getItemStack());
                }
            }
        }
    }

    private BukkitTask bukkitTask;
    private int countdown;

    public boolean isCountdown() {
        if (bukkitTask != null) {
            return true;
        }
        return false;
    }

    public void stopCountdown() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }

    public void startCountdown() {
        if (state.equals(State.LOBBY)) {
            countdown = 30;

            bukkitTask = Bukkit.getScheduler().runTaskTimer(EnderAPI.getInstance().getPlugin(), new Runnable() {
                @Override
                public void run() {
                    switch (countdown) {
                        case 30:
                        case 25:
                        case 20:
                        case 15:
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                EnderAPI.getInstance().sendActionBar(all, "§7Das Spiel beginnt in §6" + countdown + " §7Sekunden");
                                EnderAPI.getInstance().playSound(all, Sound.NOTE_PLING, 0F);
                            }
                            break;
                        case 10:
                            Bukkit.getPluginManager().callEvent(new MapSetupEvent());
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                EnderAPI.getInstance().sendActionBar(all, "§7Das Spiel beginnt in §6" + countdown + " §7Sekunden");
                                EnderAPI.getInstance().playSound(all, Sound.NOTE_PLING, 0F);
                            }
                            break;
                        case 1:
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                EnderAPI.getInstance().sendActionBar(all, "§7Das Spiel beginnt in §6" + countdown + " §7Sekunde");
                                EnderAPI.getInstance().playSound(all, Sound.NOTE_PLING, 1F);
                            }
                            break;
                        case 0:
                            Bukkit.getPluginManager().callEvent(new StartGameEvent());
                            bukkitTask.cancel();
                            break;
                    }
                    countdown--;
                }
            }, 20L, 20L);
        } else if (state.equals(State.RESTART)) {
            countdown = 10;

            bukkitTask = Bukkit.getScheduler().runTaskTimer(EnderAPI.getInstance().getPlugin(), new Runnable() {
                @Override
                public void run() {
                    switch (countdown) {
                        case 10:
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                EnderAPI.getInstance().sendActionBar(all, "§7Serverneustart in §6" + countdown + " §7Sekunden");
                                EnderAPI.getInstance().playSound(all, Sound.LAVA_POP);
                            }
                            break;
                        case 1:
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                EnderAPI.getInstance().sendActionBar(all, "§7Serverneustart in §6" + countdown + " §7Sekunde");
                                EnderAPI.getInstance().playSound(all, Sound.LAVA_POP);
                            }
                            break;
                        case 0:
                            EnderAPI.getInstance().closeServer();
                            break;
                    }
                    countdown--;
                }
            }, 20L, 20L);
        }
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getCountdown() {
        return countdown;
    }
}
