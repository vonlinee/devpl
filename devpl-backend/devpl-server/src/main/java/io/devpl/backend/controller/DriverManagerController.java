package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.DriverFileListParam;
import io.devpl.backend.domain.param.SingleFileUploadParam;
import io.devpl.backend.domain.vo.FileUploadVO;
import io.devpl.backend.entity.DriverFileInfo;
import io.devpl.backend.service.DriverService;
import io.devpl.backend.service.FileUploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 数据库驱动管理
 */
@Slf4j
@Controller
@ResponseBody
@RequestMapping("/api/jdbc/driver")
public class DriverManagerController {

    @Resource
    DriverService driverService;

    @Resource
    FileUploadService fileUploadService;

    /**
     * 上传驱动jar包
     *
     * @param file 查询参数
     * @return 分页查询结果
     */
    @PostMapping(value = "/upload")
    public Result<?> uploadDriver(MultipartFile file) {
        SingleFileUploadParam param = new SingleFileUploadParam();
        param.setFile(file);
        param.setOverride(true);
        param.setFilename(file.getOriginalFilename());
        param.setFolder("jdbcDriverJarFile");
        FileUploadVO result = fileUploadService.uploadSingleFile(param);
        DriverFileInfo driverFileInfo = new DriverFileInfo();
        driverFileInfo.setFileName(file.getOriginalFilename());
        driverFileInfo.setDeleted(false);
        if (driverService.save(driverFileInfo)) {
            return Result.ok(result.getPath());
        }
        return Result.error("上传失败");
    }

    /**
     * 已注册的驱动文件列表
     *
     * @return 驱动类名列表
     */
    @GetMapping(value = "/files")
    public ListResult<DriverFileInfo> listDriverFiles(DriverFileListParam param) {
        return driverService.listDrivers(param);
    }

    /**
     * 已注册的驱动类名
     *
     * @return 驱动类名列表
     */
    @GetMapping(value = "/list/registered")
    public Result<List<String>> listRegisteredDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        List<String> classNames = new ArrayList<>();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            Class<? extends Driver> clazz = driver.getClass();
            classNames.add(clazz.getName());
        }
        return Result.ok(classNames);
    }
}
