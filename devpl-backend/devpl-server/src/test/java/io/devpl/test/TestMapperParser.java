package io.devpl.test;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.service.impl.MyBatisServiceImpl;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class TestMapperParser {

    public static void main(String[] args) {

        MyBatisServiceImpl impl = new MyBatisServiceImpl();

        impl.identifierGenerator = new DefaultIdentifierGenerator();

        String file = "D:\\Work\\Code\\exam-univ-java\\src\\main\\resources\\mapper\\ExamRecordMapper.xml";

        List<MappedStatementItem> statements = impl.parseMappedStatements(new File(file));

    }

    public static void test1() throws ParserConfigurationException, SAXException, IOException {
        String xml = """
            <mapper>
              <select id="selectList">
                  select * from user
                  <where>
                      <if test="">
                        AND es.end_time <![CDATA[>]]> NOW()
                      </if>
                  </where>
              </select>
            </mapper>
            """;

        SAXParserFactory factory = SAXParserFactory.newInstance();
        // javax.xml.parsers.SAXParser 原生api获取parse
        SAXParser saxParser = factory.newSAXParser();
        // 获取xmlReader
        XMLReader xmlReader = saxParser.getXMLReader();

        xmlReader.setContentHandler(new DefaultHandler() {

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                System.out.println("=============");

                System.out.println(ch);
                System.out.println("start = " + start + " length = " + length);
                System.out.println("=============");
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                System.out.println("开始 " + qName);
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                // System.out.println("endElement");
            }
        });

        xmlReader.parse(new InputSource(new StringReader(xml)));
    }
}
