package net.endertime.enderkomplex.spigot.utils;

import java.util.HashMap;
import java.util.UUID;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.bungee.enums.UnbanReason;
import net.endertime.enderkomplex.bungee.enums.UnmuteReason;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class EditMenu implements Listener {

    public static HashMap<Player, UUID> proceed = new HashMap<>();

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().equals("§c§lBann bearbeiten")) {
                e.setCancelled(true);
                UUID uuid = UUID.fromString(e.getInventory().getItem(2).getItemMeta()
                        .getLore().get(6).replaceAll("§8× §7Unique ID: §e", ""));
                if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                    PlayerInfoMenu.createInventory(p, uuid);
                } else if(e.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
                    if(!e.getCurrentItem().getItemMeta().hasEnchants()) {
                        openApproveGUI_BAN(p);
                        proceed.put(p, uuid);
                    }
                } else if(e.getCurrentItem().getType().equals(Material.BOOK_AND_QUILL)) {
                    openAnvilGUI_BAN(p, uuid);
                }
            } else if(e.getInventory().getTitle().equals("§c§lMute bearbeiten")) {
                e.setCancelled(true);
                UUID uuid = UUID.fromString(e.getInventory().getItem(2).getItemMeta()
                        .getLore().get(6).replaceAll("§8× §7Unique ID: §e", ""));
                if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                    PlayerInfoMenu.createInventory(p, uuid);
                } else if(e.getCurrentItem().getType().equals(Material.BOOK)) {
                    if(!e.getCurrentItem().getItemMeta().hasEnchants()) {
                        openApproveGUI_MUTE(p);
                        proceed.put(p, uuid);
                    }
                } else if(e.getCurrentItem().getType().equals(Material.BOOK_AND_QUILL)) {
                    openAnvilGUI_MUTE(p, uuid);
                }
            } else if(e.getInventory().getTitle().equals("§6§lMutezeit wirklich verkürzen?")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
                    if(e.getCurrentItem().getDurability() == 5) {
                        UUID uuid = proceed.get(p);
                        if(Database.getActiveMuteReason(uuid).getReducedDuration(Database.getActiveMuteDuration(uuid)) != 0l) {
                            Database.updateActiveMuteDuration(uuid,
                                    Database.getActiveMuteReason(uuid).getReducedDuration(Database.getActiveMuteDuration(uuid)));
                        } else {
                            Database.unmutePlayer(uuid, UnmuteReason.UNMUTE_REQUEST, p.getUniqueId());
                        }
                        EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                        EnderAPI.getInstance().sendActionBar(p, "§7Die Mutezeit wurde §aerfolgreich §7verkürzt§8!");
                        Database.updateActiveMuteReducer(uuid, p.getUniqueId());
                        Database.updateActiveMuteReduceTimestamp(uuid);
                        proceed.remove(p);
                        p.closeInventory();
                    } else if(e.getCurrentItem().getDurability() == 14) {
                        p.closeInventory();
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        EnderAPI.getInstance().sendActionBar(p, "§cDer Vorgang wurde abgebrochen§8!");
                    }
                }
            } else if(e.getInventory().getTitle().equals("§6§lBannzeit wirklich verkürzen?")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
                    if(e.getCurrentItem().getDurability() == 5) {
                        UUID uuid = proceed.get(p);
                        if(Database.getActiveMuteReason(uuid).getReducedDuration(Database.getActiveMuteDuration(uuid)) != 0l) {
                            Database.updateActiveBanDuration(uuid,
                                    Database.getActiveBanReason(uuid).getReducedDuration(Database.getActiveBanDuration(uuid)));
                        } else {
                            Database.unbanPlayer(uuid, UnbanReason.UNBAN_REQUEST, p.getUniqueId());
                        }
                        EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                        EnderAPI.getInstance().sendActionBar(p, "§7Die Bannzeit wurde §aerfolgreich §7verkürzt§8!");
                        Database.updateActiveBanReducer(uuid, p.getUniqueId());
                        Database.updateActiveBanReduceTimestamp(uuid);
                        proceed.remove(p);
                        p.closeInventory();
                    } else if(e.getCurrentItem().getDurability() == 14) {
                        p.closeInventory();
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        EnderAPI.getInstance().sendActionBar(p, "§cDer Vorgang wurde abgebrochen§8!");
                    }
                }
            }
        }
    }

    private static void openApproveGUI_MUTE(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6§lMutezeit wirklich verkürzen?");

        inv.setItem(0, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        inv.setItem(1, EnderAPI.getInstance().getItem(Material.STAINED_CLAY, 1, 5).setDisplayName("§2§lJA").getItemStack());
        inv.setItem(2, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        inv.setItem(3, EnderAPI.getInstance().getItem(Material.STAINED_CLAY, 1, 14).setDisplayName("§4§lNEIN").getItemStack());
        inv.setItem(4, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());

        p.openInventory(inv);
    }

    private static void openApproveGUI_BAN(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6§lBannzeit wirklich verkürzen?");

        inv.setItem(0, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        inv.setItem(1, EnderAPI.getInstance().getItem(Material.STAINED_CLAY, 1, 5).setDisplayName("§2§lJA").getItemStack());
        inv.setItem(2, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        inv.setItem(3, EnderAPI.getInstance().getItem(Material.STAINED_CLAY, 1, 14).setDisplayName("§4§lNEIN").getItemStack());
        inv.setItem(4, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());

        p.openInventory(inv);
    }

    private static void openAnvilGUI_MUTE(Player p, UUID uuid) {
        AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

            @Override
            public void onAnvilClick(AnvilGUI.AnvilClickEvent e) {
                if (e.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
                    if (!Database.isChatlogExist(e.getName())) {
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        EnderAPI.getInstance().sendActionBar(p, "§cDiese Chatlog ID existiert nicht§8!");
                        e.setWillClose(true);
                        e.setWillDestroy(true);
                    } else {
                        e.setWillClose(true);
                        e.setWillDestroy(true);
                        EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                        EnderAPI.getInstance().sendActionBar(p, "§7Die Chatlog ID wurde §aerfolgreich §7überschrieben§8!");
                        Database.updateActiveMuteChatlogID(uuid, e.getName());
                    }
                }
            }
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, EnderAPI.getInstance().getItem(Material.PAPER).setDisplayName("ChatlogID").getItemStack());
        gui.open();
    }

    private static void openAnvilGUI_BAN(Player p, UUID uuid) {
        AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

            @Override
            public void onAnvilClick(AnvilGUI.AnvilClickEvent e) {
                if (e.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
                    if (!Database.isChatlogExist(e.getName())) {
                        EnderAPI.getInstance().playSound(p, Sound.ITEM_SHIELD_BREAK);
                        EnderAPI.getInstance().sendActionBar(p, "§cDiese Replay ID existiert nicht§8!");
                        e.setWillClose(true);
                        e.setWillDestroy(true);
                    } else {
                        e.setWillClose(true);
                        e.setWillDestroy(true);
                        EnderAPI.getInstance().playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                        EnderAPI.getInstance().sendActionBar(p, "§7Die Replay ID wurde §aerfolgreich §7überschrieben§8!");
                        Database.updateActiveBanReplayID(uuid, e.getName());
                    }
                }
            }
        });

        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, EnderAPI.getInstance().getItem(Material.PAPER).setDisplayName("ReplayID").getItemStack());
        gui.open();
    }

}
