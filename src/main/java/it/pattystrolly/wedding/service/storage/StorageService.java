package it.pattystrolly.wedding.service.storage;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import it.pattystrolly.wedding.service.RetryableService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

@Slf4j
public class StorageService extends RetryableService {

    public static final int RETRY_DELAY_MILLIS = 10;

    public static final int MAX_ATTEMPTS = 10;

    public static final int RETRY_PERIOD_MILLIS = 30 * 1000;


    private final String bucketName;

    private final GcsService gcsService;

    public StorageService(String bucketName) {
        this.bucketName = bucketName;
        gcsService = GcsServiceFactory.createGcsService(
                new RetryParams.Builder()
                        .initialRetryDelayMillis(RETRY_DELAY_MILLIS)
                        .retryMaxAttempts(MAX_ATTEMPTS)
                        .totalRetryPeriodMillis(RETRY_PERIOD_MILLIS)
                        .build());
    }

    public InputStream readFile(String fileName) {
        GcsFilename file = new GcsFilename(bucketName, fileName);
        final int fetchSize = 4 * 1024 * 1024;
        return Channels.newInputStream(gcsService.openPrefetchingReadChannel(file, 0, fetchSize));
    }

    private Boolean isFilePresent(String filePath) {
        try {
            GcsFilename filename = new GcsFilename(bucketName, filePath);
            GcsInputChannel channel = gcsService.openPrefetchingReadChannel(filename, 0, 1 * 1024);
            ByteBuffer bb = ByteBuffer.wrap(new byte[1 * 1024]);
            channel.read(bb);
            channel.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
