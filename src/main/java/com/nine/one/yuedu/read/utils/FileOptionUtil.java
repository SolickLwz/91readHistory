package com.nine.one.yuedu.read.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FileOptionUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将网络文件转化为文件对象
     *
     * @param fileUrl
     * @return
     */
    public File getFileByUrl(String fileUrl) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BufferedOutputStream stream = null;
        InputStream inputStream = null;
        File file = null;
        try {
            URL imageUrl = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            String suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
            logger.info("后缀是:" + suffix);
            file = File.createTempFile("pattern", suffix);
            logger.info("临时文件创建成功={}", file.getCanonicalPath());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(outStream.toByteArray());
        } catch (Exception e) {
            logger.error("创建人脸获取服务器图片异常", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (stream != null) {
                    stream.close();
                }
                outStream.close();
            } catch (Exception e) {
                logger.error("关闭流异常", e);
            }
        }
        return file;
    }

    /**
     * 将文件转化为MultipartFile
     * @param file
     * @return
     */
    public MultipartFile file2MultipartFile(File file) {
        FileInputStream input = null;
        MultipartFile multipartFile = null;
        try {
            input = new FileInputStream(file);
            multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }
}
