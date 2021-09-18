package DependencyInjector;

import DependencyInjection.ServiceDescriptor;
import DependencyInjection.ServiceLifetime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceDescriptorTest {

    @Test
    public void creatingAServiceDescriptorWithObjectLifeTimeIsSINGLETON(){
        var sut = new ServiceDescriptor(ITestInterface.class, new TestClass());

        var expected = ServiceLifetime.SINGLETON;
        var actual = sut.getLifetime();

        assertEquals(expected, actual);
    }

    @Test
    public void havingATRANSIENTDescriptorSetInstanceDoesNotSaveTheObject(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.TRANSIENT);
        var testInstance = new TestClass();

        sut.setInstance(testInstance);

        assertFalse(sut.hasInstance());
    }

    @Test
    public void havingASINGLETONDescriptionsSetInstanceSavedValues(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.SINGLETON);
        var testInstance = new TestClass();

        sut.setInstance(testInstance);

        assertTrue(sut.hasInstance());
        var instanceForDescriptor = sut.getInstance();
        assertEquals(testInstance, instanceForDescriptor);
    }

    @Test
    public void setInstanceWhenAlreadyHaveAnInstanceThrowsException(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.SINGLETON);
        sut.setInstance(new TestClass());

        assertThrows(IllegalStateException.class, () -> {
            sut.setInstance(new TestClass());
        });
    }

    @Test
    public void getInstanceWithoutInstanceThrowsException(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.SINGLETON);

        assertThrows(IllegalStateException.class, () -> {
            sut.getInstance();
        });
    }
}
