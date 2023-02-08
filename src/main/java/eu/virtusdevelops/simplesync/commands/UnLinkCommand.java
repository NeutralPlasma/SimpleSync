package eu.virtusdevelops.simplesync.commands;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.db.entity.LinkCode;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkCodeRepository;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import eu.virtusdevelops.simplesync.models.Language;
import eu.virtusdevelops.simplesync.utils.TextUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Optional;

public class UnLinkCommand extends Command {

    private LinkCodeRepository linkCodeRepository;
    private LinkedPlayerRepository linkedPlayerRepository;

    private SimpleSync simpleSync;


    public UnLinkCommand(String name, LinkCodeRepository linkCodeRepository, LinkedPlayerRepository linkedPlayerRepository, SimpleSync simpleSync) {
        super(name, "simplesync.sync", "undiscordsync", "undsync", "unrsync", "unlink", "unsync");
        this.linkedPlayerRepository = linkedPlayerRepository;
        this.linkCodeRepository = linkCodeRepository;
        this.simpleSync = simpleSync;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.ONLY_PLAYER_EXECUTION.getValue())));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        Optional<LinkedPlayer> linkedPlayer = linkedPlayerRepository.findByMinecraftID(player.getUniqueId());
        if(linkedPlayer.isPresent()){
            linkedPlayerRepository.delete(linkedPlayer.get());
            simpleSync.unSync(linkedPlayer.get().getDiscordUser());
            sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.SUCCESS_UNLINK.getValue())));
        }else{
            sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.NOT_LINKED.getValue())));
        }
    }

}
