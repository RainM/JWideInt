package singlelong;

import general.*;
import wideint.WInt128;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ImmutableSingleLong extends Number implements Q<ImmutableSingleLong> {
    private static final DecimalFormat leftFormatter = new DecimalFormat("###,###");
    private static final DecimalFormat rightFormatter = new DecimalFormat("000,000");
    private static final Storage<byte[]> STORAGE = new Storage<>(() -> new byte[WInt128.NUM_BYTES]);
    public static final int PRECISION = 6;
    private static final long STEPS_IN_ONE = Utils.pow(10, PRECISION);
    public static final ImmutableSingleLong ONE = new ImmutableSingleLong(STEPS_IN_ONE);
    private static final byte[] STEPS_IN_ONE_BYTES = ONE.getBytes();
    // constants
    public static final ImmutableSingleLong MAX_VALUE = new ImmutableSingleLong(Long.MAX_VALUE);
    public static final ImmutableSingleLong MIN_VALUE = MAX_VALUE.negate();
    public static final ImmutableSingleLong MIN_STEP = new ImmutableSingleLong(1);
    public static final ImmutableSingleLong ZERO = new ImmutableSingleLong(0);
    public static final ImmutableSingleLong THOUSAND = new ImmutableSingleLong(1000.0);
    public static final ImmutableSingleLong MILLION = new ImmutableSingleLong(1000000.0);
    private static final BigInteger BIG_INTEGER_STEPS_IN_ONE = BigInteger.valueOf(STEPS_IN_ONE);
    private static final int MAX_PRECISION = (int) Math.log10(Long.MAX_VALUE);
    private final long longValue;

    public ImmutableSingleLong(long longValue, int precision) {
        assert precision >= 0;
        int excessPrecision = precision - PRECISION;
        this.longValue = (long) (longValue * Math.pow(10, -excessPrecision));
    }

    public ImmutableSingleLong(Double value) {
        this(value, false);
    }

    public ImmutableSingleLong(Double value, boolean trimInfinityToMaxValue) {
        assert value != null;
        if (!trimInfinityToMaxValue && Math.abs(value) > MAX_VALUE.doubleValue()) {
            throw new QuantityOutOfBoundsException();
        }
        this.longValue = (long) (value * STEPS_IN_ONE);
    }

    private ImmutableSingleLong(long longValue) {
        this.longValue = longValue;
    }
//    private static singlelong.ImmutableSingleLong fromStringOld(String s) {
//        String noSpaces = s.replace("\u00A0", "").replace(",", ".");
//        String[] parts = noSpaces.split("\\.");
//        long left = Long.parseLong(parts[0]);
//        long value = left * STEPS_IN_ONE;
//        if (parts.length == 2) {
//            long right = Long.parseLong(parts[1]);
//            long stepsInOne = (long) Math.pow(10, parts[1].length());
//            value += right * STEPS_IN_ONE / stepsInOne;
//        }
//        if (parts[0].charAt(0) == '-' && value > 0) {
//            value = -value;
//        }
//        return new singlelong.ImmutableSingleLong(value);
//    }

    static long getStepsInOne() {
        return STEPS_IN_ONE;
    }

    public static ImmutableSingleLong random() {
        return QUtils.random(ImmutableSingleLong::new);
    }

    public static ImmutableSingleLong random(Double low, Double high) {
        return random(new ImmutableSingleLong(low), new ImmutableSingleLong(high));
    }

    public static ImmutableSingleLong random(ImmutableSingleLong low, ImmutableSingleLong high) {
        assert low.isLessOrEqualThan(high);
        ImmutableSingleLong range = high.subtract(low);
        ImmutableSingleLong difference = range.multiplyBy(random());
        ImmutableSingleLong result = low.add(difference);
        return result;
    }

    public static ImmutableSingleLong[] toArray(double[] array) {
        ImmutableSingleLong[] result = new ImmutableSingleLong[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new ImmutableSingleLong(array[i]);
        }
        return result;
    }

    public static String toString(ImmutableSingleLong singleLong, boolean noSpaces) {
        return singleLong == null
                ? "null"
                : singleLong.toString(noSpaces);
    }

    public static ImmutableSingleLong fromString(String str) {
        int sign = 1;
        int startIntPart = 0;
        int strlen = str.length();
        for (int i = 0; i < strlen; i++) {
            char c = str.charAt(i);
            if (c == ' ' || c == ',') {
                continue;
            }
            if (c == '-') {
                sign = -1;
                startIntPart = i + 1;
                break;
            }
            if (Utils.isDigit(c)) {
                startIntPart = i;
                break;
            }
            throw new IllegalArgumentException("Can't parse quantity from string " + str);
        }
        long longValue1 = 0;
        boolean isFracPart = false;
        int fracLength = 0;
        for (int i = startIntPart; i < strlen; i++) {
            char c = str.charAt(i);
            if (c == ' ' || c == ',') {
                continue;
            }
            if (Utils.isDigit(c)) {
                int pasrsedChar = c - '0';
                longValue1 = longValue1 * 10 + pasrsedChar;
                if (isFracPart) {
                    fracLength++;
                    if (fracLength >= PRECISION) {
                        break;
                    }
                }
                continue;
            }
            if (c == '.') {
                isFracPart = true;
                continue;
            }
            throw new IllegalArgumentException("Can't parse quantity from string " + str);
        }
        longValue1 *= Utils.pow(10, PRECISION - fracLength);
        longValue1 *= sign;
        ImmutableSingleLong result = new ImmutableSingleLong(longValue1);
        return result;
    }

    private static ImmutableSingleLong fromStringNew(String str) {
        String[] parts = str.replace(",", "").split("\\.");
        int sign;
        String intPartString;
        if (parts[0].charAt(0) == '-') {
            sign = -1;
            intPartString = parts[0].substring(1, parts[0].length());
        } else {
            sign = 1;
            intPartString = parts[0];
        }
        long intPart = Long.valueOf(intPartString);
        if (parts.length == 1) {
            return new ImmutableSingleLong(intPart, 0).multiplyBy(sign);
        }
        String fracString = parts[1].length() > PRECISION
                ? parts[1].substring(0, PRECISION)
                : parts[1];
        long fracPart = Long.valueOf(fracString);
        int precision = fracString.length();
        long intValue = Math.multiplyExact(intPart, Utils.pow(10, precision));
        long longValue1 = Math.addExact(intValue, fracPart);
        ImmutableSingleLong result = new ImmutableSingleLong(longValue1, precision);
        return result.multiplyBy(sign);
    }

    private static ImmutableSingleLong fromDoubleString(String str) {
        return new ImmutableSingleLong(Double.valueOf(str));
    }

    private static String getFractionalPartString(int fracPart) {
        String leadingZerosString = getLeadingZerosString(fracPart);
        int trimmedFractionalPart = getTrimmedFractionalPart(fracPart);
        return leadingZerosString + trimmedFractionalPart;
    }

    private static int getTrimmedFractionalPart(int fracPart) {
        if (fracPart == 0) {
            return 0;
        }
        int result = Math.abs(fracPart);
        while (result % 10 == 0) {
            result = result / 10;
        }
        return result;
    }

    private static String getLeadingZerosString(int fracPart) {
        int leadingZeros = getLeadingZeros(fracPart);
        StringBuilder lz = new StringBuilder();
        for (int i = 0; i < leadingZeros; i++) {
            lz.append("0");
        }
        return lz.toString();
    }

    private static int getLeadingZeros(int fracPart) {
        if (fracPart == 0) {
            return 0;
        }
        int digits = (int) Math.log10(Math.abs(fracPart));
        assert digits <= 5;
        return 5 - digits;
    }

    @Override
    public float signum() {
        return Math.signum(longValue);
    }

    @Override
    public double doubleValue() {
        return ((double) longValue) / STEPS_IN_ONE;
    }

    public long getLongValue() {
        return longValue;
    }

    @Override
    public String toString() {
//        return Double.toString(doubleValue());
        return toStringNew();
    }

    public String toString(boolean noSpaces) {
        String sign = isNegative()
                ? "-"
                : "";
        Long left = Math.abs(longValue(RoundingMode.DOWN));
        long fractionalPart = getFractionalPart().getLongValue();
        Long right = Math.abs(fractionalPart);
        String leftResult;
        String rightResult;
        if (noSpaces) {
            leftResult = left.toString();
            rightResult = right.toString();
        } else {
            leftResult = leftFormatter.format(left);
            rightResult = rightFormatter.format(right);
        }
        int resultLength = rightResult.length();
        int zerosToAdd = PRECISION - resultLength;
        for (int i = 0; i < zerosToAdd; i++) {
            rightResult = "0" + rightResult;
        }
        return sign + leftResult + "." + rightResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ImmutableSingleLong)) return false;
        ImmutableSingleLong singleLong = (ImmutableSingleLong) o;
        return longValue == singleLong.longValue;
    }

    public boolean equals(ImmutableSingleLong other, long tolerance) {
        assert tolerance >= 0;
        return Math.abs(longValue - other.longValue) <= tolerance;
    }

    @Override
    public int hashCode() {
        return (int) (longValue ^ (longValue >>> 32));
    }

    @Override
    public int compareTo(ImmutableSingleLong o) {
        return Long.compare(longValue, o.longValue);
    }

    @Override
    public boolean isPositive() {
        return longValue > 0;
    }

    @Override
    public boolean isNonNegative() {
        return longValue >= 0;
    }

    @Override
    public boolean isNegative() {
        return longValue < 0;
    }

    @Override
    public boolean isNonPositive() {
        return longValue <= 0;
    }

    // multiplicative operations
    public ImmutableSingleLong multiplyBy(Double value) {
        ImmutableSingleLong result = new ImmutableSingleLong((long) (value * longValue));
        return result;
    }

    public ImmutableSingleLong divideBy(Double value) {
        return multiplyBy(1 / value);
    }

    public ImmutableSingleLong divideBy(int value) {
        return new ImmutableSingleLong(longValue / value);
    }

    @Override
    public ImmutableSingleLong multiplyBy(ImmutableSingleLong other) {
        if (isZero() || other.isZero()) {
            return ImmutableSingleLong.ZERO;
        }
        if (!Utils.isOverflow(longValue, other.longValue)) {
            return new ImmutableSingleLong(longValue * other.longValue / STEPS_IN_ONE);
        }
        byte[] bytes = getBytes();
        byte[] otherBytes = other.getBytes();
        WInt128.__native_i128_mul(bytes, otherBytes);
        WInt128.__native_i128_div(bytes, STEPS_IN_ONE_BYTES);
        long longResult = WInt128.__native_i128_to_long(bytes);
        STORAGE.dispose(bytes);
        STORAGE.dispose(otherBytes);
        return new ImmutableSingleLong(longResult);
    }

    private byte[] getBytes() {
        byte[] bytes = STORAGE.get();
        WInt128.__native_i128_setl(bytes, longValue);
        return bytes;
    }

    @Override
    public ImmutableSingleLong divideBy(ImmutableSingleLong other) {
        if (other.isZero()) {
            throw new DivisionByZeroException();
        }
        if (isZero()) {
            return ImmutableSingleLong.ZERO;
        }
        if (!Utils.isOverflow(longValue, STEPS_IN_ONE)) {
            return new ImmutableSingleLong(longValue * STEPS_IN_ONE / other.longValue);
        }
        byte[] bytes = getBytes();
        byte[] otherBytes = other.getBytes();
        WInt128.__native_i128_mul(bytes, STEPS_IN_ONE_BYTES);
        WInt128.__native_i128_div(bytes, otherBytes);
        long longResult = WInt128.__native_i128_to_long(bytes);
        STORAGE.dispose(bytes);
        STORAGE.dispose(otherBytes);
        return new ImmutableSingleLong(longResult);
    }

    public ImmutableSingleLong remainderFromDivisionBy(ImmutableSingleLong singleLong) {
        long longRes = longValue % singleLong.longValue;
        ImmutableSingleLong result = new ImmutableSingleLong(longRes);
        return result;
    }

    public ImmutableSingleLong multiplyBy(int i) {
        return new ImmutableSingleLong(longValue * i);
    }

    // additive operations
    public ImmutableSingleLong add(ImmutableSingleLong other) {
        long sum = Math.addExact(longValue, other.longValue);
        return new ImmutableSingleLong(sum);
    }

    public ImmutableSingleLong addTrimInfinity(ImmutableSingleLong other) {
        int sign = this.sign();
        if (sign != other.sign()) {
            return add(other);
        }
        ImmutableSingleLong headroom = ImmutableSingleLong.MAX_VALUE.subtract(this.abs());
        if (other.abs().isLessOrEqualThan(headroom)) {
            return this.add(other);
        } else {
            return ImmutableSingleLong.MAX_VALUE.multiplyBy(sign);
        }
    }

    public ImmutableSingleLong subtract(ImmutableSingleLong other) {
        long diff = Math.subtractExact(longValue, other.longValue);
        ImmutableSingleLong result = new ImmutableSingleLong(diff);
        return result;
    }
