package com.freewave.domain.project.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.dto.response.ProjectResponse;
import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
            @Valid @RequestBody ProjectRequest request) {

        Project project = projectService.createProject(principalDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProjectResponse(project));
    }

    @GetMapping("/v1/projects")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {

        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/v1/projects/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {

        return ResponseEntity.ok(projectService.getProject(id));
    }

    @GetMapping("/v1/my-projects")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return ResponseEntity.ok(projectService.getMyProjects(principalDetails));
    }

    @PutMapping("/v1/projects/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody ProjectRequest request) {

        Project updatedProject = projectService.updateProject(id, principalDetails, request);
        return ResponseEntity.ok(new ProjectResponse(updatedProject));
    }

    @DeleteMapping("/v1/projects/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        projectService.deleteProject(id, principalDetails);
        return ResponseEntity.noContent().build();
    }
}
