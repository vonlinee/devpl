package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.ProjectInfoMapper;
import io.devpl.common.utils.ProjectModule;
import io.devpl.backend.domain.param.ProjectListParam;
import io.devpl.backend.domain.vo.ProjectSelectVO;
import io.devpl.backend.entity.ProjectInfo;
import io.devpl.backend.service.ProjectService;
import io.devpl.common.utils.ProjectUtils;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.io.ZipUtils;
import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 项目信息Service
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements ProjectService {

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
    public List<ProjectSelectVO> listSelectableProject() {
        LambdaQueryWrapper<ProjectInfo> qw = new LambdaQueryWrapper<>();
        qw.select(ProjectInfo::getId, ProjectInfo::getProjectName);
        return baseMapper.selectList(qw).stream().map(p -> new ProjectSelectVO(p.getId(), p.getProjectName())).toList();
    }

    @Override
    public ListResult<ProjectInfo> listProjectInfos(ProjectListParam param) {
        Page<ProjectInfo> page = new Page<>(param.getPageIndex(), param.getPageSize());
        LambdaQueryWrapper<ProjectInfo> qw = Wrappers.lambdaQuery();
        if (StringUtils.hasText(param.getProjectName())) {
            qw.like(ProjectInfo::getProjectName, param.getProjectName());
        }
        page = baseMapper.selectPage(page, qw);
        return ListResult.ok(page);
    }

    @Override
    public byte[] download(ProjectInfo project) throws IOException {
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
        File zipFile = ZipUtils.zipDir(destRoot, new File("D:/Temp/", project.getProjectName() + ".zip").getAbsolutePath());
        byte[] data = FileUtils.readFileToByteArray(zipFile);
        // 清空文件
        FileUtils.deleteDirectory(destRoot.getParentFile().getParentFile());
        return data;
    }

    /**
     * 获取替换规则
     */
    private Map<String, String> getReplaceMap(ProjectInfo project) {
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
    public List<String> listProjectRootPath() {
        return baseMapper.selectProjectRootPath();
    }

    @Override
    public boolean save(ProjectInfo entity) {
        entity.setExclusions(EXCLUSIONS);
        entity.setModifySuffix(MODIFY_SUFFIX);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(entity.getCreateTime());
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
                List<String> newList = lines.stream().map(line -> replaceData(line, replaceMap)).collect(Collectors.toList());
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

    /**
     * @param projectRootDir 项目根目录
     */
    public void analyzeProject(String projectRootDir) {
        final Path path = Paths.get(projectRootDir);
        if (!Files.exists(path)) {
            return;
        }
        ProjectInfo projectInfo = new ProjectInfo();

    }

    @Override
    public boolean isProjectRootDirectory(File file) {
        return getProjectBulidToolFile(file) != null;
    }

    public File getProjectBulidToolFile(File file) {
        File[] files = file.listFiles((dir, name) -> name.equals("pom.xml") || "build.gradle".equals(name));
        if (files == null || files.length == 0) {
            return null;
        }
        return files[0];
    }

    @Override
    public void analyse(File projectRootDir) {
        ProjectModule rootProject = ProjectUtils.parse(projectRootDir);
        if (rootProject == null) {
            return;
        }
        List<ProjectInfo> projectInfos = new ArrayList<>();
        ProjectInfo projectInfo = convertProjectInfo(rootProject);
        projectInfos.add(projectInfo);
        if (rootProject.hasModules()) {
            for (ProjectModule module : rootProject.getModules()) {
                projectInfos.add(convertProjectInfo(module));
            }
        }

        List<String> rootPaths = listProjectRootPath();

        projectInfos.removeIf(pf -> rootPaths.contains(pf.getProjectPath()));

        if (!CollectionUtils.isEmpty(projectInfos)) {
            saveBatch(projectInfos);
        }
    }

    private ProjectInfo convertProjectInfo(ProjectModule module) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName(module.getName());
        projectInfo.setProjectPath(module.getRootPath());

        // 一般情况下 parent module 不放代码，不需要包路径
        if (!module.hasModules()) {
            // maven和gradle项目 推测项目包路径
            // 找到第一个不为空的路径
            try {
                Path srcPath = Paths.get(module.getRootPath(), "src", "main", "java");

                String root = srcPath.toString();

                if (Files.exists(srcPath)) {
                    Path current = srcPath.toRealPath(LinkOption.NOFOLLOW_LINKS);
                    while (current != null) {
                        try (Stream<Path> stream = Files.list(current)) {
                            Path[] pathArray = stream.filter(Files::isDirectory).toArray(Path[]::new);
                            // 只有单个目录
                            if (pathArray.length == 1) {
                                current = pathArray[0];
                            } else {

                                String path = current.toString().replace(root, "");

                                projectInfo.setProjectPackage(ProjectUtils.convertPathToPackage(path));
                                current = null;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return projectInfo;
    }
}
