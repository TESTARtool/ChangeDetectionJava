package settings;

public class Settings<T> implements ISettingsFor<T> {

    private T settings;
    private boolean isValid;

    public Settings(ISettingProvider settingProvider, Class<T> settingObject){
        try {
            var instance = (T) settingObject.getConstructor().newInstance();
            this.isValid = true;

            for (var field : settingObject.getDeclaredFields()) {
                if (field.isAnnotationPresent(IsSetting.class)) {
                    var isSetting = field.getAnnotation(IsSetting.class);
                    var setting = settingProvider.tryGetSettingByName(isSetting.name());

                    if (setting.isPresent()) {
                        field.set(instance, setting.get());
                    } else {
                        isValid = false;
                    }
                }
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
