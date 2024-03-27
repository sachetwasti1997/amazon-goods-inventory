package com.sachet.goodsinventoryamazon.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AwsUtils {

    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final Regions regions = Regions.US_EAST_1;
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsUtils.class);
    private final String IMAGE_SUFFIX;

    public AwsUtils(@Value("${aws_access_key}") String accessKey,
                    @Value("${aws_secret_key}") String secretKey,
                    @Value("${aws_bucket_name}") String bucketName) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        IMAGE_SUFFIX = "https://"+bucketName+".s3.amazonaws.com/";
    }

    private AmazonS3 buildClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(regions)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    private File convertMultiToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fs = new FileOutputStream(convertedFile)) {
            fs.write(file.getBytes());
        }catch (IOException ex) {
            LOGGER.info("Exception while uploading file {}", ex.getMessage());
        }
        return convertedFile;
    }

    private String convertFileName(String fileName) {
        if (fileName == null) return  "_";
        var nameArr = fileName.split(" ");
        if (nameArr.length > 1) {
            fileName = String.join("_", nameArr);
        }
        return fileName;
    }

    public String uploadFile(MultipartFile file) {
        AmazonS3 client = buildClient();
        String fileName = System.currentTimeMillis()+convertFileName(file.getOriginalFilename());
        LOGGER.info("The fileName of file uploaded {}", fileName);
        var result = client.putObject(new PutObjectRequest(bucketName, fileName, convertMultiToFile(file)));
        LOGGER.info("Successfully uploaded files {}", result.toString());
        return IMAGE_SUFFIX+fileName;
    }
}
