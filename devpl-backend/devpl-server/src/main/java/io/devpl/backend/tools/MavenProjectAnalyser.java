package io.devpl.backend.tools;

import io.devpl.backend.domain.ProjectModule;
import io.devpl.sdk.util.StringUtils;
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

public class MavenProjectAnalyser implements ProjectAnalyser {

    /**
     * 解析本地项目模块信息
     *
     * @param entryFile 入口文件 例如pom.xml，build.grade等
     * @return 模块信息
     */
    @Override
    public ProjectModule analyse(File entryFile) {
        try {
            //通过DocumentBuilderFactory工厂
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过DocumentBuilderFactory工厂创建DocumentBuilder对象
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            //使用DocumentBuilder的parse方法，从文件中解析出Document（文档）对象
            Document document = documentBuilder.parse(entryFile);
            //通过Document的getElementsByTagName方法，获取相应的NodeList节点集
            // 获取文档元素，及根节点
            Element rootEl = document.getDocumentElement();

            ProjectModule rootModule = new ProjectModule(entryFile.getName());
            rootModule.setRootPath(entryFile.getParentFile().getAbsolutePath());
            NodeList childNodes = rootEl.getChildNodes();
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
                }
            }

            NodeList packagingElement = rootEl.getElementsByTagName("packaging");
            if (packagingElement.getLength() == 0) {
                return rootModule;
            }
            Node packagingNode = packagingElement.item(0);
            if (packagingNode != null) {
                rootModule.setPackageWay(StringUtils.trim(packagingNode.getTextContent()));
            }

            // 子模块
            NodeList modules = rootEl.getElementsByTagName("modules");
            if (modules.getLength() == 0) {
                return rootModule;
            }
            Node modulesNode = modules.item(0);
            modules = modulesNode.getChildNodes();
            int l = modules.getLength();
            for (int i = 0; i < l; i++) {
                Node item = modules.item(i);
                if ("module".equals(item.getNodeName())) {
                    rootModule.addModule(StringUtils.trim(item.getTextContent()));
                }
            }

            if (rootModule.hasModules()) {
                // 解析子模块
                for (ProjectModule module : rootModule.getModules()) {
                    String moduleName = module.getName();

                    ProjectModule childModule = analyse(new File(entryFile.getParentFile(), moduleName + File.separator + "pom.xml"));
                    module.merge(childModule);
                }
            }

            return rootModule;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
