package ir.microsign.utility;

import java.math.BigDecimal;

/**
 * Created by Mohammad on 17/06/2015.
 */
public class Math {
    public static double subtract(Double value1, Double value2) {
        if (value1.isNaN() || value1.isInfinite()) return Double.NaN;
        return BigDecimal.valueOf(value1).subtract(BigDecimal.valueOf(value2)).doubleValue();
    }

    public static double multiply(Double value1, Double value2) {
        if ((value1.isNaN() || value1.isInfinite()) && value2 != 0) return Double.NaN;
        return BigDecimal.valueOf(value1).multiply(BigDecimal.valueOf(value2)).doubleValue();
    }

    public static double divide(Double value1, Double value2) {
        return BigDecimal.valueOf(value1).divide(BigDecimal.valueOf(value2)).doubleValue();
    }

    public static double add(Double value1, Double value2) {
        if (value1.isNaN() || value1.isInfinite()) return Double.NaN;
        return BigDecimal.valueOf(value1).add(BigDecimal.valueOf(value2)).doubleValue();
    }

    public static int getDecimalNums(double value) {

        double fPart;
        fPart = Math.subtract(value, (double) ((long) value));
        if (fPart == 0) return 0;
        return getDecimalNums(Math.multiply(fPart, 10d)) + 1;
    }
}
