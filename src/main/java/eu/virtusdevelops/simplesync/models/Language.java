package eu.virtusdevelops.simplesync.models;

import java.util.Properties;

public enum Language {
    ONLY_PLAYER_EXECUTION("&cThis command can only be executed by players."),
    NO_PERMISSION("&cYou do not have permission."),
    SUCCESS_LINK("&aSuccessfully linked."),
    SUCCESS_UNLINK("&aSuccessfully unlinked"),
    INVALID_CODE("&cInvalid code."),
    EXPIRED_CODE("&cThis code has expired."),
    ALREADY_LINKED("&cYour account is already linked."),
    NOT_LINKED("&cYour account is not linked.")
    ;


    private String value;


    Language(String s) {
        this.value = s;
    }

    public String getValue()
    {
        return value;
    }

    private void setValue(final String aValue)
    {
        value = aValue;
    }

    public static void initialize(final Properties aPropertyTable)
    {
        for(final Language language : values())
        {
            final String defaultValue = language.getValue();
            language.setValue(aPropertyTable.getProperty(language.name(), defaultValue));
        }
    }

}
