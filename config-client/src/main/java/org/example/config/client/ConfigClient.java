package org.example.config.client;

import org.example.config.core.api.ConfigService;
import org.example.config.core.model.ConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kevin wang
 * @version 2023/5/16
 */
@Component
public class ConfigClient implements ConfigService {


    // TODO 轮询检查配置变更 修改本地缓存
    private final static Map<String, ConfigData> CONFIG_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private RestTemplate restTemplate;
    @Value("${config.server.addr}")
    private String configServerAddr;


    @Override
    public String getConfigContent(String dataId, String dataType) {
        ConfigData configData = getConfigData(dataId);
        return Optional.ofNullable(configData).map(ConfigData::getContent).orElse("");
    }

    @Override
    public ConfigData getConfigData(String dataId) {
        ConfigData configData = CONFIG_CACHE.get(dataId);
        if (configData != null) {
            return configData;
        }
        configData = restTemplate.getForObject(configServerAddr + "/config?dataId={?}", ConfigData.class, dataId);
        if (configData != null) {
            CONFIG_CACHE.put(dataId, configData);
        }
        return configData;
    }


    @Override
    public boolean publishConfig(String dataId, String content, String dataType) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dataId", dataId);
        params.add("content", content);
        params.add("dataType", dataType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(configServerAddr + "/config", requestEntity, Boolean.class);
        return response.getStatusCodeValue() == 200 && (response.getBody() != null && response.getBody());
    }

    @Override
    public boolean deleteConfig(String dataId) {
        restTemplate.delete(configServerAddr + "/config?dataId={?}", dataId);
        CONFIG_CACHE.remove(dataId);
        return true;
    }

    @Override
    public List<String> getHistories(String dataId) {
        return restTemplate.getForObject(configServerAddr + "/config/histories?dataId={?}", List.class, dataId);
    }

    @Override
    public ConfigData getHistoryConfigData(String dataId, String version) {
        return restTemplate.getForObject(configServerAddr + "/config/history?dataId={?}&version={?}", ConfigData.class, dataId, version);
    }

    @Override
    public boolean checkConfig(String dataId, String version, String md5) {
        Boolean checked = restTemplate.getForObject(configServerAddr + "/config/check?dataId={?}&version={?}&md5={?}", Boolean.class, dataId, version, md5);
        return checked != null && checked;
    }
}
