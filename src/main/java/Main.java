import com.google.gson.*;
import dependencyinjection.ServiceProviderBuilder;
import modeldifference.*;
import modeldifference.calculator.*;
import modeldifference.htmloutput.HtmlOutput;
import modeldifference.models.AbstractActionId;
import modeldifference.models.Identifier;
import modeldifference.orient.*;
import modeldifference.orient.query.*;
import org.fruit.alayer.IStateManagementTags;
import org.fruit.alayer.IUIAMapping;
import org.fruit.alayer.StateManagementTags;
import org.fruit.alayer.windows.UIAMapping;
import settings.*;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    class IdentifierJsonSerializer implements JsonSerializer<Identifier>, JsonDeserializer<AbstractActionId>{

        public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getValue());
        }

        public AbstractActionId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()){
                return new AbstractActionId(json.getAsString());
            }

            return null;
        }
    }

    public static void ShowHelp(){
        System.out.println("this is not very helpful help");
    }

    public static void main(String[] args) throws Exception {

        Logger.getGlobal().setLevel(Level.SEVERE);

        var settingsProvider = new SettingsProviderBuilder()
                .add(new ArgumentSettingsParser(args))
                .buildSettingsProvider();

        var isHelpQuested = settingsProvider.containsSetting("help");

        if (isHelpQuested) {
            ShowHelp();
        } else {

            var serviceProvider = new ServiceProviderBuilder()
                    .addSingleton(IUIAMapping.class, UIAMapping.class)
                    .addSingleton(IStateManagementTags.class, StateManagementTags.class )
                    .addSingleton(IApplication.class, Application.class)
                    .addSingleton(ISettingProvider.class, settingsProvider)
                    .addSingleton(IOrientDbSetting.class, OrientDbSetting.class)
                    .addSingleton(IOrientDbFactory.class, OrientDbFactory.class)
                    .addSingleton(IApplicationBuilder.class, OrientDbApplicationBuilder.class)
                    .addSingleton(IDifferenceCalculator.class, DifferenceCalculator.class)
                    .addSingleton(IAbstractStateModelEntityQuery.class, AbstractStateModelEntityQuery.class)
                    .addSingleton(IAbstractStateEntityQuery.class, AbstractStateEntityQuery.class)
                    .addSingleton(IOutputDifferences.class, HtmlOutput.class)
                    .addSingleton(IConcreteActionEntityQuery.class, ConcreteActionEntityQuery.class)
                    .addSingleton(IAbstractActionEntityQuery.class, AbstractActionEntityQuery.class)
                    .addSingleton(IConcreteStateEntityQuery.class, ConcreteStateEntityQuery.class)
                    .buildServiceProvider();

            try {


                var application = serviceProvider.getService(IApplication.class);
                application.Run();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
           /*


            */

            //  var gson = new GsonBuilder()
            //          .setPrettyPrinting()
            //          .create();
            //  var json = gson.toJson(applicationVersion1);
            //  System.out.println(json);
            //             System.out.println("--------");
        }
    }
}