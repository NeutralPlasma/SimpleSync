package eu.virtusdevelops.simplesync;

import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.simplesync.bot.DiscordBot;
import eu.virtusdevelops.simplesync.commands.LinkCommand;
import eu.virtusdevelops.simplesync.commands.UnLinkCommand;
import eu.virtusdevelops.simplesync.db.ConnectionManager;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.listeners.PlayerJoinListener;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import eu.virtusdevelops.simplesync.listeners.RankUpdateListener;
import eu.virtusdevelops.simplesync.managers.LanguageManager;
import eu.virtusdevelops.simplesync.models.BasicProperties;
import eu.virtusdevelops.simplesync.models.Properties;
import eu.virtusdevelops.simplesync.db.repositories.LinkCodeRepository;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class SimpleSync extends Plugin {

    Configuration config;
    private DiscordBot discordBot;

    private LinkCodeRepository linkCodeRepository;
    private LinkedPlayerRepository linkedPlayerRepository;

    private List<Long> allRoles = new ArrayList<>();
    private HikariDataSource hikariDataSource;


    @Override
    public void onEnable() {
        loadConfig();


        Properties<BasicProperties> mysqlSettings = getMysqlSettings();
        hikariDataSource = ConnectionManager.openConnecton(mysqlSettings);


        /*
            SETUP ALL REPOSITORIES
         */
        linkCodeRepository = new LinkCodeRepository(mysqlSettings, hikariDataSource);
        linkedPlayerRepository = new LinkedPlayerRepository(mysqlSettings, hikariDataSource);
        new LanguageManager(this, getConfig().getString("language"));


        linkCodeRepository.findById(1L);


        Properties<BasicProperties> properties = new BasicProperties();
        properties.addProperty("guild_id", getConfig().getLong("guild_id"));
        properties.addProperty("token", getConfig().getString("bot_token"));
        discordBot = new DiscordBot(properties, this);
        discordBot.setup();

        // listeners
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(this, discordBot, linkedPlayerRepository));
        new RankUpdateListener(this, discordBot, linkedPlayerRepository);

        //commands
        getProxy().getPluginManager().registerCommand(this, new LinkCommand("ranksync", linkCodeRepository, linkedPlayerRepository, this));
        getProxy().getPluginManager().registerCommand(this, new UnLinkCommand("unranksync", linkCodeRepository, linkedPlayerRepository, this));


        Configuration section = getConfig().getSection("ranks");

        for(String group : section.getKeys()){
            allRoles.add(section.getLong(group + ".discord_role_id"));
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        linkCodeRepository.close();
        discordBot.shutdown();
    }


    public Configuration getConfig() {
        return config;
    }


    private void loadConfig(){
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        }catch (Exception error){
            error.printStackTrace();
            onDisable();
        }
    }


    public LinkCodeRepository getLinkCodeRepository() {
        return linkCodeRepository;
    }

    public LinkedPlayerRepository getLinkedPlayerRepository() {
        return linkedPlayerRepository;
    }

    public List<Long> getAllRoles() {
        return allRoles;
    }




    public Properties<BasicProperties> getMysqlSettings(){
        Properties<BasicProperties> properties = new BasicProperties();
        properties.addProperty("maxPoolSize", getConfig().getInt("mysql.maxPoolSize"));
        properties.addProperty("connectionTimeOut", getConfig().getInt("mysql.connectionTimeOut"));
        properties.addProperty("address", getConfig().getString("mysql.address"));
        properties.addProperty("port", getConfig().getInt("mysql.port"));
        properties.addProperty("database", getConfig().getString("mysql.database"));
        properties.addProperty("username", getConfig().getString("mysql.username"));
        properties.addProperty("password", getConfig().getString("mysql.password"));
        properties.addProperty("useSSL", getConfig().getBoolean("mysql.useSSL"));
        properties.addProperty("table_prefix", getConfig().getString("mysql.table_prefix"));

        getLogger().warning("Prefix: " + properties.getString("table_prefix"));

        return properties;
    }

    public void firstSync(long discordID){
        discordBot.addGroup(discordID, getConfig().getLong("synced_role"));
    }

    public void unSync(long discordID){
        discordBot.removeGroup(discordID, getConfig().getLong("synced_role"));
    }
    public void sync(User user, LinkedPlayer linkedPlayer){
        sync(user, linkedPlayer, "", false);
    }

    public void sync(ProxiedPlayer player, LinkedPlayer linkedPlayer){
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(ProxiedPlayer.class).getUser(player);
        sync(user, linkedPlayer, player.getName(), true);
    }

    public void sync(User user, LinkedPlayer linkedPlayer, String name, boolean updateName){
        List<Long> toAdd = new ArrayList<>();
        for(Group group : user.getInheritedGroups(user.getQueryOptions())){
            long id = getConfig().getLong("ranks." + group.getName() + ".discord_role_id");
            if(id != 0){
                toAdd.add(id);
            }
        }
        discordBot.syncRoles(linkedPlayer.getDiscordUser(), toAdd, getAllRoles());
        if(updateName)
            discordBot.updateName(linkedPlayer.getDiscordUser(), name);
    }
}
