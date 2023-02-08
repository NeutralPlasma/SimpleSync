package eu.virtusdevelops.simplesync.commands;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.db.entity.LinkCode;
import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;
import eu.virtusdevelops.simplesync.db.repositories.LinkCodeRepository;
import eu.virtusdevelops.simplesync.db.repositories.LinkedPlayerRepository;
import eu.virtusdevelops.simplesync.models.Language;
import eu.virtusdevelops.simplesync.utils.TextUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;
import java.util.Optional;

public class LinkCommand extends Command implements TabExecutor {

    private LinkCodeRepository linkCodeRepository;
    private LinkedPlayerRepository linkedPlayerRepository;

    private SimpleSync simpleSync;


    public LinkCommand(String name, LinkCodeRepository linkCodeRepository, LinkedPlayerRepository linkedPlayerRepository, SimpleSync simpleSync) {
        super(name, "simplesync.sync", "discordsync", "dsync", "rsync", "sync", "link");
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


        if(args.length < 1){
            sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.INVALID_CODE.getValue())));
            return;
        }
        Optional<LinkCode> linkCode = linkCodeRepository.findByCode(args[0]);

        Optional<LinkedPlayer> linkedPlayer = linkedPlayerRepository.findByMinecraftID(player.getUniqueId());
        if(linkedPlayer.isPresent()){
            sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.ALREADY_LINKED.getValue())));
            return;
        }


        if(linkCode.isPresent()){
            if(linkCode.get().isExpired()){
                sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.EXPIRED_CODE.getValue())));
                linkCodeRepository.delete(linkCode.get());
            }else{
                LinkedPlayer linkedPlayer1 = new LinkedPlayer(linkCode.get().getDiscordUser(), player.getUniqueId(), player.getName());
                linkedPlayerRepository.save(linkedPlayer1);
                linkCodeRepository.delete(linkCode.get());
                sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.SUCCESS_LINK.getValue())));
                simpleSync.firstSync(linkedPlayer1.getDiscordUser());
                simpleSync.sync(player, linkedPlayer1);

            }
        }else{
            sender.sendMessage(new TextComponent(TextUtil.colorfy(Language.INVALID_CODE.getValue())));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1){
            return Collections.singleton("<code>");
        }else{
            return Collections.emptyList();
        }
    }
}
