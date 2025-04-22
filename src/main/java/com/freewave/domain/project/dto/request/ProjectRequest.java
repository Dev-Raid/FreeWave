package com.freewave.domain.project.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "프로젝트 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "프로젝트 설명은 필수입니다.")
    private String description;

    @NotNull(message = "예산은 필수입니다.")
    private Integer budget;

    @NotNull(message = "마감일은 필수입니다.")
    @Future(message = "마감일은 현재 날짜 이후여야 합니다.")
    private LocalDate deadline;
}
