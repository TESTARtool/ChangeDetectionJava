package dependencyinjector;

    public class TestClass implements ITestInterface{

    public boolean firstConstructor;

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