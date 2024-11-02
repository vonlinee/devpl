package io.devpl.backend;

import io.devpl.backend.service.MyBatisService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
public class TestMapperParser {

    @Resource
    MyBatisService myBatisService;

    @Test
    public void test1() {
        System.out.println(myBatisService);
    }

    @Test
    public void test2() throws ParserConfigurationException, SAXException, IOException {
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

    @Test
    public void test3() throws IOException {
        String path = TestMapper.class.getName().replace(".", "/");

        String absolutePath = new File("").getAbsolutePath();

        myBatisService.buildMapperXmlIndexForProject("D:\\Develop\\Code\\devpl-backend\\devpl-backend\\devpl-server", true);
    }
}
