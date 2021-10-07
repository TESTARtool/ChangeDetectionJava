import com.google.gson.*;
import dependencyinjection.ServiceProviderBuilder;
import modeldifference.IApplicationBuilder;
import modeldifference.IOutputDifferences;
import modeldifference.calculator.*;
import modeldifference.htmloutput.HtmlOutput;
import modeldifference.models.AbstractActionId;
import modeldifference.models.Identifier;
import modeldifference.orient.*;
import modeldifference.orient.query.*;

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

    public static void main(String[] args) throws Exception {

        var serviceProvider = new ServiceProviderBuilder()
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
                .buildServiceProvider()
                ;


        var applicationBuilder = serviceProvider.getService(IApplicationBuilder.class);
        var applicationVersion1 = applicationBuilder.getApplication("exp", 1);
        var applicationVersion2 = applicationBuilder.getApplication("exp", 2);

        if (applicationVersion1.isEmpty()){
            var message = String.format("Unable to find application '%s' with version '%s'", "exp", 1);
            Logger.getLogger("Main").log(Level.SEVERE, message);
            return;
        }

        if (applicationVersion2.isEmpty()){
            var message = String.format("Unable to find application '%s' with version '%s'", "exp", 2);
            Logger.getLogger("Main").log(Level.SEVERE, message);
            return;
        }

        var differenceCalculator = serviceProvider.getService(IDifferenceCalculator.class);

        var differences = differenceCalculator.findApplicationDifferences(applicationVersion1.get(), applicationVersion2.get());

        var difOutputter = serviceProvider.getService(IOutputDifferences.class);

        difOutputter.output(differences);

      //  var gson = new GsonBuilder()
      //          .setPrettyPrinting()
      //          .create();
      //  var json = gson.toJson(applicationVersion1);
      //  System.out.println(json);
      //             System.out.println("--------");
    }
}