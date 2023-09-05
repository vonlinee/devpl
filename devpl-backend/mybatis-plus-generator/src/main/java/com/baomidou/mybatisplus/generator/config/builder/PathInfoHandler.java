/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config.builder;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 路径信息处理
 * @author nieqiurong hubin
 * @since 2020-10-06
 * @since 3.5.0
 */
class PathInfoHandler {

    /**
     * 输出文件Map
     */
    private final Map<OutputFile, String> pathInfo = new HashMap<>();

    /**
     * 输出目录
     */
    private final String outputDir;

    /**
     * 包配置信息
     */
    private final PackageConfig packageConfig;

    PathInfoHandler(String outputDir, PackageConfig packageConfig) {
        this.outputDir = outputDir;
        this.packageConfig = packageConfig;
    }

    /**
     * 设置默认输出路径
     * @param isKotlin       是否是kotlin
     * @param templateConfig 模板配置
     */
    public void setDefaultPathInfo(boolean isKotlin, TemplateConfig templateConfig) {
        putPathInfo(templateConfig.getEntity(isKotlin), OutputFile.entity, ConstVal.ENTITY);
        putPathInfo(templateConfig.getMapper(), OutputFile.mapper, ConstVal.MAPPER);
        putPathInfo(templateConfig.getXml(), OutputFile.xml, ConstVal.XML);
        putPathInfo(templateConfig.getService(), OutputFile.service, ConstVal.SERVICE);
        putPathInfo(templateConfig.getServiceImpl(), OutputFile.serviceImpl, ConstVal.SERVICE_IMPL);
        putPathInfo(templateConfig.getController(), OutputFile.controller, ConstVal.CONTROLLER);
        pathInfo.putIfAbsent(OutputFile.parent, joinPath(outputDir, packageConfig.getPackageInfo(ConstVal.PARENT)));

        // 如果配置了包路径，则覆盖自定义路径
        Map<OutputFile, String> pathInfo = packageConfig.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            this.pathInfo.putAll(pathInfo);
        }
    }

    public Map<OutputFile, String> getPathInfo() {
        return this.pathInfo;
    }

    /**
     * @param template   模板路径
     * @param outputFile 输出文件类型
     * @param module     模块名称
     */
    private void putPathInfo(String template, OutputFile outputFile, String module) {
        if (StringUtils.hasText(template)) {
            pathInfo.putIfAbsent(outputFile, joinPath(outputDir, packageConfig.getPackageInfo(module)));
        }
    }

    /**
     * 连接路径字符串
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (!StringUtils.hasText(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }
}
