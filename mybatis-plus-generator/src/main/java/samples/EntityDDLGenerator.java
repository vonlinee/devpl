package samples;

import com.baomidou.mybatisplus.generator.utils.StringUtils;
import io.devpl.codegen.ddl.service.DdlBuilder;
import io.devpl.codegen.meta.ASTFieldParser;
import io.devpl.codegen.meta.Bean;
import io.devpl.codegen.meta.JavaParserUtils;
import io.devpl.codegen.meta.MetaField;

import java.io.File;

public class EntityDDLGenerator {

    public static void main(String[] args) {

        final String root = new File("").getAbsolutePath() + "/devpl-codegen/src/main/java/";

        String path = root + Bean.class.getName().replace(".", "/") + ".java";


        JavaParserUtils.parse(new File(path), new ASTFieldParser()).ifPresent(fieldInfos -> {
            DdlBuilder builder = new DdlBuilder();
            builder.create().tableName("Table");
            for (MetaField field : fieldInfos) {
                builder.addField(field.getIdentifier(), field.getDataType())
                    .addComment(StringUtils.trimWrapCharacters(field.getDescription())).addComma();
            }
            String result = builder.end();
            System.out.println(result);
        });

    }
}
