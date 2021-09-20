import dependencyinjection.ServiceProviderBuilder;
import modeldifference.IApplicationBuilder;
import modeldifference.models.Application;
import modeldifference.orient.*;

import java.util.HashSet;
import java.util.Set;

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

    private String capitalizeWordsAndRemoveSpaces(String attribute) {
        String words[] = attribute.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder("");
        for(String w : words){
            String first = w.substring(0,1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase() + afterfirst);
        }
        return capitalizeWord.toString().trim();
    }
/*
    private boolean checkStateModelAbstractAttributes(Application application1, Application application2) {
        var abstractAttributesModelOne = application1.getAttributes(); //  modelDifferenceDatabase.abstractStateModelAbstractionAttributes(identifierModelOne);
        var abstractAttributesModelTwo = application2.getAttributes(); //  modelDifferenceDatabase.abstractStateModelAbstractionAttributes(identifierModelTwo);


        var xx = abstractAttributesModelTwo.retainAll(abstractAttributesModelOne);

        // IF Abstract Attributes are different, Abstract Layer is different and no sense to continue
        if(!abstractAttributesModelOne.equals(abstractAttributesModelTwo)) {
            System.out.println("\n ************************************************************************************ \n");
            System.out.println("ERROR: Abstract Attributes are different ");
            System.out.println("Model One: " + abstractAttributesModelOne);
            System.out.println("Model Two: " + abstractAttributesModelTwo);
            System.out.println("\n ************************************************************************************ \n");
            return false;
        } else {
            // Update Set object "abstractAttributesTags" with the Tags
            // we need to check for Widget Tree difference
            var stateModelTags = application1.getStates(); // abstractAttributesModelOne.split(",");

            // Transform the String of abstractAttributesTag into a StateManagementTag
            Set<Tag<?>> stateManagementTags = new HashSet<>();
            for(int i = 0; i < stateModelTags.length; i++) {
                String settingString = capitalizeWordsAndRemoveSpaces(stateModelTags[i]);
                stateManagementTags.add(StateManagementTags.getTagFromSettingsString(settingString));
            }

            // Now check UIAWindows and WdWebriver Maps
            // To extract the specific API Tag attribute that matches with the State Management Tag
            // It could match for both:
            // StateManagementTag: Widget title matches with UIAWindows: UIAName
            // StateManagementTag: Widget title matches with WdTag: WebGenericTitle
            for(Tag<?> stateManagementTag : stateManagementTags) {
                Tag<?> windowsTag = UIAMapping.getMappedStateTag(stateManagementTag);
                if(windowsTag != null) {
                    abstractAttributesTags.add(windowsTag);
                }
                Tag<?> webdriverTag = WdMapping.getMappedStateTag(stateManagementTag);
                if(webdriverTag != null) {
                    abstractAttributesTags.add(webdriverTag);
                }
            }
        }

        return true;
    }*/


}

