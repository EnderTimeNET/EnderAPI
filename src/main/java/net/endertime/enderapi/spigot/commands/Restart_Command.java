package net.endertime.enderapi.spigot.commands;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceLifeCycle;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.ext.syncproxy.configuration.SyncProxyConfiguration;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.utils.Group;
import net.endertime.enderapi.spigot.utils.SkullType;
import net.endertime.enderapi.spigot.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Restart_Command implements CommandExecutor, Listener {

    private final SyncProxyConfiguration syncProxyConfiguration = SyncProxyConfiguration.getConfigurationFromNode();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("restart.use")) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§cRestart§8-§cMenu");

                for (Group group : Group.getGroups()) {
                    if (player.hasPermission(group.getPermission())) {
                        ItemStack itemStack = group.getItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(itemMeta.getDisplayName().split("-")[0]);
                        int count = 0;
                        for (String groupString : group.getGroup()) {
                            count += EnderAPI.getInstance().getOnlineCountByGroup(groupString);
                        }
                        if (group.equals(Group.LOBBY))
                            count -= EnderAPI.getInstance().getOnlineCountByGroup("SilentLobby");
                        itemMeta.setLore(Arrays.asList(new String[]{"", "§7§lOnline:", "§8➟ §c" + count, ""}));
                        itemStack.setItemMeta(itemMeta);
                        inventory.setItem(group.getSlot(), itemStack);
                    }
                }

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (inventory.getItem(i) != null) {
                        if (inventory.getItem(i).getType().equals(Material.AIR)) {
                            inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                    .setDisplayName("§1").getItemStack());
                        }
                    } else {
                        inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                .setDisplayName("§1").getItemStack());
                    }
                }

                player.openInventory(inventory);
            } else {
                player.sendMessage(EnderAPI.getInstance().getNoPerm());
            }
        }
        return false;
    }

    @EventHandler
    public void onClick (InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!e.getAction().equals(InventoryAction.NOTHING)) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (!e.getClickedInventory().equals(player.getInventory())) {
                        if (!e.getCurrentItem().getType().equals(Material.AIR)) {
                            if (e.getClickedInventory().getTitle().equals("§cRestart§8-§cMenu")) {
                                e.setCancelled(true);
                                if (!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                                    Group group = Group.getGroup(e.getSlot());
                                    Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER,
                                            group.getInvTitle());

                                    for (int i = 0; i < 5; i++)
                                    inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                            .setDisplayName("§1").getItemStack());

                                    if (group.equals(Group.PROXY)) {
                                        inventory.setItem(1, EnderAPI.getInstance().getSkull(SkullType.TOPAZ_CHEST).setLore(Arrays.asList(new String[]{}))
                                                .setDisplayName("§c§lGroup").getItemStack());

                                        AtomicBoolean b = new AtomicBoolean(false);


                                        this.syncProxyConfiguration.getLoginConfigurations()
                                                .stream()
                                                .filter(loginConfiguration -> loginConfiguration.getTargetGroup().equalsIgnoreCase("Proxy"))
                                                .findFirst()
                                                .ifPresent(loginConfiguration -> {
                                                    b.set(loginConfiguration.isMaintenance());

                                                    // updating in cluster
                                                    SyncProxyConfiguration.updateSyncProxyConfigurationInNetwork(this.syncProxyConfiguration);
                                                });

                                        if (b.get()) {
                                            inventory.setItem(3, EnderAPI.getInstance().getSkull(SkullType.CONCRETE_RED).setLore(Arrays.asList(new String[]{}))
                                                    .setDisplayName("§c§lWartungsmodus").getItemStack());
                                        } else {
                                            inventory.setItem(3, EnderAPI.getInstance().getSkull(SkullType.CONCRETE_LIME).setLore(Arrays.asList(new String[]{}))
                                                    .setDisplayName("§c§lWartungsmodus").getItemStack());
                                        }
                                    } else {
                                        boolean b = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(group.getGroup().get(0)).isMaintenance();
                                        inventory.setItem(0, EnderAPI.getInstance().getSkull(SkullType.WINRAR).setLore(Arrays.asList(new String[]{}))
                                                .setDisplayName("§c§lServices").getItemStack());
                                        inventory.setItem(2, EnderAPI.getInstance().getSkull(SkullType.TOPAZ_CHEST).setLore(Arrays.asList(new String[]{}))
                                                .setDisplayName("§c§lGroup").getItemStack());

                                        if (b) {
                                            inventory.setItem(4, EnderAPI.getInstance().getSkull(SkullType.CONCRETE_RED).setLore(Arrays.asList(new String[]{}))
                                                    .setDisplayName("§c§lWartungsmodus").getItemStack());
                                        } else {
                                            inventory.setItem(4, EnderAPI.getInstance().getSkull(SkullType.CONCRETE_LIME).setLore(Arrays.asList(new String[]{}))
                                                    .setDisplayName("§c§lWartungsmodus").getItemStack());
                                        }
                                    }

                                    player.openInventory(inventory);
                                }
                            } else if (e.getClickedInventory().getType().equals(InventoryType.HOPPER)) {
                                Group group = Group.getGroup(e.getClickedInventory().getTitle());
                                if (group != null) {
                                    e.setCancelled(true);
                                    if (!e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lWartungsmodus")) {
                                            if (group.equals(Group.PROXY)) {
                                                AtomicBoolean b = new AtomicBoolean(false);


                                                this.syncProxyConfiguration.getLoginConfigurations()
                                                        .stream()
                                                        .filter(loginConfiguration -> loginConfiguration.getTargetGroup().equalsIgnoreCase("Proxy"))
                                                        .findFirst()
                                                        .ifPresent(loginConfiguration -> {
                                                            b.set(loginConfiguration.isMaintenance());

                                                            // updating in cluster
                                                            SyncProxyConfiguration.updateSyncProxyConfigurationInNetwork(this.syncProxyConfiguration);
                                                        });

                                                if (b.get()) {
                                                    this.syncProxyConfiguration.getLoginConfigurations()
                                                            .stream()
                                                            .filter(loginConfiguration -> loginConfiguration.getTargetGroup().equalsIgnoreCase("Proxy"))
                                                            .findFirst()
                                                            .ifPresent(loginConfiguration -> {
                                                                loginConfiguration.setMaintenance(false);

                                                                // updating in cluster
                                                                SyncProxyConfiguration.updateSyncProxyConfigurationInNetwork(this.syncProxyConfiguration);
                                                            });

                                                    e.getClickedInventory().setItem(e.getSlot(), EnderAPI.getInstance()
                                                            .getSkull(SkullType.CONCRETE_LIME).setLore(Arrays.asList(new String[]{}))
                                                            .setDisplayName("§c§lWartungsmodus").getItemStack());
                                                } else {
                                                    this.syncProxyConfiguration.getLoginConfigurations()
                                                            .stream()
                                                            .filter(loginConfiguration -> loginConfiguration.getTargetGroup().equalsIgnoreCase("proxy"))
                                                            .findFirst()
                                                            .ifPresent(loginConfiguration -> {
                                                                loginConfiguration.setMaintenance(true);

                                                                // updating in cluster
                                                                SyncProxyConfiguration.updateSyncProxyConfigurationInNetwork(this.syncProxyConfiguration);
                                                            });

                                                    e.getClickedInventory().setItem(e.getSlot(), EnderAPI.getInstance()
                                                            .getSkull(SkullType.CONCRETE_RED).setLore(Arrays.asList(new String[]{}))
                                                            .setDisplayName("§c§lWartungsmodus").getItemStack());
                                                }
                                            } else {
                                                boolean b = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(group.getGroup().get(0)).isMaintenance();

                                                if (b) {
                                                    for (String groupName : group.getGroup()) {
                                                        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(groupName);
                                                        serviceTask.setMaintenance(false);
                                                        CloudNetDriver.getInstance().getServiceTaskProvider().addPermanentServiceTask(serviceTask);
                                                    }

                                                    e.getClickedInventory().setItem(e.getSlot(), EnderAPI.getInstance()
                                                            .getSkull(SkullType.CONCRETE_LIME).setLore(Arrays.asList(new String[]{}))
                                                            .setDisplayName("§c§lWartungsmodus").getItemStack());
                                                } else {
                                                    for (String groupName : group.getGroup()) {
                                                        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(groupName);
                                                        serviceTask.setMaintenance(true);
                                                        CloudNetDriver.getInstance().getServiceTaskProvider().addPermanentServiceTask(serviceTask);
                                                    }

                                                    e.getClickedInventory().setItem(e.getSlot(), EnderAPI.getInstance()
                                                            .getSkull(SkullType.CONCRETE_RED).setLore(Arrays.asList(new String[]{}))
                                                            .setDisplayName("§c§lWartungsmodus").getItemStack());
                                                }
                                            }
                                        } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lServices")) {
                                            int count = 0;
                                            List<Collection<ServiceInfoSnapshot>> serviceInfoSnapshots = new ArrayList<Collection<ServiceInfoSnapshot>>();
                                            for (String groupName : group.getGroup()) {
                                                Collection<ServiceInfoSnapshot> snapshots = CloudNetDriver.getInstance()
                                                        .getCloudServiceProvider().getCloudServicesByGroup(groupName);
                                                serviceInfoSnapshots.add(snapshots);

                                                count += snapshots.size();
                                            }

                                            if (count % 9 != 0) {
                                                count += (9 - (count % 9));
                                            }

                                            if (count != 0) {
                                                Inventory inventory = Bukkit.createInventory(null, count, "§cServices");

                                                for (Collection<ServiceInfoSnapshot> snapshots : serviceInfoSnapshots) {
                                                    for (ServiceInfoSnapshot snapshot : snapshots) {
                                                        if (group.equals(Group.LOBBY)) {
                                                            if (!snapshot.getServiceId().getName().startsWith("SilentLobby")) {
                                                                if (snapshot.getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
                                                                    ItemStack itemStack = group.getItemStack();
                                                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                                                    itemMeta.setDisplayName(group.getPrefix() + snapshot.getServiceId().getName());
                                                                    itemMeta.setLore(Arrays.asList(new String[]{"", "§7§lOnline:", "§8➟ §c" +
                                                                            EnderAPI.getInstance().getOnlineCountByService(snapshot), ""}));
                                                                    itemStack.setItemMeta(itemMeta);
                                                                    inventory.addItem(itemStack);
                                                                }
                                                            }
                                                        } else {
                                                            if (snapshot.getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
                                                                ItemStack itemStack = group.getItemStack();
                                                                ItemMeta itemMeta = itemStack.getItemMeta();
                                                                itemMeta.setDisplayName(group.getPrefix() + snapshot.getServiceId().getName());
                                                                itemMeta.setLore(Arrays.asList(new String[]{"", "§7§lOnline:", "§8➟ §c" +
                                                                        EnderAPI.getInstance().getOnlineCountByService(snapshot), ""}));
                                                                itemStack.setItemMeta(itemMeta);
                                                                inventory.addItem(itemStack);
                                                            }
                                                        }
                                                    }
                                                }

                                                player.openInventory(inventory);
                                            } else {
                                                player.closeInventory();
                                                EnderAPI.getInstance().sendActionBar(player, "§7Es ist derzeit §ckein §7Service der Gruppe "
                                                        + group.getInvTitle() + " §7online");
                                                EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                                            }
                                        } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lGroup")) {
                                            Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, group.getInvTitle() + " §7stoppen");

                                            for (int i = 0; i < 5; i++)
                                                inventory.setItem(i, EnderAPI.getInstance().getItem(Material.STAINED_GLASS_PANE, 1, 15)
                                                        .setDisplayName("§1").getItemStack());

                                            inventory.setItem(1, EnderAPI.getInstance().getSkull(SkullType.CONCRETE_LIME)
                                                    .setDisplayName("§a§lJa").setLore(Arrays.asList(new String[]
                                                            {"", "§7§lKlicke um alle Server", "§7§lder Gruppe §a§lneuzustarten", ""})).getItemStack());

                                            inventory.setItem(3, EnderAPI.getInstance().getSkull(SkullType.CONCRETE_RED)
                                                    .setDisplayName("§c§lNein").setLore(Arrays.asList(new String[]
                                                            {"", "§7§lKlicke um §c§lkein §7§lServer", "§7§lder Gruppe §c§lneuzustarten", ""})).getItemStack());

                                            player.openInventory(inventory);
                                        }
                                    }
                                } else if (e.getClickedInventory().getTitle().contains(" §7stoppen")) {
                                    e.setCancelled(true);
                                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lNein")) {
                                        player.closeInventory();
                                        EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§a§lJa")) {
                                        group = Group.getGroup(e.getClickedInventory().getTitle().replaceAll(" §7stoppen", ""));

                                        for (String groupName : group.getGroup()) {
                                            for (ServiceInfoSnapshot snapshot : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServicesByGroup(groupName)) {
                                                if (group.equals(Group.LOBBY)) {
                                                    if (!snapshot.getServiceId().getName().startsWith("SilentLobby")) {
                                                        if (snapshot.getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
                                                            CloudNetDriver.getInstance().getCloudServiceProvider(snapshot).delete();
                                                        }
                                                    }
                                                } else {
                                                    if (snapshot.getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
                                                        CloudNetDriver.getInstance().getCloudServiceProvider(snapshot).delete();
                                                    }
                                                }
                                            }
                                        }
                                        player.closeInventory();
                                        EnderAPI.getInstance().playSound(player, Sounds.SUCCESS);
                                        EnderAPI.getInstance().sendActionBar(player, "§7Du hast die Gruppe " + group.getInvTitle() + " §aneugestartet");
                                    }
                                }
                            } else if (e.getClickedInventory().getTitle().equals("§cServices")) {
                                e.setCancelled(true);

                                ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider()
                                        .getCloudServiceByName(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("(§([a-fk-or-r0-9]))", ""));

                                if (ServiceInfoSnapshotUtil.getOnlineCount(serviceInfoSnapshot) == 0 && serviceInfoSnapshot.getLifeCycle().equals(ServiceLifeCycle.RUNNING)) {
                                    CloudNetDriver.getInstance().getCloudServiceProvider(serviceInfoSnapshot).delete();
                                    EnderAPI.getInstance().playSound(player, Sounds.SUCCESS);
                                    EnderAPI.getInstance().sendActionBar(player, "§7Du hast den Service "
                                            + e.getCurrentItem().getItemMeta().getDisplayName() + " §aneugestartet");
                                } else {
                                    EnderAPI.getInstance().playSound(player, Sounds.FAILED);
                                    EnderAPI.getInstance().sendActionBar(player, "§7Auf dem Service " + e.getCurrentItem().getItemMeta().getDisplayName()
                                            + " §7sind §czu viele §7Spieler drauf");
                                }
                                player.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }
}
