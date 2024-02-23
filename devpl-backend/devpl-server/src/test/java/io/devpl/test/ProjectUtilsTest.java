package io.devpl.test;

import io.devpl.backend.utils.ProjectUtils;
import org.junit.Test;

public class ProjectUtilsTest {

    @Test
    public void test1() {

        String pathname = ProjectUtils.convertPackageNameToPathname(ProjectUtilsTest.class.getPackageName());

        System.out.println(pathname);

        System.out.println(ProjectUtils.convertPathToPackage(pathname));
    }
}
