package general;

public class QUtils {
    public static <T extends Additive<T>> T min(T x, T y) {
        return x.isLessOrEqualThan(y)
                ? x
                : y;
    }

    public static <T extends Additive<T>> T max(T x, T y) {
        return x.isGreaterOrEqualThan(y)
                ? x
                : y;
    }

    public static <T extends Additive<T>> T average(T x, T y) {
        T sum = x.add(y);
        T result = sum.divideBy(2);
        assert !(sum instanceof Mutable);
        return result;
    }

//    public static <T, Z extends MutableAdditive<T>> Z average(Z x, Z y) {
//        MutableAdditive<T> result = x.add(y);
//        result.divideSelfBy(2);
//        return (Z) result;
//    }

    public static <T extends Additive<T>> T distance(T x, T y) {
        T difference = x.subtract(y);
        T result = difference.abs();
        assert !(difference instanceof Mutable);
        return result;
    }

//    public static <T extends MutableAdditive<T>> Z distance(Z x, Z y) {
//        MutableAdditive<T> result = x.subtract(y);
//        result.absSelf();
//        return (Z) result;
//    }

    public static <T extends Q<T>> T random(DoubleFactory<T> factory) {
        return factory.create(Math.random());
    }

    public interface DoubleFactory<T extends Q<T>> {
        T create(double value);
    }
    //    private static void test() {
//        ImmutableIntByte.ImmutableIntByte x = ImmutableIntByte.ImmutableIntByte.create(10, 2); // 1000
//        ImmutableIntByte.ImmutableIntByte y = ImmutableIntByte.ImmutableIntByte.create(123, -2); // 1.23
//        average(x, y);
//    }
}
