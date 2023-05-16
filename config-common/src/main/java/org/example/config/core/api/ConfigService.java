package org.example.config.core.api;


import org.example.config.core.model.ConfigData;

import java.util.List;

/**
 * ConfigService
 *
 * @author kevin wang
 * @version 2023/5/15
 * @see org.example.config.server.service.impl.MemoryConfigService
 */
public interface ConfigService {

    // 普通增删改查

    /**
     * 查询配置内容 (最新的配置内容)
     */
    String getConfigContent(String dataId, String dataType);

    /**
     * 查询配置 (最新的配置)
     */
    ConfigData getConfigData(String dataId);

    /**
     * 发布配置
     */
    boolean publishConfig(String dataId, String content, String dataType);

    /**
     * 删除配置
     */
    boolean deleteConfig(String dataId);

    // 配置版本

    /**
     * 获取某一配置的所有历史版本号
     */
    List<String> getHistories(String dataId);

    /**
     * 获取某一配置 某一版本的配置
     */
    ConfigData getHistoryConfigData(String dataId, String version);

    /**
     * 通过版本以及配置内容的 MD5 值 校验是否与最新配置一致
     */
    boolean checkConfig(String dataId, String version, String md5);

}
