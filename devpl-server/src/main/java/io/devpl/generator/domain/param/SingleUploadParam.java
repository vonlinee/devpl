package io.devpl.generator.domain.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 单个文件上传，文件大小大于配置的分块大小时，进行分块上传，需要前后端配合
 */
@Data
public class SingleUploadParam {

    /**
     * 当"folder/filename"指向的文件存在时，是否覆盖文件，默认false
     */
    private boolean force = false;

    /**
     * 存放子目录名称
     */
    @NotBlank(message = "保存子目录不能为空")
    private String folder;

    /**
     * 上传任务ID（UUID），文件上传过程中使用taskId作为临时文件名，文件上传完成后再重命名为filename
     * taskId也作为记录分块完成情况的Key。分块上传，不同分块的taskId要相同
     */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String filename;

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

    /**
     * 文件
     */
    @NotNull(message = "上传文件不能为空")
    private MultipartFile file;
}
