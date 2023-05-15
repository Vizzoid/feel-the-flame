package org.vizzoid.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public class PresetMap<T> {

    private final String defaultKey;
    private final HashMap<String, T> byKey = new HashMap<>();
    private final HashMap<T, String> byValue = new HashMap<>();
    private final ArrayList<T> byOrdinal = new ArrayList<>();
    private boolean closed = false;
    private final String category;

    public PresetMap() {
        this("");
    }

    public PresetMap(String category) {
        this(category, "null");
    }

    public PresetMap(String category, String defaultKey) {
        this.category = category.isEmpty() || category.endsWith(".") ? category : category + ".";
        this.defaultKey = defaultKey;
    }

    public <T1 extends T> T1 put(T1 preset) {
        if (preset instanceof Keyed key) return put(key.getKey(), preset);
        new UnsupportedOperationException("No key given to non-Keyed object.").printStackTrace();
        return put(defaultKey, preset);
    }

    public <T1 extends T> T1 put(String key, T1 preset) {
        if (closed) throw new UnsupportedOperationException("Map is closed");
        key = key.toLowerCase(Locale.ROOT);
        key = key.replaceAll(" ", "_");

        if (!key.startsWith(category)) key = category + key;
        byKey.put(key, preset);
        byValue.put(preset, key);
        byOrdinal.add(preset);
        if (preset instanceof Keyable keyable) keyable.key = key;
        return preset;
    }

    public <T1 extends T> T1 put(String key, IBuilder<T1> preset) {
        return put(key, preset.build());
    }

    public <T1 extends T> T1 put(IBuilder<T1> preset) {
        return put(preset.build());
    }

    public <T1 extends T> T1 remove(T1 preset) {
        if (preset instanceof org.vizzoid.utils.Keyed key) return remove(key.getKey(), preset);
        new UnsupportedOperationException("No key given to non-Keyed object.").printStackTrace();
        return remove(byValue.get(preset), preset);
    }

    public <T1 extends T> T1 remove(String key, T1 preset) {
        if (closed) throw new UnsupportedOperationException("Map is closed");
        byKey.remove(key, preset);
        byValue.remove(preset, key);
        byOrdinal.remove(preset);
        return preset;
    }

    public T get(String key) {
        if (key == null) return null;
        return byKey.get(key.toLowerCase(Locale.ROOT));
    }

    public String getKey(T preset) {
        if (preset == null) return null;
        return byValue.get(preset);
    }

    public T get(int index) {
        return byOrdinal.get(index);
    }

    public Collection<? extends T> values() {
        return byOrdinal;
    }

    public T[] array(T[] array) {
        return byOrdinal.toArray(array);
    }

    public void close() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return "PresetMap{" +
            "byKey=" + byKey +
            '}';
    }
}
