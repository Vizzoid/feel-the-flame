package org.vizzoid.utils.random;

import java.util.function.Supplier;

public class WeightedEntry<E> {

    final Supplier<E> entrySupplier;
    final E entry;
    final int weight;

    public WeightedEntry(Supplier<E> entrySupplier, E entry, int weight) {
        this.entrySupplier = entrySupplier;
        this.entry = entry;
        this.weight = weight;
    }

    public WeightedEntry(E entry, int weight) {
        this(null, entry, weight);
    }

    public WeightedEntry(Supplier<E> entry, int weight) {
        this(entry, null, weight);
    }

    public WeightedEntry(E entry) {
        this(null, entry, -1);
    }

    public WeightedEntry(Supplier<E> entry) {
        this(entry, null, -1);
    }

    public E getEntry() {
        return entry != null ? entry : entrySupplier.get();
    }

    public int getWeight() {
        return weight;
    }
}
