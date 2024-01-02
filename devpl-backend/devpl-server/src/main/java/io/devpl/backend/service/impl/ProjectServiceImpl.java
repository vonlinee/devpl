package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.ProjectInfoMapper;
import io.devpl.backend.domain.ProjectModule;
import io.devpl.backend.domain.param.ProjectListParam;
import io.devpl.backend.domain.vo.ProjectSelectVO;
import io.devpl.backend.entity.ProjectInfo;
import io.devpl.backend.service.ProjectService;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.io.ZipUtils;
import io.devpl.sdk.util.Arrays;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        File[] srcFiles = Arrays.isEmpty(exclusions) ? srcRoot.listFiles() : srcRoot.listFiles(file -> !Arrays.contains(exclusions, file.getName()));

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

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        try {
            File inputFile = new File("");
            inputFile = new File(inputFile.getAbsolutePath(), "pom.xml");

            //通过DocumentBuilderFactory工厂
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过DocumentBuilderFactory工厂创建DocumentBuilder对象
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            //使用DocumentBuilder的parse方法，从文件中解析出Document（文档）对象
            Document document = documentBuilder.parse(inputFile);
            //通过Document的getElementsByTagName方法，获取相应的NodeList节点集

            // 获取文档元素，及根节点
            Element documentElement = document.getDocumentElement();
            ProjectModule rootModule = new ProjectModule(inputFile.getName());
            NodeList childNodes = documentElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                switch (item.getNodeName()) {
                    case "groupId" -> {
                        item.normalize();
                        rootModule.setGroupId(item.getTextContent());
                    }
                    case "artifactId" -> {
                        item.normalize();
                        rootModule.setArtifactId(item.getTextContent());
                        rootModule.setName(item.getTextContent());
                    }
                    case "version" -> {
                        item.normalize();
                        rootModule.setVersion(item.getTextContent());
                    }
                    case "modules" -> {
                        childNodes = item.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                rootModule.addModule(item.getTextContent());
                            }
                        }
                    }
                }
            }
            System.out.println(rootModule);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
