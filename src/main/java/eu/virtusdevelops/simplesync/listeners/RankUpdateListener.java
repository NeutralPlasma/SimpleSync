package eu.virtusdevelops.simplesync.listeners;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.bot.DiscordBot;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.user.User;

import java.util.Optional;

public class RankUpdateListener {
    private SimpleSync simpleSync;
    private DiscordBot discordBot;
    private LinkedPlayerRepository linkedPlayerRepository;


    public RankUpdateListener(SimpleSync simpleSync, DiscordBot discordBot, LinkedPlayerRepository linkedPlayerRepository){
        this.simpleSync = simpleSync;
        this.discordBot = discordBot;
        this.linkedPlayerRepository = linkedPlayerRepository;


        EventBus eventBus = LuckPermsProvider.get().getEventBus();
        eventBus.subscribe(simpleSync, NodeMutateEvent .class, this::onRankUpdate);
    }


    public void onRankUpdate(NodeMutateEvent event){

        if(event.isUser()){
            User user = (User) event.getTarget();
            Optional<LinkedPlayer> linkedPlayer = linkedPlayerRepository.findByMinecraftID(user.getUniqueId());
            if(!linkedPlayer.isPresent()){
                return;
            }
            simpleSync.sync(user, linkedPlayer.get());
        }
    }
}
