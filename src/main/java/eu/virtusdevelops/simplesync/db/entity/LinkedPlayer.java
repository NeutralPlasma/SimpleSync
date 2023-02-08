package eu.virtusdevelops.simplesync.db.entity;



import java.util.UUID;



public class LinkedPlayer {

    private long ID;

    private long discordUser;
    private UUID userID;
    private String username;

    public LinkedPlayer(){
        this.ID = -1;
    }

    public LinkedPlayer(long discordUser, UUID userID, String username){
        this.discordUser = discordUser;
        this.username = username;
        this.userID = userID;
        this.ID = -1;
    }


    public LinkedPlayer(long discordUser, UUID userID, String username, long ID){
        this.discordUser = discordUser;
        this.username = username;
        this.userID = userID;
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getDiscordUser() {
        return discordUser;
    }

    public void setDiscordUser(long discordUser) {
        this.discordUser = discordUser;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
