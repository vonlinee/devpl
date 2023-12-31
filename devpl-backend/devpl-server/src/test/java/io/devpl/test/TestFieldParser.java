package io.devpl.test;

import io.devpl.backend.interfaces.impl.URLFieldParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestFieldParser {

    @Test
    public void test1() {

        URLFieldParser parser = new URLFieldParser();

        List<Map<String, Object>> fields = parser.parse("https://github.com/notifications/714637141/watch_subscription?aria_id_prefix=repository-details&button_block=false&show_count=true");

        System.out.println(fields);
    }
}
