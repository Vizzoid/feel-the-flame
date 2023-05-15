package org.vizzoid.utils;

public abstract class Keyable {

    String key;

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Keyable keyable)) return false;

        return getKey() != null ? getKey().equals(keyable.getKey()) : keyable.getKey() == null;
    }

    @Override
    public int hashCode() {
        return getKey() != null ? getKey().hashCode() : 0;
    }
}
