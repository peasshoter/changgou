package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtil {

    static {
        String filename = new ClassPathResource("fdfs_client.conf").getPath();
        try {
            ClientGlobal.init(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public static String[] upload(FastDFSFile fastDFSFile) throws IOException, MyException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(connection, null);

        String[] upload_file = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), null);
        return upload_file;

    }

    public static FileInfo fileinfo(String groupname, String filename) throws IOException, MyException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(connection, null);
        return storageClient.get_file_info(groupname, filename);
    }

    public static InputStream downfile(String groupname, String filename) throws IOException, MyException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(connection, null);
        byte[] download_file = storageClient.download_file(groupname, filename);
        return new ByteArrayInputStream(download_file);
    }
}
