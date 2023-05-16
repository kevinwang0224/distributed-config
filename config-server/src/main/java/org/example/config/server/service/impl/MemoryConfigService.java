package org.example.config.server.service.impl;

import org.example.config.core.api.ConfigService;
import org.example.config.core.model.ConfigData;
import org.example.config.server.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Memory Config Service
 *
 * @author kevin wang
 * @version 2023/5/15
 */
@Service
public class MemoryConfigService implements ConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryConfigService.class);

    private static final Map<String, List<ConfigData>> CONFIG_MAP = new HashMap<>();
    private static final AtomicLong VERSION_COUNTER = new AtomicLong();

    @Override
    public String getConfigContent(String dataId, String dataType) {
        // TODO dataType converter
        ConfigData configData = getConfigData(dataId);
        return Optional.ofNullable(configData).map(ConfigData::getContent).orElse("");
    }

    @Override
    public ConfigData getConfigData(String dataId) {
        List<ConfigData> configDataList = CONFIG_MAP.get(dataId);
        if (CollectionUtils.isEmpty(configDataList)) {
            return null;
        }
        return configDataList.get(0);
    }

    @Override
    public synchronized boolean publishConfig(String dataId, String content, String dataType) {
        ConfigData newConfigData = new ConfigData();
        newConfigData.setDataId(dataId);
        newConfigData.setContent(content);
        newConfigData.setType(dataType);
        newConfigData.setVersion(String.valueOf(VERSION_COUNTER.incrementAndGet()));
        newConfigData.setMd5(MD5Utils.md5Hex(content));

        long currentTime = System.currentTimeMillis();
        newConfigData.setCreateTime(currentTime);
        newConfigData.setUpdateTime(currentTime);

        CONFIG_MAP.compute(dataId, (s, configDataList) -> {
            if (configDataList == null) {
                configDataList = new ArrayList<>();
            }
            configDataList.add(0, newConfigData);
            return configDataList;
        });
        return true;
    }

    @Override
    public synchronized boolean deleteConfig(String dataId) {
        CONFIG_MAP.remove(dataId);
        return true;
    }

    @Override
    public List<String> getHistories(String dataId) {
        List<ConfigData> configDataList = CONFIG_MAP.get(dataId);
        if (CollectionUtils.isEmpty(configDataList)) {
            return new ArrayList<>();
        }

        // 版本 由近及远
        return configDataList.stream().map(ConfigData::getVersion).collect(Collectors.toList());
    }

    @Override
    public ConfigData getHistoryConfigData(String dataId, String version) {
        List<ConfigData> configDataList = CONFIG_MAP.get(dataId);
        if (CollectionUtils.isEmpty(configDataList)) {
            return null;
        }

        return configDataList.stream()
                .filter(configData -> configData.getVersion() != null && configData.getVersion().equals(version))
                .findFirst().orElse(null);
    }

    @Override
    public boolean checkConfig(String dataId, String version, String md5) {
        ConfigData configData = this.getConfigData(dataId);
        if (configData == null) {
            return false;
        }
        String cVersion = configData.getVersion();
        String cMd5 = configData.getMd5();
        return cVersion != null && cVersion.equals(version) && cMd5 != null && cMd5.equals(md5);
    }
}
