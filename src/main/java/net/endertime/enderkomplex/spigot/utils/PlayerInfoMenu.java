package net.endertime.enderkomplex.spigot.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.endertime.enderapi.permission.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.utils.SkullType;
import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.*;
import net.endertime.enderkomplex.bungee.objects.BanInfo;
import net.endertime.enderkomplex.bungee.objects.MuteInfo;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.ServerHandler;
import net.endertime.enderkomplex.spigot.objects.ReportInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class PlayerInfoMenu implements Listener {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void createInventory(Player p, UUID uuid) {
        checkExpire(uuid);
        ArrayList<String> lore = new ArrayList<>();
        Inventory inv = Bukkit.createInventory(null, 9*2, "§7Informationen von §c" + EnderAPI.getInstance().getName(uuid));

        lore.clear();
        lore.add("§0");
        lore.add("§8➟ §7Rang: " + EnderAPI.getInstance().getRang(uuid));
        lore.add("§8➟ §7Onlinezeit: §e" + TimeUnit.HOURS.convert(Database.getTime(uuid), TimeUnit.MILLISECONDS) + "h");
        lore.add("§8➟ §7EnderCoins: §6" + EnderAPI.getInstance().getCoins(uuid));
        lore.add("§8➟ §7GlobalPoints: §9" + EnderAPI.getInstance().getPoints(uuid));
        lore.add("§1");
        lore.add("§7§lBann Info:");
        if(Database.hasActiveBan(uuid)) {
            lore.add("§8× §7Gebannt: §4§lJA §8(§c" + Database.getActiveBanReason(uuid).getTitle() + "§8)");
        } else {
            if(Database.isIpExistAlts(uuid)) {
                if(Database.hasActiveBan(Database.getIPFromAlts(uuid))) {
                    if(Database.isBannumgehung(Database.getIPFromAlts(uuid))) {
                        lore.add("§8× §7Gebannt: §2NEIN §8(§cIP-Sperre§8)");
                    } else if(Database.isAdminban(Database.getIPFromAlts(uuid))) {
                        lore.add("§8× §7Gebannt: §2NEIN §8(§cIP-Sperre§8)");
                    } else {
                        if(Database.hasActiveBan(uuid)) {
                            lore.add("§8× §7Gebannt: §4JA §8(§c§l" + Database.getActiveBanReason(uuid).getTitle() + "§8)");
                        } else {
                            lore.add("§8× §7Gebannt: §2NEIN");
                        }
                    }
                } else {
                    if(Database.hasActiveBan(uuid)) {
                        lore.add("§8× §7Gebannt: §4JA §8(§c" + Database.getActiveBanReason(uuid).getTitle() + "§8)");
                    } else {
                        lore.add("§8× §7Gebannt: §2NEIN");
                    }
                }
            } else {
                lore.add("§8× §7Gebannt: §2NEIN");
            }
        }
        lore.add("§2");
        lore.add("§7§lMute Info:");
        if(Database.hasActiveMute(uuid)) {
            lore.add("§8× §7Gemutet: §4JA §8(§c" + Database.getActiveMuteReason(uuid).getTitle() + "§8)");
        } else {
            lore.add("§8× §7Gemutet: §2NEIN");
        }
        lore.add("§3");
        lore.add("§7§lSonstiges:");
        if(EnderAPI.getInstance().getSettings().getOnline(uuid)) {
            if(EnderAPI.getInstance().getVersion(uuid) != null) {
                lore.add("§8× §7Online auf: §3" + EnderAPI.getInstance().getSettings().getServer(uuid) + " §8(§c"
                        + EnderAPI.getInstance().getVersion(uuid).getVersionName() + "§8)");
            } else {
                lore.add("§8× §7Online auf: §3" + EnderAPI.getInstance().getSettings().getServer(uuid));
            }
        } else {
            lore.add("§8× §7Zuletzt online: §e" + sdf.format(Database.getLastSeen(uuid)) + " §7auf §3"
                    + EnderAPI.getInstance().getSettings().getServer(uuid));
        }
        lore.add("§8× §7Unique ID: §e" + uuid.toString());
        lore.add("§4");
        inv.setItem(4, EnderAPI.getInstance().getSkull(uuid).setDisplayName("§6§lQuickinfo").setLore(lore).getItemStack());

        lore.clear();
        lore.add("§0");
        lore.add("§7Banns erhalten: §c" + Database.getBanAmount(uuid) + " §7davon entschuldigt: §a" + Database.getExcusedBanAmount(uuid));
        lore.add("§1");
        if(p.hasPermission("ek.info.seeunbans")) {
            if(PermAPI.getInstance().hasPermission(uuid, "ek.info.displayunbans")
                    && PermAPI.getInstance().hasPermission(uuid, "ek.commands.ban")) {
                loadAdditionalTextBan(lore, uuid);
            }
        }
        inv.setItem(10, EnderAPI.getInstance().getItem(Material.DIAMOND_SWORD).setDisplayName("§6§lBannarchiv")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setLore(lore).getItemStack());

        lore.clear();
        lore.add("§0");
        lore.add("§7Mutes erhalten: §c" + Database.getMuteAmount(uuid) + " §7davon entschuldigt: §a" + Database.getExcusedMuteAmount(uuid));
        lore.add("§1");
        if(p.hasPermission("ek.info.seeunbans")) {
            if(PermAPI.getInstance().hasPermission(uuid, "ek.info.displayunbans")
                    && PermAPI.getInstance().hasPermission(uuid, "ek.commands.mute")) {
                loadAdditionalTextMute(lore, uuid);
            }
        }
        inv.setItem(11, EnderAPI.getInstance().getItem(Material.BOOK).setDisplayName("§6§lMutearchiv").setLore(lore).getItemStack());

        lore.clear();
        lore.add("§0");
        lore.add("§7Reports erstellt: §c" + Database.getCreatedReportsCount(uuid));
        lore.add("§1");
        if(!PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
            lore.add("§7§lErhaltene Reports:");
            for(ReportReason rr : ReportReason.values()) {
                int reports = Database.getReportsGot(uuid, rr);
                if(reports <= 0) {
                    lore.add("§70 §8➟ §c" + rr.getTitle());
                } else {
                    lore.add("§6" + reports + " §8➟ §c" + rr.getTitle());
                }
            }
            lore.add("§e");
            int reports = Database.getReportAmount(uuid);
            int reportsac = Database.getReportAmountAntiCheat(uuid);
            lore.add("§7Total: §6" + reports);
            lore.add("§8× §7Vom AntiCheat: §6" + reportsac);
            lore.add("§8× §7Von Spielern: §6" + (reports - reportsac));
            lore.add("§b");
        }
        if(p.hasPermission("ek.info.seeunbans")) {
            if(PermAPI.getInstance().hasPermission(uuid, "ek.info.displayunbans")
                    && PermAPI.getInstance().hasPermission(uuid, "ek.commands.reports")) {
                loadAdditionalTextReports(lore, uuid);
            }
        }
        inv.setItem(15, EnderAPI.getInstance().getItem(Material.NAME_TAG).setDisplayName("§6§lReportarchiv").setLore(lore).getItemStack());

        lore.clear();
        lore.add("§0");
        if(Database.isIpExistAlts(uuid)) {
            lore.add("§7Alt Accounts§8: §c" + Database.getAltAmount(Database.getIP(uuid)));
        } else {
            lore.add("§7Alt Accounts§8: §c0");
        }
        lore.add("§c");
        inv.setItem(16, EnderAPI.getInstance().getItem(Material.SKULL_ITEM, 1, 3)
                .setDisplayName("§6§lAlt Accounts").setLore(lore).getItemStack());

        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null) {
                inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
            }
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().startsWith("§7Informationen von §c")) {
                e.setCancelled(true);
                UUID uuid = UUID.fromString(e.getInventory().getItem(4).getItemMeta()
                        .getLore().get(14).replaceAll("§8× §7Unique ID: §e", ""));
                if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                    if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§6§lAlt Accounts")) {
                        if(Database.isIpExistAlts(uuid)) {
                            AltsMenu.createInventory(p, uuid);
                        }
                    }
                } else if(e.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
                    if(!PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
                        if(Database.isEverBeenBanned(uuid)) {
                            openBansArchive(p, uuid, 1);
                        }
                    } else {
                        if(p.hasPermission("ek.info.teamhistory")) {
                            openBansArchive(p, uuid, 1);
                        } else {
                            if(p.hasPermission("ek.commands.ban")) {
                                if(uuid.toString().equals(p.getUniqueId().toString())) {
                                    openBansArchive(p, uuid, 1);
                                }
                            }
                        }
                    }
                } else if(e.getCurrentItem().getType().equals(Material.BOOK)) {
                    if(!PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
                        if(Database.isEverBeenMuted(uuid)) {
                            openMutesArchive(p, uuid, 1);
                        }
                    } else {
                        if(p.hasPermission("ek.info.teamhistory")) {
                            openMutesArchive(p, uuid, 1);
                        } else {
                            if(p.hasPermission("ek.commands.mute")) {
                                if(uuid.toString().equals(p.getUniqueId().toString())) {
                                    openMutesArchive(p, uuid, 1);
                                }
                            }
                        }
                    }
                } else if(e.getCurrentItem().getType().equals(Material.NAME_TAG)) {
                    if(!PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
                        if(Database.hasEverMadeReport(uuid)) {
                            openReportsArchive(p, uuid, 1);
                        }
                    } else {
                        if(p.hasPermission("ek.info.teamhistory")) {
                            openReportsArchive(p, uuid, 1);
                        } else {
                            if(p.hasPermission("ek.info.reports")) {
                                if(uuid.toString().equals(p.getUniqueId().toString())) {
                                    openReportsArchive(p, uuid, 1);
                                }
                            }
                        }
                    }
                }
            } else if(e.getInventory().getTitle().startsWith("§6Bannarchiv von §c")) {
                e.setCancelled(true);
                if(e.isLeftClick()) {
                    UUID open = EnderAPI.getInstance().getUUID(e.getInventory().getTitle().replaceAll("§6Bannarchiv von §c", ""));
                    int currentsite = Integer.valueOf(e.getInventory().getItem(49).getItemMeta()
                            .getDisplayName().replaceAll("§7Aktuell auf Seite §c", "").substring(0, 1));
                    if(e.getRawSlot() <= 35) {
                        String banid = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
                        UUID uuid = Database.getBanedPlayer(banid);
                        if(!PermAPI.getInstance().hasPermission(open, "teamserver.join")) {
                            if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§6#EB")) {
                                createInventory(p, uuid);
                            }
                        }
                    } else  {
                        if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                            if(e.getRawSlot() == 46) {
                                //nach ganz oben
                                openBansArchive(p, open, 1);
                            } else if(e.getRawSlot() == 47) {
                                //nach oben
                                openBansArchive(p, open, currentsite -1);
                            } else if(e.getRawSlot() == 51) {
                                //nach unten
                                openBansArchive(p, open, currentsite +1);
                            } else if(e.getRawSlot() == 52) {
                                //nach ganz unten
                                openBansArchive(p, open, -1);
                            }
                        }
                    }
                }
            } else if(e.getInventory().getTitle().startsWith("§6Mutearchiv von §c")) {
                e.setCancelled(true);
                if(e.isRightClick()) {
                    if(e.getCurrentItem().getItemMeta().getLore().get(1).contains("Chatlog")) {
                        String chatlogid = e.getCurrentItem().getItemMeta().getLore().get(1).replaceAll("§7Chatlog ID: §5", "");
                        p.sendMessage("§8§l┃ §5EnderTime §8» §7" + ProxyData.ChatlogLink + chatlogid);
                    }
                } else if(e.isLeftClick()) {
                    UUID open = EnderAPI.getInstance().getUUID(e.getInventory().getTitle().replaceAll("§6Mutearchiv von §c", ""));
                    int currentsite = Integer.valueOf(e.getInventory().getItem(49).getItemMeta()
                            .getDisplayName().replaceAll("§7Aktuell auf Seite §c", "").substring(0, 1));
                    if(e.getRawSlot() <= 35) {
                        String muteid = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
                        UUID uuid = Database.getMutedPlayer(muteid);
                        if(!PermAPI.getInstance().hasPermission(open, "teamserver.join")) {
                            if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§6#EM")) {
                                createInventory(p, uuid);
                            }
                        }
                    } else {
                        if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                            if(e.getRawSlot() == 46) {
                                //nach ganz oben
                                openMutesArchive(p, open, 1);
                            } else if(e.getRawSlot() == 47) {
                                //nach oben
                                openMutesArchive(p, open, currentsite -1);
                            } else if(e.getRawSlot() == 51) {
                                //nach unten
                                openMutesArchive(p, open, currentsite +1);
                            } else if(e.getRawSlot() == 52) {
                                //nach ganz unten
                                openMutesArchive(p, open, -1);
                            }
                        }
                    }
                }
            } else if(e.getInventory().getTitle().startsWith("§6Reportarchiv von §c")) {
                e.setCancelled(true);
                if(e.isRightClick()) {
                    if(e.getCurrentItem().getItemMeta().getLore().get(1).contains("Chatlog")) {
                        String chatlogid = e.getCurrentItem().getItemMeta().getLore().get(1).replaceAll("§7Chatlog ID: §5", "");
                        p.sendMessage("§8§l┃ §5EnderTime §8» §7" + ProxyData.ChatlogLink + chatlogid);
                    }
                } else if(e.isLeftClick()) {
                    int currentsite = Integer.valueOf(e.getInventory().getItem(49).getItemMeta()
                            .getDisplayName().replaceAll("§7Aktuell auf Seite §c", "").substring(0, 1));
                    UUID open = EnderAPI.getInstance().getUUID(e.getInventory().getTitle().replaceAll("§6Reportarchiv von §c", ""));
                    if(e.getRawSlot() <= 35) {
                        String reportid = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
                        UUID uuid = Database.getReportedPlayer(reportid);
                        if(!PermAPI.getInstance().hasPermission(open, "teamserver.join")) {
                            if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§6#ER")) {
                                createInventory(p, uuid);
                            }
                        }
                    } else {
                        if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                            if(e.getRawSlot() == 46) {
                                //nach ganz oben
                                openReportsArchive(p, open, 1);
                            } else if(e.getRawSlot() == 47) {
                                //nach oben
                                openReportsArchive(p, open, currentsite -1);
                            } else if(e.getRawSlot() == 51) {
                                //nach unten
                                openReportsArchive(p, open, currentsite +1);
                            } else if(e.getRawSlot() == 52) {
                                //nach ganz unten
                                openReportsArchive(p, open, -1);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void checkExpire(UUID uuid) {
        if(Database.hasActiveBan(uuid))  {
            long duration = Database.getActiveBanDuration(uuid);
            if(duration > 0) {
                if((Database.getActiveBanTimestamp(uuid) + duration) < System.currentTimeMillis()) {
                    Database.unbanPlayerExpired(uuid);
                }
            }
        }
        if(Database.hasActiveMute(uuid)) {
            long duration = Database.getActiveMuteDuration(uuid);
            if(duration > 0) {
                if((Database.getActiveMuteTimestamp(uuid) + duration) < System.currentTimeMillis()) {
                    Database.unmutePlayerExpired(uuid);
                }
            }
        }
    }

    private static void loadAdditionalTextBan(ArrayList<String> lore, UUID uuid) {
        lore.add("§7§lAusgeführte Banns:");
        for(BanReason br : BanReason.values()) {
            if(PermAPI.getInstance().hasPermission(uuid, br.getPermission())) {
                int bans = Database.getBanedAmount(uuid, br);
                if(bans <= 0) {
                    lore.add("§70 §8➟ §c" + br.getTitle());
                } else {
                    lore.add("§6" + bans + " §8➟ §c" + br.getTitle());
                }
            }
        }
        lore.add("§2");
        int baned = Database.getBanedAmount(uuid);
        int reportsbaned = Database.getReportsBaned(uuid);
        lore.add("§7Total: §6" + baned);
        lore.add("§8× §7Reports: §6" + reportsbaned);
        lore.add("§8× §7Manuell: §6" + (baned - reportsbaned));
        lore.add("§3");
        lore.add("§7§lAusgeführte Entbannungen:");
        for(UnbanReason ubr : UnbanReason.values()) {
            if(ubr.equals(UnbanReason.EXPIRED)) continue;
            int unbans = Database.getUnbanedAmount(uuid, ubr);
            if(unbans <= 0) {
                lore.add("§70 §8➟ §c" + ubr.getTitle());
            } else {
                lore.add("§6" + unbans + " §8➟ §c" + ubr.getTitle());
            }
        }
        lore.add("§4");
        lore.add("§7Total: §6" + Database.getUnbanedAmount(uuid));
        lore.add("§5");
    }

    private static void loadAdditionalTextMute(ArrayList<String> lore, UUID uuid) {
        lore.add("§7§lAusgeführte Mutes:");
        for(MuteReason mr : MuteReason.values()) {
            int mutes = Database.getMutedAmount(uuid, mr);
            if(mutes <= 0) {
                lore.add("§70 §8➟ §c" + mr.getTitle());
            } else {
                lore.add("§6" + mutes + " §8➟ §c" + mr.getTitle());
            }
        }
        lore.add("§2");
        int muted = Database.getMutedAmount(uuid);
        int chatfilter = Database.getMutedChatfiltersCount(uuid);
        lore.add("§7Total: §6" + muted);
        lore.add("§8× §7Chatfilter: §6" + chatfilter);
        lore.add("§8× §7Manuell: §6" + (muted - chatfilter));
        lore.add("§3");
        lore.add("§7§lAusgeführte Entmutungen:");
        for(UnmuteReason umr : UnmuteReason.values()) {
            if(umr.equals(UnmuteReason.EXPIRED)) continue;
            int unmutes = Database.getUnmutedAmount(uuid, umr);
            if(unmutes <= 0) {
                lore.add("§70 §8➟ §c" + umr.getTitle());
            } else {
                lore.add("§6" + unmutes + " §8➟ §c" + umr.getTitle());
            }
        }
        lore.add("§4");
        lore.add("§7Total: §6" + Database.getUnmutedAmount(uuid));
        lore.add("§5");
    }

    private static void loadAdditionalTextReports(ArrayList<String> lore, UUID uuid) {
        lore.add("§7§lBearbeitete Reports:");
        for(ReportReason rr : ReportReason.values()) {
            int finished = Database.getReportsFinished(uuid, rr);
            if(finished <= 0) {
                lore.add("§70 §8➟ §c" + rr.getTitle());
            } else {
                lore.add("§6" + finished + " §8➟ §c" + rr.getTitle());
            }
        }
        lore.add("§2");
        lore.add("§7Total: §6" + Database.getReportsFinished(uuid));
        lore.add("§8× §7Bestraft: §6" + Database.getReportsBaned(uuid));
        lore.add("§8× §7Geschlossen: §6" + Database.getReportsClosed(uuid));
        lore.add("§8× §7Zurückgestellt: §6" + Database.getReportsRejected(uuid));
        lore.add("§3");
    }

    private static void openBansArchive(Player p, UUID uuid, int site) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "§6Bannarchiv von §c" + EnderAPI.getInstance().getName(uuid));
        ArrayList<BanInfo> bans;
        if(PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
            bans = Database.getAllHandledBans(uuid);
        } else {
            bans = Database.getAllBans(uuid);
        }
        ArrayList<String> lore = new ArrayList<>();
        int maxsites = (int)((double)Math.ceil(bans.size()/35d));
        if(site > maxsites) return;
        if(site == -1) site = maxsites;

        HashMap<Integer, ArrayList<BanInfo>> sites = new HashMap<>();
        for(int a = 0; a < maxsites; a++) {
            sites.put(a, new ArrayList<BanInfo>());
            int h = 35;
            if(a > 0) {
                h = (a+1)*35;
            }
            for(int u = (a*35); u <= h; u++) {
                if(u < bans.size()) {
                    sites.get(a).add(bans.get(u));
                } else {
                    break;
                }
            }
        }

        for(BanInfo bi : sites.get(site -1)) {
            lore.clear();
            lore.add("§0");
            lore.add("§7Grund: §c" + bi.getBanReason().getTitle());
            lore.add("§7Name: §3" + EnderAPI.getInstance().getName(bi.getBanedUUID()));
            lore.add("§7Anzahl: §c" + bi.getBanAmount());
            lore.add("§7Bannzeitpunkt: §e" + sdf.format(new Date(bi.getBanTimestamp())));
            lore.add("§7Banndauer: §c" + ProxyHandler.getBanEnd(bi.getBanTimestamp(), bi.getBanDuration()));
            if(bi.getUnbanReason() != null) {
                if(bi.getUnbanReason().equals(UnbanReason.EXPIRED)) {
                    lore.add("§7Unbangrund: §c" + bi.getUnbanReason().getTitle());
                } else {
                    lore.add("§7Unbangrund: §c" + bi.getUnbanReason().getTitle());
                    lore.add("§7Unban von: §3" + EnderAPI.getInstance().getName(bi.getUnbanUUID()));
                    lore.add("§7Unban Zeitpunkt: §e" + sdf.format(new Date(bi.getUnbanTimestamp())));
                }
            }
            lore.add("§1");
            if(bi.isActive()) {
                inv.addItem(EnderAPI.getInstance().getItem(ServerHandler.getMaterial(bi.getBanReason())).setDisplayName("§6" + bi.getID()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setHideEnchantments().setLore(lore).getItemStack());
            } else {
                inv.addItem(EnderAPI.getInstance().getItem(ServerHandler.getMaterial(bi.getBanReason())).setDisplayName("§6" + bi.getID()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setLore(lore).getItemStack());
            }
        }

        for(int i = 36; i < inv.getSize(); i++) {
            inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        }

        inv.setItem(46, EnderAPI.getInstance().getSkull(SkullType.STONE_ARROW_UP).setDisplayName("§6Nach ganz oben").getItemStack());
        inv.setItem(47, EnderAPI.getInstance().getSkull(SkullType.MHF_ArrowUp).setDisplayName("§6Nach oben").getItemStack());

        inv.setItem(49, EnderAPI.getInstance().getItem(Material.EMPTY_MAP).setDisplayName("§7Aktuell auf Seite §c" + site + " §7von §6" + maxsites).getItemStack());

        inv.setItem(51, EnderAPI.getInstance().getSkull(SkullType.MHF_ArrowDown).setDisplayName("§6Nach unten").getItemStack());
        inv.setItem(52, EnderAPI.getInstance().getSkull(SkullType.STONE_ARROW_DOWN).setDisplayName("§6Nach ganz unten").getItemStack());

        p.openInventory(inv);
    }

    private static void openMutesArchive(Player p, UUID uuid, int site) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "§6Mutearchiv von §c" + EnderAPI.getInstance().getName(uuid));
        ArrayList<MuteInfo> mutes;
        if(PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
            mutes = Database.getAllHandledMutes(uuid);
        } else {
            mutes = Database.getAllMutes(uuid);
        }
        ArrayList<String> lore = new ArrayList<>();
        int maxsites = (int)((double)Math.ceil(mutes.size()/36d));
        if(site > maxsites) return;
        if(site == -1) site = maxsites;

        HashMap<Integer, ArrayList<MuteInfo>> sites = new HashMap<>();
        for(int a = 0; a <= maxsites; a++) {
            sites.put(a, new ArrayList<MuteInfo>());
            int h = 35;
            if(a > 0) {
                h = (a+1)*35;
            }
            for(int u = (a*35); u <= h; u++) {
                if(u < mutes.size()) {
                    sites.get(a).add(mutes.get(u));
                } else {
                    break;
                }
            }
        }

        for(MuteInfo mi : sites.get(site -1)) {
            lore.clear();
            if(!mi.getChatlogID().equals("null")) {
                lore.add("§0");
                lore.add("§7Chatlog ID: §5" + mi.getChatlogID());
            }
            lore.add("§1");
            lore.add("§7Grund: §c" + mi.getMuteReason().getTitle());
            lore.add("§7Name: §3" + EnderAPI.getInstance().getName(mi.getMutedUUID()));
            lore.add("§7Anzahl: §c" + mi.getMuteAmount());
            lore.add("§7Mutezeitpunkt: §e" + sdf.format(new Date(mi.getMuteTimestamp())));
            if(mi.isActive()) {
                lore.add("§7Mutedauer: §c" + ProxyHandler.getBanEnd(mi.getMuteTimestamp(), mi.getMuteDuration()));
            }
            if(mi.getUnmuteReason() != null) {
                if(mi.getUnmuteReason().equals(UnmuteReason.EXPIRED)) {
                    lore.add("§7Unbangrund: §c" + mi.getUnmuteReason().getTitle());
                } else {
                    lore.add("§7Unmutegrund: §c" + mi.getUnmuteReason().getTitle());
                    lore.add("§7Unmute von: §3" + EnderAPI.getInstance().getName(mi.getUnmuteUUID()));
                    lore.add("§7Unmute Zeitpunkt: §e" + sdf.format(new Date(mi.getUnmuteTimestamp())));
                }
            }
            lore.add("§2");
            if(mi.isActive()) {
                inv.addItem(EnderAPI.getInstance().getItem(ServerHandler.getMaterial(mi.getMuteReason())).setDisplayName("§6" + mi.getID()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setHideEnchantments().setLore(lore).getItemStack());
            } else {
                inv.addItem(EnderAPI.getInstance().getItem(ServerHandler.getMaterial(mi.getMuteReason())).setDisplayName("§6" + mi.getID()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setLore(lore).getItemStack());
            }
        }

        for(int i = 36; i < inv.getSize(); i++) {
            inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        }

        inv.setItem(46, EnderAPI.getInstance().getSkull(SkullType.STONE_ARROW_UP).setDisplayName("§6Nach ganz oben").getItemStack());
        inv.setItem(47, EnderAPI.getInstance().getSkull(SkullType.MHF_ArrowUp).setDisplayName("§6Nach oben").getItemStack());

        inv.setItem(49, EnderAPI.getInstance().getItem(Material.EMPTY_MAP).setDisplayName("§7Aktuell auf Seite §c" + site + " §7von §6" + maxsites).getItemStack());

        inv.setItem(51, EnderAPI.getInstance().getSkull(SkullType.MHF_ArrowDown).setDisplayName("§6Nach unten").getItemStack());
        inv.setItem(52, EnderAPI.getInstance().getSkull(SkullType.STONE_ARROW_DOWN).setDisplayName("§6Nach ganz unten").getItemStack());

        p.openInventory(inv);
    }

    private static void openReportsArchive(Player p, UUID uuid, int site) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "§6Reportarchiv von §c" + EnderAPI.getInstance().getName(uuid));
        ArrayList<ReportInfo> reports;
        if(PermAPI.getInstance().hasPermission(uuid, "teamserver.join")) {
            reports = Database.getAllHandledReports(uuid);
        } else {
            reports = Database.getAllReports(uuid);
        }
        ArrayList<String> lore = new ArrayList<>();
        int maxsites = (int)((double)Math.ceil(reports.size()/36d));
        if(site > maxsites) return;
        if(site == -1) site = maxsites;

        HashMap<Integer, ArrayList<ReportInfo>> sites = new HashMap<>();
        for(int a = 0; a <= maxsites; a++) {
            sites.put(a, new ArrayList<ReportInfo>());
            int h = 35;
            if(a > 0) {
                h = (a+1)*35;
            }
            for(int u = (a*35); u <= h; u++) {
                if(u < reports.size()) {
                    sites.get(a).add(reports.get(u));
                } else {
                    break;
                }
            }
        }

        for(ReportInfo ri : sites.get(site -1)) {
            lore.clear();
            lore.add("§0");
            if(!ri.getChatlogID().equals("null")) {
                lore.add("§7Chatlog ID: §5" + ri.getChatlogID());
                lore.add("§1");
            }
            lore.add("§7Grund: §c" + ri.getReason().getTitle());
            lore.add("§7Reportet: §3" + EnderAPI.getInstance().getName(ri.getReported()));
            if(ri.getReporter().equals("AntiCheat")) {
                lore.add("§7Ersteller: §3" + ri.getReporter());
            } else {
                lore.add("§7Ersteller: §3" + EnderAPI.getInstance().getName(UUID.fromString(ri.getReporter())));
            }
            lore.add("§7Erstellt am: §e" + sdf.format(new Date(ri.getReportTimestamp())));
            lore.add("§7Status: " + ri.getStatus().getTitle());
            if(ri.getStatus().equals(ReportStatus.FINISHED)) {
                lore.add("§7Bearbeitet von: §3" + EnderAPI.getInstance().getName(ri.getDoneBy()));
                lore.add("§7Bearbeitet am: §e" + sdf.format(new Date(ri.getDoneTimestamp())));
            }
            lore.add("§2");
            int rejected = Database.getRejectAmount(ri.getID());
            if(rejected > 0) {
                lore.add("§7Dieser Report wurde §6" + Database.getRejectAmount(ri.getID()) + "x §7Zurückgestellt");
                lore.add("§3");
            }
            if(ri.getStatus().equals(ReportStatus.OPEN)) {
                inv.addItem(EnderAPI.getInstance().getItem(Material.valueOf(ri.getReason().getMaterial())).setDisplayName("§6" + ri.getID()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setHideEnchantments().setLore(lore).getItemStack());
            } else {
                inv.addItem(EnderAPI.getInstance().getItem(Material.valueOf(ri.getReason().getMaterial())).setDisplayName("§6" + ri.getID()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setLore(lore).getItemStack());
            }
        }

        for(int i = 36; i < inv.getSize(); i++) {
            inv.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15).setDisplayName("§c").getItemStack());
        }

        inv.setItem(46, EnderAPI.getInstance().getSkull(SkullType.STONE_ARROW_UP).setDisplayName("§6Nach ganz oben").getItemStack());
        inv.setItem(47, EnderAPI.getInstance().getSkull(SkullType.MHF_ArrowUp).setDisplayName("§6Nach oben").getItemStack());

        inv.setItem(49, EnderAPI.getInstance().getItem(Material.EMPTY_MAP).setDisplayName("§7Aktuell auf Seite §c" + site + " §7von §6" + maxsites).getItemStack());

        inv.setItem(51, EnderAPI.getInstance().getSkull(SkullType.MHF_ArrowDown).setDisplayName("§6Nach unten").getItemStack());
        inv.setItem(52, EnderAPI.getInstance().getSkull(SkullType.STONE_ARROW_DOWN).setDisplayName("§6Nach ganz unten").getItemStack());

        p.openInventory(inv);
    }

}
