package org.vizzoid.utils.engine;

public class MultiKeyListener implements KeyboardListener {

    private final SingleKeyListener[] listeners;

    public MultiKeyListener(SingleKeyListener... listeners) {
        this.listeners = listeners;
    }

    public void tick(long missedTime) {
        for (SingleKeyListener listener : listeners) {
            listener.tick(missedTime);
        }
    }

    public void attempt(int keycode, boolean set) {
        for (SingleKeyListener listener : listeners) {
            listener.attempt(keycode, set);
        }
    }


}