//    @Override
//    public singlelong.ImmutableSingleLong add(general.Additive<singlelong.ImmutableSingleLong> o) {
//        singlelong.ImmutableSingleLong other = (singlelong.ImmutableSingleLong) o;
//        return add(other);
//    }

    @Override
    public ImmutableSingleLong negate() {
        return new ImmutableSingleLong(-longValue);
    }
//    @Override
//    public singlelong.ImmutableSingleLong subtract(general.Additive<singlelong.ImmutableSingleLong> other) {
//        singlelong.ImmutableSingleLong o = (singlelong.ImmutableSingleLong) other;
//        return subtract(o);
//    }

    @Override
    public boolean isZero() {
        return longValue == 0;
    }

    // rounding
    public ImmutableSingleLong round() {
        return round(ImmutableSingleLong.ONE);
    }

    public ImmutableSingleLong roundCeiling() {
        return roundCeiling(ImmutableSingleLong.ONE);
    }

    public ImmutableSingleLong roundFloor() {
        return roundFloor(ImmutableSingleLong.ONE);
    }

    public ImmutableSingleLong round(ImmutableSingleLong step) {
        ImmutableSingleLong lowerStep = roundFloor(step);
        ImmutableSingleLong upperStep = roundCeiling(step);
        if (QUtils.distance(this, lowerStep).isLessOrEqualThan(QUtils.distance(this, upperStep))) {
            return lowerStep;
        } else {
            return upperStep;
        }
    }

    public ImmutableSingleLong roundCeiling(ImmutableSingleLong step) {
        if (step.equals(ImmutableSingleLong.ZERO)) {
            return this;
        }
        ImmutableSingleLong remainder = this.remainderFromDivisionBy(step);
        if (remainder.isZero()) {
            return this;
        }
        ImmutableSingleLong subtracted = this.subtract(remainder);
        if (remainder.isPositive()) {
            return subtracted.add(step);
        }
        return subtracted;
    }

    public ImmutableSingleLong roundFloor(ImmutableSingleLong step) {
        if (step.equals(ImmutableSingleLong.ZERO)) {
            return this;
        }
        ImmutableSingleLong remainder = this.remainderFromDivisionBy(step);
        if (remainder.isZero()) {
            return this;
        }
        ImmutableSingleLong subtracted = this.subtract(remainder);
        if (remainder.isNegative()) {
            return subtracted.subtract(step);
        }
        return subtracted;
    }

    public ImmutableSingleLong roundUp(ImmutableSingleLong step) {
        ImmutableSingleLong abs = (ImmutableSingleLong) abs();
        return abs.roundCeiling(step).multiplyBy(sign());
    }

    public ImmutableSingleLong roundDown(ImmutableSingleLong step) {
        ImmutableSingleLong abs = (ImmutableSingleLong) abs();
        return abs.roundFloor(step).multiplyBy(sign());
    }

    public ImmutableSingleLong roundHalfUp(ImmutableSingleLong step) {
        if (step.equals(ImmutableSingleLong.ZERO)) {
            return this;
        }
        ImmutableSingleLong remainder = this.remainderFromDivisionBy(step);
        if (remainder.isZero()) {
            return this;
        }
        if (remainder.abs().multiplyBy(2).equals(step.abs())) {
            return roundUp(step);
        }
        ImmutableSingleLong subtracted = this.subtract(remainder);
        if (remainder.isPositive()) {
            return subtracted.add(step);
        }
        return subtracted;
    }

    public int sign() {
        if (longValue == 0) {
            return 0;
        }
        if (longValue > 0) {
            return 1;
        }
        return -1;
    }

    public ImmutableSingleLong round(RoundingMode roundingMode) {
        return round(ImmutableSingleLong.ONE, roundingMode);
    }

    public ImmutableSingleLong round(ImmutableSingleLong step, RoundingMode roundingMode) {
        switch (roundingMode) {
            case CEILING:
                return roundCeiling(step);
            case FLOOR:
                return roundFloor(step);
            case UP:
                return roundUp(step);
            case DOWN:
                return roundDown(step);
            case HALF_UP:
                return roundHalfUp(step);
            default:
                throw new UnsupportedOperationException("Unknown rounding mode: " + roundingMode);
        }
    }

    public ImmutableSingleLong getFractionalPart() {
        return remainderFromDivisionBy(ImmutableSingleLong.ONE);
    }

    @Override
    public long longValue() {
        return longValue(RoundingMode.DOWN);
    }

    @Override
    public float floatValue() {
        return new Double(doubleValue()).floatValue();
    }

    public Long longValue(RoundingMode roundingMode) {
        return round(roundingMode).getLongValue() / STEPS_IN_ONE;
    }

    @Override
    public ImmutableSingleLong inverse() {
        return ImmutableSingleLong.ONE.divideBy(this);
    }

    @Override
    public int intValue() {
        return new Double(doubleValue()).intValue();
    }

    @Override
    public boolean isMaxValue() {
        return equals(MAX_VALUE);
    }

    @Override
    public boolean isMinValue() {
        return equals(MIN_VALUE);
    }

    public String toStringNew() {
        String signString = sign() == -1
                ? "-"
                : "";
        ImmutableSingleLong abs = (ImmutableSingleLong) abs();
        long intPart = abs.longValue / STEPS_IN_ONE;
        long fracPart = abs.longValue % STEPS_IN_ONE;
        assert intPart * STEPS_IN_ONE + fracPart == abs.longValue;
        String delimitedIntPart = delimit(String.valueOf(intPart), false);
        String fractionalPartString = getFractionalPartString((int) fracPart);
        String delimitFractionalPart = delimit(fractionalPartString, true);
        return signString + delimitedIntPart + "." + delimitFractionalPart;
    }

    public static String delimit(String string, boolean leftToRight) {
        return Utils.delimit(string, ',', leftToRight, 3);
    }

    public ImmutableSingleLong power(Number power) {
        double result = Math.pow(doubleValue(), power.doubleValue());
        return new ImmutableSingleLong(result);
    }

    public ImmutableSingleLong square() {
        return this.multiplyBy(this);
    }

    public ImmutableSingleLong sqrt() {
        return new ImmutableSingleLong(Math.sqrt(doubleValue()));
    }

    @Override
    public boolean isOne() {
        return equals(ONE);
    }

    public class QuantityOutOfBoundsException extends RuntimeException {
    }

    public class DivisionByZeroException extends ArithmeticException {
        public DivisionByZeroException() {
            super("Division by zero");
        }
    }
}
