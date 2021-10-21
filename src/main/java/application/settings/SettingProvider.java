package application.settings;

import java.util.HashMap;
import java.util.Optional;

public class SettingProvider implements ISettingProvider{

    private final HashMap<String, String> settings;

    public SettingProvider(HashMap<String, String> settings){
        this.settings = settings;
    }

    public Object getSettingByName(String setting){
        if (!settings.containsKey(setting)){
            throw new RuntimeException("Unable to find a setting for '"+ setting + "'");
        }
        return getSettingByName(settings.get(setting));
    }

    public boolean containsSetting(String setting){
        return settings.containsKey(setting);
    }

    public Optional<Object> tryGetSettingByName(String setting){
        if (!containsSetting(setting)){
            return Optional.empty();
        }
        return Optional.of(settings.get(setting));
    }

    public <T> ISettingsFor<T> resolve(Class<T> settingsClass){
        return new Settings<T>(this, settingsClass);
    }
}
