package eu.virtusdevelops.simplesync.utils;

import net.md_5.bungee.api.ChatColor;

public class TextUtil {
    public static String colorfy(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
