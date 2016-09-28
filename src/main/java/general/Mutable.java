package general;

interface Mutable<T> {
    Mutable<T> copy();

    void dispose();
}
