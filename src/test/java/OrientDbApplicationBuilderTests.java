import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OrientDbApplicationBuilderTests {

    private final OrientDbApplicationBuilder sut;

    public OrientDbApplicationBuilderTests(){
        sut = new OrientDbApplicationBuilder();
    }

    @Test
    public void whenApplicationCannotBeFoundGetApplicationReturnsEmpty(){
        var actual = sut.getApplication("not-found", 404);
        var expected = Optional.empty();

        Assertions.assertEquals(actual, expected);
    }
}
