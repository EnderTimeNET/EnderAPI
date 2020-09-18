package net.endertime.enderkomplex.bungee.enums;

public enum MuteReason {

    CHAT1(1, "Leichtes Chatvergehen", new Long[] {10800000l, 0l}, "§cSpam\n§cWortwahl\n§cRespektlos\n§cBeleidigung\n§cProvokation\n§cSupportabuse"), //3h//
    CHAT2(2, "Mittleres Chatvergehen", new Long[] {604800000l, 86400000l}, "§cWerbung\n§cDrohung\n§cSexismus\n§cExtreme Beleidigung"), //7d// - //1d//
    CHAT3(3, "Schweres Chatvergehen", new Long[] {-1l, 2592000000l}, "§cRassismus\n§cTodeswunsch\n§cRechtsradikal\n§cExtreme Werbung\n§cExtreme Drohung\n§cPersöhnliche Daten"), //1y// - //14d//
    MUTE_UMGEHUNG(4, "Muteumgehung", new Long[] {-1l, 2592000000l}, "§cUmgehung eines §4aktiven §cMutes mit einem Zweit-Account"); //perma// - //30d//

    int banID;
    String title, examples;
    Long[] duration;

    private MuteReason(int banID, String title, Long[] duration, String examples) {
        this.banID = banID;
        this.title = title;
        this.duration = duration;
        this.examples = examples;
    }

    public int getID() {
        return this.banID;
    }

    public String getTitle() {
        return this.title;
    }

    public long getReducedDuration(long originalduration) {
        for(MuteReason mr : MuteReason.values()) {
            long bd1 = mr.duration[0];
            long bd2 = mr.duration[0];
            long bd3 = mr.duration[0];
            if(originalduration == bd1) {
                return mr.duration[1];
            } else if(originalduration == bd2) {
                return mr.duration[1];
            } else if(originalduration == bd3) {
                return mr.duration[1];
            }
        }
        return 0;
    }

    public long getDuration() {
        return this.duration[0];
    }

    public String getExamples() {
        return this.examples;
    }

}
