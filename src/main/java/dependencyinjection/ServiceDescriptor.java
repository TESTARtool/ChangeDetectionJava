package dependencyinjection;
import java.util.Optional;

public class ServiceDescriptor{

    private final Class<?> interfaceType;
    private final Class<?> implementationType;
    private final ServiceLifetime lifetime;
    private Optional<Object> instance;

    public <I, T extends I> ServiceDescriptor(Class<I> interfaceType, Class<T> implementationType, ServiceLifetime lifetime){
        this.interfaceType = interfaceType;
        this.implementationType = implementationType;
        this.lifetime = lifetime;
        this.instance = Optional.empty();
    }

    public <I, T extends I> ServiceDescriptor (Class<I> interfaceType, T implementation ){
        this.interfaceType = interfaceType;
        this.implementationType = implementation.getClass();
        this.lifetime = ServiceLifetime.SINGLETON;
        this.instance = Optional.of(implementation);
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public Class<?> getImplementationType() {
        return implementationType;
    }

    public Object getInstance(){
        if (!instance.isPresent()){
            throw new IllegalStateException("Instance has not been set");
        }

        return instance.get();
    }

    public boolean hasInstance(){
        return instance.isPresent();
    }

    public ServiceLifetime getLifetime(){
        return this.lifetime;
    }

    public void setInstance(Object instance){

        if (hasInstance()){
            throw new IllegalStateException("Instance is already set");
        }

        if (lifetime == ServiceLifetime.SINGLETON) {
            this.instance = Optional.of(instance);
        }
    }
}
