package DependencyInjection;

import java.util.ArrayList;

public class ServiceProviderBuilder {

    private final ArrayList<ServiceDescriptor> register = new ArrayList<>();

    public <TInterface, TImplementation extends TInterface> ServiceProviderBuilder addSingleton(Class<TInterface> interfaceType, Class<TImplementation> implementationType) {
        register.add(new ServiceDescriptor(interfaceType,  implementationType, ServiceLifetime.SINGLETON));

        return this;
    }

    public <TInterface, TImplementation extends TInterface> ServiceProviderBuilder addSingleton(Class<TInterface> interfaceType, TImplementation implementation) {
        register.add(new ServiceDescriptor(interfaceType, implementation));

        return this;
    }

    public <TInterface, TImplementation extends TInterface> ServiceProviderBuilder addTransient(Class<TInterface> interfaceType, Class<TImplementation> implementationType) {
        register.add(new ServiceDescriptor(interfaceType,  implementationType, ServiceLifetime.TRANSIENT));

        return this;
    }

    public ServiceProvider BuildServiceProvider(){
        return new ServiceProvider(register);
    }
}
