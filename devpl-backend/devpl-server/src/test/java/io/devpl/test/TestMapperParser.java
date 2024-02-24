package io.devpl.test;

import io.devpl.backend.service.impl.MyBatisServiceImpl;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Comparator;
import java.util.function.Function;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestMapperParser {

    @Resource
    MyBatisServiceImpl myBatisService;

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

}
