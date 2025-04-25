package com.freewave.domain.estimate.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.estimate.dto.request.EstimateRequest;
import com.freewave.domain.estimate.dto.response.EstimateResponse;
import com.freewave.domain.estimate.service.EstimateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EstimateController {

    private final EstimateService estimateService;

    @PostMapping("/v1/estimates/{projectId}")
    public ResponseEntity<EstimateResponse> proposeEstimate(
            @PathVariable Long projectId,
            @Valid @RequestBody EstimateRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        EstimateResponse response = estimateService.proposeEstimate(projectId, principalDetails,
                request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
