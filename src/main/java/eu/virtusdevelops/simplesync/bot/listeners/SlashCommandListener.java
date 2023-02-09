package eu.virtusdevelops.simplesync.bot.listeners;

import eu.virtusdevelops.simplesync.bot.commands.SlashCommand;
import eu.virtusdevelops.simplesync.db.entity.LinkCode;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkCodeRepository;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import eu.virtusdevelops.simplesync.utils.CodeUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SlashCommandListener extends ListenerAdapter {

    List<SlashCommand> slashCommandList = new ArrayList<>();


    public SlashCommandListener(){
    }



    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for(SlashCommand slashCommand: slashCommandList){
            if(slashCommand.getName().equals(event.getName())){
                slashCommand.execute(event);
            }
        }
    }

    public void registerCommand(SlashCommand slashCommand){
        slashCommandList.add(slashCommand);
    }
}
