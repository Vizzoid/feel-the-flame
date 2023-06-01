package org.vizzoid.utils.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandom<E> extends InputOutputRandom<AssignedEntry<E>, E> {

    protected final List<AssignedEntry<E>> entries;
    protected int totalWeight;

    public WeightedRandom(WeightedRandom<E> random) {
        this.entries = new ArrayList<>(random.entries);
        this.totalWeight = random.totalWeight;
    }

    @SafeVarargs
    public WeightedRandom(WeightedEntry<E>... entries) {
        this(List.of(entries));
    }

    public WeightedRandom(List<WeightedEntry<E>> entries) {
        this.entries = new ArrayList<>();
        int totalWeight = 0;
        int pastWeight = 0;
        for (WeightedEntry<E> entry : entries) {
            int entryWeight = entry.getWeight();
            if (entryWeight <= 0) entryWeight = 1;
            int weight = entryWeight + pastWeight;
            this.entries.add(new AssignedEntry<>(entry, weight, pastWeight));
            pastWeight = weight;
            totalWeight += entryWeight;
        }
        this.totalWeight = totalWeight;
    }

    @Override
    protected AssignedEntry<E> getFromRandom(Random random) {
        int result = random.nextInt(totalWeight);
        for (AssignedEntry<E> entry : entries) {
            if (entry.is(result)) return entry;
        }
        throw new UnsupportedOperationException("Result of " + result + " was not contained in weight entry list in " + this);
    }

    @Override
    public E next() {
        return next0().getEntry();
    }

    @Override
    public String toString() {
        return "WeightedRandom{" +
            "entries=" + entries +
            ", totalWeight=" + totalWeight +
            '}';
    }

    public List<AssignedEntry<E>> entries() {
        return entries;
    }

    public void removeEntry(AssignedEntry<E> entry) {
        entries.remove(entry);
        int totalWeight = 0;
        int pastWeight = 0;
        for (AssignedEntry<E> e : entries) {
            int entryWeight = e.getWeight();
            if (entryWeight <= 0) entryWeight = 1;
            int weight = entryWeight + pastWeight;

            e.pastWeight = pastWeight;
            e.assignedWeight = weight;

            pastWeight = weight;
            totalWeight += e.getWeight();
        }
        this.totalWeight = totalWeight;
    }

    public int getTotalWeight() {
        return totalWeight;
    }
}
