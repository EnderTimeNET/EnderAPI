package net.endertime.enderkomplex.bungee.objects;

import net.endertime.enderkomplex.mysql.Database;

public class ChatlogInfo {

    String logid, logtime, logdate, logged;
    int messagecount;

    public ChatlogInfo(String logid, String logtime, String logdate, String logged) {
        this.logid = logid;
        this.logtime = logtime;
        this.logdate = logdate;
        this.messagecount = Database.getMessagesCount(logid);
        this.logged = logged;
    }

    public String getID() {
        return this.logid;
    }

    public String getTimeString() {
        return this.logtime;
    }

    public String getDateString() {
        return this.logdate;
    }

    public int getMessageCount() {
        return this.messagecount;
    }

    public String getLoggedName() {
        return this.logged;
    }

}
