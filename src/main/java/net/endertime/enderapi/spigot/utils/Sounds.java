package net.endertime.enderapi.spigot.utils;

import org.bukkit.Sound;

public enum Sounds {

    SUCCESS(Sound.LEVEL_UP),
    FAILED(Sound.ITEM_BREAK);

    private Sound sound;

    Sounds(Sound sound) {
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
    }
}
