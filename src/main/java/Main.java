import dependencyinjection.ServiceProviderBuilder;
import modeldifference.IApplicationBuilder;
import modeldifference.calculator.*;
import modeldifference.orient.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws Exception {

        var serviceProvider = new ServiceProviderBuilder()
                .addSingleton(IOrientDbSetting.class, OrientDbSetting.class)
                .addSingleton(IOrientDbFactory.class, OrientDbFactory.class)
                .addSingleton(IApplicationBuilder.class, OrientDbApplicationBuilder.class)
                .addSingleton(IDifferenceCalculator.class, DifferenceCalculator.class)
                .buildServiceProvider();


        var applicationBuilder = serviceProvider.getService(IApplicationBuilder.class);
        var applicationVersion1 = applicationBuilder.getApplication("exp", 1);
        var applicationVersion2 = applicationBuilder.getApplication("exp", 1111);

        if (applicationVersion1.isEmpty()){
            var message =String.format("Unable to find application '%s' with version '%s'", "exp", 1);
            Logger.getLogger("Main").log(Level.SEVERE, message);
            return;
        }

        if (applicationVersion2.isEmpty()){
            var message =String.format("Unable to find application '%s' with version '%s'", "exp", 1111);
            Logger.getLogger("Main").log(Level.SEVERE, message);
            return;
        }

        var differenceCalculator = serviceProvider.getService(IDifferenceCalculator.class);

        var differences = differenceCalculator.findApplicationDifferences(applicationVersion1.get(), applicationVersion2.get());


//                    var gson = new GsonBuilder()
//                            .setPrettyPrinting()
//                            .create();
//
//                    var json = gson.toJson(application);
//
//                    System.out.println(json);
//
//                    System.out.println("--------");

    }
}