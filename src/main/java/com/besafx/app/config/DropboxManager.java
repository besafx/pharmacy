package com.besafx.app.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Future;

@Service
public class DropboxManager {

    private static final String ACCESS_TOKEN = "lwXbn73MQTAAAAAAAAAACtvJCtgSD7Rp5hwd7V8jM2V4O9I8c9javetzqM49b1-Y";
    private final Logger log = LoggerFactory.getLogger(DropboxManager.class);
    private DbxRequestConfig config;

    private DbxClientV2 client;

    @PostConstruct
    public void init() {
        // Create Dropbox client
        log.info("Preparing dropbox client...");
        config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").withUserLocale("en_US").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
        log.info("Connecting with dropbox client successfully");
    }

    public void createFolder(String path) {
        try {
            client.files().createFolder(path);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Async("threadPoolFileUploader")
    public Future<Boolean> uploadFile(MultipartFile file, String path) {
        try {
            log.info("Trying to upload file: " + file.getName());
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            client.files().uploadBuilder(path).uploadAndFinish(file.getInputStream());
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadPoolFileUploader")
    public Future<Boolean> uploadFile(File file, String path) {
        try {
            log.info("Trying to upload file: " + file.getName());
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            client.files().uploadBuilder(path).uploadAndFinish(new FileInputStream(file));
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadPoolFileUploader")
    public Future<Boolean> deleteFile(String path) {
        try {
            log.info("Trying to delete file from path: " + path);
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            client.files().delete(path);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadPoolFileSharing")
    public Future<String> shareFile(String path) {
        SharedLinkMetadata metadata;
        String link = null;
        try {
            log.info("Trying to share file from path: " + path);
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            metadata = client.sharing().createSharedLinkWithSettings(path);
            link = metadata.getUrl().replaceAll("dl=0", "raw=1");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            try {
                link = client.sharing().listSharedLinksBuilder().withPath(path).withDirectOnly(true).start().getLinks().get(0).getUrl().replaceAll("dl=0", "raw=1");
            } catch (Exception ex_) {
                log.error(ex_.getMessage(), ex_);
            }
        }
        return new AsyncResult<>(link);
    }
}
