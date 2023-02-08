package eu.virtusdevelops.simplesync.db.entity;


public class LinkCode  {
    private static final long serialVersionUID = 1L;

    private long ID;

    private long discordUser;
    private String code;
    private long expireTimeStamp;

    public LinkCode(){
        this.ID = -1;
    }

    public LinkCode(long discordUser, String code, long expireTimeStamp, long ID){
        this.code = code;
        this.expireTimeStamp = expireTimeStamp;
        this.discordUser = discordUser;
        this.ID = ID;
    }


    public LinkCode(long discordUser, String code, long expireTimeStamp){
        this.code = code;
        this.expireTimeStamp = expireTimeStamp;
        this.discordUser = discordUser;
        this.ID = -1;
    }

    public LinkCode(long discordUser, String code){
        this.code = code;
        this.expireTimeStamp = System.currentTimeMillis() + 300*1000;
        this.discordUser = discordUser;
        this.ID = -1;
    }

    public long getID() {
        return ID;
    }

    public String getCode() {
        return code;
    }

    public long getExpireTimeStamp() {
        return expireTimeStamp;
    }

    public long getDiscordUser() {
        return discordUser;
    }

    public void setExpireTimeStamp(long expireTimeStamp) {
        this.expireTimeStamp = expireTimeStamp;
    }

    public void setDiscordUser(long discordUser) {
        this.discordUser = discordUser;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public boolean isExpired(){
        return expireTimeStamp - System.currentTimeMillis() <= 0;
    }
}
