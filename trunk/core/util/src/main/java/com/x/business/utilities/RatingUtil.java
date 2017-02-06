package com.x.business.utilities;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;

/**
 * Created by olukoredeaguda on 05/02/2017.
 *
 * Various Ratings utilities
 */
public class RatingUtil {

    /**
     * Collector for classic weighted average formula https://i.stack.imgur.com/YM946.png
     *
     * weighted_mean = n * Sum(x(i) * w(i)) / (n * Sum(w(i))) ; i = 1+
     * */
    public static <T> Collector<T,?,Double> averagingWeighted(ToDoubleFunction<T> valueFunction, ToIntFunction<T> weightFunction) {
        class Bound {
            private double numerator = 0;
            private long denominator = 0;
        }

        return Collector.of(
                Bound::new,
                (b, e) -> {
                    b.numerator += valueFunction.applyAsDouble(e) * weightFunction.applyAsInt(e);
                    b.denominator += weightFunction.applyAsInt(e);
                },
                (b1, b2) -> { b1.numerator += b2.numerator; b1.denominator += b2.denominator; return b1; },
                b -> b.numerator / b.denominator
        );
    }

}
