package org.vizzoid.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Value containing object that cannot contain if inside value cannot be found.
 * <p>
 * Modeled after java.util.Optional. Contains most of their methods with similar
 * semantics but also includes many methods and functions useful. Is also abstraction
 * friendly, and uses inheritance instead of conditionals.
 */
// lets be real, java.util.Optional is barren and needs something more :))))
public class Optional<T> {

    private static final Optional<?> EMPTY = new Optional<>() {

        @Override
        public @NotNull Object getValue() {
            throw new NoSuchElementException("No value set");
        }

        @Override
        public @NotNull Optional<Object> setValue(Object value) {
            return Optional.of(value);
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void onPresent(@NotNull Consumer<? super Object> onPresent) {
        }

        @Override
        public void onPresentOrElse(@NotNull Consumer<? super Object> onPresent, @NotNull Runnable onEmpty) {
            onEmpty.run();
        }

        @Override
        public @NotNull Optional<Object> remove() {
            return this;
        }

        @Override
        public @NotNull Optional<Object> removeIfShould(@NotNull Predicate<? super Object> ifShould) {
            return this;
        }

        @Override
        public @NotNull <T1> Optional<T1> map(@NotNull Function<? super Object, ? extends T1> mapper) {
            return empty();
        }

        @Override
        public @NotNull <T1> Optional<T1> flatMap(@NotNull Function<? super Object, ? extends Optional<? extends T1>> mapper) {
            return empty();
        }

        @Override
        public @NotNull Optional<Object> or(@NotNull Supplier<? extends Optional<?>> supplier) {
            //noinspection unchecked
            return (Optional<Object>) supplier.get();
        }

        @Override
        public @NotNull Stream<Object> stream() {
            return Stream.empty();
        }

        @Override
        @Contract("null -> _; !null -> !null")
        public @Nullable Object orValue(@Nullable Object value) {
            return value;
        }

        @Override
        public @NotNull Object orGet(@NotNull Supplier<?> supplier) {
            return supplier.get();
        }

        @Override
        public @NotNull <T1> Object orThrow(@NotNull Supplier<? extends T1> exceptionSupplier) {
            return exceptionSupplier.get();
        }

        @Override
        public @NotNull Optional<Object> substitute(@Nullable Object value) {
            return setValue(value);
        }

        @Override
        public @NotNull Optional<Object> substitute(boolean ifShould, @Nullable Object value) {
            if (ifShould) return substitute(value);
            return this;
        }

        @Override
        public @NotNull Optional<Object> substituteGet(@NotNull Supplier<Boolean> ifShould, @Nullable Object value) {
            return substitute(ifShould.get(), value);
        }

        @Override
        public int hashCode() {
            return 0;
        }
    };

    private T value; // SHOULD NOT BE CALLED DIRECTLY WITHIN THIS CLASS

    protected Optional() {

    }

    protected Optional(@NotNull T value) {
        this.value = value;
    }

    public static <T> @NotNull Optional<T> empty() {
        //noinspection unchecked
        return (Optional<T>) EMPTY;
    }
    /*
        Char
        Double
        Float
        Byte
        Int
        Long
        Short
     */

    public static @NotNull Optional<Character> asChar() {
        return empty();
    }

    public static @NotNull Optional<Double> asDouble() {
        return empty();
    }

    public static @NotNull Optional<Float> asFloat() {
        return empty();
    }

    public static @NotNull Optional<Byte> asByte() {
        return empty();
    }

    public static @NotNull Optional<Integer> asInt() {
        return empty();
    }

    public static @NotNull Optional<Long> asLong() {
        return empty();
    }

    public static @NotNull Optional<Short> asShort() {
        return empty();
    }

    public static <T> @NotNull Optional<T> of(@Nullable T value) {
        return value == null ? empty() : new Optional<>(value);
    }

    public @NotNull Optional<T> setValue(@Nullable T value) {
        if (value == null) return empty();
        this.value = value;
        return this;
    }

    public @NotNull T getValue() {
        return value;
    }

    public boolean isPresent() {
        return true;
    }

    public boolean isEmpty() {
        return false;
    }

    public void onPresent(@NotNull Consumer<? super T> onPresent) {
        onPresent.accept(getValue());
    }

    public void onPresentOrElse(@NotNull Consumer<? super T> onPresent, @NotNull Runnable onEmpty) {
        onPresent.accept(getValue());
    }

    public @NotNull Optional<T> remove() {
        return empty();
    }

    public @NotNull Optional<T> removeIfShould(@NotNull Predicate<? super T> ifShould) {
        if (ifShould.test(getValue())) return remove();
        return this;
    }

    public <T1> @NotNull Optional<T1> map(@NotNull Function<? super T, ? extends T1> mapper) {
        return of(mapper.apply(getValue()));
    }

    public <T1> @NotNull Optional<T1> flatMap(@NotNull Function<? super T, ? extends Optional<? extends T1>> mapper) {
        //noinspection unchecked
        return (Optional<T1>) mapper.apply(getValue());
    }

    public @NotNull Optional<T> or(@NotNull Supplier<? extends Optional<? extends T>> supplier) {
        return this;
    }

    public @NotNull Stream<T> stream() {
        return Stream.of(getValue());
    }

    @Contract("null -> _; !null -> !null")
    public @Nullable T orValue(@Nullable T value) {
        return getValue();
    }

    public @NotNull T orGet(@NotNull Supplier<? extends T> supplier) {
        return getValue();
    }

    // semantics, indifferent from getValue
    public @NotNull T orThrow() {
        return getValue();
    }

    // semantics, indifferent from getValue
    public <T1> @NotNull T orThrow(@NotNull Supplier<? extends T1> exceptionSupplier) {
        return orThrow();
    }

    /**
     * setValue only if not present
     */
    public @NotNull Optional<T> substitute(@Nullable T value) {
        return this;
    }

    /**
     * setValue only if not present and boolean is true
     */
    public @NotNull Optional<T> substitute(boolean ifShould, @Nullable T value) {
        return this;
    }

    /**
     * setValue only if not present and boolean is true
     */
    public @NotNull Optional<T> substituteGet(@NotNull Supplier<Boolean> ifShould, @Nullable T value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Optional<?> optional)) return false;

        return Objects.equals(value, optional.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
