package sample;

import org.apache.ddlutils.util.ListOrderedMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Test {

    public static void main(String[] args) {

        ListOrderedMap<String, Integer> map = new ListOrderedMap<>();

        map.put("k1", 1);
        map.put("k2", 5);
        map.put("k3", 3);
        map.put("k4", 2);

        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        System.out.println(entries);

        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            System.out.println(next);
        }

    }
}
