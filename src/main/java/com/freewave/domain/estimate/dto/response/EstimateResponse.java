package com.freewave.domain.estimate.dto.response;

import com.freewave.domain.estimate.entity.Estimate;
import lombok.Getter;

@Getter
public class EstimateResponse {

    private final Long id;
    private final String freelancerName;
    private final Integer proposeBudget;
    private final String content;
    private final String status;

    public EstimateResponse(Estimate estimate) {
        id = estimate.getId();
        freelancerName = estimate.getFreelancer().getNickname();
        proposeBudget = estimate.getProposeBudget();
        content = estimate.getContent();
        status = estimate.getStatus().name();
    }
}
