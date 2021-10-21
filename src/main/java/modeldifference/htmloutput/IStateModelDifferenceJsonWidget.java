package modeldifference.htmloutput;

import modeldifference.models.AbstractState;
import org.fruit.alayer.Tag;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface IStateModelDifferenceJsonWidget {
    List<String> jsonWidgetTreeDifference(Set<Tag<?>> abstractAttributesTags, Path location, AbstractState dissStateModelOne, AbstractState newStateModelTwo);
}
