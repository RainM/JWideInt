package wideint;

/**
 *
 * @author Sergey Melnikov
 */
public class WInt extends Number implements Comparable<WInt> {

    private final long[] __w_int_data = new long[2];
    private double __float_data;

    public WInt() {
        this.__float_data = 0.0;
    }

    public WInt(WInt i) {
        this.__w_int_data[0] = i.__w_int_data[0];
        this.__w_int_data[1] = i.__w_int_data[1];
        this.__float_data    = i.__float_data;
    }

    public WInt(long l) {
        this.__w_int_data[0] = l;
        this.__float_data    = l;
    }

    public WInt(int i) {
        assert i >= 0;

        this.__w_int_data[0] = i;
        this.__float_data    = i;
    }

    public void Add(WInt i) {
        __native_add(this.__w_int_data, i.__w_int_data);
        this.__float_data += i.__float_data;
    }

    public void Sub(WInt i) {
        __native_sub(this.__w_int_data, i.__w_int_data);
        this.__float_data -= i.__float_data;
    }

    public void Mul(WInt i) {
        __native_mul(this.__w_int_data, i.__w_int_data);
        this.__float_data *= i.__float_data;
    }

    public void Div(WInt i) {
        __native_div(this.__w_int_data, i.__w_int_data);
        this.__float_data /= i.__float_data;
    }

    @Override
    public int intValue() {
        return (int) __native_to_long(this.__w_int_data);
    }

    @Override
    public long longValue() {
        return __native_to_long(this.__w_int_data);
    }

    @Override
    public float floatValue() {
        return (float) __native_to_double(this.__w_int_data);
    }

    @Override
    public double doubleValue() {
        return __native_to_double(this.__w_int_data);
    }

    private static native void __native_add(long[] a, long[] b);
    private static native void __native_sub(long[] a, long[] b);
    private static native void __native_mul(long[] a, long[] b);
    private static native void __native_div(long[] a, long[] b);
    private static native long    __native_to_long  (long[] a);
    private static native double  __native_to_double(long[] a);

    @Override
    public int compareTo(WInt t) {
        return (int) (this.__float_data - t.__float_data);
    }

    static {
        System.loadLibrary("wideint");
    }
}
