package DependencyInjector;

    public class TestClass implements ITestInterface{

    public boolean firstConstructor;
    public boolean secondConstructor;

    public TestClass(){
        firstConstructor = true;
    }

    public TestClass(String value){
        firstConstructor = true;
    }

    public boolean forTest() {
        return false;
    }
}