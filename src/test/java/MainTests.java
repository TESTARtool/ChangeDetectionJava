import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class MainTests {
    @Test
    void name() {
        int expected = 3;
        Main sut = new Main();
        var actual = sut.add(1,2);
        Assertions.assertEquals(expected, actual);
    }
}
