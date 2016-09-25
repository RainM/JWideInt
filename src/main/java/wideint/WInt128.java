package wideint;

import java.math.BigInteger;

/**
 *
 * @author Sergey Melnikov
 */
public class WInt128 extends Number implements Comparable<WInt128> {

    public static final int BITNESS = 128;
    public static final int NUM_BYTES = BITNESS / 8 /*Bitness of BYTE*/ ;
    
    private final byte[] __byte_data = new byte[NUM_BYTES];
    private double __double_data;

    
    public WInt128() {
        this.__double_data = 0.0;
    }

    public WInt128(WInt128 wi) {
        for (int i = 0; i < this.__byte_data.length; ++i) {
            this.__byte_data[i] = wi.__byte_data[i];
        }
        this.__double_data    = wi.__double_data;
    }

    public WInt128(long l) {
        __native_i128_setl(this.__byte_data, l);
        this.__double_data    = l;
    }

    public WInt128(int i) {
        __native_i128_setl(this.__byte_data, (long)i);
        this.__double_data    = i;
    }
    
    public WInt128(BigInteger bi) {
        int i = 0;
        byte[] beEncoding = bi.toByteArray();
        for (;i < beEncoding.length; ++i) {
            this.__byte_data[i] = beEncoding[beEncoding.length - i - 1];
        }
        for (;i < this.__byte_data.length;++i) {
            this.__byte_data[i] = 0;
        }
    }

    public void Add(WInt128 i) {
        __native_i128_add(this.__byte_data, i.__byte_data);
        this.__double_data += i.__double_data;
    }

    public void Sub(WInt128 i) {
        __native_i128_sub(this.__byte_data, i.__byte_data);
        this.__double_data -= i.__double_data;
    }

    public void Mul(WInt128 i) {
        __native_i128_mul(this.__byte_data, i.__byte_data);
        this.__double_data *= i.__double_data;
    }

    public void Div(WInt128 i) {
        __native_i128_div(this.__byte_data, i.__byte_data);
        this.__double_data /= i.__double_data;
    }

    @Override
    public int intValue() {
        return (int) __native_i128_to_long(this.__byte_data);
    }

    @Override
    public long longValue() {
        return __native_i128_to_long(this.__byte_data);
    }

    @Override
    public float floatValue() {
        return (float) __native_i128_to_double(this.__byte_data);
    }

    @Override
    public double doubleValue() {
        return __native_i128_to_double(this.__byte_data);
    }

    private static native void    __native_i128_setl     (byte[] a, long l);
    private static native void    __native_i128_add      (byte[] a, byte[] b);
    private static native void    __native_i128_sub      (byte[] a, byte[] b);
    private static native void    __native_i128_mul      (byte[] a, byte[] b);
    private static native void    __native_i128_div      (byte[] a, byte[] b);
    private static native long    __native_i128_to_long  (byte[] a);
    private static native double  __native_i128_to_double(byte[] a);

    @Override
    public int compareTo(WInt128 t) {
        return (int) (this.__double_data - t.__double_data);
    }

    public BigInteger toBigInteger() {
        byte[] beData = new byte[NUM_BYTES];
        for (int i = 0; i < this.__byte_data.length; ++i) {
            beData[i] = this.__byte_data[this.__byte_data.length - i - 1];
        }
        return new BigInteger(beData);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof WInt128) {
            WInt128 wi = (WInt128)o;
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
