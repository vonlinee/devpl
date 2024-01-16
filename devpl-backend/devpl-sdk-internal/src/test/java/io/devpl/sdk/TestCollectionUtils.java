package io.devpl.sdk;

import io.devpl.sdk.collection.ArraySet;
import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.util.CollectionUtils;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class TestCollectionUtils {

    @Test
    public void test1() {

        List<Integer> list1 = ArrayUtils.asArrayList(1, 3, 6, 6);
        List<Integer> list2 = ArrayUtils.asArrayList(1, 3, 4, 6);

        List<Integer> list = CollectionUtils.differ(list1, list2);

        System.out.println(list);
    }

    @Test
    public void test2() {
        ArraySet<Integer> set = new ArraySet<>();

        set.addAll(1, 2, 3, 5, 6, 1, 2, 4);

        System.out.println(set);
    }
}
