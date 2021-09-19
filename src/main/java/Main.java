import dependencyinjection.ServiceProviderBuilder;
import modeldifference.IApplicationBuilder;
import modeldifference.orient.*;

public class Main {

    public static void main(String[] args) throws Exception {

        var serviceProvider = new ServiceProviderBuilder()
                .addSingleton(IOrientDbSetting.class, OrientDbSetting.class)
                .addSingleton(IOrientDbFactory.class, OrientDbFactory.class)
                .addSingleton(IApplicationBuilder.class, OrientDbApplicationBuilder.class)
                .buildServiceProvider();


        var applicationBuilder = serviceProvider.getService(IApplicationBuilder.class);
        var applicationVersion1 = applicationBuilder.getApplication("exp", 1);
        var applicationVersion2 = applicationBuilder.getApplication("exp",2);




//
//
//
//
//                    var gson = new GsonBuilder()
//                            .setPrettyPrinting()
//                            .create();
//
//                    var json = gson.toJson(application);
//
//                    System.out.println(json);
//
//                    System.out.println("--------");
//
//
//
//                }
//                else {
//                    System.out.println("Exp : 1 not found in database");
//                }
//            }
//        }
    }


}

