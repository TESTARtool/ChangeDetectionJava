package modeldifference;

import application.settings.IsSetting;

public class ApplicationSettings {

    @IsSetting(name = "applicationName1", description = "Name of the old application")
    public String applicationName1;

    @IsSetting(name = "applicationVersion1", description = "Version of the old application")
    public String applicationVersion1;

    @IsSetting(name = "applicationName2", description = "Name of the newer application")
    public String applicationName2;

    @IsSetting(name = "applicationVersion2", description = "Version of the newer application")
    public String applicationVersion2;

}
