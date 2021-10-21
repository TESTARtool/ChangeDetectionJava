package org.fruit.alayer;

import java.util.Optional;

public interface IUIAMapping {
    <T> Optional<Tag<T>> getMappedStateTag(Tag<T> mappedTag);
}
