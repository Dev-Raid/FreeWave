package com.freewave.domain.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일을 S3에 업로드하고 URL을 반환 (메타데이터 포함)
    public String uploadToS3WithMetadata(MultipartFile file, Map<String, String> metadata) throws IOException {
        String s3Key = generateS3Key(file.getName());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .metadata(metadata)
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(file.getBytes())
        );

        return generateFileUrl(s3Key);
    }

    // 바이트 배열을 S3에 업로드하고 URL을 반환 (메타데이터 포함)
    public String uploadBytesToS3WithMetadata(byte[] data, String contentType, String filename, Map<String, String> metadata) {
        String s3Key = generateS3Key(filename);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(contentType)
                .contentLength((long) data.length)
                .metadata(metadata)
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(data)
        );

        return generateFileUrl(s3Key);
    }

    // 기본 메타데이터를 생성
    public Map<String, String> createBasicMetadata(MultipartFile file, String fileType) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("original-filename", file.getOriginalFilename());
        metadata.put("content-type", file.getContentType());
        metadata.put("upload-date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        metadata.put("file-size", String.valueOf(file.getSize()));
        metadata.put("file-type", fileType);
        return metadata;
    }

    // S3에서 파일을 다운로드
    public byte[] downloadFromS3(String url) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(extractS3KeyFromUrl(url))
                .build();

        ResponseBytes<?> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return responseBytes.asByteArray();
    }

    // S3에서 파일을 삭제
    public void deleteFromS3(String url) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(extractS3KeyFromUrl(url))
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    // S3 객체의 메타데이터를 가져옴
    public Map<String, String> getMetadata(String url) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(extractS3KeyFromUrl(url))
                .build();

        HeadObjectResponse response = s3Client.headObject(headObjectRequest);
        return response.metadata();
    }

    // S3 키를 생성
    public String generateS3Key(String filename) {
        return UUID.randomUUID() + "_" + filename;
    }

    // S3 파일 URL을 생성
    public String generateFileUrl(String s3Key) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;
    }

    // S3 파일 URL에서 S3 키를 추출
    public String extractS3KeyFromUrl(String url) {
        int startIndex = url.indexOf(".s3.amazonaws.com/") + ".s3.amazonaws.com/".length();
        return url.substring(startIndex);
    }
}
