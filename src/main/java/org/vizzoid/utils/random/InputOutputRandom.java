package org.vizzoid.utils.random;

import org.vizzoid.utils.Check;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class InputOutputRandom<E_INPUT, E_OUTPUT> implements IRandom<E_OUTPUT> {

    private final boolean controlled;
    protected E_INPUT target;
    protected int maxTries;
    protected int minTries;
    protected int currentTry;

    public InputOutputRandom() {
        this(null, 0, false);
    }

    public InputOutputRandom(E_INPUT target, int maxTries) {
        this(target, maxTries, true);
    }

    public InputOutputRandom(E_INPUT target, int maxTries, int minTries) {
        this(target, maxTries, minTries, true);
    }

    protected InputOutputRandom(E_INPUT target, int maxTries, boolean controlled) {
        this(target, maxTries, 0, controlled);
    }

    protected InputOutputRandom(E_INPUT target, int maxTries, int minTries, boolean controlled) {
        if (controlled) Check.higher(maxTries, minTries);

        this.target = target;
        this.maxTries = maxTries;
        this.minTries = minTries;
        this.controlled = controlled;
    }

    /**
     * TARGET SHOULD BE IMMUTABLE
     *
     * @return target
     */
    public E_INPUT getTarget() {
        return target;
    }

    public int getMaxTries() {
        return maxTries;
    }

    public int getMinTries() {
        return minTries;
    }

    public int getCurrentTry() {
        return currentTry;
    }

    public void setCurrentTry(int currentTry) {
        this.currentTry = currentTry;
    }

    /**
     * Pulls object from our controlled random.
     * If object is controlled, it will run through several tests.
     * If random object equals our target, if the number of tries is under the minimum limit, it will randomize again
     * Otherwise, the number of tries is reset and object is returned.
     * If the object does not equal our target, our try count is increased by one
     * If the try count is above or equal to maximum try limit, our target will be returned, and the count will be reset
     * IMPORTANT: THE TARGET SHOULD BE IMMUTABLE
     *
     * @return random object based on input
     */
    @SuppressWarnings("unchecked")
    public E_OUTPUT next() {
        return (E_OUTPUT) next0();
    }

    protected E_INPUT next0() {
        E_INPUT t = getFromRandom(ThreadLocalRandom.current());
        if (controlled) {
            if (!t.equals(target)) {
                currentTry++;
                if (maxTries <= currentTry) {
                    currentTry = 0;
                    return target;
                }
            } else {
                if (currentTry < minTries) return next0(); // always wanted to use this
                else currentTry = 0;
            }
        }
        return t;
    }

    protected abstract E_INPUT getFromRandom(Random random);

}
