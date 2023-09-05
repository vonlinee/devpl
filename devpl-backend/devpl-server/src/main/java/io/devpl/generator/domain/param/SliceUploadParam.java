package io.devpl.generator.domain.param;

import jakarta.validation.constraints.Min;

/**
 * 分片上传参数
 */
public class SliceUploadParam extends FileUploadParam {

    /**
     * 文件总块数，文件不分块时为1
     */
    @Min(value = 1, message = "文件总块数不能小于1")
    private int chunks;

    /**
     * 当前块数，从0开始
     */
    @Min(value = 0, message = "当前块数不能小于0")
    private int chunk;

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }
}
