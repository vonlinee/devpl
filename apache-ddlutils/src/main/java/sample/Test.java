package sample;

import org.apache.ddlutils.util.ListOrderedMap;
import org.apache.ddlutils.util.MapIterator;
import org.apache.ddlutils.util.OrderedMapIterator;

public class Test {

    public static void main(String[] args) {

        ListOrderedMap map = new ListOrderedMap();

        map.put("k1", 1);
        map.put("k2", 5);
        map.put("k3", 3);
        map.put("k4", 2);

        // MapIterator mapIterator = map.mapIterator();
        //
        // while (mapIterator.hasNext()) {
        //     Object next = mapIterator.next();
        //
        //     mapIterator.remove();
        //
        //     System.out.println(next);
        // }
        //
        // System.out.println(map);


        OrderedMapIterator orderedMapIterator = map.orderedMapIterator();

        while (orderedMapIterator.hasNext()) {

            if (orderedMapIterator.hasPrevious()) {
                System.out.println(orderedMapIterator.previous());
            }

            Object next = orderedMapIterator.next();

            System.out.println(next);

        }
    }
}
