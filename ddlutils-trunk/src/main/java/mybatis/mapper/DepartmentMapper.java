package mybatis.mapper;

import mybatis.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper {

    List<Department> selectList();

    List<Map<String, Object>> listCloudServiceStatus();
}
