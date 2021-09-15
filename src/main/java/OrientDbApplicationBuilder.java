import Models.Application;

import java.util.Optional;

public class OrientDbApplicationBuilder implements IApplicationBuilder{

    public Optional<Application> getApplication(String applicationName, int version) {
        return Optional.empty();
    }
}
