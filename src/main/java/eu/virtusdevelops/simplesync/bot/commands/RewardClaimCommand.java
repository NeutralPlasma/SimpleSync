package eu.virtusdevelops.simplesync.bot.commands;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import eu.virtusdevelops.simplesync.utils.ImageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.internal.utils.IOUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RewardClaimCommand extends SlashCommand{


    private LinkedPlayerRepository linkedPlayerRepository;
    private SimpleSync simpleSync;

    public RewardClaimCommand(String name, LinkedPlayerRepository linkedPlayerRepository, SimpleSync simpleSync) {
        super(name);
        this.linkedPlayerRepository = linkedPlayerRepository;
        this.simpleSync = simpleSync;
    }

    @Override
    public SlashCommandInteractionEvent execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        // DO THE SYNCING
        Optional<LinkedPlayer> linkedPlayer = linkedPlayerRepository.findByDiscordUser(event.getUser().getIdLong());
        if(linkedPlayer.isPresent()){
            // do the rewards!!!



            try{
                BufferedImage image = ImageUtil.generateImage(linkedPlayer.get(), simpleSync.getTemplate());
                event.getHook().sendFiles(FileUpload.fromData(ImageUtil.toByteArray(image, "png"), "test.png")).queue();

            }catch (IOException e){
                event.getHook().sendMessage("Error loading your data T_T").queue();

            }




        }else{
            event.getHook().sendMessage("You don't have any account linked.")
                    .setEphemeral(true).queue((result) -> {
                        result.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
        }

        return event;
    }
}
