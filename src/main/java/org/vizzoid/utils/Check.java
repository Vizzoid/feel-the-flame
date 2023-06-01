package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

/**
 * This class is a series of methods that will throw an error if the end result is opposite to what's expected
 */
public class Check {

    public static boolean valid(boolean bool, String reason) {
        if (!bool) throw new IllegalArgumentException(reason);
        return true;
    }

    public static boolean invalid(boolean bool, String reason) {
        if (bool) throw new IllegalArgumentException(reason);
        return false;
    }

    public static boolean valid(boolean bool) {
        if (!bool) throw new IllegalArgumentException();
        return true;
    }

    public static boolean invalid(boolean bool) {
        if (bool) throw new IllegalArgumentException();
        return false;
    }

    public static boolean valid(BooleanSupplier bool, String reason) {
        return valid(bool.getAsBoolean(), reason);
    }

    public static boolean invalid(BooleanSupplier bool, String reason) {
        return invalid(bool.getAsBoolean(), reason);
    }

    public static boolean valid(BooleanSupplier bool) {
        return valid(bool.getAsBoolean());
    }

    public static boolean invalid(BooleanSupplier bool) {
        return invalid(bool.getAsBoolean());
    }

    public static <T> @NotNull T notNull(T obj, String reason) {
        if (obj == null) throw new IllegalArgumentException(reason);
        return obj;
    }

    public static <T> T isNull(T obj, String reason) {
        if (obj != null) throw new IllegalArgumentException(reason);
        return null;
    }

    public static <T> @NotNull T notNull(T obj) {
        if (obj == null) throw new IllegalArgumentException();
        return obj;
    }

    public static <T> T isNull(T obj) {
        if (obj != null) throw new IllegalArgumentException();
        return null;
    }

    public static <T> T equal(T obj, T obj1, String reason) {
        if (!obj.equals(obj1)) throw new IllegalArgumentException(reason);
        return obj;
    }

    public static void notEqual(Object obj, Object obj1, String reason) {
        if (obj.equals(obj1)) throw new IllegalArgumentException(reason);
    }

    public static <T> T equal(T obj, T obj1) {
        if (!obj.equals(obj1)) throw new IllegalArgumentException();
        return obj;
    }

    public static void notEqual(Object obj, Object obj1) {
        if (obj.equals(obj1)) throw new IllegalArgumentException();
    }

    public static void higher(Number a, Number b, String reason) {
        if (a.doubleValue() <= b.doubleValue()) throw new IllegalArgumentException(reason);
    }

    public static void lower(Number a, Number b, String reason) {
        if (a.doubleValue() >= b.doubleValue()) throw new IllegalArgumentException(reason);
    }

    public static void higher(Number a, Number b) {
        if (a.doubleValue() <= b.doubleValue()) throw new IllegalArgumentException();
    }

    public static void lower(Number a, Number b) {
        if (a.doubleValue() >= b.doubleValue()) throw new IllegalArgumentException();
    }
    //


    public static void higher(short a, short b, String reason) {
        if (a <= b) throw new IllegalArgumentException(reason);
    }

    public static void lower(short a, short b, String reason) {
        if (a >= b) throw new IllegalArgumentException(reason);
    }

    public static void higher(short a, short b) {
        if (a <= b) throw new IllegalArgumentException();
    }

    public static void lower(short a, short b) {
        if (a >= b) throw new IllegalArgumentException();
    }

    public static void higher(int a, int b, String reason) {
        if (a <= b) throw new IllegalArgumentException(reason);
    }

    public static void lower(int a, int b, String reason) {
        if (a >= b) throw new IllegalArgumentException(reason);
    }

    public static void higher(int a, int b) {
        if (a <= b) throw new IllegalArgumentException();
    }

    public static void lower(int a, int b) {
        if (a >= b) throw new IllegalArgumentException();
    }

    public static void higher(float a, float b, String reason) {
        if (a <= b) throw new IllegalArgumentException(reason);
    }

    public static void lower(float a, float b, String reason) {
        if (a >= b) throw new IllegalArgumentException(reason);
    }

    public static void higher(float a, float b) {
        if (a <= b) throw new IllegalArgumentException();
    }

    public static void lower(float a, float b) {
        if (a >= b) throw new IllegalArgumentException();
    }

    public static void higher(long a, long b, String reason) {
        if (a <= b) throw new IllegalArgumentException(reason);
    }

    public static void lower(long a, long b, String reason) {
        if (a >= b) throw new IllegalArgumentException(reason);
    }

    public static void higher(long a, long b) {
        if (a <= b) throw new IllegalArgumentException();
    }

    public static void lower(long a, long b) {
        if (a >= b) throw new IllegalArgumentException();
    }

    public static void higher(double a, double b, String reason) {
        if (a <= b) throw new IllegalArgumentException(reason);
    }

    public static void lower(double a, double b, String reason) {
        if (a >= b) throw new IllegalArgumentException(reason);
    }

    public static void higher(double a, double b) {
        if (a <= b) throw new IllegalArgumentException();
    }

    public static void lower(double a, double b) {
        if (a >= b) throw new IllegalArgumentException();
    }
    //

    public static void higherOrEqual(Number a, Number b, String reason) {
        if (a.doubleValue() < b.doubleValue()) throw new IllegalArgumentException(reason);
    }

