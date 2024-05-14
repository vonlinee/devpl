package io.devpl.sdk;

import io.devpl.sdk.collection.Maps;
import org.junit.Test;

import java.util.Map;

public class MapsTest {

    @Test
    public void test1() {

        Map<String, Object> map = Maps.<String, Object>builder()
            .put("user.name", "zs")
            .put("user.age", "ls")
            .put("sex", false)
            .build();

        Map<String, Object> result = Maps.expandKeys(map, "\\.");

        System.out.println(result);
    }

    @Test
    public void test2() {
        Map<String, Object> map = Maps.<String, Object>builder()
            .put("user.name", "zs")
            .put("user.age", "ls")
            .put("sex", false)
            .build();

        Map<String, Object> result = Maps.expandKeys(map, "\\.");
        System.out.println(result);

        result = Maps.flattenKeys(result, ".");

        System.out.println(result);
    }
}
