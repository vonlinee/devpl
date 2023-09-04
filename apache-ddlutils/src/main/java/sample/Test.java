package sample;

import org.apache.ddlutils.util.ListOrderedMap;

import java.util.Collection;

public class Test {

    public static void main(String[] args) {

        ListOrderedMap<String, Integer> map = new ListOrderedMap<>();

        map.put("k1", 1);
        map.put("k2", 5);
        map.put("k3", 3);
        map.put("k4", 2);


        Collection<Integer> values = map.values();

        System.out.println(values);

    }
}
