package org.example.config.core.model;

/**
 * 配置
 *
 * @author kevin wang
 * @version 2023/5/15
 */
public class ConfigData {


    /**
     * 配置id
     */
    private String dataId;
    /**
     * 配置内容
     */
    private String content;
    /**
     * 配置内容的md5值
     */
    private String md5;
    /**
     * 配置版本号
     */
    private String version;
    /**
     * 配置类型 JSON XML YAML 等
     */
    private String type;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 最后更新时间
     */
    private long updateTime;


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
