package general;

public interface Q<T extends Q<T>> extends Additive<T> {
    T multiplyBy(T other);

    T divideBy(T other);

    T inverse();

    boolean isOne();
}
