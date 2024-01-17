package com.sampleapp.knotejava;

import io.minio.MinioClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Getter
@Component
public class NoteConfig {

    @Autowired
    private NoteProperties properties;

    private MinioClient minioClient;

    public void initMinio() {
        boolean success = false;
        while (!success) {
            try {
                minioClient = new MinioClient("http://" + properties.getMinioHost() + ":9000",
                        properties.getMinioAccessKey(),
                        properties.getMinioSecretKey(),
                        false);
                // Check if the bucket already exists.
                boolean isExist = minioClient.bucketExists(properties.getMinioBucket());
                if (isExist) {
                    System.out.println("> Bucket already exists.");
                } else {
                    minioClient.makeBucket(properties.getMinioBucket());
                }
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("> Minio Reconnect: " + properties.isMinioReconnectEnabled());
                if (properties.isMinioReconnectEnabled()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    success = true;
                }
            }
        }
        System.out.println("> Minio initialized!");
    }
}
