package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.FileDownloadParam;
import io.devpl.backend.domain.param.MultiFileUploadParam;
import io.devpl.backend.domain.param.SingleFileUploadParam;
import io.devpl.backend.domain.vo.FileUploadVO;
import io.devpl.backend.service.FileStorageService;
import io.devpl.backend.service.FileUploadService;
import io.devpl.common.model.FileNode;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 文件相关控制器，文件上传，下载等
 * 参考实现
 * <a href="https://blog.csdn.net/minaki_/article/details/85163343">...</a>
 */
@Controller
@RequestMapping(value = "/api/file")
public class FileController {

    @Resource
    FileUploadService fileUploadService;
    @Resource
    FileStorageService fileStorageService;

    /**
     * 单文件上传
     *
     * @return 返回文件上传成功的路径 保存目录 ${devpl.file.upload.root}作为根目录，folder/filename
     */
    @ResponseBody
    @PostMapping(value = "/upload/single")
    public Result<FileUploadVO> uploadFile(@Validated SingleFileUploadParam param) {
        try {
            return Result.ok(fileUploadService.uploadSingleFile(param));
        } catch (Exception exception) {
            return Result.exception(exception);
        }
    }

    /**
     * 多文件上传
     */
    @ResponseBody
    @PostMapping(value = "/upload/multiple", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<FileUploadVO> uploadFile(MultiFileUploadParam param) {
        return Result.ok(fileUploadService.uploadMultiFiles(param));
    }

    /**
     * 文件下载，不通过静态资源访问
     *
     * @return 文件字节
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(FileDownloadParam param) {
        return new ResponseEntity<>(new byte[]{}, HttpStatusCode.valueOf(200));
    }

    /**
     * 获取服务器文件目录结构
     *
     * @param parent 父路径
     * @return 文件目录
     */
    @Deprecated // 耗时
    @ResponseBody
    @GetMapping(value = "/fs/tree")
    public List<FileNode> getFileSystemTree(String parent) {
        if (parent == null) {
            parent = "/";
        }
        return fileStorageService.getFileTree(parent);
    }

    /**
     * 获取服务器文件目录结构
     *
     * @param parent 父路径
     * @return 文件目录
     */
    @ResponseBody
    @GetMapping(value = "/fs/list-files")
    public List<FileNode> getChildren(String parent) {
        if (parent == null) {
            parent = "/";
        }
        return fileStorageService.listFiles(parent);
    }
}
