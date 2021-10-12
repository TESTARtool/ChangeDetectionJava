package dependencyinjection;

import settings.ISettingProvider;
import settings.ISettingsFor;
import settings.ISettingsForInternal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceProvider {

    private final List<ServiceDescriptor> services;

    public ServiceProvider(List<ServiceDescriptor> services){
        this.services = services;
    }

    private ServiceDescriptor getServiceDescriptor(Class<?> interfaceType) throws IllegalStateException{
        var result = services.stream()
                .filter(x -> x.getInterfaceType() == interfaceType)
                .findFirst();

        if (!result.isPresent()){
            throw new IllegalStateException("No service for type '" + interfaceType.getName() + "' registered.");
        }

        return result.get();
    }

    public <I> I getService(Class<I> interfaceType) throws IllegalStateException{
        var serviceDescriptor = getServiceDescriptor(interfaceType);

        if (serviceDescriptor.hasInstance()) {
            return (I) serviceDescriptor.getInstance();
        }

        try {
            var instance = (I) createInstance(serviceDescriptor);
            serviceDescriptor.setInstance(instance);
            return instance;
        }
        catch(FailedServiceException ex) {
            throw new IllegalStateException("Unable to resolve service for type '" + ex.getFailedService() + "' while attempting to activate '" + interfaceType.getName() + "'.");
        }
        catch(Exception ex){
            throw new IllegalStateException("Exception while activating '"+ interfaceType.getName() +"'");
        }
    }

    private Object createInstance(ServiceDescriptor serviceDescriptor)
            throws NoSuchMethodException, SecurityException,InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var implementation = serviceDescriptor.getImplementationType();
        var constructors = implementation.getConstructors();
        var parametersLists = Arrays.stream(constructors)
                .map(Constructor::getParameterTypes)
                .sorted((x1, x2) -> x1.length - x2.length)
                .collect(Collectors.toList());

        var parameters = parametersLists.get(0);

        var resolvedItems = Arrays.stream(parameters)
                .map(this::mapServiceToInstance)
                .toArray();

        return implementation.getDeclaredConstructor(parameters).newInstance(resolvedItems);
    }

    private Object mapServiceToInstance(Class<?> x) {
        try {
             return getService(x);
        } catch (Exception ex) {
            throw new FailedServiceException(x.getName());
        }
    }

    private class FailedServiceException extends RuntimeException{
        private final String failedService;

        public FailedServiceException(String failedService){

            this.failedService = failedService;
        }

        public String getFailedService(){
            return this.failedService;
        }
    }
}