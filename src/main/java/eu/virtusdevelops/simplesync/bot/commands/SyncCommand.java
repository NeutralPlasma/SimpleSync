package eu.virtusdevelops.simplesync.bot.commands;

import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkCodeRepository;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SyncCommand extends SlashCommand{


    private LinkedPlayerRepository linkedPlayerRepository;

    public SyncCommand(String name, LinkedPlayerRepository linkedPlayerRepository) {
        super(name);
        this.linkedPlayerRepository = linkedPlayerRepository;
    }

    @Override
    public SlashCommandInteractionEvent execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        // DO THE SYNCING
        Optional<LinkedPlayer> linkedPlayer = linkedPlayerRepository.findByDiscordUser(event.getUser().getIdLong());
        if(linkedPlayer.isPresent()){
            event.getHook().sendMessage("Already linked: " + linkedPlayer.get().getUsername())
                    .setEphemeral(true).queue((result) -> {
                        result.delete().queueAfter(5, TimeUnit.SECONDS);
                    });

        }else{
            event.getHook().sendMessage("You don't have any account linked.")
                    .setEphemeral(true).queue((result) -> {
                        result.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
        }

        return event;
    }
}
