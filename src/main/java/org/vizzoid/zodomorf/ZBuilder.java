package org.vizzoid.zodomorf;

import org.apache.commons.lang3.StringUtils;
import org.vizzoid.utils.IBuilder;
import org.vizzoid.zodomorf.engine.Images;

import java.awt.*;

public abstract class ZBuilder<K extends ZBuilder<K, T>, T> implements IBuilder<T> {

    public Image image;
    public String name;
    public String key;

    private K k() {
        //noinspection unchecked
        return (K) this;
    }

    public K image(Image image) {
        this.image = image;
        return k();
    }

    public K name(String name) {
        this.name = name;
        return k();
    }

    public K key(String key) {
        this.key = key;
        return image(Images.fromKey(key)).name(StringUtils.capitalize(key.replaceAll("_", " ")));
    }

}
