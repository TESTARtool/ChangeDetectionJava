package modeldifference.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class IdentifierTests {

    @Test
    void AddedSameIdentifierToAHashSetResultInOneIdentifier()
    {
        var set = new HashSet<Identifier>();
        var id1 = new TestId("Hello");
        var id2 = new TestId("Hello");

        set.add(id1);
        var didAdd = set.add(id2);

        Assertions.assertFalse(didAdd);
    }

    @Test
    void SameIdFromDifferentTypeMustBeSaveInSet(){
        var set = new HashSet<Identifier>();
        var id1 = new TestId("Hello");
        var id2 = new OtherTestId("Hello");

        set.add(id1);
        var didAdd = set.add(id2);

        Assertions.assertTrue(didAdd);
        Assertions.assertEquals(0, set.size());
    }

    private class OtherTestId extends Identifier{
        public OtherTestId(String value) {
            super(value);
        }
    }

    private class TestId extends Identifier {
        public TestId(String value) {
            super(value);
        }
    }
}

