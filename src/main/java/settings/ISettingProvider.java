package settings;

import java.util.Optional;

public interface ISettingProvider {
    Object getSettingByName(String setting);

    boolean containsSetting(String setting);

    Optional<Object> tryGetSettingByName(String setting);

    <T> ISettingsFor<T> resolve(Class<T> type);
}
