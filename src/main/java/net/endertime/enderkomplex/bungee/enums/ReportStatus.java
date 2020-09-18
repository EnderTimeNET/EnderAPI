package net.endertime.enderkomplex.bungee.enums;

public enum ReportStatus {

    OPEN(2, "§cOffen ⚑"),
    WORKING(1, "§6In Bearbeitung ⚑"),
    FINISHED(0, "§aAbgeschlossen ⚑");

    String title;
    int id;

    private ReportStatus(int id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getID() {
        return this.id;
    }

    public static ReportStatus translate(int status) {
        for(ReportStatus rs : ReportStatus.values()) {
            if(rs.getID() == status) {
                return rs;
            }
        }
        return null;
    }

}
