package com.nine.one.yuedu.read.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author 不忘初心
 * 阿里云OSS配置
 */
@Configuration
@PropertySource(value = {"classpath:application-oss.properties"})
@ConfigurationProperties(prefix = "aliyun")
public class AliyunOSSConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String urlPrefix;


    @Bean(name = "hangzhou")
    public OSS hangzhou() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    @Bean(name = "beijing")
    public OSS beijing() {
        return new OSSClient("oss-cn-beijing.aliyuncs.com", accessKeyId, accessKeySecret);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
