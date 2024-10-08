package com.ape.miniodemo.test;

import com.ape.file.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
@SpringBootTest
public class MinIOTest {


    public static void main(String[] args) {

        FileInputStream fileInputStream = null;
        try {

            fileInputStream =  new FileInputStream("C:\\work\\list.html");

            //1.创建minio链接客户端
            //*******************一定注意，端口是API端口**************************
            MinioClient minioClient = MinioClient.builder().credentials("root", "ywq2001ywq").endpoint("http://localhost:9005").build();
            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("list.html")//文件名
                    .contentType("text/html")//文件类型
                    .bucket("ape")//桶名词  与minio创建的名词一致
                    .stream(fileInputStream, fileInputStream.available(), -1) //文件流
                    .build();
            minioClient.putObject(putObjectArgs);

            System.out.println("http://localhost:9005/ape/list.html");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testUpdateImgFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\项目\\1.jpg");
            String filePath = fileStorageService.uploadImgFile("", "1.jpg", fileInputStream);
            System.out.println(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}