package com.freewave.domain.project.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectSaveRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/v1/projects")
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ProjectSaveRequest request) {

        Project project = projectService.createProject(principalDetails, request);
        return ResponseEntity.ok(new ProjectResponse(project));
    }

}
