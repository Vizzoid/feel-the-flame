package org.vizzoid.zodomorf;

import org.vizzoid.utils.random.IntDecider;

import java.util.Random;

public class TickTimer {

    private final Runnable runnable;
    private final IntDecider maxUntil;
    private int until;

    public TickTimer(Runnable runnable, int max) {
        this(runnable, IntDecider.always(max));
    }

    public TickTimer(Runnable runnable, int min, int max) {
        this(runnable, IntDecider.between(min, max));
    }

    public TickTimer(Runnable runnable, Random r, int min, int max) {
        this(runnable, IntDecider.between(r, min, max));
    }

    public TickTimer(Runnable runnable, IntDecider maxUntil) {
        this.runnable = runnable;
        this.maxUntil = maxUntil;
        resetTimer();
    }

    public void resetTimer() {
        this.until = this.maxUntil.next();
    }

    public void tick(long ticks) {
        //if ((until -= ticks) < 0) {
            resetTimer();
            runnable.run();
        //}
    }

}
