package settings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings<T> implements ISettingsFor<T> {

    private T settings;
    private boolean isValid;

    public Settings(ISettingProvider settingProvider, Class<T> settingObject){
        try {
            var instance = (T) settingObject.getConstructor().newInstance();
            this.isValid = true;
            var hasItems = false;

            for (var field : settingObject.getDeclaredFields()) {
                if (field.isAnnotationPresent(IsSetting.class)) {
                    hasItems = true;
                    var isSetting = field.getAnnotation(IsSetting.class);
                    var setting = settingProvider.tryGetSettingByName(isSetting.name());

                    if (setting.isPresent()) {
                        field.set(instance, setting.get());
                    } else {
                        Logger.getLogger("Settings<T>").log(Level.INFO, "Unable to find value for setting -" + isSetting.name());
                        isValid = false;
                    }
                }
            }

            if (!hasItems){
                this.isValid = false;
            }

            this.settings = instance;
        }
        catch (Exception ex){
            isValid = false;
            ex.printStackTrace();
        }
    }

    public T getValue(){
        return this.settings;
    }

    public boolean isValid(){
        return this.isValid;
    }
}
