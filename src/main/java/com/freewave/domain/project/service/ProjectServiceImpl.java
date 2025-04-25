package com.freewave.domain.project.service;

import com.freewave.domain.common.exception.ServiceNotFoundException;
import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectRequest;
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
    public Project createProject(PrincipalDetails principalDetails, ProjectRequest request) {

        if (principalDetails.getUser().getUserRole() != UserRole.ROLE_CLIENT) {
            throw new AccessDeniedException("Only clients are allowed to register a project.");
        }

        Long clientId = principalDetails.getUser().getId();

        return projectRepository.save(Project.create(clientId, request));
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectResponse::new)
                .toList();
    }

    @Override
    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Project not found."));
        return new ProjectResponse(project);
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
                .orElseThrow(() -> new ServiceNotFoundException("Project not found."));

        if (!project.getClientId().equals(principalDetails.getUser().getId())) {
            throw new AccessDeniedException("You do not have permission to update this project.");
        }

        project.update(request);
        return project;
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, PrincipalDetails principalDetails) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ServiceNotFoundException("Project not found."));

        if (!project.getClientId().equals(principalDetails.getUser().getId())) {
            throw new AccessDeniedException("You do not have permission to delete this project.");
        }

        projectRepository.delete(project);
    }
}
