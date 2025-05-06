package com.freewave.domain.project.service;

import com.freewave.domain.common.exception.InvalidRequestException;
import com.freewave.domain.common.exception.ServiceNotFoundException;
import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.entity.ProjectSkill;
import com.freewave.domain.project.enums.ProjectStatus;
import com.freewave.domain.project.repository.ProjectRepository;
import com.freewave.domain.resume.entity.Skill;
import com.freewave.domain.resume.enums.TechStack;
import com.freewave.domain.resume.repository.SkillRepository;
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
    private final SkillRepository skillRepository;

    @Override
    @Transactional
    public Project createProject(PrincipalDetails principalDetails, ProjectRequest request) {

        if (principalDetails.getUser().getUserRole() != UserRole.ROLE_CLIENT) {
            throw new AccessDeniedException("Only clients are allowed to register a project.");
        }

        Long clientId = principalDetails.getUser().getId();

        if (request.getSkills() == null || request.getSkills().isEmpty()) {
            throw new InvalidRequestException("At least one skill must be provided.");
        }

        List<Skill> skills = request.getSkills().stream()
                .map(TechStack::of)
                .map(ts -> skillRepository.findByTechStack(ts)
                        .orElseThrow(
                                () -> new InvalidRequestException("Invalid tech stack: " + ts)))
                .toList();

        return projectRepository.save(Project.create(clientId, request, skills));
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

        // 기존 스킬을 새로 받은 스킬 목록으로 교체
        List<Skill> newSkills = request.getSkills().stream()
                .map(TechStack::of)
                .map(ts -> skillRepository.findByTechStack(ts)
                        .orElseThrow(() -> new InvalidRequestException(
                                "Invalid tech stack: " + ts)))
                .toList();

        project.getProjectSkillsList().clear();

        for (Skill skill : newSkills) {
            project.getProjectSkillsList().add(new ProjectSkill(project, skill));
        }

        project.update(request);

        return projectRepository.save(project);
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

    @Override
    @Transactional
    public void updateProjectStatusToQuoting(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ServiceNotFoundException("Project not found."));

        project.updateStatus(ProjectStatus.QUOTING);
    }

    @Override
    @Transactional
    public void updateProjectStatusToInProgress(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ServiceNotFoundException("Project not found."));

        project.updateStatus(ProjectStatus.IN_PROGRESS);
    }
}
