package general;

import org.apache.commons.collections4.Factory;

import java.util.ArrayList;

public class Storage<T> {
    private final ArrayList<T> array = new ArrayList<>(1000);
    private final Factory<T> factory;

    public Storage(Factory<T> factory) {
        this.factory = factory;
    }

    public T get() {
        if (array.isEmpty()) {
            return factory.create();
        } else {
            return array.remove(array.size() - 1);
        }
    }

    public void dispose(T element) {
        array.add(element);
    }
}
