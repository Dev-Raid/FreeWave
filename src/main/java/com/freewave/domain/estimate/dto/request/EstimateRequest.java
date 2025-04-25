package com.freewave.domain.estimate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstimateRequest {

    @NotNull(message = "예산은 필수입니다.")
    private Integer proposeBudget;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
