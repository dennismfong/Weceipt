package com.example.server;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

public class S3Wrapper {
  private AmazonS3 s3Client;
  private String clientRegion = "us-west-1";
  private String bucketName = "weceiptuploads";
  private String bucketPrefix = "https://s3-us-west-1.amazonaws.com/weceiptuploads/";

  public S3Wrapper() {
    s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(clientRegion)
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();
  }

  public String uploadToS3(String imageString) {
    byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64(imageString.getBytes());
    InputStream fis = new ByteArrayInputStream(bI);
    UUID uuid = UUID.randomUUID();
    String objectKey = uuid.toString();

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(bI.length);
    metadata.setContentType("image/png");
    metadata.setCacheControl("public, max-age=31536000");
    s3Client.putObject(bucketName, objectKey, fis, metadata);
    s3Client.setObjectAcl(bucketName, objectKey, CannedAccessControlList.PublicRead);
    return bucketPrefix + objectKey;
  }
}