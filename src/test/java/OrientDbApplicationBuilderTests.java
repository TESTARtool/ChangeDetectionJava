import modeldifference.orient.IOrientDbFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;

public class OrientDbApplicationBuilderTests {

  //  private final ModelDifference.OrientDb.OrientDbApplicationBuilder sut;

    public OrientDbApplicationBuilderTests(){
        var factoryMock = mock(IOrientDbFactory.class);
        //sut = new ModelDifference.OrientDb.OrientDbApplicationBuilder(factoryMock);
    }

    @Test
    public void whenApplicationCannotBeFoundGetApplicationReturnsEmpty(){
        //var actual = sut.getApplication("not-found", 404);
        var expected = Optional.empty();

       // Assertions.assertEquals(actual, expected);
    }
}
