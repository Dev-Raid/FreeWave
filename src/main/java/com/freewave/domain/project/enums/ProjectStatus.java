package com.freewave.domain.project.enums;

public enum ProjectStatus {
    REGISTERED,   // 프로젝트 등록됨
    QUOTING,      // 프리랜서 견적 제안 중
    IN_PROGRESS,  // 견적 수락 → 협업 중
    COMPLETED     // 완료됨
}
