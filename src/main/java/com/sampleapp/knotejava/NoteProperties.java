package com.sampleapp.knotejava;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties
class NoteProperties {
    @Value("${minio.host:localhost}")
    private String minioHost;

    @Value("${minio.bucket:image-storage}")
    private String minioBucket;

    @Value("${minio.access.key}")
    private String minioAccessKey;

    @Value("${minio.secret.key}")
    private String minioSecretKey;

    @Value("${minio.useSSL:false}")
    private boolean minioUseSSL;

    @Value("${minio.reconnect.enabled:true}")
    private boolean minioReconnectEnabled;

}
