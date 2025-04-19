package com.freewave.domain.project.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.repository.ProjectRepository;
import com.freewave.domain.user.enums.UserRole;
import jakarta.persistence.EntityNotFoundException;
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
    public Project createProject(PrincipalDetails principalDetails, ProjectRequest request) {

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

    @Override
    public List<ProjectResponse> getMyProjects(PrincipalDetails principalDetails) {
        Long clientId = principalDetails.getUser().getId();

        return projectRepository.findByClientId(clientId).stream()
                .map(ProjectResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public Project updateProject(Long projectId, PrincipalDetails principalDetails,
            ProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getClientId().equals(principalDetails.getUser().getId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        project.update(request);
        return project;
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, PrincipalDetails principalDetails) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getClientId().equals(principalDetails.getUser().getId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        projectRepository.delete(project);
    }
}
