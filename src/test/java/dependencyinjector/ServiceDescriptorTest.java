package dependencyinjector;

import dependencyinjection.ServiceDescriptor;
import dependencyinjection.ServiceLifetime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDescriptorTest {

    @Test
    void creatingAServiceDescriptorWithObjectLifeTimeIsSINGLETON(){
        var sut = new ServiceDescriptor(ITestInterface.class, new TestClass());

        var expected = ServiceLifetime.SINGLETON;
        var actual = sut.getLifetime();

        assertEquals(expected, actual);
    }

    @Test
    void havingATRANSIENTDescriptorSetInstanceDoesNotSaveTheObject(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.TRANSIENT);
        var testInstance = new TestClass();

        sut.setInstance(testInstance);

        assertFalse(sut.hasInstance());
    }

    @Test
    void havingASINGLETONDescriptionsSetInstanceSavedValues(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.SINGLETON);
        var testInstance = new TestClass();

        sut.setInstance(testInstance);

        assertTrue(sut.hasInstance());
        var instanceForDescriptor = sut.getInstance();
        assertEquals(testInstance, instanceForDescriptor);
    }

    @Test
    void setInstanceWhenAlreadyHaveAnInstanceThrowsException(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.SINGLETON);
        sut.setInstance(new TestClass());

        var newInstance = new TestClass();

        assertThrows(IllegalStateException.class, () -> {
            sut.setInstance(newInstance);
        });
    }

    @Test
    void getInstanceWithoutInstanceThrowsException(){
        var sut = new ServiceDescriptor(ITestInterface.class, TestClass.class, ServiceLifetime.SINGLETON);

        assertThrows(IllegalStateException.class, () -> {
            sut.getInstance();
        });
    }
}
