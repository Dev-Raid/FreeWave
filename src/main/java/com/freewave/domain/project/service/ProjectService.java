package com.freewave.domain.project.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectSaveRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import java.util.List;

public interface ProjectService {

    Project createProject(PrincipalDetails principalDetails, ProjectSaveRequest request);

    List<ProjectResponse> getAllProjects();
}
