package eu.virtusdevelops.simplesync.managers;

import eu.virtusdevelops.simplesync.SimpleSync;
import eu.virtusdevelops.simplesync.models.Language;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static java.io.File.separator;

public class LanguageManager {

    Properties properties;
    SimpleSync simpleSync;
    String language;

    public LanguageManager(SimpleSync simpleSync, String language){
        this.simpleSync = simpleSync;
        this.language = language;
        setup();
    }


    public void setup(){
        properties = new Properties();

        File file = new File(simpleSync.getDataFolder() + separator + "language_" + language + ".properties");

        try{
            if(file.exists()){
                properties.load(Files.newInputStream(Paths.get(file.getPath())));
                Language.initialize(properties);
                simpleSync.getLogger().info("Successfully loaded custom language file.");
            }else{
                try(InputStream inputStream = simpleSync.getResourceAsStream("language_en.properties")){
                    properties.load(inputStream);
                    Language.initialize(properties);
                    simpleSync.getLogger().info("Successfully loaded default language file.");

                }catch (IOException e){
                    simpleSync.getLogger().severe("Could not load default language file");
                }
            }
        }catch (IOException e){
            simpleSync.getLogger().warning("Could not load language file");
        }
    }
}
