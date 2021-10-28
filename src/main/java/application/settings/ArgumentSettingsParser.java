package application.settings;

import java.util.HashMap;

public class ArgumentSettingsParser implements ISettingsParser {

    private final HashMap<String, String> settings =  new HashMap<String, String>();

    public ArgumentSettingsParser(String[] args){
        parseArgument(args);
    }

    public HashMap<String, String> getSettings(){
        return settings;
    }

    private void parseArgument(String[] args) {

        var settingsString = " " + String.join(" ", args);
        var settingsToParse = settingsString.split(" -");

        for (var setting: settingsToParse){
            var nameValue = setting.split(" ");
            if (!nameValue[0].equals("")){
                var name = nameValue[0];
                var value = "true";

                if (nameValue.length == 2){
                    value = nameValue[1];
                }

                if (!settings.containsKey(name)) {
                    settings.put(name, value);
                }
          }
        }
    }
}
