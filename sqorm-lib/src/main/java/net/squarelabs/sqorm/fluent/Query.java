package net.squarelabs.sqorm.fluent;

import java.util.Collection;

public interface Query extends Iterable<Object> {

    public <T> T top();

    public <T> Collection<T> all();

}
