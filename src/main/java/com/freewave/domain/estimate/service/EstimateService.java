package com.freewave.domain.estimate.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.estimate.dto.request.EstimateRequest;
import com.freewave.domain.estimate.dto.response.EstimateResponse;

public interface EstimateService {

    EstimateResponse proposeEstimate(Long projectId, PrincipalDetails principalDetails,
            EstimateRequest request);

    EstimateResponse acceptEstimate(Long estimateId, PrincipalDetails principalDetails);

    EstimateResponse rejectEstimate(Long estimateId, PrincipalDetails principalDetails);
}
