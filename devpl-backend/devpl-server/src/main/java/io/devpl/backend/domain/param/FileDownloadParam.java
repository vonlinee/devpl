package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件下载参数
 */
@Getter
@Setter
public class FileDownloadParam {

    /**
     * 是否下载
     * 下载或访问，true时设置为APPLICATION_OCTET_STREAM + Content-Disposition: attachment
     */
    private boolean download = false;

    /**
     * 文件下载路径列表
     * 即上传时返回前端的pathList值
     */
    private String pathList;
}
