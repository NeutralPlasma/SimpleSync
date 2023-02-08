package eu.virtusdevelops.simplesync.bot;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.bot.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import eu.virtusdevelops.simplesync.models.Properties;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class DiscordBot {
    private JDA jda;
    private boolean enabled;
    private Properties properties;
    private Guild guild;
    private SimpleSync simpleSync;


    public DiscordBot(Properties properties, SimpleSync simpleSync){
        this.enabled = false;
        this.properties = properties;
        this.simpleSync = simpleSync;

        try{
            this.jda = JDABuilder.createDefault(properties.getString("token"))
                    .addEventListeners(new SlashCommandListener(
                            simpleSync.getLinkCodeRepository(),
                            simpleSync.getLinkedPlayerRepository()
                    ))
                    .build().awaitReady();
        }catch (Exception e){
          throw new RuntimeException(e);
        }
    }


    public void setup(){
        this.guild = jda.getGuildById(this.properties.getLong("guild_id"));
        simpleSync.getLogger().info("Guild is found: " + guild.isLoaded());
        if(this.guild != null){
            guild.updateCommands().addCommands(
                    Commands.slash("linkcode", "Generates unique code for you to link your account."),
                    Commands.slash("sync", "Forcefully syncs your rank."))
                    .queue();

        }else{
            this.enabled = false;
            /*
                SEND ERROR BLABLA...
             */


            //jda.shutdownNow();
        }


    }


    /**
     *
     * @param discordId id of the user
     * @param roles roles to add
     * @param allRoles all possible roles to be achieved.
     */
    public void syncRoles(long discordId, List<Long> roles, List<Long> allRoles){
        Member member = guild.getMemberById(discordId);
        if(member == null) return;
        List<Role> userRoles = member.getRoles();
        for(Role userRole : userRoles){
            long roleID = userRole.getIdLong();
            if(allRoles.contains(roleID)){ // is rank role
                if(!roles.contains(roleID)){ // check if user is supposed to have this role
                    guild.removeRoleFromMember(member, userRole).queue();
                    roles.remove(roleID);
                }else{
                    roles.remove(roleID);
                }

            }
        }



        for(long role : roles){
            Role role1 = guild.getRoleById(role);
            if(role1 != null){
                guild.addRoleToMember(member, role1).queue();
            }
        }
    }

    public void addGroup(long discordID, long groupID){
        Member member = guild.getMemberById(discordID);
        Role role = guild.getRoleById(groupID);
        if(member != null && role != null){
            guild.addRoleToMember(member.getUser(), role).queue();
        }
    }

    public void removeGroup(long discordID, long groupID){
        Member member = guild.getMemberById(discordID);
        Role role = guild.getRoleById(groupID);
        if(member != null && role != null){
            guild.removeRoleFromMember(member.getUser(), role).queue();
        }
    }

    public void updateName(long discordID, String name){
        Member member = guild.getMemberById(discordID);
        if(member != null){
            guild.modifyNickname(member, name).queue();
        }
    }

    public void shutdown(){
        jda.shutdown();
    }
}
