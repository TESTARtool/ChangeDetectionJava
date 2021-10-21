package application.dependencyinjection;

import java.util.ArrayList;

public class ServiceProviderBuilder {

    private final ArrayList<ServiceDescriptor> register = new ArrayList<>();

    public <I, T extends I> ServiceProviderBuilder addSingleton(Class<I> interfaceType, Class<T> implementationType) {
        register.add(new ServiceDescriptor(interfaceType,  implementationType, ServiceLifetime.SINGLETON));

        return this;
    }

    public <I, T extends I> ServiceProviderBuilder addSingleton(Class<I> interfaceType, T implementation) {
        register.add(new ServiceDescriptor(interfaceType, implementation));

        return this;
    }

    public <I, T extends I> ServiceProviderBuilder addTransient(Class<I> interfaceType, Class<T> implementationType) {
        register.add(new ServiceDescriptor(interfaceType,  implementationType, ServiceLifetime.TRANSIENT));

        return this;
    }

    public ServiceProvider buildServiceProvider(){
        return new ServiceProvider(register);
    }
}
