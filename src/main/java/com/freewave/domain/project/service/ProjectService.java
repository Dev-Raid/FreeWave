package com.freewave.domain.project.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import java.util.List;

public interface ProjectService {

    Project createProject(PrincipalDetails principalDetails, ProjectRequest request);

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProject(Long id);

    List<ProjectResponse> getMyProjects(PrincipalDetails principalDetails);

    Project updateProject(Long projectId, PrincipalDetails principalDetails,
            ProjectRequest request);

    void deleteProject(Long projectId, PrincipalDetails principalDetails);

    void updateProjectStatusToQuoting(Long projectId);

    void updateProjectStatusToInProgress(Long projectId);
}
