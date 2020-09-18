package net.endertime.enderkomplex.bungee.enums;

public enum ChatType {

    SERVERCHAT("Serverchat"),
    PRIVATCHAT("Privatchat"),
    PARTYCHAT("Partychat"),
    CLANCHAT("Clanchat");

    String title;

    private ChatType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
