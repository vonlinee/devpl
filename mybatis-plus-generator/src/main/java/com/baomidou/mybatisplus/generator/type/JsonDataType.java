package com.baomidou.mybatisplus.generator.type;

/**
 * JSON数据类型枚举
 */
public enum JsonDataType implements DataType {
	ARRAY,
	OBJECT,
	STRING,
	NUMBER,
	BOOLEAN,
	NULL;

	@Override
	public String getQualifier() {
		return null;
	}

}
