package eu.virtusdevelops.simplesync.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class SlashCommand {

    String name;

    public SlashCommand(String name){
        this.name = name;
    }

    public SlashCommandInteractionEvent execute(SlashCommandInteractionEvent event){
        return event;
    }

    public String getName() {
        return name;
    }
}
