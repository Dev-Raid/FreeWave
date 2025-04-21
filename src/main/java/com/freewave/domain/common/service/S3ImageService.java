package com.freewave.domain.common.service;

import com.freewave.domain.common.component.S3Util;
import com.freewave.domain.common.config.ImageProperties;
import com.freewave.domain.common.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3ImageService implements S3Service {

    private static final List<String> SUPPORTED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private final S3Util s3Util;
    private final ImageProperties imageProperties;

    @Override
    public String uploadFile(MultipartFile file) {
        // 기본 설정에 따라 자동 리사이징 수행
        return uploadFile(file, null, null);
    }

    /**
     * 이미지를 업로드하고 선택적으로 리사이징
     * targetWidth와 targetHeight가 모두 null이면 설정에 따라 자동 리사이징
     * targetWidth와 targetHeight가 지정되면 해당 크기로 강제 리사이징
     */
    public String uploadFile(MultipartFile file, Integer targetWidth, Integer targetHeight) {
        try {
            validateFileType(file);

            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new FileProcessingException("Image cannot be read");
            }

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 사용자 지정 크기가 있는지 확인
            boolean hasCustomSize = targetWidth != null && targetHeight != null;

            // 리사이징이 필요한지 결정
            boolean needsResize;
            int newWidth, newHeight;

            if (hasCustomSize) {
                // 사용자 지정 크기로 강제 리사이징
                needsResize = true;
                newWidth = targetWidth;
                newHeight = targetHeight;
            } else {
                // 설정에 따라 자동 리사이징 여부 결정
                needsResize = shouldResizeImage(originalWidth, originalHeight);
                if (needsResize) {
                    Dimension newDimension = calculateNewDimension(originalWidth, originalHeight);
                    newWidth = (int) newDimension.getWidth();
                    newHeight = (int) newDimension.getHeight();
                } else {
                    newWidth = originalWidth;
                    newHeight = originalHeight;
                }
            }

            if (needsResize) {
                // 이미지 리사이징
                BufferedImage resizedImage = resizeImage(originalImage, newWidth, newHeight);

                // 리사이징된 이미지를 바이트 배열로 변환
                byte[] resizedImageBytes = convertImageToBytes(resizedImage, file.getContentType());

                // 메타데이터 설정
                Map<String, String> metadata = s3Util.createBasicMetadata(file, "image");
                addImageMetadata(metadata, newWidth, newHeight, originalWidth, originalHeight, true);
                metadata.put("file-size", String.valueOf(resizedImageBytes.length));

                if (hasCustomSize) {
                    metadata.put("custom-resize", "true");
                }

                // S3Util을 통한 업로드
                return s3Util.uploadBytesToS3WithMetadata(
                        resizedImageBytes,
                        file.getContentType(),
                        file.getOriginalFilename(),
                        metadata
                );
            } else {
                // 리사이징이 필요 없는 경우 원본 업로드
                Map<String, String> metadata = s3Util.createBasicMetadata(file, "image");
                addImageMetadata(metadata, originalWidth, originalHeight, originalWidth, originalHeight, false);

                return s3Util.uploadToS3WithMetadata(file, metadata);
            }
        } catch (IOException e) {
            throw new FileUploadException("An error occurred while uploading the image");
        }
    }

    @Override
    public byte[] downloadFile(String imageUrl) {
        try {
            return s3Util.downloadFromS3(imageUrl);
        } catch (S3Exception e) {
            throw new FileDownloadException("An error occurred while downloading the image: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public void deleteFile(String imageUrl) {
        try {
            s3Util.deleteFromS3(imageUrl);
        } catch (S3Exception e) {
            throw new FileDeleteException("An error occurred while deleting the image: " + e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    public boolean supportsContentType(String contentType) {
        return contentType != null && SUPPORTED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }

    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !supportsContentType(contentType)) {
            throw new UnsupportedFileTypeException("Unsupported image format: " + contentType);
        }
    }

    private boolean shouldResizeImage(int width, int height) {
        return imageProperties.isForceResize() ||
                width > imageProperties.getMaxWidth() ||
                height > imageProperties.getMaxHeight();
    }

    private Dimension calculateNewDimension(int originalWidth, int originalHeight) {
        int maxWidth = imageProperties.getMaxWidth();
        int maxHeight = imageProperties.getMaxHeight();

        if (imageProperties.isPreserveAspectRatio()) {
            double ratio = Math.min(
                    (double) maxWidth / originalWidth,
                    (double) maxHeight / originalHeight
            );

            int newWidth = (int) (originalWidth * ratio);
            int newHeight = (int) (originalHeight * ratio);

            return new Dimension(newWidth, newHeight);
        } else {
            int newWidth = Math.min(originalWidth, maxWidth);
            int newHeight = Math.min(originalHeight, maxHeight);

            return new Dimension(newWidth, newHeight);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        // 원본 이미지가 투명도를 지원하는 경우 (PNG 등)
        BufferedImage resizedImage;
        if (originalImage.getTransparency() != BufferedImage.OPAQUE) {
            resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        } else {
            resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D graphics2D = resizedImage.createGraphics();

        // 이미지 품질 향상을 위한 렌더링 힌트 설정
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private byte[] convertImageToBytes(BufferedImage image, String contentType) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String formatName = getFormatNameFromContentType(contentType);
        ImageIO.write(image, formatName, outputStream);
        return outputStream.toByteArray();
    }

    private void addImageMetadata(Map<String, String> metadata, int width, int height,
                                  int originalWidth, int originalHeight, boolean resized) {
        metadata.put("image-width", String.valueOf(width));
        metadata.put("image-height", String.valueOf(height));
        metadata.put("original-width", String.valueOf(originalWidth));
        metadata.put("original-height", String.valueOf(originalHeight));
        metadata.put("resized", String.valueOf(resized));
    }

    private String getFormatNameFromContentType(String contentType) {
        if (contentType.contains("jpeg") || contentType.contains("jpg")) {
            return "jpg";
        } else if (contentType.contains("png")) {
            return "png";
        } else if (contentType.contains("gif")) {
            return "gif";
        } else if (contentType.contains("webp")) {
            return "webp";
        } else {
            return "jpg"; // 기본값
        }
    }
}
