package net.endertime.enderapi.bungee.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Version {

    public static Map<UUID, Version> versions = new HashMap<UUID, Version>();

    private UUID uuid;
    private int version;
    private String versionName;

    public Version(UUID uuid, int version) {
        this.uuid = uuid;
        this.version = version;

        if (version == 47) {
            versionName = "1.8.x";
        } else if (107 >= version && version > 47) {
            versionName = "1.9";
        } else if (108 >= version && version > 107) {
            versionName = "1.9.2";
        } else if (109 >= version && version > 108) {
            versionName = "1.9.x";
        } else if (110 >= version && version > 109) {
            versionName = "";
        } else if (210 >= version && version > 110) {
            versionName = "1.10.x";
        } else if (315 >= version && version > 210) {
            versionName = "1.11";
        } else if (316 >= version && version > 315) {
            versionName = "1.11.x";
        } else if (335 >= version && version > 316) {
            versionName = "1.12";
        } else if (338 >= version && version > 335) {
            versionName = "1.12.1";
        } else if (340 >= version && version > 338) {
            versionName = "1.12.2";
        } else if (393 >= version && version > 340) {
            versionName = "1.13";
        } else if (401 >= version && version > 393) {
            versionName = "1.13.1";
        } else if (404 >= version && version > 401) {
            versionName = "1.13.2";
        } else if (477 >= version && version > 404) {
            versionName = "1.14";
        } else if (480 >= version && version > 477) {
            versionName = "1.14.1";
        } else if (485 >= version && version > 480) {
            versionName = "1.14.2";
        } else if (490 >= version && version > 485) {
            versionName = "1.14.3";
        } else if (498 >= version && version > 490) {
            versionName = "1.14.4";
        } else if (573 >= version && version > 498) {
            versionName = "1.15";
        } else if (575 >= version && version > 573) {
            versionName = "1.15.1";
        } else if (578 >= version && version > 575) {
            versionName = "1.15.2";
        }

        versions.put(uuid, this);
    }

    public int getVersion() {
        return version;
    }

    public String getVersionName() {
        return versionName;
    }

    public UUID getUUID() {
        return uuid;
    }
}
