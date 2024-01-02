package io.devpl.backend.utils;

import io.devpl.sdk.util.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XmlUtils {

    private static final String DEFAULT_CHATSET = "UTF-8";
    /**
     * 缓存DocumentBuilderFactory, 其中T 标识忽略注释与否， 第二个T 标识是否解析域名
     */
    private static DocumentBuilderFactory domFactoryTT;
    private static DocumentBuilderFactory domFactoryTF;
    private static DocumentBuilderFactory domFactoryFT;
    private static DocumentBuilderFactory domFactoryFF;

    private static final Map<String, DocumentBuilderFactory> DocumentBuilderFactoryCache = new ConcurrentHashMap<>(8);
    private static final ThreadLocal<DocumentBuilderCache> REUSABLE_BUILDER = ThreadLocal.withInitial(DocumentBuilderCache::new);

    private static class DocumentBuilderCache {

        DocumentBuilder cacheTT;
        DocumentBuilder cacheTF;
        DocumentBuilder cacheFT;
        DocumentBuilder cacheFF;

        /**
         * 根据传入的特性，提供满足条件的DocumentBuilder
         *
         * @param ignorComments
         * @param namespaceAware
         * @return
         */
        private DocumentBuilder getDocumentBuilder(boolean ignorComments, boolean namespaceAware) {
            if (ignorComments && namespaceAware) {
                if (DocumentBuilderFactoryCache.get("domFactoryTT") == null) {
                    DocumentBuilderFactoryCache.put("doFactoryTT", initFactory(true, true));
                }
                if (cacheTT == null) {
                    cacheTT = initBuilder(DocumentBuilderFactoryCache.get("domFactoryTT"));
                }
                return cacheTT;
            } else if (ignorComments) {
                if (DocumentBuilderFactoryCache.get("domFactoryTF") == null) {
                    DocumentBuilderFactoryCache.put("domFactoryTF", initFactory(true, true));
                }
                if (cacheTF == null) {
                    cacheTF = initBuilder(DocumentBuilderFactoryCache.get("domFactoryTF"));
                }
                return cacheTF;
            } else if (namespaceAware) {
                if (DocumentBuilderFactoryCache.get("domFactoryFT") == null) {
                    DocumentBuilderFactoryCache.put("domFactoryFT", initFactory(true, true));
                }
                if (cacheFT == null) {
                    cacheFT = initBuilder(DocumentBuilderFactoryCache.get("domFactoryFT"));
                }
                return cacheFT;
            } else {
                if (DocumentBuilderFactoryCache.get("domFactoryFF") == null) {
                    DocumentBuilderFactoryCache.put("domFactoryFF", initFactory(true, true));
                }
                if (cacheFF == null) {
                    cacheFF = initBuilder(DocumentBuilderFactoryCache.get("domFactoryFF"));
                }
                return cacheFF;
            }
        }

        private DocumentBuilder initBuilder(DocumentBuilderFactory domFactory) {
            DocumentBuilder builder;
            try {
                builder = domFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new UnsupportedOperationException(e);
            }
            return builder;
        }
    }

    private static DocumentBuilderFactory initFactory(boolean ignorComments, boolean namespaceAware) {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(ignorComments);
        documentBuilderFactory.setNamespaceAware(namespaceAware);

        return documentBuilderFactory;
    }

    public static Document loadDocument(String filePath, boolean ignoreComment) throws IOException, SAXException, ParserConfigurationException {
        return loadDocument(new FileInputStream(new File(filePath)), DEFAULT_CHATSET, ignoreComment);
    }

    public static Document loadDocument(InputStream in, String charSet, boolean ignoreComment) throws ParserConfigurationException, IOException, SAXException {
        return loadDocument(in, charSet, ignoreComment, false);
    }

    /**
     * 加载xml文件
     *
     * @param in
     * @param charSet       字符编码
     * @param ignoreComment 是否忽略注释
     * @return
     */
    public static Document loadDocument(InputStream in, String charSet, boolean ignoreComment, boolean namespaceAware) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = REUSABLE_BUILDER.get().getDocumentBuilder(ignoreComment, namespaceAware);
        InputSource is = null;
        // 解析流来获取charset
        if (charSet == null) {// 读取头200个字节来分析编码
            charSet = DEFAULT_CHATSET;
        }
        is = new InputSource(new XmlFixedReader(new InputStreamReader(in, charSet)));
        is.setEncoding(charSet);
        Document doc = db.parse(is);
        doc.setXmlStandalone(true);// 避免出现standalone="no"
        return doc;
    }

    /**
     * 通过读取XML头部文字来判断xml文件的编码
     *
     * @param buf XML文件头部若干字节
     * @param len 判定长度
     * @return 获得XML编码。如果不成功返回null。
     */
    public static String getCharsetInXml(byte[] buf, int len) {
        if (buf != null) {
            String s = new String(buf).toLowerCase();
            int n = s.indexOf("encoding=");
            if (n > -1) {
                s = s.substring(n + 9);
                if (s.charAt(0) == '"' || s.charAt(0) == '\'') {
                    s = s.substring(1);
                }
                n = s.indexOf("\" ' ><");
                if (n > -1) {
                    s = s.substring(0, n);
                }
                if (StringUtils.isEmpty(s)) {
                    return null;
                }
                s = String.valueOf(Charset.forName(s));
                return s;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 过滤xml的无效字符。
     * <p/>
     * XML中出现以下字符就是无效的，此时Parser会抛出异常，仅仅因为个别字符导致整个文档无法解析，是不是小题大作了点？
     * 为此编写了这个类来过滤输入流中的非法字符。
     * 不过这个类的实现不够好，性能比起原来的Reader实现和nio的StreamReader下降明显，尤其是read(char[] b, int
     * off, int len)方法. 如果不需要由XmlFixedReader带来的容错性，还是不要用这个类的好。
     * <ol>
     * <li>0x00 - 0x08</li>
     * <li>0x0b - 0x0c</li>
     * <li>0x0e - 0x1f</li>
     * </ol>
     */
    static class XmlFixedReader extends FilterReader {
        public XmlFixedReader(Reader reader) {
            super(new BufferedReader(reader));
        }

        public int read() throws IOException {
            int ch = super.read();
            while ((ch >= 0x00 && ch <= 0x08) || (ch >= 0x0b && ch <= 0x0c) || (ch >= 0x0e && ch <= 0x1f) || ch == 0xFEFF) {
                ch = super.read();
            }
            return ch;
        }

        // 最大的问题就是这个方法，一次读取一个字符速度受影响。

        public int read(char[] b, int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || len < 0 || len > b.length - off) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            int c = read();
            if (c == -1) {
                return -1;
            }
            b[off] = (char) c;
            int i = 1;
            try {
                for (; i < len; i++) {
                    c = read();
                    if (c == -1) {
                        break;
                    }
                    b[off + i] = (char) c;
                }
            } catch (IOException ee) {
            }
            return i;
        }
    }

}
