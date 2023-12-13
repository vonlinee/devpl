package io.devpl.sdk.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    /**
     * 压缩目录为zip
     *
     * @param fileToZip 根目录
     * @param target    压缩文件存放位置
     * @return 文件
     */
    public static File zipDir(File fileToZip, String target) {
        try (FileOutputStream fos = new FileOutputStream(target);
             ZipOutputStream zipOut = new ZipOutputStream(fos);) {
            //递归压缩文件夹
            zipFile(fileToZip, fileToZip.getName(), zipOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new File(target);
    }

    /**
     * 将fileToZip文件夹及其子目录文件递归压缩到zip文件中
     *
     * @param fileToZip 递归当前处理对象，可能是文件夹，也可能是文件
     * @param fileName  fileToZip文件或文件夹名称
     * @param zipOut    压缩文件输出流
     * @throws IOException IO出错
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        //不压缩隐藏文件夹
        if (fileToZip.isHidden()) {
            return;
        }
        //判断压缩对象如果是一个文件夹
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                //如果文件夹是以“/”结尾，将文件夹作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                //如果文件夹不是以“/”结尾，将文件夹结尾加上“/”之后作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            //遍历文件夹子目录，进行递归的zipFile
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
            }
            //如果当前递归对象是文件夹，加入ZipEntry之后就返回
            return;
        }
        //如果当前的fileToZip不是一个文件夹，是一个文件，将其以字节码形式压缩到压缩包里面
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
