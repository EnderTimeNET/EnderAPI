package net.endertime.enderkomplex.spigot.utils;

import net.endertime.enderkomplex.bungee.enums.ReportReason;
import net.endertime.enderkomplex.mysql.Database;
import net.endertime.enderkomplex.spigot.core.ServerHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReportsMenu implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(!e.getAction().equals(InventoryAction.NOTHING)) {
            if(e.getInventory().getTitle().equals("§c§lOffene Reports")) {
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) return;
                if(e.getCurrentItem().getItemMeta().hasEnchants()) return;
                for(ReportReason rpr : ReportReason.values()) {
                    if(e.getCurrentItem().getType().equals(Material.valueOf(rpr.getMaterial()))) {
                        if(Database.getReportCount(rpr) > 0) {
                            ServerHandler.sendReportIdToBungee(p, Database.getRandomOpenReportID(rpr));
                        }
                        p.closeInventory();
                        return;
                    }
                }
            }
        }
    }

}
