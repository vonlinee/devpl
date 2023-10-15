package io.devpl.generator.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SS {

    public static void main(String[] args) {

        String[] schoolIds = {"S-1007",
            "S-1008",
            "S-1009",
            "S-1010",
            "S-1011",
            "S-1012",
            "S-1013",
            "S-1014",
            "S-1015",
            "S-1016",
            "S-1017",
            "S-1018"};

        String sql = """
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 1, 1, 160, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 1, 2, 425, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 1, 3, 250, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 2, 1, 85, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 2, 2, 498, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 2, 3, 140, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 3, 1, 763, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 3, 2, 172, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chinese', 3, 3, 394, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 1, 1, 708, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 1, 2, 71, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 1, 3, 133, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 2, 1, 306, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 2, 2, 283, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 2, 3, 374, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 3, 1, 1278, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 3, 2, 23, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Maths', 3, 3, 385, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 1, 1, 106, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 1, 2, 120, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 1, 3, 197, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 2, 1, 2597, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 2, 2, 71, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 2, 3, 1429, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 3, 1, 197, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 3, 2, 718, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-English', 3, 3, 343, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 1, 1, 75, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 1, 2, 216, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 1, 3, 34, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 2, 1, 296, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 2, 2, 789, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 2, 3, 908, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 3, 1, 87, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 3, 2, 384, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Physics', 3, 3, 33, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 1, 1, 222, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 1, 2, 663, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 1, 3, 394, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 2, 1, 128, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 2, 2, 803, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 2, 3, 180, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 3, 1, 235, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 3, 2, 149, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Chemistry', 3, 3, 149, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 1, 1, 82, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 1, 2, 686, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 1, 3, 1038, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 2, 1, 876, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 2, 2, 384, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 2, 3, 362, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 3, 1, 267, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 3, 2, 699, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Biology', 3, 3, 410, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 1, 1, 852, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 1, 2, 293, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 1, 3, 184, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 2, 1, 764, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 2, 2, 92, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 2, 3, 77, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 3, 1, 290, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 3, 2, 143, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Politics', 3, 3, 70, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 1, 1, 389, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 1, 2, 144, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 1, 3, 808, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 2, 1, 159, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 2, 2, 47, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 2, 3, 254, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 3, 1, 304, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 3, 2, 367, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-History', 3, 3, 345, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 1, 1, 292, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 1, 2, 1034, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 1, 3, 317, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 2, 1, 982, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 2, 2, 383, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 2, 3, 38, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 3, 1, 250, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 3, 2, 195, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            INSERT INTO `lgdb_edu_center`.`resource_use_summary` (`id`, `school_id`, `subject_id`, `use_cycle`, `use_type`, `use_num`, `create_time`, `update_time`) VALUES (NULL, 'S-1001', 'S2-Geography', 3, 3, 731, '2023-10-12 10:46:25', '2023-10-12 10:46:25');
            """;

        Path path = Paths.get("D:/Temp/1.sql");

        for (String schoolId : schoolIds) {
            String replace = sql.replace("S-1001", schoolId);
            try {
                Files.write(path, replace.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