    public static void lowerOrEqual(Number a, Number b, String reason) {
        if (a.doubleValue() > b.doubleValue()) throw new IllegalArgumentException(reason);
    }

    public static void higherOrEqual(Number a, Number b) {
        if (a.doubleValue() < b.doubleValue()) throw new IllegalArgumentException();
    }

    public static void lowerOrEqual(Number a, Number b) {
        if (a.doubleValue() > b.doubleValue()) throw new IllegalArgumentException();
    }
    //


    public static void higherOrEqual(short a, short b, String reason) {
        if (a < b) throw new IllegalArgumentException(reason);
    }

    public static void lowerOrEqual(short a, short b, String reason) {
        if (a > b) throw new IllegalArgumentException(reason);
    }

    public static void higherOrEqual(short a, short b) {
        if (a < b) throw new IllegalArgumentException();
    }

    public static void lowerOrEqual(short a, short b) {
        if (a > b) throw new IllegalArgumentException();
    }

    public static void higherOrEqual(int a, int b, String reason) {
        if (a < b) throw new IllegalArgumentException(reason);
    }

    public static void lowerOrEqual(int a, int b, String reason) {
        if (a > b) throw new IllegalArgumentException(reason);
    }

    public static void higherOrEqual(int a, int b) {
        if (a < b) throw new IllegalArgumentException();
    }

    public static void lowerOrEqual(int a, int b) {
        if (a > b) throw new IllegalArgumentException();
    }

    public static void higherOrEqual(float a, float b, String reason) {
        if (a < b) throw new IllegalArgumentException(reason);
    }

    public static void lowerOrEqual(float a, float b, String reason) {
        if (a > b) throw new IllegalArgumentException(reason);
    }

    public static void higherOrEqual(float a, float b) {
        if (a < b) throw new IllegalArgumentException();
    }

    public static void lowerOrEqual(float a, float b) {
        if (a > b) throw new IllegalArgumentException();
    }

    public static void higherOrEqual(long a, long b, String reason) {
        if (a < b) throw new IllegalArgumentException(reason);
    }

    public static void lowerOrEqual(long a, long b, String reason) {
        if (a > b) throw new IllegalArgumentException(reason);
    }

    public static void higherOrEqual(long a, long b) {
        if (a < b) throw new IllegalArgumentException();
    }

    public static void lowerOrEqual(long a, long b) {
        if (a > b) throw new IllegalArgumentException();
    }

    public static void higherOrEqual(double a, double b, String reason) {
        if (a < b) throw new IllegalArgumentException(reason);
    }

    public static void lowerOrEqual(double a, double b, String reason) {
        if (a > b) throw new IllegalArgumentException(reason);
    }

    public static void higherOrEqual(double a, double b) {
        if (a < b) throw new IllegalArgumentException();
    }

    public static void lowerOrEqual(double a, double b) {
        if (a > b) throw new IllegalArgumentException();
    }

    //

    public static void negative(short a, String reason) {
        if (a >= 0) throw new IllegalArgumentException(reason);
    }

    public static void negative(short a) {
        if (a >= 0) throw new IllegalArgumentException();
    }

    public static void negative(int a, String reason) {
        if (a >= 0) throw new IllegalArgumentException(reason);
    }

    public static void negative(int a) {
        if (a >= 0) throw new IllegalArgumentException();
    }

    public static void negative(float a, String reason) {
        if (a >= 0) throw new IllegalArgumentException(reason);
    }

    public static void negative(float a) {
        if (a >= 0) throw new IllegalArgumentException();
    }

    public static void negative(long a, String reason) {
        if (a >= 0) throw new IllegalArgumentException(reason);
    }

    public static void negative(long a) {
        if (a >= 0) throw new IllegalArgumentException();
    }

    public static void negative(double a, String reason) {
        if (a >= 0) throw new IllegalArgumentException(reason);
    }

    public static void negative(double a) {
        if (a >= 0) throw new IllegalArgumentException();
    }
    //

    public static void negative(Number a, String reason) {
        if (a.doubleValue() >= 0) throw new IllegalArgumentException(reason);
    }

    public static void negative(Number a) {
        if (a.doubleValue() >= 0) throw new IllegalArgumentException();
    }

    //

    public static void positive(short a, String reason) {
        if (a < 0) throw new IllegalArgumentException(reason);
    }

    public static void positive(short a) {
        if (a < 0) throw new IllegalArgumentException();
    }

    public static void positive(int a, String reason) {
        if (a < 0) throw new IllegalArgumentException(reason);
    }

    public static void positive(int a) {
        if (a < 0) throw new IllegalArgumentException();
    }

    public static void positive(float a, String reason) {
        if (a < 0) throw new IllegalArgumentException(reason);
    }

    public static void positive(float a) {
        if (a < 0) throw new IllegalArgumentException();
    }

    public static void positive(long a, String reason) {
        if (a < 0) throw new IllegalArgumentException(reason);
    }

    public static void positive(long a) {
        if (a < 0) throw new IllegalArgumentException();
    }

    public static void positive(double a, String reason) {
        if (a < 0) throw new IllegalArgumentException(reason);
    }

    public static void positive(double a) {
        if (a < 0) throw new IllegalArgumentException();
    }
    //

    public static void positive(Number a, String reason) {
        if (a.doubleValue() < 0) throw new IllegalArgumentException(reason);
    }

    public static void positive(Number a) {
        if (a.doubleValue() < 0) throw new IllegalArgumentException();
    }

}
