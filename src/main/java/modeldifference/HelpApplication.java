package modeldifference;

import application.IApplication;
import application.settings.IsSetting;
import modeldifference.orient.OrientDbSetting;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpApplication implements IApplication {



    public void Run() throws Exception {
        System.out.println("TESTAR Model Difference application");
        System.out.println("");
        System.out.println("Parameters list:");

        printSettings(ApplicationSettings.class);
        printSettings(OrientDbSetting.class);
    }

    private <T> void printSettings(Class<T> settingObject) throws Exception {
       //  var instance =  (T) settingObject.getConstructor().newInstance();
        for (var field : settingObject.getDeclaredFields()) {
            if (field.isAnnotationPresent(IsSetting.class)) {
                var isSetting = field.getAnnotation(IsSetting.class);

                System.out.println("-" + isSetting.name() + ": " + isSetting.description() );
            }
        }
    }

}
