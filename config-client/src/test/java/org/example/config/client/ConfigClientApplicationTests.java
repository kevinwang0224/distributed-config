package org.example.config.client;

import org.example.config.core.model.ConfigData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Disabled
@SpringBootTest
class ConfigClientApplicationTests {

    @Autowired
    private ConfigClient configClient;


    @Test
    void publishConfig() {
        boolean published = configClient.publishConfig("application-dev", "my.name=kevin", "properties");
        Assertions.assertTrue(published);
    }

    @Test
    void getHistories() {
        boolean published = configClient.publishConfig("application-dev", "my.name=kevin", "properties");
        Assertions.assertTrue(published);

        List<String> histories = configClient.getHistories("application-dev");
        Assertions.assertFalse(CollectionUtils.isEmpty(histories));
    }

    @Test
    void getHistory() {
        ConfigData configData = configClient.getHistoryConfigData("application-dev", "1");
        Assertions.assertNotNull(configData);
    }

    @Test
    void getConfigData() {
        ConfigData configData = configClient.getConfigData("application-dev");
        Assertions.assertNotNull(configData);
    }

    @Test
    void getConfigContent() {
        String configContent = configClient.getConfigContent("application-dev", "json");
        Assertions.assertEquals("my.name=kevin", configContent);
    }


    @Test
    void deleteConfig() {
        boolean deleted = configClient.deleteConfig("application-dev");
        Assertions.assertTrue(deleted);
    }

    @Test
    void checkConfig() {
        boolean checked = configClient.checkConfig("application-dev", "7", "cc28ddaea80c5db94b6e74095306266a");
        Assertions.assertTrue(checked);
    }
}
