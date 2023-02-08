package eu.virtusdevelops.simplesync.listeners;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.bot.DiscordBot;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    private SimpleSync simpleSync;
    private DiscordBot discordBot;

    private LinkedPlayerRepository linkedPlayerRepository;


    public PlayerJoinListener(SimpleSync simpleSync, DiscordBot discordBot, LinkedPlayerRepository linkedPlayerRepository){
        this.discordBot = discordBot;
        this.simpleSync = simpleSync;
        this.linkedPlayerRepository = linkedPlayerRepository;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PostLoginEvent event){
        Optional<LinkedPlayer> linkedPlayer = linkedPlayerRepository.findByMinecraftID(event.getPlayer().getUniqueId());
        if(!linkedPlayer.isPresent()){
            return;
        }
        simpleSync.sync(event.getPlayer(), linkedPlayer.get());
        simpleSync.firstSync(linkedPlayer.get().getDiscordUser());

    }
}
