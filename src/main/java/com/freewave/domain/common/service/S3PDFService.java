package com.freewave.domain.common.service;

import com.freewave.domain.common.component.S3Util;
import com.freewave.domain.common.exception.FileDeleteException;
import com.freewave.domain.common.exception.FileDownloadException;
import com.freewave.domain.common.exception.FileUploadException;
import com.freewave.domain.common.exception.UnsupportedFileTypeException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3PDFService implements S3Service {

    private static final List<String> SUPPORTED_PDF_TYPES = List.of(
            "application/pdf"
    );

    private final S3Util s3Util;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            validateFileType(file);

            // 메타데이터 설정
            Map<String, String> metadata = createPdfMetadata(file);

            // S3Util을 통한 업로드
            return s3Util.uploadToS3WithMetadata(file, metadata);
        } catch (IOException e) {
            throw new FileUploadException("An error occurred while uploading PDF: " + e.getMessage());
        }
    }

    private Map<String, String> createPdfMetadata(MultipartFile file) {
        // 기본 메타데이터 생성
        Map<String, String> metadata = s3Util.createBasicMetadata(file, "pdf");

        try {
            PDDocument document = PDDocument.load(file.getBytes());
            metadata.put("page-count", String.valueOf(document.getNumberOfPages()));

            PDDocumentInformation info = document.getDocumentInformation();
            if (info.getTitle() != null) metadata.put("pdf-title", info.getTitle());
            if (info.getAuthor() != null) metadata.put("pdf-author", info.getAuthor());
            if (info.getSubject() != null) metadata.put("pdf-subject", info.getSubject());
            if (info.getCreator() != null) metadata.put("pdf-creator", info.getCreator());
            if (info.getProducer() != null) metadata.put("pdf-producer", info.getProducer());

            document.close();
        } catch (Exception e) {
            // PDF 정보 읽기 실패 시 무시
        }

        return metadata;
    }

    @Override
    public byte[] downloadFile(String imageUrl) {
        try {
            return s3Util.downloadFromS3(imageUrl);
        } catch (S3Exception e) {
            throw new FileDownloadException("An error occurred while downloading the PDF: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public void deleteFile(String imageUrl) {
        try {
            s3Util.deleteFromS3(imageUrl);
        } catch (S3Exception e) {
            throw new FileDeleteException("An error occurred while deleting PDF: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public boolean supportsContentType(String contentType) {
        return contentType != null && SUPPORTED_PDF_TYPES.contains(contentType.toLowerCase());
    }

    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !supportsContentType(contentType)) {
            throw new UnsupportedFileTypeException("Only PDF files can be uploaded: " + contentType);
        }
    }
}
