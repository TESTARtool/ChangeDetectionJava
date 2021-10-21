package application.settings;

import java.util.HashMap;

public class SettingsProviderBuilder {
    private final HashMap<String, String> settings =  new HashMap<String, String>();

    public SettingsProviderBuilder add(ISettingsParser parser){
        var parsedSettings = parser.getSettings();

        for (var setting : parsedSettings.entrySet()){

            if(!settings.containsKey(setting.getKey())) {
                settings.put(setting.getKey(), setting.getValue());
            }
        }

        return this;
    }

    public ISettingProvider buildSettingsProvider(){
        return new SettingProvider(settings);
    }
}
