package net.endertime.enderkomplex.bungee.enums;

public enum KickReason {

    TIME(1, "Spielverzögerung", "§cUntaktische Verzögerung des Spiels", "ek.kickreason.1"),
    BUGUSING(2, "Bugusing", "§cAusnutzung milder Bugs", "ek.kickreason.2"),
    AFKFARMING(3, "AFK-Farming", "§cFarming in abwesenheit", "ek.kickreason.3");

    int kickID;
    String title, examples, permission;
    long duration1, duration2, duration3;
    boolean isIpBan;

    private KickReason(int kickID, String title, String examples, String permission) {
        this.kickID = kickID;
        this.title = title;
        this.examples = examples;
        this.permission = permission;
    }

    public int getID() {
        return this.kickID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getExamples() {
        return this.examples;
    }

    public String getPermission() {
        return this.permission;
    }

}
