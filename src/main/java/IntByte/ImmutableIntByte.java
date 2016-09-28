package IntByte;

import general.Q;
import general.Utils;
import singlelong.ImmutableSingleLong;

import static general.Utils.powersOf10;
import static java.lang.Math.multiplyExact;
import static singlelong.ImmutableSingleLong.PRECISION;

/**
 * value * 10^power
 */
public class ImmutableIntByte extends Number implements Q<ImmutableIntByte> {
    private static final byte MAX_POWER = 9; // 10^9 can already overflow
    private static final int MAX_LONG_POWER = 2 * MAX_POWER; // 10^10 results in Integer overflow
    private static final long MAX_LONG_VALUE = Utils.pow((long) 10, MAX_POWER); // 10^10 results in Integer overflow
    private static final int thresholds[] = initThresholds();
    private static final ImmutableIntByte ZERO = new ImmutableIntByte(0, (byte) 0);
    private static final ImmutableIntByte ONE = new ImmutableIntByte(1, (byte) 0);
    private static final ImmutableIntByte MAX_VALUE = new ImmutableIntByte(Integer.MAX_VALUE, Byte.MAX_VALUE);
    private static final ImmutableIntByte MIN_VALUE = new ImmutableIntByte(Integer.MIN_VALUE, Byte.MAX_VALUE);
    private final int value;
    private final byte power;
    private final double doubleValue;

    private ImmutableIntByte(int value, byte power) {
        this.value = value;
        this.power = power;
        this.doubleValue = doubleValue(value, power);
    }
//    public static ImmutableIntByte create(int value) {
//        return create(value, (byte) 0);
//    }

    // min/max
    public static ImmutableIntByte random() {
        return create(1000000.0 * Math.random() + 1);
    }

    public static ImmutableIntByte create(double number) {
        double doubleLog = Math.log10(number);
        if (Math.abs(doubleLog) > Byte.MAX_VALUE) {
            throw new ArithmeticException();
        }
        byte log = (byte) doubleLog;
        byte power = (byte) (log - MAX_POWER + 1);
        int value = (int) (number / Math.pow(10, power));
        ImmutableIntByte result = new ImmutableIntByte(value, power);
        return result;
    }

    public static ImmutableIntByte create(ImmutableSingleLong singleLong) {
        long longValue = singleLong.getLongValue();
        byte power = -PRECISION;
        while (Math.abs(longValue) > Integer.MAX_VALUE) {
            longValue /= 10;
            power += 1;
        }
        int value = (int) longValue;
        ImmutableIntByte result = new ImmutableIntByte(value, power);
        return result;
    }

    public static ImmutableIntByte random(double low, double high) {
        double value = low + (high - low) * Math.random();
        return create(value);
    }

    @Override
    public boolean isPositive() {
        return value > 0;
    }

    @Override
    public ImmutableIntByte add(ImmutableIntByte other) {
        if (other.value == 0) {
            return this;
        }
        if (value == 0) {
            return other;
        }
        int resultValue;
        byte resultPower;
        if (power == other.power) {
            long longValue = (long) value + (long) other.value;
            resultPower = power;
            if (Math.abs(longValue) > Integer.MAX_VALUE) {
                longValue /= 10;
                resultPower++;
            }
            assert Math.abs(longValue) <= Integer.MAX_VALUE;
            resultValue = (int) longValue;
//            normalize();
            return new ImmutableIntByte(resultValue, resultPower);
        }
        // make power == other.power
        ImmutableIntByte qmax, qmin;
        byte ideal_delta_power;
        if (power > other.power) {
            qmax = this;
            qmin = other;
            ideal_delta_power = (byte) (power - other.power);
        } else {
            qmax = other;
            qmin = this;
            ideal_delta_power = (byte) (other.power - power);
        }
        byte log_max = qmax.getLog10Value();
        byte delta_max = (byte) Math.min(ideal_delta_power, MAX_POWER - log_max);
        byte delta_min = (byte) (ideal_delta_power - delta_max);
        byte middle_power = (byte) (qmin.power + delta_min);
        assert middle_power == qmax.power - delta_max;
        int new_vmax = qmax.value * Utils.pow(10, delta_max);
        int new_vmin = qmin.value / Utils.pow(10, delta_min);
        long new_value = new_vmax + new_vmin;
        resultPower = middle_power;
        if (Math.abs(new_value) > Integer.MAX_VALUE) {
            resultValue = (int) (new_value / 10);
            resultPower++;
            assert Math.abs(new_value) < Integer.MAX_VALUE;
        } else {
            resultValue = (int) new_value;
        }
//        normalize();
        return new ImmutableIntByte(resultValue, resultPower);
    }

    @Override
    public ImmutableIntByte subtract(ImmutableIntByte other) {
        return add(other.negate());
    }

    @Override
    public ImmutableIntByte negate() {
        return new ImmutableIntByte(-value, power);
    }

    @Override
    public ImmutableIntByte multiplyBy(ImmutableIntByte other) {
        long productValue = (long) value * (long) other.value;
        if (productValue == 0) {
            return ZERO;
        }
        byte resultPower = (byte) (power + other.power);
        if (productValue > Integer.MAX_VALUE) {
            do {
                productValue /= 10;
                resultPower++;
            } while (productValue > Integer.MAX_VALUE);
        } else if (productValue < -Integer.MAX_VALUE) {
            do {
                productValue /= 10;
                resultPower++;
            } while (productValue < -Integer.MAX_VALUE);
        }
        int resultValue = (int) productValue;
//        normalize();
        return new ImmutableIntByte(resultValue, resultPower);
    }

