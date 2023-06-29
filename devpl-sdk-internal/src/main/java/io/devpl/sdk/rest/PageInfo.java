package io.devpl.sdk.rest;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装分页信息
 */
@Data
public class PageInfo implements Serializable {

    public static final PageInfo UNKNOWN = new PageInfo();

    private static final long serialVersionUID = -9005418320425464234L;

    private int pageIndex;
    private int pageNum;
    private int pageSize;
    private int nextPage;
    private int firstRowNum;
}