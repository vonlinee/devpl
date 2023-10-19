package io.devpl.sdk.io;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

/**
 * Copied from Apache Commons-io FileUtils
 */
public class FileUtils {

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;
    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;
    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;
    /**
     * An empty array of type <code>File</code>.
     */
    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    /**
     * Instances should NOT be constructed in standard programming.
     */
    private FileUtils() {
        super();
    }

    // -----------------------------------------------------------------------

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p>
     * At the end of the method either the stream will be successfully opened, or an
     * exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist. An exception is thrown if
     * the file object exists but is a directory. An exception is thrown if the file
     * exists but cannot be read.
     * @param file the file to open for input, must not be <code>null</code>
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException           if the file object is a directory
     * @throws IOException           if the file cannot be read
     * @since Commons IO 1.3
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
     * Determines whether the {@code parent} directory contains the {@code child} element (a file or directory).
     * <p>
     * Files are normalized before comparison.
     * </p>
     * <p>
     * Edge cases:
     * <ul>
     * <li>A {@code directory} must not be null: if null, throw IllegalArgumentException</li>
     * <li>A {@code directory} must be a directory: if not a directory, throw IllegalArgumentException</li>
     * <li>A directory does not contain itself: return false</li>
     * <li>A null child file is not contained in any parent: return false</li>
     * </ul>
     * @param directory the file to consider as the parent.
     * @param child     the file to consider as the child.
     * @return true is the candidate leaf is under by the specified composite. False otherwise.
     * @throws IOException              if an IO error occurs while checking the files.
     * @throws NullPointerException     if the given {@code File} is {@code null}.
     * @throws IllegalArgumentException if the given {@code File} does not exist or is not a directory.
     * @since 2.2
     */
    public static boolean directoryContains(final File directory, final File child) throws IOException {
        requireDirectoryExists(directory, "directory");
        if (child == null) {
            return false;
        }
        if (!directory.exists() || !child.exists()) {
            return false;
        }
        // Canonicalize paths (normalizes relative paths)
        return FilenameUtils.directoryContains(directory.getCanonicalPath(), child.getCanonicalPath());
    }

    /**
     * Requires that the given {@code File} exists and is a directory.
     * @param directory The {@code File} to check.
     * @param name      The parameter name to use in the exception message in case of null input.
     * @return the given directory.
     * @throws NullPointerException     if the given {@code File} is {@code null}.
     * @throws IllegalArgumentException if the given {@code File} does not exist or is not a directory.
     */
    private static File requireDirectoryExists(final File directory, final String name) {
        requireExists(directory, name);
        requireDirectory(directory, name);
        return directory;
    }

