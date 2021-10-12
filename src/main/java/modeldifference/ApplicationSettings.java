package modeldifference;

import settings.IsSetting;

public class ApplicationSettings {

    @IsSetting(name = "applicationName1")
    public String applicationName1;

    @IsSetting(name = "applicationVersion1")
    public int applicationVersion1;

    @IsSetting(name = "applicationName2")
    public String applicationName2;

    @IsSetting(name = "applicationVersion2")
    public int applicationVersion2;

}
