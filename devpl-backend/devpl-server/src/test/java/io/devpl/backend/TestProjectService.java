package io.devpl.backend;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.Function;

public class TestProjectService {


    @Test
    public void test3() {
        Comparator<Integer> comparing = Comparator.comparing((Function<Integer, Integer>) o -> null);
        Comparator<Integer> comparing1 = Comparator.comparing((Function<Integer, Integer>) o -> null);

        System.out.println(comparing1 == comparing);

        System.out.println(comparing1);
        System.out.println(comparing);
    }
}
