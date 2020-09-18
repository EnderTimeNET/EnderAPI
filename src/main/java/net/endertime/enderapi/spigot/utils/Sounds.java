package net.endertime.enderapi.spigot.utils;

import org.bukkit.Sound;

public enum Sounds {

    SUCCESS(Sound.ENTITY_PLAYER_LEVELUP),
    FAILED(Sound.ITEM_SHIELD_BREAK);

    private Sound sound;

    Sounds(Sound sound) {
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
    }
}
