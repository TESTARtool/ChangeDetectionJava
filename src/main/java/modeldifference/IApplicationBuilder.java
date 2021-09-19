package modeldifference;

import modeldifference.models.Application;

import java.util.Optional;

public interface IApplicationBuilder {
        Optional<Application> getApplication(String applicationName, int version);
}
