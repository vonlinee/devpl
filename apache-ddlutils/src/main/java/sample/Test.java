package sample;

import org.apache.ddlutils.util.ParamMap;

public class Test {

    public static void main(String[] args) {

        ParamMap map = new ParamMap();

        map.set("username", "ZS");
        map.set("age", 1);
        map.set("sex", 1);
        map.set("user", "true");

        Boolean user = map.getCompatiableBoolean("user");

        System.out.println(user);

        System.out.println(map);
    }
}
