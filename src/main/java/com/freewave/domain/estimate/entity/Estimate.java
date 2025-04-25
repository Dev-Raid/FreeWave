package com.freewave.domain.estimate.entity;

import com.freewave.domain.common.Timestamped;
import com.freewave.domain.estimate.enums.EstimateStatus;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "estimate")
public class Estimate extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer proposeBudget;
    private String content;

    @Enumerated(EnumType.STRING)
    private EstimateStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public static Estimate create(User freelancer, Project project, Integer proposeBudget,
            String content) {
        Estimate estimate = new Estimate();
        estimate.freelancer = freelancer;
        estimate.project = project;
        estimate.proposeBudget = proposeBudget;
        estimate.content = content;
        estimate.status = EstimateStatus.PROPOSED;
        return estimate;
    }

    public void accept() {
        this.status = EstimateStatus.ACCEPTED;
    }

    public void reject() {
        this.status = EstimateStatus.REJECTED;
    }
}
