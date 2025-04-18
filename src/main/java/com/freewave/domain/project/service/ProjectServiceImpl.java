package com.freewave.domain.project.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectSaveRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.repository.ProjectRepository;
import com.freewave.domain.user.enums.UserRole;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Project createProject(PrincipalDetails principalDetails, ProjectSaveRequest request) {

        if (principalDetails.getUser().getUserRole() != UserRole.ROLE_CLIENT) {
            throw new AccessDeniedException("프로젝트 등록은 클라이언트만 가능합니다.");
        }

        Long clientId = principalDetails.getUser().getId();

        Project project = Project.create(clientId, request);

        return projectRepository.save(project);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectResponse::new)
                .toList();
    }

}
