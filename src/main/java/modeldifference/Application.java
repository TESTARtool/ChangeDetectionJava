package modeldifference;

import modeldifference.calculator.IDifferenceCalculator;
import settings.ISettingProvider;
import settings.ISettingsFor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application implements IApplication {
    private final ISettingProvider settingProvider;
    private final IApplicationBuilder applicationBuilder;
    private final IDifferenceCalculator differenceCalculator;
    private final IOutputDifferences outputDifferences;

    public Application(ISettingProvider settingProvider, IApplicationBuilder applicationBuilder, IDifferenceCalculator differenceCalculator, IOutputDifferences outputDifferences){
        this.settingProvider = settingProvider;
        this.applicationBuilder = applicationBuilder;
        this.differenceCalculator = differenceCalculator;
        this.outputDifferences = outputDifferences;
    }

    public void Run() throws Exception {

        var applicationSettings = settingProvider.resolve(ApplicationSettings.class);

        if (!applicationSettings.isValid()){
            Logger.getLogger("Application").log(Level.SEVERE, "Application settings are invalid");
            return;
        }

        var settings = applicationSettings.getValue();

        var application1 = applicationBuilder.getApplication(settings.applicationName1, Integer.parseInt(settings.applicationVersion1));
        var application2 = applicationBuilder.getApplication(settings.applicationName2, Integer.parseInt(settings.applicationVersion2));

        if (application1.isEmpty()) {
            var message = String.format("Unable to find application '%s' with version '%s'", settings.applicationName1, settings.applicationVersion1);
            Logger.getLogger("Main").log(Level.SEVERE, message);
            return;
        }

        if (application2.isEmpty()) {
            var message = String.format("Unable to find application '%s' with version '%s'", settings.applicationName2, settings.applicationVersion2);
            Logger.getLogger("Main").log(Level.SEVERE, message);
            return;
        }

        var differences = differenceCalculator.findApplicationDifferences(application1.get(), application2.get());

        outputDifferences.output(differences);
    }
}
