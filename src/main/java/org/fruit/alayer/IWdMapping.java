package org.fruit.alayer;

import java.util.Optional;

public interface IWdMapping {
    <T> Optional<Tag<T>> getMappedStateTag(Tag<T> mappedTag);
}
