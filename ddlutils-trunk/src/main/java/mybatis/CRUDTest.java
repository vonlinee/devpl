package mybatis;

import mybatis.entity.Department;
import mybatis.mapper.DepartmentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sun.misc.Unsafe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CRUDTest {

    private static final CRUDTest main = new CRUDTest();
    private final SqlSessionFactory factory;

    static {
        disableWarning();
    }

    private static SqlSession openSession() {
        return main.factory.openSession();
    }

    public CRUDTest() {
        // 1.指定MyBatis主配置文件位置
        String resource = "mybatis-config.xml";
        // 2.加载配置文件
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3.创建SqlSessionFactory会话工厂
        this.factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static void main(String[] args) throws IOException {
        try (SqlSession session = openSession()) {
            DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("class_id", UUID.randomUUID().toString());
            List<Department> departments = mapper.selectList();
            System.out.println(departments);
            session.commit();
        }
    }

    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);
            Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore ，肯定会抛异常
        }
    }
}