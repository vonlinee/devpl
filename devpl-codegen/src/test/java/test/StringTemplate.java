package test;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * 基于String Template 4
 * 官网：<a href="https://www.stringtemplate.org/">...</a>
 */
public class StringTemplate {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        STGroup group = new STGroupFile("st/test.stg");
        ST st = group.getInstanceOf("decl");
        st.add("type", "int");
        st.add("name", "x");
        st.add("value", 10);
        String result = st.render(); // yields "int x = 0;"
        System.out.println(result);
    }

    public void test2() {
        ST st = new ST("<name; separator=\", \">");
        st.add("name", "zs");
        st.add("name", "ls");
        System.out.println(st.render());
    }

    public void test3() {
        STGroup group = new STGroupFile("st/test.stg");
        ST st = group.getInstanceOf("test");
        st.add("name", "zs");
        st.add("name", "ls");
        st.add("name", "ww");
        String result = st.render(); // yields "int x = 0;"
        System.out.println(result);
    }

    public void test4() {
        STGroup group = new STGroupFile("st/test.stg");
        ST st = group.getInstanceOf("show_user");
        st.add("user", new Object());
        String result = st.render(); // yields "int x = 0;"
        System.out.println(result);
    }

    public void test6() {
        STGroup group = new STGroupFile("st/test.stg");
        ST st = group.getInstanceOf("show_user");
        Dictionary<String, Object> map = new Hashtable<>();
        map.put("zs", 1);
        map.put("ls", 2);
        st.add("user", map);
        System.out.println(map);
        String result = st.render(); // yields "int x = 0;"
        System.out.println(result);
    }

    public void test7() {
        int[] num =
                new int[]{3, 9, 20, 2, 1, 4, 6, 32, 5, 6, 77, 888, 2, 1, 6, 32, 5, 6, 77,
                        4, 9, 20, 2, 1, 4, 63, 9, 20, 2, 1, 4, 6, 32, 5, 6, 77, 6, 32, 5, 6, 77,
                        3, 9, 20, 2, 1, 4, 6, 32, 5, 6, 77, 888, 1, 6, 32, 5};
        String t = ST.format(30, "int <%1>[] = { <%2; wrap, anchor, separator=\", \"> };", "a", num);
        System.out.println(t);
    }

    public void testAggrete() {
        ST st = new ST("<items:{it|<it.id>: <it.lastName>, <it.firstName>\n}>");
        st.addAggr("items.{ firstName ,lastName, id }", "Ter", "Parr", 99); // add() uses varargs
        st.addAggr("items.{firstName, lastName ,id}", "Tom", "Burns", 34);
        String expecting = "99: Parr, Ter\n" + "34: Burns, Tom\n";
    }
}
