package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.apache.ddlutils.jdbc.meta.TableMetadata;

@Getter
@Setter
@TableName(value = "table_metadata")
public class TableMetadataInfo extends TableMetadata {
}
