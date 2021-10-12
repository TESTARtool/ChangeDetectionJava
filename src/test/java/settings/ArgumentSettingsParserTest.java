package settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArgumentSettingsParserTest {
    @Test
    void ParseTwoNamedSettings(){
        var args = "-Setting1 hello -Setting2 world".split("\\s");
        var sut = new ArgumentSettingsParser(args);
        var settings = sut.getSettings();

        Assertions.assertEquals("hello", settings.get("Setting1"));
        Assertions.assertEquals("world", settings.get("Setting2"));
    }

    @Test
    void ParseEmptySettingToTrue(){
        var args = "-Setting1".split(" ");
        var sut = new ArgumentSettingsParser(args);
        var settings =  sut.getSettings();

        Assertions.assertEquals("true", settings.get("Setting1"));
    }

    @Test
    void EmptyAndNamedSettingsAreParsed(){
        var args = "-Setting1 -Setting hello".split(" ");
        var sut = new ArgumentSettingsParser(args);
        var settings= sut.getSettings();

        Assertions.assertEquals("true", settings.get("Setting1"));
        Assertions.assertEquals("hello", settings.get("Setting"));
    }
}
