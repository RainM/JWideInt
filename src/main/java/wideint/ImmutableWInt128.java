package wideint;

import general.Q;

import java.math.BigInteger;

/**
 * @author Sergey Melnikov
 */
public class ImmutableWInt128 extends Number implements Q<ImmutableWInt128> {
    private static final int BITNESS = 128;
    private static final int NUM_BYTES = BITNESS / 8 /*Bitness of BYTE*/;
    private static final ImmutableWInt128 ZERO = new ImmutableWInt128(0);
    private static final ImmutableWInt128 ONE = new ImmutableWInt128(1);
    private static final ImmutableWInt128 MAX_VALUE = new ImmutableWInt128(Long.MAX_VALUE);
    private static final ImmutableWInt128 MIN_VALUE = MAX_VALUE.negate();
    private final byte[] __byte_data;
    private final double __double_data;

    private ImmutableWInt128(byte[] byte_data, double double_data) {
        this.__byte_data = byte_data;
        this.__double_data = double_data;
    }

    private ImmutableWInt128(byte[] byte_data) {
        this(byte_data, WInt128.__native_i128_to_double(byte_data));
    }

    public ImmutableWInt128() {
        this(createBytes());
    }

    private static byte[] createBytes() {
        return new byte[NUM_BYTES];
    }

    public ImmutableWInt128(ImmutableWInt128 other) {
        this.__byte_data = createBytes();
        System.arraycopy(other.__byte_data, 0, this.__byte_data, 0, this.__byte_data.length);
        this.__double_data = other.__double_data;
    }

    public ImmutableWInt128(long l) {
        this.__byte_data = createBytes();
        WInt128.__native_i128_setl(this.__byte_data, l);
        this.__double_data = l;
    }

    public ImmutableWInt128(BigInteger bi) {
        this.__byte_data = createBytes();
        int i = 0;
        byte[] beEncoding = bi.toByteArray();
        for (; i < beEncoding.length; ++i) {
            this.__byte_data[i] = beEncoding[beEncoding.length - i - 1];
        }
        for (; i < this.__byte_data.length; ++i) {
            this.__byte_data[i] = 0;
        }
        this.__double_data = bi.doubleValue();
    }

    private byte[] copyBytes(byte[] byteData) {
        byte[] result = createBytes();
        System.arraycopy(byteData, 0, result, 0, NUM_BYTES);
        return result;
    }

    @Override
    public ImmutableWInt128 add(ImmutableWInt128 other) {
        byte[] byte_data = copyBytes(__byte_data);
        WInt128.__native_i128_add(byte_data, other.__byte_data);
        return new ImmutableWInt128(byte_data, __double_data + other.__double_data);
    }

    @Override
    public ImmutableWInt128 subtract(ImmutableWInt128 other) {
        byte[] byte_data = copyBytes(__byte_data);
        WInt128.__native_i128_sub(byte_data, other.__byte_data);
        return new ImmutableWInt128(byte_data, __double_data - other.__double_data);
    }

    @Override
    public ImmutableWInt128 negate() {
        return ZERO.subtract(this);
    }

    @Override
    public ImmutableWInt128 multiplyBy(int i) {
        return multiplyBy(new ImmutableWInt128(i));
    }

    @Override
    public ImmutableWInt128 divideBy(int i) {
        return divideBy(new ImmutableWInt128(i));
    }

    @Override
    public boolean isZero() {
        return equals(ZERO);
    }

    @Override
    public boolean isPositive() {
        return __double_data > 0;
    }

    @Override
    public boolean isNonNegative() {
        return __double_data >= 0;
    }

    @Override
    public boolean isNegative() {
        return __double_data < 0;
    }

    @Override
    public boolean isNonPositive() {
        return __double_data <= 0;
    }

    @Override
    public float signum() {
        return floatValue();
    }

    @Override
    public int sign() {
        return __double_data > 0
                ? 1
                : __double_data < 0
                        ? -1
                        : 0;
    }

    @Override
    public boolean isMaxValue() {
        return false;
    }

    @Override
    public boolean isMinValue() {
        return false;
    }

    @Override
    public ImmutableWInt128 multiplyBy(ImmutableWInt128 other) {
        byte[] result_bytes = copyBytes(__byte_data);
        WInt128.__native_i128_mul(result_bytes, other.__byte_data);
        return new ImmutableWInt128(result_bytes, __double_data * other.__double_data);
    }

    @Override
    public ImmutableWInt128 divideBy(ImmutableWInt128 other) {
        byte[] result_bytes = copyBytes(__byte_data);
        WInt128.__native_i128_div(result_bytes, other.__byte_data);
        return new ImmutableWInt128(result_bytes, __double_data / other.__double_data);
    }

    @Override
    public ImmutableWInt128 inverse() {
        return ONE.divideBy(this);
    }

    @Override
    public boolean isOne() {
        return equals(ONE);
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return WInt128.__native_i128_to_long(this.__byte_data);
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return WInt128.__native_i128_to_double(this.__byte_data);
    }

    @Override
    public int compareTo(ImmutableWInt128 t) {
        return (int) (this.__double_data - t.__double_data);
    }

    public BigInteger toBigInteger() {
        byte[] beData = createBytes();
        for (int i = 0; i < this.__byte_data.length; ++i) {
            beData[i] = this.__byte_data[this.__byte_data.length - i - 1];
        }
        return new BigInteger(beData);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImmutableWInt128) {
            ImmutableWInt128 wi = (ImmutableWInt128) o;
            for (int i = 0; i < wi.__byte_data.length; ++i) {
                if (wi.__byte_data[i] != this.__byte_data[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static {
        Loader.LoadLibrary("wideint");
    }
}