    @Override
    public ImmutableIntByte divideBy(ImmutableIntByte other) {
        return multiplyBy(other.inverse());
    }

    private byte getLog10Value() {
        return Utils.log10(Math.abs(value));
    }

    public static ImmutableIntByte fromString(String str) {
        double value = Double.parseDouble(str);
        return create(value);
    }

    private static int[] initThresholds() {
        int[] result = new int[powersOf10.length];
        for (int i = 0; i < powersOf10.length; i++) {
            result[i] = Integer.MAX_VALUE / powersOf10[i];
        }
        return result;
    }

    @Override
    public int intValue() {
        return multiplyExact(value, Utils.pow(10, power));
    }

    @Override
    public long longValue() {
        return multiplyExact((long) value, Utils.pow(10L, power));
    }

    @Override
    public float floatValue() {
        double v = doubleValue();
        if (Math.abs(v) > Float.MAX_VALUE) {
            throw new ArithmeticException();
        }
        return (float) v;
    }

    @Override
    public double doubleValue() {
        return doubleValue;
    }

    private static double doubleValue(int value, byte power) {
        return (double) value * Math.pow(10, power);
    }

    @Override
    public int hashCode() {
        return 31 * value + power;
    }

    @Override
    public String toString() {
        return value + " * 10^" + power;
    }

    @Override
    public boolean isNonNegative() {
        return value >= 0;
    }

    @Override
    public boolean isNegative() {
        return value < 0;
    }

    @Override
    public boolean isNonPositive() {
        return value <= 0;
    }

    @Override
    public ImmutableIntByte abs() {
        if (isPositive()) {
            return this;
        } else {
            return negate();
        }
    }

    @Override
    public ImmutableIntByte multiplyBy(int number) {
        ImmutableIntByte other = create(number);
        return multiplyBy(other);
    }
//    public ImmutableIntByte multiplyBy(Double number) {
//        ImmutableIntByte result = create(number);
//        result.multiplySelfBy(this);
//        return result;
//    }

    @Override
    public float signum() {
        return Math.signum(value);
    }
//    @Override
//    public void negateSelf() {
//        value = -value;
//    }

    @Override
    public ImmutableIntByte inverse() {
        if (isZero()) {
            throw new ArithmeticException();
        }
        long inverseValue = MAX_LONG_VALUE / value;
        byte resultPower = (byte) (-power - MAX_LONG_POWER);
        if (inverseValue > 0) {
            while (inverseValue > Integer.MAX_VALUE) {
                inverseValue /= 10;
                resultPower += 1;
            }
        } else {
            while (inverseValue < -Integer.MAX_VALUE) {
                inverseValue /= 10;
                resultPower += 1;
            }
        }
        int resultValue = (int) inverseValue;
        return new ImmutableIntByte(resultValue, resultPower);
    }

    @Override
    public boolean isOne() {
        return equals(ONE);
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isMaxValue() {
//        ImmutableIntByte maxValue = maxValue();
        boolean result = equals(MAX_VALUE);
//        maxValue.dispose();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImmutableIntByte)) {
            return false;
        }
        ImmutableIntByte other = (ImmutableIntByte) obj;
        if (value == 0) {
            return other.value == 0;
        }
        if (other.value == 0) {
            return false;
        }
        // Both values are non zero. Check powers.
        if (power == other.power) {
            return value == other.value;
        }
        // powers are different
        ImmutableIntByte maxPowerQuantity, minPowerQuantity;
        if (power > other.power) {
            maxPowerQuantity = this;
            minPowerQuantity = other;
        } else {
            maxPowerQuantity = other;
            minPowerQuantity = this;
        }
        int deltaPower = maxPowerQuantity.power - minPowerQuantity.power;
        // Multiply maxPowerQuantity by 10*deltaPower and compare
        if (Math.abs(maxPowerQuantity.value) > thresholds[deltaPower]) {
            return false;
        }
        return maxPowerQuantity.value * powersOf10[deltaPower] == minPowerQuantity.value;
    }

    @Override
    public boolean isMinValue() {
//        ImmutableIntByte minValue = minValue();
        boolean result = equals(MIN_VALUE);
//        minValue.dispose();
        return result;
    }

    @Override
    public int compareTo(ImmutableIntByte other) {
        return (int) (doubleValue() - other.doubleValue());
//        long product = (long) value * (long) other.value;
//        if (product > 0) {
//            // same sign, so subtract one from another
//            return subtract(other).value;
//        } else if (product < 0) {
//            // different signs. If I am positive, then other is negative.
//            return isPositive()
//                    ? 1
//                    : -1;
//        } else {
//            // product == 0
//            if (other.isZero()) {
//                return value;
//            } else {
//                // I am zero
//                return -other.value;
//            }
//        }
    }

    @Override
    public ImmutableIntByte divideBy(int number) {
        ImmutableIntByte other = create(number);
        return divideBy(other);
    }

    public ImmutableIntByte power(Number power) {
        double result = Math.pow(doubleValue(), power.doubleValue());
        return create(result);
    }

    @Override
    public int sign() {
        return value > 0
                ? 1
                : value < 0
                        ? -1
                        : 0;
    }
}
