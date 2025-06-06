package nl.vu.cs.softwaredesign.domain;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class EmissionIterator<T extends Emissions> implements Iterator<T> {
    private final List<T> items;
    private final Predicate<T> filter;
    private int index = 0;

    public EmissionIterator(List<T> items) {
        this.items = items;
        this.filter = null;
    }

    public EmissionIterator(List<T> items, Predicate<T> filter) {
        this.items = items;
        this.filter = filter;
        advanceToNextValid();
    }

    private void advanceToNextValid() {
        if (filter != null) {
            while (index < items.size() && !filter.test(items.get(index))) {
                index++;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return index < items.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T nextItem = items.get(index++);
        if (filter != null) {
            advanceToNextValid();
        }
        return nextItem;
    }
}