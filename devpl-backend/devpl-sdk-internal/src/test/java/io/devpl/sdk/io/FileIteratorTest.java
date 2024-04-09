package io.devpl.sdk.io;

import org.junit.Test;

import java.io.StringReader;

public class FileIteratorTest {

    /**
     * 测试 CustomLineIterator 类
     */
    @Test
    public void testCustomLineSeparator() {
        String content = """
            INSERT INTO `room_seat`\s
            VALUES (27853, 1, 1, 1, '1-1', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27854, 1, 1, 2, '1-2', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27855, 1, 1, 3, '1-3', 1, 0, 0);
            INSERT INTO `room_seat` VALUES\s
            (27856, 1, 1, 4, '1-4', 1, 0, 0);
            INSERT INTO `room_seat` VALUES\s
            (27857, 1, 1, 5, '1-5', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27858, 1, 1, 6, '1-6', 1, 0, 0);
            INSERT INTO `room_seat` VALUES\s
            (27859, 1, 2, 1, '2-1', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27860, 1, 2, 2, '2-2', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27861, 1, 2, 3, '2-3', 1, 0, 0);
            INSERT INTO `room_seat` VALUES\s
            (27862, 1, 2, 4, '2-4', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27863, 1, 2, 5, '2-5', 1, 0, 0);
            INSERT INTO `room_seat` VALUES\s
            (27864, 1, 2, 6, '2-6', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27865, 1, 3, 1, '3-1', 1, 0, 0);
            INSERT INTO `room_seat` VALUES\s
            (27866, 1, 3, 2, '3-2', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27867, 1, 3, 3, '3-3', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (27868, 1, 3, 4, '3-4', 1, 0, 0);
            INSERT INTO `room_seat` VALUES (
            27869, 1, 3, 5, '3-5', 1, 0, 0);
            """;
        CustomLineIterator iterator = new CustomLineIterator(new StringReader(content), ";", true);
        while (iterator.hasNext()) {
            String line = iterator.next();
            System.out.println(line);
        }
    }
}
