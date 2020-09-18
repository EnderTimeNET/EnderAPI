package net.endertime.enderkomplex.bungee.enums;

public enum UnmuteReason {

    EXPIRED(0, "Abgelaufen", "Zeitlicher Ablauf des Mutes"),
    FALSEMUTE(1, "Fehlmute", "§cFalschinterpretation eines Verstosses"),
    UNMUTE_REQUEST(2, "Entmutungsantrag", "§cAngenommener Entmutungsantrag"),
    TECH_DIFFI(3, "Technischer Fehler", "§cFehlerhafte Beweismittel");

    int unmuteID;
    String title, examples;

    private UnmuteReason(int unmuteID, String title, String examples) {
        this.unmuteID = unmuteID;
        this.title = title;
        this.examples = examples;
    }

    public int getID() {
        return this.unmuteID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getExamples() {
        return this.examples;
    }

}
