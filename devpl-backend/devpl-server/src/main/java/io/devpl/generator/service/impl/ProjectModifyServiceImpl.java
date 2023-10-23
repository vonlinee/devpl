package io.devpl.generator.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.dao.ProjectModifyMapper;
import io.devpl.generator.entity.ProjectModify;
import io.devpl.generator.service.ProjectModifyService;
import io.devpl.generator.utils.ArrayUtils;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名变更
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProjectModifyServiceImpl extends BaseServiceImpl<ProjectModifyMapper, ProjectModify> implements ProjectModifyService {

    /**
     * 需要变更的文件后缀
     */
    public final static String MODIFY_SUFFIX = "java,xml,yml,factories,md,txt";
    /**
     * 排除的文件
     */
    public final static String EXCLUSIONS = ".git,.idea,target,logs";
    /**
     * 分隔符
     */
    public final static String SPLIT = ",";

    @Override
    public PageResult<ProjectModify> page(Query query) {
        IPage<ProjectModify> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public byte[] download(ProjectModify project) throws IOException {
        // 原项目根路径
        File srcRoot = new File(project.getProjectPath());
        // 临时项目根路径
        File destRoot = new File(getTempDirPath(project.getModifyProjectName()));
        // 排除的文件
        String[] exclusions = StringUtils.split(project.getExclusions(), SPLIT);
        // 获取替换规则
        Map<String, String> replaceMap = getReplaceMap(project);
        // 拷贝项目到新路径，并替换路径和文件名
        copyDirectory(srcRoot, destRoot, exclusions, replaceMap);
        // 需要替换的文件后缀
        // 替换文件内容数据
        contentFormat(destRoot, List.of(project.getModifySuffix().split(SPLIT)), replaceMap);
        // 生成zip文件
        File zipFile = ZipUtil.zip(destRoot);

        byte[] data = FileUtils.readFileToByteArray(zipFile);
        // 清空文件
        FileUtils.deleteDirectory(destRoot.getParentFile().getParentFile());
        return data;
    }

    /**
     * 获取替换规则
     */
    private Map<String, String> getReplaceMap(ProjectModify project) {
        Map<String, String> map = new LinkedHashMap<>();

        // 项目路径替换
        String srcPath = "src/main/java/" + project.getProjectPackage().replaceAll("\\.", "/");
        String destPath = "src/main/java/" + project.getModifyProjectPackage().replaceAll("\\.", "/");
        map.put(srcPath, destPath);

        // 项目包名替换
        map.put(project.getProjectPackage(), project.getModifyProjectPackage());

        // 项目标识替换
        map.put(project.getProjectCode(), project.getModifyProjectCode());
        map.put(StringUtils.upperFirst(project.getProjectCode()), StringUtils.upperFirst(project.getModifyProjectCode()));

        return map;
    }

    @Override
    public boolean save(ProjectModify entity) {
        entity.setExclusions(EXCLUSIONS);
        entity.setModifySuffix(MODIFY_SUFFIX);
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    /**
     * 生成临时路径
     */
    public String getTempDirPath(String... names) {
        StringBuilder tmpPath = new StringBuilder(System.getProperty("java.io.tmpdir"));
        tmpPath.append("generator");
        tmpPath.append(File.separator);
        tmpPath.append(System.currentTimeMillis());
        for (String name : names) {
            tmpPath.append(File.separator).append(name);
        }
        return tmpPath.toString();
    }

    /**
     * 拷贝目录文件
     *
     * @param srcRoot    原文件
     * @param destRoot   目标文件
     * @param exclusions 排除文件
     * @param replaceMap 替换规则
     */
    public static void copyDirectory(File srcRoot, File destRoot, String[] exclusions, Map<String, String> replaceMap) throws IOException {
        String destPath = destRoot.getPath().replaceAll("\\\\", "/");
        destRoot = new File(replaceData(destPath, replaceMap));

        // 获取排除后的源文件
        File[] srcFiles = ArrayUtils.isEmpty(exclusions) ? srcRoot.listFiles() : srcRoot.listFiles(file -> !ArrayUtils.contains(exclusions, file.getName()));

        if (srcFiles == null) {
            throw new IOException("没有需要拷贝的文件 " + srcRoot);
        }

        for (File srcFile : srcFiles) {
            String fileName = srcFile.getName();
            if (srcFile.isFile()) {
                fileName = replaceData(fileName, replaceMap);
            }
            File destFile = new File(destRoot, fileName);
            if (srcFile.isDirectory()) {
                copyDirectory(srcFile, destFile, exclusions, replaceMap);
            } else {
                FileUtils.copyFile(srcFile, destFile);
            }
        }
    }

    /**
     * 内容格式化
     *
     * @param rootFile   文件根目录
     * @param suffixList 需要格式化的文件后缀
     * @param replaceMap 替换规则
     */
    public static void contentFormat(File rootFile, List<String> suffixList, Map<String, String> replaceMap) {
        List<File> destList = FileUtils.findFiles(rootFile, file -> suffixList.contains(FilenameUtils.getExtension(rootFile.getAbsolutePath())));
        for (File dest : destList) {
            try {
                List<String> lines = FileUtils.readLines(dest, StandardCharsets.UTF_8);
                List<String> newList = lines.stream().map(line -> replaceData(line, replaceMap))
                    .collect(Collectors.toList());
                FileUtils.writeLines(dest, newList);
            } catch (IOException e) {
                log.error("内容格式化失败", e);
            }
        }
    }

    /**
     * 替换数据
     *
     * @param str 待替换的字符串
     * @param map 替换的kv集合
     * @return 返回替换后的数据
     */
    private static String replaceData(String str, Map<String, String> map) {
        for (String key : map.keySet()) {
            str = str.replaceAll(key, map.get(key));
        }
        return str;
    }
}
