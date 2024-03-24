package io.devpl.backend.domain.param;

import jakarta.validation.constraints.Min;

/**
 * 文件分片上传参数
 */
public class SliceUploadParam extends FileUploadParam {

    /**
     * 文件总块数，文件不分块时为1
     */
    @Min(value = 1, message = "文件总块数不能小于1")
    private Integer chunks = 1;

    /**
     * 当前块数，从0开始
     */
    @Min(value = 0, message = "当前块数不能小于0")
    private Integer chunk = 1;

    public int getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        if (chunks != null) {
            this.chunks = chunks;
        }
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        if (chunks != null) {
            this.chunk = chunk;
        }
    }
}
