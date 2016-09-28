package general;

public interface Additive<T extends Additive<T>> extends Comparable<T> {
    T add(T other);

    T subtract(T other);

    T negate();

    T multiplyBy(int i);

    T divideBy(int i);

    boolean isZero();

    boolean isPositive();

    boolean isNonNegative();

    boolean isNegative();

    boolean isNonPositive();

    float signum();

    int sign();

    boolean isMaxValue();

    boolean isMinValue();

    default T abs() {
        return (T) (isPositive()
                ? this
                : negate());
    }

    default boolean isExtremeValue() {
        return isMaxValue() || isMinValue();
    }

    default boolean isGreaterThan(T other) {
        return compareTo(other) > 0;
    }

    default boolean isGreaterOrEqualThan(T other) {
        return compareTo(other) >= 0;
    }

    default boolean isLessThan(T other) {
        return compareTo(other) < 0;
    }

    default boolean isLessOrEqualThan(T other) {
        return compareTo(other) <= 0;
    }

    default boolean isClose(T other, T tolerance) {
        assert !(this instanceof Mutable);
        T distance = QUtils.distance((T) this, other);
        boolean result = distance.isLessOrEqualThan(tolerance);
        return result;
    }
}
