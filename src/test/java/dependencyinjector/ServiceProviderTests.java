package dependencyinjector;

import application.dependencyinjection.ServiceProviderBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ServiceProviderTests {

    @Test
    void getServiceThrowsExceptionWhenTypeIsNotRegistered(){
        var sut = new ServiceProviderBuilder()
                .buildServiceProvider();

        var exception = assertThrows(IllegalStateException.class, () -> {
            sut.getService(String.class);
        });

        var expected = "No service for type 'java.lang.String' registered.";
        var actual  = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void getServiceCallsFirstConstructorBasedOnAmountOfParameters() throws Exception{
        var sut = new ServiceProviderBuilder()
                .addSingleton(ITestInterface.class, TestClass.class)
                .addSingleton(ISecondTestInterface.class, SecondTestClass.class)
                .buildServiceProvider();

        var service = (TestClass) sut.getService(ITestInterface.class);

        // first constructor is empty
        assertTrue(service.firstConstructor);
    }

    @Test
    void whenUnableToCreateInstanceTheFailedServiceIsNamedInTheException(){
        var sut2 = new ServiceProviderBuilder()
                .addSingleton(ISecondTestInterface.class, SecondTestClass.class)
                .buildServiceProvider();

        var exception = assertThrows(IllegalStateException.class, () -> {
            sut2.getService(ISecondTestInterface.class);
        });

        var expected = "Unable to resolve service for type 'dependencyinjector.ITestInterface' while attempting to activate 'dependencyinjector.ISecondTestInterface'.";
        var actual = exception.getMessage();

        assertEquals(expected, actual);
    }
}
