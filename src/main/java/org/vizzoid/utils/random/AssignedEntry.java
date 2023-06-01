package org.vizzoid.utils.random;

public class AssignedEntry<E> extends WeightedEntry<E> {

    private final WeightedEntry<E> entry;
    protected int assignedWeight;
    protected int pastWeight;

    public AssignedEntry(WeightedEntry<E> entry, int assignedWeight, int pastWeight) {
        super(entry.entrySupplier, entry.entry, entry.weight);
        this.entry = entry;
        this.assignedWeight = assignedWeight;
        this.pastWeight = pastWeight;
    }

    @Override
    public int getWeight() {
        return entry.getWeight();
    }

    public int getAssignedWeight() {
        return assignedWeight;
    }

    public int getPastWeight() {
        return pastWeight;
    }

    @Override
    public E getEntry() {
        return entry.getEntry();
    }

    public boolean is(int weight) {
        return weight >= pastWeight && weight < assignedWeight;
    }

    public WeightedEntry<E> original() {
        return entry;
    }

}
