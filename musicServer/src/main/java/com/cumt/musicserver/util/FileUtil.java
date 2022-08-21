package com.cumt.musicserver.util;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class FileUtil {

    public final static String BUCKET="music";

    private static MinioClient minioClient;
    @Autowired
    public void setMinioClient(MinioClient minioClient){
        FileUtil.minioClient=minioClient;
    }

    public static String uploadFile(MultipartFile multipartFile,String type,String fun){
        String extension="."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String time= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid= UUID.randomUUID().toString().replace("-","");
        String newFileName=time+uuid+extension;
        String index;
        try {
            index=type+"/"+ fun+"/"+ LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
            String name=index+"/"+newFileName;
            InputStream inputStream = multipartFile.getInputStream();
            minioClient.putObject(PutObjectArgs.builder().bucket(BUCKET).object(name).stream(
                            inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build());
            return index+"/"+newFileName;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String uploadFileByStream(File file,String type, String fun,String extension,String ContentType){
//        String extension="."+ FilenameUtils.getExtension();

        String time= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid= UUID.randomUUID().toString().replace("-","");
        String newFileName=time+uuid+extension;
        String index;
        try {
            index=type+"/"+ fun+"/"+ LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
            String name=index+"/"+newFileName;
            InputStream inputStream= Files.newInputStream(file.toPath());
            minioClient.putObject(PutObjectArgs.builder().bucket(BUCKET).object(name).stream(
                            inputStream, file.length(), -1)
                    .contentType(ContentType)
                    .build());
            return index+"/"+newFileName;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean deleteFile(List<String> list){
        List<DeleteObject> objects = new LinkedList<>();
        list.forEach(a-> {
            if(ObjectUtils.isNotEmpty(a)) {
                objects.add(new DeleteObject(a));
            }
        });
        Iterable<io.minio.Result<DeleteError>> results = minioClient.removeObjects(
                RemoveObjectsArgs.builder().bucket(BUCKET).objects(objects).build());
        for (io.minio.Result<DeleteError> result : results) {
            DeleteError error = null;
            try {
                error = result.get();
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                throw new RuntimeException(e);
            }
            System.out.println(
                    "Error in deleting object " + error.objectName() + "; " + error.message());
        }
        return true;
    }

    public static Result afterUpload(String s){
        Map<String,String> map=new HashMap<String,String>();
        if("".equals(s)){
            return Result.fail("上传失败");
        }
        map.put("message","上传成功");
        map.put("url",s);
        return Result.ok(map);
    }

//
//    docker run \
//            -p 9000:9000 \
//            -p 9001:9001 \
//            --name minio1 \
//            -v $PWD/data:/data \
//            -e "MINIO_ROOT_USER=root" \
//            -e "MINIO_ROOT_PASSWORD=lzsLZS555555" \
//    quay.io/minio/minio server /data --console-address ":9001"


    public static void downloadMP3(String name, String url, HttpServletResponse httpServletResponse) throws IOException {
        String pathFirst = ResourceUtils.getURL("classpath:").getPath() + "static";
        String realPath=pathFirst+url;
        FileInputStream is=new FileInputStream(realPath);
        httpServletResponse.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(name,"UTF-8")+".mp3");
        ServletOutputStream os=httpServletResponse.getOutputStream();
        IOUtils.copy(is,os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }


    public static boolean fileIsExist(String url){
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(BUCKET).object(url).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            return false;
        }
    }
}
