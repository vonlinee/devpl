package org.apache.ddlutils.util;

import org.junit.jupiter.api.Test;

public class OrderedSetTest {

    @Test
    public void test1() {

        OrderedSet<Object> set = new OrderedSet<>();

        set.add(null);
        set.add(null);
        set.add(null);
        set.add(null);

        System.out.println(set);

        set.add(1);
        set.add(3);
        set.add(5);
        set.add(2);
        System.out.println(set);

        set.add(5);

        System.out.println(set);
    }
}
