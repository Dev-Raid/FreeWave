package com.freewave.domain.estimate.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.estimate.dto.request.EstimateRequest;
import com.freewave.domain.estimate.dto.response.EstimateResponse;

public interface EstimateService {

    EstimateResponse proposeEstimate(Long projectId, PrincipalDetails principalDetails,
            EstimateRequest request);
}
