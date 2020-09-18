package net.endertime.enderkomplex.bungee.enums;

public enum BanReason {

    CLIENTMODS(1, "Unerlaubte Clientmods", new Long[] {2592000000l, 604800000l}, new Long[] {7776000000l, 2592000000l}, new Long[] {-1l, 7776000000l}, false, "§cMakros\n§cMinimap\n§cHackclients\n§cAutoclicker\n§cDamage Indicator\n§cX-Ray Mod/Texturepack"), //30d, 90d, perma// - //7d, 30d, 90d//
    TEAMING(2, "Teaming", new Long[] {86400000l, 0l}, new Long[] {259200000l, 0l}, new Long[] {604800000l, 86400000l}, false, "§cCrossteaming"), //1d, 3d, 7d// - //unban, unban, 1d//
    TROLLING(3, "Trolling", new Long[] {43200000l, 0l}, new Long[] {86400000l, 0l}, new Long[] {259200000l, 43200000l}, false, "§cTeamtrolling\n§cSpawntrapping\n§cSpielverzögerung"), //12h, 1d, 3d// - //unban, unban, 12h//
    BUGUSING(4, "Bugusing", new Long[] {604800000l, 0l}, new Long[] {1209600000l, 604800000l}, new Long[] {2592000000l, 1209600000l}, false, "§cDuplizieren\n§cOut of map glitching\n§cAllg. Fehlerausnutzung"), //7d, 14d, 30d// - //unban, 7d, 14d//
    SKIN_NAME(5, "Skin/Name", new Long[] {86400000l, 0l}, new Long[] {604800000l, 86400000l}, new Long[] {1209600000l, 604800000l}, false, "§cAnstössig\n§cNacktskins\n§cRassistisch\n§cRechtsradikal"), //1d, 7d, 14d// - //unban, 1d, 7d//
    REPORTABUSE(6, "Reportabuse", new Long[] {43200000l, 0l}, new Long[] {86400000l, 0l}, new Long[] {259200000l, 43200000l}, false, "§cRandom Reports\n§cChatfilter spam\n§cMehrfache Falschreports"), //12h, 1Tag, 3Tage// - //unban, unban, 12h//
    BANNUMGEHUNG(7, "Bannumgehung", new Long[] {-1l, 2592000000l}, new Long[] {-1l, 2592000000l}, new Long[] {-1l, 2592000000l}, true, "§cUmgehung eines §4aktiven §cBanns mit einem Alt-Account"), //365Tage(1 Jahr), 1825Tage(5 Jahre), 3650Tage(10 Jahre)// - //14d, 30d, 45d//
    BAUWERKE(8, "Bauwerke", new Long[] {86400000l, 0l}, new Long[] {604800000l, 86400000l}, new Long[] {1209600000l, 604800000l}, false, "§cAnstössig\n§cRassistisch\n§cRechtsradikal"), //1Tag, 7Tage, 14Tage// - //unban, 1d, 7d//
    STATS_BOOSTING(9, "Stats boosting", new Long[] {1209600000l, 0l}, new Long[] {2592000000l, 604800000l}, new Long[] {3888000000l, 1209600000l}, false, "§cBots\n§cZweit-Accounts"), //12Tage, 1Tag, 3Tage// + Stats reset //unban, 7d, 14d//
    AFK_RAFMING(10, "AFK-Farming", new Long[] {86400000l, 0l}, new Long[] {259200000l, 86400000l}, new Long[] {604800000l, 259200000l}, false, "§cBezieht sich auf Survival Spielmodi"), //1d, 3d, 7d// - //unban, 1d, 3d//
    ADMIN_BAN(20, "Admin Bann", new Long[] {-1l, 2592000000l}, new Long[] {-1l, 2592000000l}, new Long[] {-1l, 2592000000l}, true, "§cHausverbot"); //365Tage(1 Jahr), 1825Tage(5 Jahre), 3650Tage(10 Jahre)//

    int banID;
    String title, examples;
    Long[] duration1, duration2, duration3;
    boolean isIpBan;

    private BanReason(int banID, String title, Long[] duration1, Long[] duration2, Long[] duration3, boolean isIpBan, String examples) {
        this.banID = banID;
        this.title = title;
        this.duration1 = duration1;
        this.duration2 = duration2;
        this.duration3 = duration3;
        this.isIpBan = isIpBan;
        this.examples = examples;
    }

    public int getID() {
        return this.banID;
    }

    public String getTitle() {
        return this.title;
    }

    public long getReducedDuration(long originalduration) {
        for(BanReason br : BanReason.values()) {
            long bd1 = br.duration1[0];
            long bd2 = br.duration2[0];
            long bd3 = br.duration3[0];
            if(originalduration == bd1) {
                return br.duration1[1];
            } else if(originalduration == bd2) {
                return br.duration2[1];
            } else if(originalduration == bd3) {
                return br.duration3[1];
            }
        }
        return 0;
    }

    public long getDuration(int banamount) {
        switch(banamount) {
            case 1:
                return this.duration1[0];
            case 2:
                return this.duration2[0];
            default:
                return this.duration3[0];
        }
    }

    public boolean isIpBan() {
        return this.isIpBan;
    }

    public String getExamples() {
        return this.examples;
    }

    public String getPermission() {
        return "ek.banreason." + this.banID;
    }

}
