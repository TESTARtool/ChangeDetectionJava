package platform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamTests {

    private final ArrayList<String> callList = new ArrayList<>();

    @Test
    void findFirstDoesNotExecuteOtherItemsInTheCall(){
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

    @Test
    void testSameHash(){
        var list1 = new TreeSet<String>();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        list1.add("Four");

        var list2 = new TreeSet<String>();
        list2.add("One");
        list2.add("Two");
        list2.add("Three");
        list2.add("Four");

        var isSame = list1.containsAll(list2) && list2.containsAll(list1);
        Assertions.assertTrue(isSame);
    }

    @Test
    void retainAllRemoveItemsFromList1(){
        var list1 = new TreeSet<String>();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        list1.add("Four");

        var list2 = new TreeSet<String>();
        list2.add("Two");
        list2.add("Four");
        list2.add("Five");

       // var reati = list1.removeAll(list2);
        var sameStates = list1.retainAll(list2);
    }

    private int executeSomethingDifficult(String value){
        callList.add(value);
        return value.length();
    }
}
