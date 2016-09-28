package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

public class Utils {
    public static final int MLN = 1000000;
    public static final String virtualServerAddress = "s-msk-t-fxa-ls1";
    private static final double defaultTolerance = 1e-8;
    //    private static final IdGenerator globalIdGenerator = new IdGenerator();
    private static final long msInDay = 1000 * 60 * 60 * 24;
    public static final int powersOf10[] = {1, 10, 100, 1000, 10_000, 100_000, 1000_000, 10_000_000, 100_000_000, 1000_000_000};

    public static String path(Object... strings) {
        StringJoiner sj = new StringJoiner(File.separator);
        for (Object string : strings) {
            sj.add(string.toString());
        }
        return sj.toString();
    }

    public static int hashCode(Object... objects) {
        int result = 0;
        for (Object object : objects) {
            result = 31 * result + object.hashCode();
        }
        return result;
    }

    public static Long tryMultiplyExact(long x, long y) {
        long r = x * y;
        long ax = Math.abs(x);
        long ay = Math.abs(y);
        if (((ax | ay) >>> 31 != 0)) {
            if (((y != 0) && (r / y != x)) || (x == Long.MIN_VALUE && y == -1)) {
                return null;
            }
        }
        return r;
    }

    public static boolean isOverflow(long x, long y) {
        long r = x * y;
        long ax = Math.abs(x);
        long ay = Math.abs(y);
        if (((ax | ay) >>> 31 != 0)) {
            if (((y != 0) && (r / y != x)) || (x == Long.MIN_VALUE && y == -1)) {
                return true;
            }
        }
        return false;
    }


    public static void arrayToCsv(String[] array, String filePath) throws IOException {
        FileWriter csvWriter = new FileWriter(filePath);
        csvWriter.append("USD/RUB Price difference\n");
        StringBuilder sb = new StringBuilder();
        for (String element : array) {
            sb.append(element);
            sb.append("\n");
        }
        csvWriter.append(sb);
        csvWriter.flush();
        csvWriter.close();
    }
    public static Stream<String[]> parseFileStream(String filepath, String separator) {
        try {
            return Files.lines(Paths.get(filepath))
                    .map(line -> line.split(separator))
                    .filter(line -> !(line.length == 0 || (line.length == 1 && line[0].equals(""))));
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    public static Stream<Double> readSizesFromCsv(String filepath) {
        return parseFileStream(filepath, ",")
                .flatMap(Arrays::stream)
                .map(Double::valueOf);
    }

    public static boolean doubleEquals(double x, double y) {
        return doubleEquals(x, y, defaultTolerance);
    }

    public static boolean doubleEquals(double x, double y, double tolerance) {
        double size = Math.max(Math.abs(x), Math.abs(y));
        double denominator = Math.max(size, 1);
        double diff = Math.abs(x - y) / denominator;
        return diff < tolerance;
    }

    public static boolean implies(boolean x, boolean y) {
        return !x || y;
    }

    public static long pow(long base, int power) {
        long res = 1;
        for (int i = 0; i < power; i++) {
            res *= base;
        }
        return res;
    }

    public static int pow(int base, int power) {
        int res = 1;
        for (int i = 0; i < power; i++) {
            res *= base;
        }
        return res;
    }

    public static long powExact(long base, int power) {
        long res = 1;
        for (int i = 0; i < power; i++) {
            res = Math.multiplyExact(res, base);
        }
        return res;
    }

    public static int powExact(int base, int power) {
        int res = 1;
        for (int i = 0; i < power; i++) {
            res = Math.multiplyExact(res, base);
        }
        return res;
    }

    public static int log(int value, int base) {
        return (int) (Math.log(value) / Math.log(base));
    }

    public static boolean isPowerOfTwo(int v) {
        return v != 0 && (v & (v - 1)) == 0;
    }

    public static int not(int v) {
        return -v - 1;
    }

    public static byte log10(int v) {
        int t = (log2floor(v) + 1) * 1233 >> 12;
        int r = t - (v < powersOf10[t]
                ? 1
                : 0);
        assert Math.abs(r) < Byte.MAX_VALUE;
        return castToByte(r);
    }

    public static byte castToByte(int number) {
        if (Math.abs(number) > Byte.MAX_VALUE) {
            throw new ArithmeticException("Can't cast number to byte: " + number);
        }
        return (byte) number;
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isDigit(byte c) {
        return c >= '0' && c <= '9';
    }

    public static int log2floor(int v) {
        assert v > 0;
        int r = 0;
        while ((v >>= 1) != 0) {
            r++;
        }
        return r;
    }

    public static int pow2(int power) {
        return 1 << power;
    }

    public static String myToString(Collection collection) {
        return myToString(collection, " ");
    }

    public static String myToString(Collection collection, String separator) {
        if (collection == null) return "null";
        if (collection.size() == 0) return "empty";
        Iterator iterator = collection.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(Objects.toString(iterator.next()));
        while (iterator.hasNext()) {
            sb.append(separator).append(Objects.toString(iterator.next()));
        }
        return sb.toString();
    }


    public static String path(String... strings) {
        return String.join(File.separator, strings);
    }

    public static <T> T nextOrNull(Iterator<T> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }
    public static Integer numberOfSecondsSinceMidnight() {
        long x = System.currentTimeMillis();
        int res = (int) ((x % msInDay) / 1000);
        return res;
    }

    public static int trueSign(boolean isTrue) {
        return isTrue
                ? 1
                : -1;
    }

    public static int falseSign(boolean isTrue) {
        return isTrue
                ? -1
                : 1;
    }

    public static String delimit(String string, char delimiter, boolean leftToRight, int step) {
        StringBuilder sb = new StringBuilder();
        int length = string.length();
        int min = 1;
        if (!leftToRight) {
            min -= length % step;
        }
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(i));
            if ((i + min) % step == 0 && i < length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static int castToIntException(long x) {
        if (x < Integer.MIN_VALUE || x > Integer.MAX_VALUE) {
            throw new ArithmeticException("Integer overflow");
        }
        return (int) x;
    }

    public static int castToIntAssert(long x) {
        assert x >= Integer.MIN_VALUE && x <= Integer.MAX_VALUE;
        return (int) x;
    }

    public static String[] getSubFolderNames(String parentFolder) {
        return new File(parentFolder).list((dir, name) -> new File(dir, name).isDirectory());
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

}
