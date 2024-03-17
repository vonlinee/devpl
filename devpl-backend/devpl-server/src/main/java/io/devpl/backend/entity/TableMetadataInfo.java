package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "table_metadata")
public class TableMetadataInfo extends TableMetadata {
}
