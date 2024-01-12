package io.devpl.sdk;

import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.util.CollectionUtils;
import org.junit.Test;

import java.util.List;

public class TestCollectionUtils {

    @Test
    public void test1() {

        List<Integer> list1 = ArrayUtils.asArrayList(1, 3, 6, 6);
        List<Integer> list2 = ArrayUtils.asArrayList(1, 3, 4, 6);

        List<Integer> list = CollectionUtils.differ(list1, list2);

        System.out.println(list);
    }
}
