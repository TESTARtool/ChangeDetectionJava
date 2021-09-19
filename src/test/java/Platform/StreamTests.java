package Platform;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTests {

    private final ArrayList<String> callList = new ArrayList<>();

    @Test
    public void findFirstDoesNotExecuteOtherItemsInTheCall(){
        // I expect that findFirst would not execute the whole stream
        // but that it will execute items until the requested item is found.
        var list = new ArrayList<String>();
        list.add("Alpha");
        list.add("Beta");
        list.add("Gamma");

        var lenght = list.stream()
            .map(x -> executeSomethingDifficult(x))
            .findFirst();

        var actual = lenght.get();
        var expected = 5;

        assertEquals(expected, actual);

        // now the test
        assertEquals(1, callList.size());
    }

    private int executeSomethingDifficult(String value){
        callList.add(value);
        return value.length();
    }
}
