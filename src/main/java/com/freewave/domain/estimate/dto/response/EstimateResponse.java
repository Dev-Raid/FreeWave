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
        this.id = estimate.getId();
        this.freelancerName = estimate.getFreelancer().getNickname();
        this.proposeBudget = estimate.getProposeBudget();
        this.content = estimate.getContent();
        this.status = estimate.getStatus().name();
    }
}
