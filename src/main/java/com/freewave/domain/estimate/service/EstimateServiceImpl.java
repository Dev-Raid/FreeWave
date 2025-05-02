package com.freewave.domain.estimate.service;

import com.freewave.domain.common.exception.ServiceNotFoundException;
import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.estimate.dto.request.EstimateRequest;
import com.freewave.domain.estimate.dto.response.EstimateResponse;
import com.freewave.domain.estimate.entity.Estimate;
import com.freewave.domain.estimate.enums.EstimateStatus;
import com.freewave.domain.estimate.repository.EstimateRepository;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.enums.ProjectStatus;
import com.freewave.domain.project.repository.ProjectRepository;
import com.freewave.domain.project.service.ProjectService;
import com.freewave.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateServiceImpl implements EstimateService {

    private final EstimateRepository estimateRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Override
    @Transactional
    public EstimateResponse proposeEstimate(Long projectId, PrincipalDetails principalDetails,
            EstimateRequest request) {

        if (principalDetails.getUser().getUserRole() != UserRole.ROLE_FREELANCER) {
            throw new AccessDeniedException("Only freelancers are allowed to submit estimates.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ServiceNotFoundException("Project not found."));

        projectService.updateProjectStatusToQuoting(projectId);

        Estimate estimate = Estimate.create(principalDetails.getUser(), project,
                request.getProposeBudget(), request.getContent());
        estimateRepository.save(estimate);

        return new EstimateResponse(estimate);
    }

    @Override
    @Transactional
    public EstimateResponse acceptEstimate(Long estimateId, PrincipalDetails principalDetails) {
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ServiceNotFoundException("Estimate not found."));

        if (estimate.getStatus() != EstimateStatus.ACCEPTED) {
            estimate.accept();
            projectService.updateProjectStatusToInProgress(estimate.getProject().getId());
        }

        return new EstimateResponse(estimate);
    }

    @Override
    @Transactional
    public EstimateResponse rejectEstimate(Long estimateId, PrincipalDetails principalDetails) {
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new ServiceNotFoundException("Estimate not found."));

        Project project = estimate.getProject();
        Long clientId = principalDetails.getUser().getId();

        if (!project.getClientId().equals(clientId)) {
            throw new AccessDeniedException("You are not authorized to reject this estimate.");
        }

        estimate.reject();

        // 모든 견적이 거절 상태일 경우 프로젝트 상태 REGISTERED로 변경
        if (project.getEstimates().stream()
                .allMatch(e -> e.getStatus() == EstimateStatus.REJECTED)) {
            project.updateStatus(ProjectStatus.REGISTERED);
        }

        return new EstimateResponse(estimate);
    }
}
