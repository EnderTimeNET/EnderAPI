package net.endertime.enderapi.spigot.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collection;

public class AdvancementMessenger {

    private NamespacedKey id;
    private String icon;
    private String title;
    private String description;
    private String frame = "task";
    private Boolean announce = false;
    private Boolean toast = true;

    public AdvancementMessenger(String title, String description, Material icon) {
        this(new NamespacedKey(EnderAPI.getInstance().getPlugin(), String.valueOf(Math.round(Math.random() * 100))), title, description, icon);
    }

    public AdvancementMessenger(NamespacedKey id, String title, String description, Material icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon.toString();
    }

    public void showTo(Player p) {
        showTo(Arrays.asList(p));
    }

    public void showTo(final Collection<? extends Player> players) {
        add();
        grant(players);
        new BukkitRunnable() {

            public void run() {
                revoke(players);
                remove();
            }
        }.runTaskLater(EnderAPI.getInstance().getPlugin(), 20);
    }

    @SuppressWarnings("deprecation")
    private void add() {
        try {
            Bukkit.getUnsafe().loadAdvancement(id, getJson());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void remove() {
        Bukkit.getUnsafe().removeAdvancement(id);
    }

    private void grant(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players) {

            progress = player.getAdvancementProgress(advancement);
            if (!progress.isDone()) {
                for (String criteria : progress.getRemainingCriteria()) {
                    progress.awardCriteria(criteria);
                }
            }
        }
    }

    private void revoke(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        for (Player player : players) {

            progress = player.getAdvancementProgress(advancement);
            if (progress.isDone()) {
                for (String criteria : progress.getAwardedCriteria()) {
                    progress.revokeCriteria(criteria);
                }
            }
        }
    }

    public String getJson() {

        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);

        JsonObject text = new JsonObject();
        text.addProperty("text", this.title);

        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.add("title", text);
        display.addProperty("description", this.description);
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame", this.frame);
        display.addProperty("announce_to_chat", announce);
        display.addProperty("show_toast", toast);
        display.addProperty("hidden", true);

        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();

        trigger.addProperty("trigger", "minecraft:impossible");
        criteria.add("impossible", trigger);

        json.add("criteria", criteria);
        json.add("display", display);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);

    }

}
