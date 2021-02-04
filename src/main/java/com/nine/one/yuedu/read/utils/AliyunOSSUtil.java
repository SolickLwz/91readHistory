package com.nine.one.yuedu.read.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.nine.one.yuedu.read.config.AliyunOSSConfig;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author wangyuliang
 * 阿里巴巴的OSS工具类
 */
@Component
public class AliyunOSSUtil {
    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};
    @Resource(name = "hangzhou")
    private OSS ossClient;

    @Resource(name = "beijing")
    private OSS jxOssClient;

    @Autowired
    private AliyunOSSConfig aliyunConfig;


    /**
     * 上传一个字符串,把它变成一个txt文件,不去上传到OSS
     *
     * @param bookId
     * @param chapterId
     * @param content
     * @return
     */
    public FileUploadResult stringToTxtAndUploadOSS(Integer bookId, Integer chapterId, String content) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StringBuffer filePath = new StringBuffer("booktxt/");
        filePath.append(bookId).append("/").append(chapterId).append(".txt");
        ossClient.putObject(aliyunConfig.getBucketName(), filePath.toString(), stream);
        FileUploadResult result = new FileUploadResult();
        result.setStatus("done");
        result.setName(this.aliyunConfig.getUrlPrefix() + filePath.toString());
        result.setResponse("success");
        return result;
    }

    /**
     * @author 不忘初心
     * @desc 文件上传
     * @date 2019-07-31 11:31
     */
    public FileUploadResult upload(MultipartFile uploadFile) {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }
        //封装Result对象，并且将文件的byte数组放置到result对象中
        FileUploadResult fileUploadResult = new FileUploadResult();
        if (!isLegal) {
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        //文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        // 上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new
                    ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setResponse("success");
        fileUploadResult.setName(this.aliyunConfig.getUrlPrefix() + filePath);
        fileUploadResult.setUid(String.valueOf(System.currentTimeMillis()));
        return fileUploadResult;
    }

    /**
     * 上传文件到景像的OSS
     *
     * @param uploadFile
     * @return
     */
    public FileUploadResult uploadFileToNxstory(MultipartFile uploadFile) {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }
        //封装Result对象，并且将文件的byte数组放置到result对象中
        FileUploadResult fileUploadResult = new FileUploadResult();
        if (!isLegal) {
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        //文件新路径
        String fileName = uploadFile.getOriginalFilename();
        DateTime dateTime = new DateTime();
        String filePath =
                "cover/" + dateTime.toString("yyyy")
                        + "/" + dateTime.toString("MM") + "/"
                        + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                        RandomUtils.nextInt(100, 9999) + "." +
                        StringUtils.substringAfterLast(fileName, ".");
        // 上传到阿里云
        try {
            jxOssClient.putObject("nxstory-res", filePath, new
                    ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setResponse("success");
        fileUploadResult.setName("http://res.nxstory.com/" + filePath);
        fileUploadResult.setUid(String.valueOf(System.currentTimeMillis()));
        return fileUploadResult;
    }


    /**
     * @author 不忘初心
     * @desc 生成路径以及文件名 例如：//images/2019/08/10/15564277465972939.jpg
     * @date 2019-07-31 11:31
     */
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }

    /**
     * @author 不忘初心
     * @desc 查看文件列表
     * @date 2019-07-31 11:31
     */
    public List<OSSObjectSummary> list() {
        // 设置最大个数。
        final int maxKeys = 200;
        // 列举文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(aliyunConfig.getBucketName()).withMaxKeys(maxKeys));
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        return sums;
    }

    /**
     * @author 不忘初心
     * @desc 删除文件
     * @date 2019-07-31 11:31
     */
    public FileUploadResult delete(String objectName) {
        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(aliyunConfig.getBucketName(), objectName);
        FileUploadResult fileUploadResult = new FileUploadResult();
        fileUploadResult.setName(objectName);
        fileUploadResult.setStatus("removed");
        fileUploadResult.setResponse("success");
        return fileUploadResult;
    }


    /**
     * @author 不忘初心
     * @desc 下载文件
     * @date 2019-07-31 11:31
     */
    public void exportOssFile(String objectName) throws IOException {


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliyunConfig.getEndpoint(), aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());

        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getBucketName(), objectName);

        // 读取文件内容。
        System.out.println("Object content:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }

            System.out.println("\n" + line);
        }
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        reader.close();

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**
     * @author wangyuliang
     * 文件上传结果
     */
    public class FileUploadResult {
        // 文件唯一标识
        private String uid;
        // 文件名
        private String name;
        // 状态有：uploading done error removed
        private String status;
        // 服务端响应内容，如：'{"status": "success"}'
        private String response;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        @Override
        public String toString() {
            return "FileUploadResult{" +
                    "uid='" + uid + '\'' +
                    ", name='" + name + '\'' +
                    ", status='" + status + '\'' +
                    ", response='" + response + '\'' +
                    '}';
        }
    }


}


