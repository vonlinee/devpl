package io.devpl.backend;

import io.devpl.common.utils.ProjectUtils;

public class ProjectUtilsTest {

    public void test1() {

        String pathname = ProjectUtils.convertPackageNameToPathname(ProjectUtilsTest.class.getPackageName());

        System.out.println(pathname);

        System.out.println(ProjectUtils.convertPathToPackage(pathname));
    }
}
