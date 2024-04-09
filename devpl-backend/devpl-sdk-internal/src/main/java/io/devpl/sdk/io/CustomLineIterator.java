package io.devpl.sdk.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Objects;

/**
 * 可以自定义行结尾字符串(非特殊字符)，默认是\n\r等
 * \r: 软空格 在Linux、unix 中表示返回到当行的最开始位置。在Mac OS 中表示换行且返回到下一行的最开始位置，相当于Windows 里的 \n 的效果
 * \n: 换行符
 *
 * @see LineIterator
 */
public class CustomLineIterator implements Iterator<String> {

    /**
     * The reader that is being read.
     */
    private final BufferedReader bufferedReader;
    /**
     * The current line.
     */
    private StringBuilder cachedLine;

    /**
     * 换行符
     */
    private String lineSeparator;

    /**
     * 下一行的开头
     */
    private String nextLinePart;

    /**
     * A flag indicating if the iterator has been fully read.
     */
    private boolean finished = false;

    /**
     * 自定义分隔符时是否保留分隔符
     */
    private boolean reserveLineSeparator;

    public CustomLineIterator(Reader reader) {
        this(reader, System.lineSeparator(), false);
    }

    public CustomLineIterator(Reader reader, String lineSeparator) {
        this(reader, lineSeparator, true);
    }

    public CustomLineIterator(Reader reader, String lineSeparator, boolean reserveLineSeparator) {
        this.bufferedReader = new BufferedReader(reader);
        cachedLine = new StringBuilder();
        this.lineSeparator = Objects.requireNonNull(lineSeparator, "line separator cannot be null");
        if (lineSeparator.equals(System.lineSeparator())) {
            this.reserveLineSeparator = false;
        } else {
            this.reserveLineSeparator = reserveLineSeparator;
        }
    }

    @Override
    public boolean hasNext() {
        if (!cachedLine.isEmpty()) {
            return true;
        } else if (finished) {
            return false;
        } else if (nextLinePart != null && !nextLinePart.isEmpty()) {
            return true;
        } else {
            try {
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        finished = true;
                        return false;
                    } else {
                        nextLinePart = line;
                        break;
                    }
                }
                return true;
            } catch (IOException ioe) {
                close();
                throw new IllegalStateException(ioe.toString());
            }
        }
    }

    @Override
    public String next() {
        if (cachedLine.isEmpty()) {
            if (nextLinePart != null && !nextLinePart.isEmpty()) {
                int index = nextLinePart.indexOf(lineSeparator);
                if (index > 0) {
                    // 将该行分成两段
                    String _nextLinePart = nextLinePart.substring(index + 1);
                    String currentLine = nextLinePart.substring(0, index);
                    nextLinePart = _nextLinePart;
                    cachedLine.append(nextLinePart);
                    if (reserveLineSeparator) {
                        return currentLine + lineSeparator;
                    }
                    return currentLine;
                }
                cachedLine.append(nextLinePart);
            }
        }
        while (true) {
            try {
                String line = bufferedReader.readLine();
                int index = line.indexOf(lineSeparator);
                if (index > 0) {
                    // 将该行分成两段
                    nextLinePart = line.substring(index + 1);
                    cachedLine.append(line, 0, index);
                    break;
                } else {
                    cachedLine.append(line);
                }
            } catch (IOException e) {
                close();
                throw new RuntimeException(e);
            }
        }
        String currentLine = cachedLine.toString();
        cachedLine.delete(0, cachedLine.length());
        if (reserveLineSeparator) {
            return currentLine + lineSeparator;
        }
        return currentLine;
    }

    public void close() {
        finished = true;
        IOUtils.closeQuietly(bufferedReader);
        cachedLine.delete(0, cachedLine.length());
        cachedLine = null;
    }
}
