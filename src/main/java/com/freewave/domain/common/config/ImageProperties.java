package com.freewave.domain.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ImageProperties {
    private int maxWidth = 1920;
    private int maxHeight = 1080;
    private boolean preserveAspectRatio = true;
    private boolean forceResize = false; // 모든 이미지를 강제로 리사이징할지 여부
}