    /**
     * Requires that the given {@code File} is a directory.
     * @param directory The {@code File} to check.
     * @param name      The parameter name to use in the exception message in case of null input or if the file is not a directory.
     * @return the given directory.
     * @throws NullPointerException     if the given {@code File} is {@code null}.
     * @throws IllegalArgumentException if the given {@code File} does not exist or is not a directory.
     */
    private static File requireDirectory(final File directory, final String name) {
        Objects.requireNonNull(directory, name);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
        }
        return directory;
    }

    /**
     * Requires that the given {@code File} exists and throws an {@link IllegalArgumentException} if it doesn't.
     * @param file          The {@code File} to check.
     * @param fileParamName The parameter name to use in the exception message in case of {@code null} input.
     * @return the given file.
     * @throws NullPointerException     if the given {@code File} is {@code null}.
     * @throws IllegalArgumentException if the given {@code File} does not exist.
     */
    private static File requireExists(final File file, final String fileParamName) {
        Objects.requireNonNull(file, fileParamName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        }
        return file;
    }

    // -----------------------------------------------------------------------

    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened, or an
     * exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist. The file will be
     * created if it does not exist. An exception is thrown if the file object
     * exists but is a directory. An exception is thrown if the file exists but
     * cannot be written to. An exception is thrown if the parent directory cannot
     * be created.
     * @param file the file to open for output, must not be <code>null</code>
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be written to
     * @throws IOException if a parent directory needs creating but that fails
     * @since Commons IO 1.3
     */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    // -----------------------------------------------------------------------

    /**
     * Returns a human-readable version of the file size, where the input represents
     * a specific number of bytes.
     * @param size the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;
        if (size / ONE_GB > 0) {
            displaySize = size / ONE_GB + " GB";
        } else if (size / ONE_MB > 0) {
            displaySize = size / ONE_MB + " MB";
        } else if (size / ONE_KB > 0) {
            displaySize = size / ONE_KB + " KB";
        } else {
            displaySize = size + " bytes";
        }
        return displaySize;
    }

    public static boolean exists(File file) {
        return file.exists();
    }

    /**
     * TODO 优化内存占用
     * @param rootPath 根目录
     * @return List
     */
    public static List<File> listFiles(String rootPath, FileFilter filter) {
        List<File> resultList = new ArrayList<>();
        File file = new File(rootPath); // 获取当前文件夹
        if (!file.exists()) {
            return new ArrayList<>(0);
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return new ArrayList<>(0);
        }
        File[] directorys = file.listFiles(); // 获取根目录下所有文件与文件夹
        if (directorys == null || directorys.length == 0) {
            return new ArrayList<>(0);
        }
        LinkedList<File> directoryList = new LinkedList<>(Arrays.asList(directorys));
        while (!directoryList.isEmpty()) { // 文件集合中若存在数据，则继续循环
            File first = directoryList.removeFirst();
            if (first.exists()) {
                if (first.isDirectory()) {
                    directoryList.addAll(Arrays.asList(Objects.requireNonNull(first.listFiles())));
                }
                // 不管文件或目录，根据传入的过滤器选择是否加入结果
                if (filter.accept(first)) {
                    resultList.add(first);
                }
            }
        }
        return resultList;
    }

    public static void deleteProjectFiles(String rootDirectory) {
        FileFilter filter = pathname -> {
            boolean isFile = pathname.isFile();
            boolean isDirectory = pathname.isDirectory();
            boolean exists = pathname.exists();
            if (!exists) return false;
            if (isDirectory) {
                String name = pathname.getName().trim();
                if (name.equals(".idea") || name.equals(".target") || name.equals("bin") || name.equals(".settings")) {
                    return true;
                }
            }
            if (isFile) {
                String name = pathname.getName();
                return name.endsWith(".classpath") || name.endsWith(".iml") || name.endsWith(".project");
            }
            return false;
        };
        List<File> files = listFiles(rootDirectory, filter);
        System.out.println("共找到 " + files.size() + " 个文件");
        files.forEach(file -> {
            if (file.isFile()) {
                boolean delete = file.delete();
                if (delete) {
                    System.out.println("删除" + file.getAbsolutePath() + "成功");
                } else {
                    System.out.println("删除" + file.getAbsolutePath() + "失败");
                }
            } else if (file.isDirectory()) {
                try {
                    deleteDirectory(file);
                    System.out.println("删除" + file.getAbsolutePath() + "成功");
                } catch (IOException e) {
                    System.out.println("删除" + file.getAbsolutePath() + "失败," + e.getMessage());
                }
            }
        });
    }

    public static String readUTF8String(File file) throws IOException {
        return readString(file, StandardCharsets.UTF_8);
    }

    public static String readString(File file, Charset charset) throws IOException {
        Path path = file.toPath();
        // 一个文本文件如果已经大于int最大值，这种文件一般来说很少见有可能是log文件
        if (file.length() <= Integer.MAX_VALUE - 8) {
            // 使用nio提高读取速度
            try (FileChannel in = FileChannel.open(path, StandardOpenOption.READ)) {
                ByteBuffer byteBuffer = ByteBuffer.allocate((int) in.size());
                in.read(byteBuffer);
                return new String(byteBuffer.array(), charset);
            }
        }
        StringBuilder msg = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            for (; ; ) {
                String line = reader.readLine();
                if (line == null) break;
                msg.append(line);
            }
        }
        return msg.toString();
    }

    // -----------------------------------------------------------------------

    /**
     * Convert from a <code>URL</code> to a <code>File</code>.
     * <p>
     * From version 1.1 this method will decode the URL. Syntax such as
     * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
     * <code>/my docs/file.txt</code>.
     * @param url the file URL to convert, <code>null</code> returns
     *            <code>null</code>
     * @return the equivalent <code>File</code> object, or <code>null</code> if the
     * URL's protocol is not <code>file</code>
     * @throws IllegalArgumentException if the file is incorrectly encoded
     */
    public static File toFile(URL url) {
        if (url == null || !url.getProtocol().equals("file")) {
            return null;
        } else {
            String filename = url.getFile().replace('/', File.separatorChar);
            int pos = 0;
            while ((pos = filename.indexOf('%', pos)) >= 0) {
                if (pos + 2 < filename.length()) {
                    String hexStr = filename.substring(pos + 1, pos + 3);
                    char ch = (char) Integer.parseInt(hexStr, 16);
                    filename = filename.substring(0, pos) + ch + filename.substring(pos + 3);
                }
            }
            return new File(filename);
        }
    }

    /**
     * Converts each of an array of <code>URL</code> to a <code>File</code>.
     * <p>
     * Returns an array of the same size as the input. If the input is
     * <code>null</code>, an empty array is returned. If the input contains
     * <code>null</code>, the output array contains <code>null</code> at the same
     * index.
     * <p>
     * This method will decode the URL. Syntax such as
     * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
     * <code>/my docs/file.txt</code>.
     * @param urls the file URLs to convert, <code>null</code> returns empty array
     * @return a non-<code>null</code> array of Files matching the input, with a
     * <code>null</code> item if there was a <code>null</code> at that index
     * in the input array
     * @throws IllegalArgumentException if any file is not a URL file
     * @throws IllegalArgumentException if any file is incorrectly encoded
     * @since Commons IO 1.1
     */
    public static File[] toFiles(URL[] urls) {
        if (urls == null || urls.length == 0) {
            return EMPTY_FILE_ARRAY;
        }
        File[] files = new File[urls.length];
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            if (url != null) {
                if (!"file".equals(url.getProtocol())) {
                    throw new IllegalArgumentException("URL could not be converted to a File: " + url);
                }
                files[i] = toFile(url);
            }
        }
        return files;
    }

    /**
     * Converts each of an array of <code>File</code> to a <code>URL</code>.
     * <p>
     * Returns an array of the same size as the input.
     * @param files the files to convert
     * @return an array of URLs matching the input
     * @throws IOException if a file cannot be converted
     */
    public static URL[] toURLs(File[] files) throws IOException {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    // -----------------------------------------------------------------------

    /**
     * Copies a file to a directory preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to a file of the
     * same name in the specified destination directory. The destination directory
     * is created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * @param srcFile an existing file to copy, must not be <code>null</code>
     * @param destDir the directory to place the copy in, must not be
     *                <code>null</code>
     * @throws NullPointerException if source or destination is null
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    /**
     * Copies a file to a directory optionally preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to a file of the
     * same name in the specified destination directory. The destination directory
     * is created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * @param srcFile          an existing file to copy, must not be
     *                         <code>null</code>
     * @param destDir          the directory to place the copy in, must not be
     *                         <code>null</code>
     * @param preserveFileDate true if the file date of the copy should be the same
     *                         as the original
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFile(File, File, boolean)
     * @since Commons IO 1.3
     */
    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyFile(srcFile, new File(destDir, srcFile.getName()), preserveFileDate);
    }

    /**
     * Copies a file to a new location preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to the specified
     * destination file. The directory holding the destination file is created if it
     * does not exist. If the destination file exists, then this method will
     * overwrite it.
     * @param srcFile  an existing file to copy, must not be <code>null</code>
     * @param destFile the new file, must not be <code>null</code>
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File)
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    /**
     * Copies a file to a new location.
     * <p>
     * This method copies the contents of the specified source file to the specified
     * destination file. The directory holding the destination file is created if it
     * does not exist. If the destination file exists, then this method will
     * overwrite it.
     * @param srcFile          an existing file to copy, must not be
     *                         <code>null</code>
     * @param destFile         the new file, must not be <code>null</code>
     * @param preserveFileDate true if the file date of the copy should be the same
     *                         as the original
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File, boolean)
     */
    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
            if (!destFile.getParentFile().mkdirs()) {
                throw new IOException("Destination '" + destFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && !destFile.canWrite()) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    /**
     * Internal copy file method.
     * @param srcFile          the validated source file, must not be
     *                         <code>null</code>
     * @param destFile         the validated destination file, must not be
     *                         <code>null</code>
     * @param preserveFileDate whether to preserve the file date
     * @throws IOException if an error occurs
     */
    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream input = new FileInputStream(srcFile);
        try {
            FileOutputStream output = new FileOutputStream(destFile);
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    // -----------------------------------------------------------------------

    /**
     * Copies a directory to within another directory preserving the file dates.
     * <p>
     * This method copies the source directory and all its contents to a directory
     * of the same name in the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist. If the destination
     * directory did exist, then this method merges the source with the destination,
     * with the source taking precedence.
     * @param srcDir  an existing directory to copy, must not be <code>null</code>
     * @param destDir the directory to place the copy in, must not be
     *                <code>null</code>
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @since Commons IO 1.2
     */
    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (srcDir.exists() && !srcDir.isDirectory()) {
            throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
    }

    /**
     * Copies a whole directory to a new location preserving the file dates.
     * <p>
     * This method copies the specified directory and all its child directories and
     * files to the specified destination. The destination is the new location and
     * name of the directory.
     * <p>
     * The destination directory is created if it does not exist. If the destination
     * directory did exist, then this method merges the source with the destination,
     * with the source taking precedence.
     * @param srcDir  an existing directory to copy, must not be <code>null</code>
     * @param destDir the new directory, must not be <code>null</code>
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @since Commons IO 1.1
     */
    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    /**
     * Copies a whole directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory to within
     * the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist. If the destination
     * directory did exist, then this method merges the source with the destination,
     * with the source taking precedence.
     * @param srcDir           an existing directory to copy, must not be
     *                         <code>null</code>
     * @param destDir          the new directory, must not be <code>null</code>
     * @param preserveFileDate true if the file date of the copy should be the same
     *                         as the original
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @since Commons IO 1.1
     */
    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' exists but is not a directory");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }
        doCopyDirectory(srcDir, destDir, preserveFileDate);
    }

    /**
     * Internal copy directory method.
     * @param srcDir           the validated source directory, must not be
     *                         <code>null</code>
     * @param destDir          the validated destination directory, must not be
     *                         <code>null</code>
     * @param preserveFileDate whether to preserve the file date
     * @throws IOException if an error occurs
     * @since Commons IO 1.1
     */
    private static void doCopyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else {
            if (!destDir.mkdirs()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }
            if (preserveFileDate) {
                destDir.setLastModified(srcDir.lastModified());
            }
        }
        if (!destDir.canWrite()) {
            throw new IOException("Destination '" + destDir + "' cannot be written to");
        }
        // recurse
        File[] files = srcDir.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + srcDir);
        }
        for (File file : files) {
            File copiedFile = new File(destDir, file.getName());
            if (file.isDirectory()) {
                doCopyDirectory(file, copiedFile, preserveFileDate);
            } else {
                doCopyFile(file, copiedFile, preserveFileDate);
            }
        }
    }

    // -----------------------------------------------------------------------

    /**
     * Copies bytes from the URL <code>source</code> to a file
     * <code>destination</code>. The directories up to <code>destination</code> will
     * be created if they don't already exist. <code>destination</code> will be
     * overwritten if it already exists.
     * @param source      the <code>URL</code> to copy bytes from, must not be
     *                    <code>null</code>
     * @param destination the non-directory <code>File</code> to write bytes to
     *                    (possibly overwriting), must not be <code>null</code>
     * @throws IOException if <code>source</code> URL cannot be opened
     * @throws IOException if <code>destination</code> is a directory
     * @throws IOException if <code>destination</code> cannot be written
     * @throws IOException if <code>destination</code> needs creating but can't be
     * @throws IOException if an IO error occurs during copying
     */
    public static void copyURLToFile(URL source, File destination) throws IOException {
        InputStream input = source.openStream();
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    // -----------------------------------------------------------------------

    /**
     * Recursively delete a directory.
     * @param directory directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }
        cleanDirectory(directory);
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Clean a directory without deleting it.
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Reads the contents of a file into a String. The file is always closed.
     * @param file     the file to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return the file contents, never <code>null</code>
     * @throws IOException                  in case of an I/O error
     * @throws UnsupportedEncodingException if the encoding is not supported by the
     *                                      VM
     */
    public static String readString(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.toString(in, encoding);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * Reads the contents of a file into a String using the default encoding for the
     * VM. The file is always closed.
     * @param file the file to read, must not be <code>null</code>
     * @return the file contents, never <code>null</code>
     * @throws IOException in case of an I/O error
     * @since Commons IO 1.3.1
     */
    public static String readString(File file) throws IOException {
        return readString(file, (Charset) null);
    }

    /**
     * Reads the contents of a file into a byte array. The file is always closed.
     * @param file the file to read, must not be <code>null</code>
     * @return the file contents, never <code>null</code>
     * @throws IOException in case of an I/O error
     * @since Commons IO 1.1
     */
    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.toByteArray(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * Reads the contents of a file line by line to a List of Strings. The file is
     * always closed.
     * @param file     the file to read, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return the list of Strings representing each line in the file, never
     * <code>null</code>
     * @throws IOException                  in case of an I/O error
     * @throws UnsupportedEncodingException if the encoding is not supported by the
     *                                      VM
     * @since Commons IO 1.1
     */
    public static List<String> readLines(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.readLines(in, encoding);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static List<String> readLines(File file, Charset encoding) throws IOException {
        return readLines(file, encoding.name());
    }

    /**
     * Reads the contents of a file line by line to a List of Strings using the
     * default encoding for the VM. The file is always closed.
     * @param file the file to read, must not be <code>null</code>
     * @return the list of Strings representing each line in the file, never
     * <code>null</code>
     * @throws IOException in case of an I/O error
     * @since Commons IO 1.3
     */
    public static List<String> readLines(File file) throws IOException {
        return readLines(file, (String) null);
    }

    /**
     * Return an Iterator for the lines in a <code>File</code>.
     * <p>
     * This method opens an <code>InputStream</code> for the file. When you have
     * finished with the iterator you should close the stream to free internal
     * resources. This can be done by calling the {@link LineIterator#close()} or
     * {@link LineIterator#closeQuietly(LineIterator)} method.
     * <p>
     * The recommended usage pattern is:
     *
     * <pre>
     * LineIterator it = FileUtils.lineIterator(file, "UTF-8");
     * try {
     * 	while (it.hasNext()) {
     * 		String line = it.nextLine();
     * 		/// do something with line
     *    }
     * } finally {
     * 	LineIterator.closeQuietly(iterator);
     * }
     * </pre>
     * <p>
     * If an exception occurs during the creation of the iterator, the underlying
     * stream is closed.
     * @param file     the file to open for input, must not be <code>null</code>
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @return an Iterator of the lines in the file, never <code>null</code>
     * @throws IOException in case of an I/O error (file closed)
     * @since Commons IO 1.2
     */
    public static LineIterator lineIterator(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return lineIterator(in, encoding);
        } catch (IOException | RuntimeException ex) {
            IOUtils.closeQuietly(in);
            throw ex;
        }
    }

    // IOUtils.lineIterator
    public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
        Reader reader = null;
        if (encoding == null) {
            reader = new InputStreamReader(input);
        } else {
            reader = new InputStreamReader(input, encoding);
        }
        return new LineIterator(reader);
    }

    /**
     * Return an Iterator for the lines in a <code>File</code> using the default
     * encoding for the VM.
     * @param file the file to open for input, must not be <code>null</code>
     * @return an Iterator of the lines in the file, never <code>null</code>
     * @throws IOException in case of an I/O error (file closed)
     * @see #lineIterator(File, String)
     * @since Commons IO 1.3
     */
    public static LineIterator lineIterator(File file) throws IOException {
        return lineIterator(file, null);
    }

    // -----------------------------------------------------------------------

    /**
     * Writes a String to a file creating the file if it does not exist.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created if
     * they do not exist.
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @throws IOException                  in case of an I/O error
     * @throws UnsupportedEncodingException if the encoding is not supported by the VM
     */
    public static void writeStringToFile(File file, String data, String encoding) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            IOUtils.write(data, out, encoding);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist using the
     * default encoding for the VM.
     * @param file the file to write
     * @param data the content to write to the file
     * @throws IOException in case of an I/O error
     */
    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, null);
    }

    public static void writeString(File file, String data) {
        try {
            writeStringToFile(file, data, null);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    /**
     * Writes a byte array to a file creating the file if it does not exist.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created if
     * they do not exist.
     * @param file the file to write to
     * @param data the content to write to the file
     * @throws IOException in case of an I/O error
     * @since Commons IO 1.1
     */
    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            out.write(data);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to the
     * specified <code>File</code> line by line. The specified character encoding
     * and the default line ending will be used.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created if
     * they do not exist.
     * @param file     the file to write to
     * @param encoding the encoding to use, <code>null</code> means platform default
     * @param lines    the lines to write, <code>null</code> entries produce blank
     *                 lines
     * @throws IOException                  in case of an I/O error
     * @throws UnsupportedEncodingException if the encoding is not supported by the
     *                                      VM
     * @since Commons IO 1.1
     */
    public static void writeLines(File file, String encoding, Collection<String> lines) throws IOException {
        writeLines(file, encoding, lines, null);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to the
     * specified <code>File</code> line by line. The default VM encoding and the
     * default line ending will be used.
     * @param file  the file to write to
     * @param lines the lines to write, <code>null</code> entries produce blank
     *              lines
     * @throws IOException in case of an I/O error
     * @since Commons IO 1.3
     */
    public static void writeLines(File file, Collection<String> lines) throws IOException {
        writeLines(file, null, lines, null);
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to the
     * specified <code>File</code> line by line. The specified character encoding
     * and the line ending will be used.
     * <p>
     * NOTE: As from v1.3, the parent directories of the file will be created if
     * they do not exist.
     * @param file       the file to write to
     * @param encoding   the encoding to use, <code>null</code> means platform
     *                   default
     * @param lines      the lines to write, <code>null</code> entries produce blank
     *                   lines
     * @param lineEnding the line separator to use, <code>null</code> is system
     *                   default
     * @throws IOException                  in case of an I/O error
     * @throws UnsupportedEncodingException if the encoding is not supported by the
     *                                      VM
     * @since Commons IO 1.1
     */
    public static void writeLines(File file, String encoding, Collection<String> lines, String lineEnding) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            IOUtils.writeLines(lines, lineEnding, out, encoding);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Writes the <code>toString()</code> value of each item in a collection to the
     * specified <code>File</code> line by line. The default VM encoding and the
     * specified line ending will be used.
     * @param file       the file to write to
     * @param lines      the lines to write, <code>null</code> entries produce blank
     *                   lines
     * @param lineEnding the line separator to use, <code>null</code> is system
     *                   default
     * @throws IOException in case of an I/O error
     * @since Commons IO 1.3
     */
    public static void writeLines(File file, Collection<String> lines, String lineEnding) throws IOException {
        writeLines(file, null, lines, lineEnding);
    }

    // -----------------------------------------------------------------------

    /**
     * Delete a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     * @param file file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException          in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            if (!file.delete()) {
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * Schedule a file to be deleted when JVM exits. If file is directory delete it
     * and all sub-directories.
     * @param file file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the file is <code>null</code>
     * @throws IOException          in case deletion is unsuccessful
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * Recursively schedule directory for deletion on JVM exit.
     * @param directory directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException          in case deletion is unsuccessful
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectoryOnExit(directory);
        directory.deleteOnExit();
    }

    /**
     * Clean a directory without deleting it.
     * @param directory directory to clean, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException          in case cleaning is unsuccessful
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Make a directory, including any necessary but nonexistent parent directories.
     * If there already exists a file with specified name or the directory cannot be
     * created then an exception is thrown.
     * @param directory directory to create, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException          if the directory cannot be created
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (directory.isFile()) {
                String message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                String message = "Unable to create directory " + directory;
                throw new IOException(message);
            }
        }
    }

    // -----------------------------------------------------------------------

    /**
     * Recursively count size of a directory (sum of the length of all files).
     * @param directory directory to inspect, must not be <code>null</code>
     * @return size of directory in bytes, 0 if directory is security restricted
     * @throws NullPointerException if the directory is <code>null</code>
     */
    public static long sizeOfDirectory(File directory) {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        long size = 0;

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            return 0L;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                size += sizeOfDirectory(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    // -----------------------------------------------------------------------

    /**
     * Tests if the specified <code>File</code> is newer than the reference
     * <code>File</code>.
     * @param file      the <code>File</code> of which the modification date must be
     *                  compared, must not be <code>null</code>
     * @param reference the <code>File</code> of which the modification date is
     *                  used, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified more
     * recently than the reference <code>File</code>
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the reference file is <code>null</code>
     *                                  or doesn't exist
     */
    public static boolean isFileNewer(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '" + file + "' doesn't exist");
        }
        return isFileNewer(file, reference.lastModified());
    }

    /**
     * Tests if the specified <code>File</code> is newer than the specified
     * <code>Date</code>.
     * @param file the <code>File</code> of which the modification date must be
     *             compared, must not be <code>null</code>
     * @param date the date reference, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified after the
     * given <code>Date</code>.
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isFileNewer(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileNewer(file, date.getTime());
    }

    /**
     * Tests if the specified <code>File</code> is newer than the specified time
     * reference.
     * @param file       the <code>File</code> of which the modification date must
     *                   be compared, must not be <code>null</code>
     * @param timeMillis the time reference measured in milliseconds since the epoch
     *                   (00:00:00 GMT, January 1, 1970)
     * @return true if the <code>File</code> exists and has been modified after the
     * given time reference.
     * @throws IllegalArgumentException if the file is <code>null</code>
     */
    public static boolean isFileNewer(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() > timeMillis;
    }

    // -----------------------------------------------------------------------

    /**
     * Tests if the specified <code>File</code> is older than the reference
     * <code>File</code>.
     * @param file      the <code>File</code> of which the modification date must be
     *                  compared, must not be <code>null</code>
     * @param reference the <code>File</code> of which the modification date is
     *                  used, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified before the
     * reference <code>File</code>
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the reference file is <code>null</code>
     *                                  or doesn't exist
     */
    public static boolean isFileOlder(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '" + file + "' doesn't exist");
        }
        return isFileOlder(file, reference.lastModified());
    }

    /**
     * Tests if the specified <code>File</code> is older than the specified
     * <code>Date</code>.
     * @param file the <code>File</code> of which the modification date must be
     *             compared, must not be <code>null</code>
     * @param date the date reference, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified before the
     * given <code>Date</code>.
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isFileOlder(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileOlder(file, date.getTime());
    }

    /**
     * Tests if the specified <code>File</code> is older than the specified time
     * reference.
     * @param file       the <code>File</code> of which the modification date must
     *                   be compared, must not be <code>null</code>
     * @param timeMillis the time reference measured in milliseconds since the epoch
     *                   (00:00:00 GMT, January 1, 1970)
     * @return true if the <code>File</code> exists and has been modified before the
     * given time reference.
     * @throws IllegalArgumentException if the file is <code>null</code>
     */
    public static boolean isFileOlder(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() < timeMillis;
    }

    // -----------------------------------------------------------------------

    /**
     * Computes the checksum of a file using the CRC32 checksum routine. The value
     * of the checksum is returned.
     * @param file the file to checksum, must not be <code>null</code>
     * @return the checksum value
     * @throws NullPointerException     if the file or checksum is <code>null</code>
     * @throws IllegalArgumentException if the file is a directory
     * @throws IOException              if an IO error occurs reading the file
     * @since Commons IO 1.3
     */
    public static long checksumCRC32(File file) throws IOException {
        CRC32 crc = new CRC32();
        checksum(file, crc);
        return crc.getValue();
    }

    /**
     * Computes the checksum of a file using the specified checksum object. Multiple
     * files may be checked using one <code>Checksum</code> instance if desired
     * simply by reusing the same checksum object. For example:
     *
     * <pre>
     * long csum = FileUtils.checksum(file, new CRC32()).getValue();
     * </pre>
     * @param file     the file to checksum, must not be <code>null</code>
     * @param checksum the checksum object to be used, must not be <code>null</code>
     * @return the checksum specified, updated with the content of the file
     * @throws NullPointerException     if the file or checksum is <code>null</code>
     * @throws IllegalArgumentException if the file is a directory
     * @throws IOException              if an IO error occurs reading the file
     * @since Commons IO 1.3
     */
    public static Checksum checksum(File file, Checksum checksum) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Checksums can't be computed on directories");
        }
        InputStream in = null;
        try {
            in = new CheckedInputStream(new FileInputStream(file), checksum);
            // IOUtils.copy(in, new NullOutputStream());
        } finally {
            IOUtils.closeQuietly(in);
        }
        return checksum;
    }

    /**
     * 使用相对路径创建的File对象没有父级目录 System.out.println(new File(""));
     * System.out.println(new File("D:/Temp/1.txt").getParentFile());
     * @param file File
     * @return 返回父目录
     */
    public static File getParentFile(File file) {
        if (file == null) return null;
        if (isPathAbsolute(file)) {
            return file.getParentFile();
        }
        return file.getAbsoluteFile().getParentFile();
    }

    /**
     * System.out.println(new File("").isAbsolute()); f System.out.println(new
     * File("./1.txt").isAbsolute()); f System.out.println(new
     * File("./AAA").isAbsolute()); f System.out.println(new
     * File("D:/1.txt").isAbsolute()); t System.out.println(new
     * File("D:/Temp").isAbsolute()); t
     * @param file 文件
     * @return 是否是绝对路径
     */
    public static boolean isPathAbsolute(File file) {
        if (file == null) return false;
        return file.isAbsolute();
    }

    /**
     * 不同平台
     * @param filepath 文件路径
     * @return 文件是否存在
     */
    public static boolean exists(String filepath) {
        return Files.exists(Paths.get(filepath), LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * 获取文件名
     * @param file 文件对象
     * @return 文件名
     */
    public static String getFileName(File file) {
        String filename = file.getName();
        int index = filename.lastIndexOf(File.separator);
        if (index > 0) {
            return filename.substring(index + 1);
        }
        return "";
    }

    public static File[] listFiles(File directory) {
        if (directory == null || directory.isFile()) {
            return new File[0];
        }
        File[] files = directory.listFiles();
        return files == null ? new File[0] : files;
    }

    /**
     * 获取文件扩展名
     * @param file 文件对象
     * @return 文件扩展名
     */
    @Nonnull
    public static String getExtension(File file, String placeholder) {
        if (file == null) {
            return placeholder;
        }
        String extension = FilenameUtils.getExtension(file.getName());
        if (extension == null || extension.isEmpty()) {
            extension = placeholder;
        }
        return extension;
    }

    /**
     * 清空文件夹<br>
     * 注意：清空文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     * @param directory 文件夹
     * @return 成功与否
     * @throws RuntimeException IO异常
     * @since 3.0.6
     */
    public static boolean clean(File directory) throws RuntimeException {
        if (directory == null || directory.exists() == false || false == directory.isDirectory()) {
            return true;
        }

        final File[] files = directory.listFiles();
        if (null != files) {
            for (File childFile : files) {
                if (false == del(childFile)) {
                    // 删除一个出错则本次删除任务失败
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 删除文件或者文件夹<br>
     * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     *
     * <p>
     * 从5.7.6开始，删除文件使用{@link Files#delete(Path)}代替 {@link File#delete()}<br>
     * 因为前者遇到文件被占用等原因时，抛出异常，而非返回false，异常会指明具体的失败原因。
     * </p>
     * @param file 文件对象
     * @return 成功与否
     * @throws RuntimeException IO异常
     * @see Files#delete(Path)
     */
    public static boolean del(File file) throws RuntimeException {
        if (file == null || false == file.exists()) {
            // 如果文件不存在或已被删除，此处返回true表示删除成功
            return true;
        }

        if (file.isDirectory()) {
            // 清空目录下所有文件和目录
            boolean isOk = clean(file);
            if (false == isOk) {
                return false;
            }
        }

        // 删除文件或清空后的目录
        final Path path = file.toPath();
        try {
            delFile(path);
        } catch (DirectoryNotEmptyException e) {
            // 遍历清空目录没有成功，此时补充删除一次（可能存在部分软链）
            del(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 删除文件或空目录，不追踪软链
     * @param path 文件对象
     * @throws IOException IO异常
     * @since 5.7.7
     */
    protected static void delFile(Path path) throws IOException {
        try {
            Files.delete(path);
        } catch (AccessDeniedException e) {
            // 可能遇到只读文件，无法删除.使用 file 方法删除
            if (!path.toFile().delete()) {
                throw e;
            }
        }
    }

    /**
     * 删除文件或者文件夹，不追踪软链<br>
     * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     * @param path 文件对象
     * @return 成功与否
     * @throws RuntimeException IO异常
     * @since 4.4.2
     */
    public static boolean del(Path path) throws RuntimeException {
        if (Files.notExists(path)) {
            return true;
        }
        try {
            if (isDirectory(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    /**
                     * 访问目录结束后删除目录，当执行此方法时，子文件或目录都已访问（删除）完毕<br>
                     * 理论上当执行到此方法时，目录下已经被清空了
                     *
                     * @param dir 目录
                     * @param e   异常
                     * @return {@link FileVisitResult}
                     * @throws IOException IO异常
                     */
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                        if (e == null) {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        } else {
                            throw e;
                        }
                    }
                });
            } else {
                delFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean isDirectory(Path path) {
        return isDirectory(path, false);
    }

    /**
     * 判断是否为目录，如果file为null，则返回false
     * @param path          {@link Path}
     * @param isFollowLinks 是否追踪到软链对应的真实地址
     * @return 如果为目录true
     * @since 3.1.0
     */
    public static boolean isDirectory(Path path, boolean isFollowLinks) {
        if (null == path) {
            return false;
        }
        final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        return Files.isDirectory(path, options);
    }

    /**
     * 遍历指定path下的文件并做处理
     * @param start    起始路径，必须为目录
     * @param maxDepth 最大遍历深度，-1表示不限制深度
     * @param visitor  {@link FileVisitor} 接口，用于自定义在访问文件时，访问目录前后等节点做的操作
     * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
     * @since 4.6.3
     */
    public static void walkFiles(Path start, int maxDepth, FileVisitor<? super Path> visitor) {
        if (maxDepth < 0) {
            // < 0 表示遍历到最底层
            maxDepth = Integer.MAX_VALUE;
        }
        try {
            Files.walkFileTree(start, EnumSet.noneOf(FileVisitOption.class), maxDepth, visitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     * @param file       当前遍历文件或目录
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
     * @return 文件列表
     */
    public static List<File> findFiles(File file, FileFilter fileFilter) {
        return findFiles(file, -1, fileFilter);
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     * @param file       当前遍历文件或目录
     * @param maxDepth   遍历最大深度，-1表示遍历到没有目录为止
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录，null表示接收全部文件
     * @return 文件列表
     * @since 4.6.3
     */
    public static List<File> findFiles(File file, int maxDepth, FileFilter fileFilter) {
        return findFiles(file.toPath(), maxDepth, fileFilter);
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供path为文件，直接返回过滤结果
     * @param path       当前遍历文件或目录
     * @param maxDepth   遍历最大深度，-1表示遍历到没有目录为止
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录，null表示接收全部文件
     * @return 文件列表
     * @since 5.4.1
     */
    public static List<File> findFiles(Path path, int maxDepth, FileFilter fileFilter) {
        final List<File> fileList = new ArrayList<>();
        if (null == path || !Files.exists(path)) {
            return fileList;
        } else if (!isDirectory(path)) {
            final File file = path.toFile();
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
            return fileList;
        }
        walkFiles(path, maxDepth, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                final File file = path.toFile();
                if (null == fileFilter || fileFilter.accept(file)) {
                    fileList.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return fileList;
    }

    public static String readToString(File file) {
        try {
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
