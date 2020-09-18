package net.endertime.enderkomplex.bungee.enums;

public enum UnbanReason {

    EXPIRED(0, "Abgelaufen", false, "Zeitlicher Ablauf des Banns"),
    FALSEBAN(1, "Fehlbann", false, "§cFalschinterpretation eines Verstosses"),
    UNBAN_REQUEST(2, "Entbannungsantrag", true, "§cAngenommener Entbannungsantrag"),
    TECH_DIFFI(3, "Technischer Fehler", false, "§cFehlerhafte Beweismittel");

    int unbanID;
    String title, examples;
    boolean increaseDuration;

    private UnbanReason(int unbanID, String title, boolean increaseDuration, String examples) {
        this.unbanID = unbanID;
        this.title = title;
        this.increaseDuration = increaseDuration;
        this.examples = examples;
    }

    public int getID() {
        return this.unbanID;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean doesIncreaseDuration() {
        return this.increaseDuration;
    }

    public String getExamples() {
        return this.examples;
    }

}
