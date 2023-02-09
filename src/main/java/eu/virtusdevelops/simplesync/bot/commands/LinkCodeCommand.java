package eu.virtusdevelops.simplesync.bot.commands;

import eu.virtusdevelops.simplesync.db.entity.LinkCode;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkCodeRepository;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import eu.virtusdevelops.simplesync.utils.CodeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LinkCodeCommand extends SlashCommand{

    private LinkCodeRepository linkCodeRepository;
    private LinkedPlayerRepository linkedPlayerRepository;

    public LinkCodeCommand(String name, LinkedPlayerRepository linkedPlayerRepository, LinkCodeRepository linkCodeRepository) {
        super(name);
        this.linkedPlayerRepository = linkedPlayerRepository;
        this.linkCodeRepository = linkCodeRepository;
    }

    @Override
    public SlashCommandInteractionEvent execute(SlashCommandInteractionEvent event) {
        // do the sync command
        event.deferReply().queue();

        Optional<LinkCode> code = linkCodeRepository.findByDiscordUser(event.getUser().getIdLong());
        if(code.isPresent()){
            if(code.get().isExpired()){
                linkCodeRepository.delete(code.get());
                LinkCode code1  = new LinkCode(event.getUser().getIdLong(), CodeUtil.randomCode());
                linkCodeRepository.save(code1);
                event.getHook().sendMessage("Your code has been generated, sending it now...").setEphemeral(true).queue((result) -> {
                    result.delete().queueAfter(5, TimeUnit.SECONDS);
                });
                event.getHook().sendMessage("Here's your code: `" + code1.getCode() + "`")
                        .setEphemeral(true).queue();
            }else{
                event.getHook().sendMessage("Your code has been generated, sending it now...").setEphemeral(true).queue((result) -> {
                    result.delete().queueAfter(5, TimeUnit.SECONDS);
                });
                event.getHook().sendMessage("Here's your code: `" + code.get().getCode() + "`")
                        .setEphemeral(true).queue();
            }
        }else{
            String codeString = CodeUtil.randomCode();
            LinkCode code1  = new LinkCode(event.getUser().getIdLong(), codeString);
            linkCodeRepository.save(code1);

            event.getHook().sendMessage("Your code has been generated, sending it now...").setEphemeral(true).queue((result) -> {
                result.delete().queueAfter(5, TimeUnit.SECONDS);
            });
            event.getHook().sendMessage("Here's your code: `" + code1.getCode() + "`")
                    .setEphemeral(true).queue();
        }

        return event;
    }
}
