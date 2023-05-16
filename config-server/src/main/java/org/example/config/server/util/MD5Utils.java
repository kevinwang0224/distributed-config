package org.example.config.server.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * MD5 util
 *
 * @author kevin wang
 * @version 2023/5/15
 */
public class MD5Utils {

    public static String md5Hex(String content) {
        return DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));
    }
}
