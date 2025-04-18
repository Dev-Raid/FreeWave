package com.freewave.domain.project.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            @RequestBody ProjectRequest request) {

        Project project = projectService.createProject(principalDetails, request);
        return ResponseEntity.ok(new ProjectResponse(project));
    }

    @GetMapping("/v1/projects")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/v1/myprojects")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(projectService.getMyProjects(principalDetails));
    }

    @PutMapping("/v1/projects/{Id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ProjectRequest request) {

        Project updatedProject = projectService.updateProject(projectId, principalDetails, request);
        return ResponseEntity.ok(new ProjectResponse(updatedProject));
    }
}
