package org.vizzoid.zodomorf;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class Latice<T> implements Iterable<T> {

    private final int width;
    private final int height;
    private final int size;
    private Object[] latice;
    private T defaultValue;

    public Latice(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = width * height;
        this.latice = new Object[size];
    }

    public Object[] getLatice() {
        return latice;
    }

    public void clear() {
        latice = new Object[size];
    }

    public int getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public T get(double x, double y) {
        return get((int) x, (int) y);
    }

    public void set(double x, double y, T t) {
        set((int) x, (int) y, t);
    }

    public T get(int x, int y) {
        if (isOutOfBounds(x, y)) return defaultValue;
        return get(toIndex(x, y));
    }

    public T get(int index) {
        if (isOutOfBounds(index)) return defaultValue;
        Object tile = latice[index];
        if (tile == null) return defaultValue;
        return (T) tile;
    }

    public void set(int x, int y, T t) {
        if (isOutOfBounds(x, y)) return;
        set(toIndex(x, y), t);
    }

    public void set(int index, T t) {
        if (isOutOfBounds(index)) return;
        latice[index] = t;
    }

    public void fill(T t) {
        Arrays.fill(latice, t);
    }

    public void fillRow(int y, T t) {
        for (int x = 0; x < width; x++) {
            set(x, y, t);
        }
    }

    public void fillColumn(int x, T t) {
        for (int y = 0; y < height; y++) {
            set(x, y, t);
        }
    }

    public boolean isOutOfBounds(int index) {
        return index < 0 || index >= latice.length;
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= width || y >= height;
    }

    public int toIndex(int x, int y) {
        return (y * width) + x; // VERY IMPORANT INFORMATION: IF X IS ABOVE WIDTH IT WILL CAUSE A REPEAT OF TILES INCREASING Y BY ONE
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getHeight() {
        return height;
    }

    @NotNull
    @Override
    public LaticeIterator iterator() {
        return new LaticeIterator();
    }

    public class LaticeIterator implements Iterator<T> {

        public int cursor;

        public boolean hasNext() {
            return cursor != size;
        }

        public T next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            cursor = i + 1;
            Object o = latice[i];
            if (o == null) o = defaultValue;
            return (T) o;
        }
    }

}
