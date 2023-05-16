package org.example.config.server.controller;

import org.example.config.core.api.ConfigService;
import org.example.config.core.model.ConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kevin wang
 * @version 2023/5/15
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    private final ConfigService memoryConfigService;

    public ConfigController(ConfigService memoryConfigService) {
        this.memoryConfigService = memoryConfigService;
    }

    /**
     * 获取配置内容
     */
    @GetMapping("content")
    public String getConfigContent(@NonNull @RequestParam("dataId") String dataId,
                                   @RequestParam(value = "dataType", required = false) String dataType) {
        return memoryConfigService.getConfigContent(dataId, dataType);
    }

    @GetMapping
    public ConfigData getConfigData(@NonNull @RequestParam("dataId") String dataId) {
        return memoryConfigService.getConfigData(dataId);
    }


    @PostMapping
    public boolean publishConfig(@NonNull @RequestParam("dataId") String dataId,
                                 @NonNull @RequestParam("content") String content,
                                 @RequestParam("dataType") String dataType) {
        if (!StringUtils.hasLength(dataId)) {
            return false;
        }
        return memoryConfigService.publishConfig(dataId, content, dataType);
    }

    @DeleteMapping
    public boolean deleteConfig(@NonNull @RequestParam("dataId") String dataId) {
        return memoryConfigService.deleteConfig(dataId);
    }


    @GetMapping("check")
    public boolean checkConfig(@NonNull @RequestParam("dataId") String dataId,
                               @NonNull @RequestParam("version") String version,
                               @NonNull @RequestParam("md5") String md5) {
        return memoryConfigService.checkConfig(dataId, version, md5);
    }

    @GetMapping("histories")
    public List<String> getConfigHistories(@NonNull @RequestParam("dataId") String dataId) {
        return memoryConfigService.getHistories(dataId);
    }

    @GetMapping("history")
    public ConfigData getConfigHistories(@NonNull @RequestParam("dataId") String dataId,
                                         @NonNull @RequestParam("version") String version) {
        return memoryConfigService.getHistoryConfigData(dataId, version);
    }
}
