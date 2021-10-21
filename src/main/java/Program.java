import application.IApplication;
import application.dependencyinjection.ServiceProviderBuilder;
import modeldifference.*;
import modeldifference.calculator.*;
import modeldifference.htmloutput.HtmlOutput;
import modeldifference.htmloutput.IStateModelDifferenceJsonWidget;
import modeldifference.htmloutput.StateModelDifferenceJsonWidget;
import modeldifference.orient.*;
import modeldifference.orient.query.*;
import org.fruit.alayer.IStateManagementTags;
import org.fruit.alayer.IUIAMapping;
import org.fruit.alayer.IWdMapping;
import org.fruit.alayer.StateManagementTags;
import org.fruit.alayer.webdriver.enums.WdMapping;
import org.fruit.alayer.windows.UIAMapping;
import application.settings.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Program {

    public static void main(String[] args) {

        var settingsProvider = new SettingsProviderBuilder()
                .add(new ArgumentSettingsParser(args))
                .buildSettingsProvider();

        var isHelpQuested = settingsProvider.containsSetting("help");

        var serviceProviderBuilder = new ServiceProviderBuilder()
            .addSingleton(IStateModelDifferenceJsonWidget.class, StateModelDifferenceJsonWidget.class)
            .addSingleton(IWdMapping.class, WdMapping.class)
            .addSingleton(IUIAMapping.class, UIAMapping.class)
            .addSingleton(IStateManagementTags.class, StateManagementTags.class )
            .addSingleton(ISettingProvider.class, settingsProvider)
            .addSingleton(IOrientDbFactory.class, OrientDbFactory.class)
            .addSingleton(IModelApplicationBuilder.class, OrientDbApplicationBuilder.class)
            .addSingleton(IDifferenceCalculator.class, DifferenceCalculator.class)
            .addSingleton(IOutputDifferences.class, HtmlOutput.class)
            .addSingleton(IAbstractStateModelEntityQuery.class, AbstractStateModelEntityQuery.class)
            .addSingleton(IAbstractStateEntityQuery.class, AbstractStateEntityQuery.class)
            .addSingleton(IConcreteActionEntityQuery.class, ConcreteActionEntityQuery.class)
            .addSingleton(IAbstractActionEntityQuery.class, AbstractActionEntityQuery.class)
            .addSingleton(IConcreteStateEntityQuery.class, ConcreteStateEntityQuery.class)
            .addSingleton(IWidgetTreeQuery.class, WidgetTreeQuery.class)
            ;

        if (isHelpQuested){
            serviceProviderBuilder.addSingleton(IApplication.class, HelpApplication.class);
        }
        else
        {
            serviceProviderBuilder.addSingleton(IApplication.class, ModelDifferenceApplication.class);
        }

        var serviceProvider = serviceProviderBuilder.buildServiceProvider();

        try {
            var application = serviceProvider.getService(IApplication.class);
            application.Run();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}