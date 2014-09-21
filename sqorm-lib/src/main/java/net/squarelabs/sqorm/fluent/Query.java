package net.squarelabs.sqorm.fluent;

import java.util.Collection;

public interface Query extends Iterable<Object> {

    public <T> Collection<T> all();

}
