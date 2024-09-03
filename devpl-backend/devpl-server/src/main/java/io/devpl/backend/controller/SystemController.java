package io.devpl.backend.controller;

import io.devpl.backend.domain.VMInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api/system")
public class SystemController {

    /**
     * 列出本机运行的JVM列表
     *
     * @return PID，主类名称，运行参数
     */
    @GetMapping("/local/vms")
    public List<VMInfo> listVirtualMachines() {
        return Collections.emptyList();
    }
}
