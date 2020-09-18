package net.endertime.enderkomplex.bungee.enums;

public enum ReportReason {

    HACKING(1, "Hacking", "IRON_SWORD", 11, true),
    CHAT(2, "Chat", "BOOK_AND_QUILL", 10, false),
    TEAMING(3, "Teaming", "RABBIT_FOOT", 16, true),
    TROLLING(4, "Trolling", "TNT", 15, true),
    SKIN(5, "Skin", "PAINTING", 14, false),
    NAME(6, "Name", "NAME_TAG", 13, false),
    BUGUSING(7, "Bugusing", "DEAD_BUSH", 12, true);

    String title, mat;
    int slot, id;
    boolean instajump;

    private ReportReason(int id, String title, String mat, int slot, boolean instajump) {
        this.id = id;
        this.title = title;
        this.mat = mat;
        this.slot = slot;
        this.instajump = instajump;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMaterial() {
        return this.mat;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getID() {
        return this.id;
    }

    public boolean isInstantJump() {
        return this.instajump;
    }

}
