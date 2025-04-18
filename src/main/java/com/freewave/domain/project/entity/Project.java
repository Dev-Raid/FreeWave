package com.freewave.domain.project.entity;

import com.freewave.domain.project.dto.request.ProjectSaveRequest;
import com.freewave.domain.project.enums.ProjectStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "project")
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;
    private String title;
    private String description;
    private Integer budget;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Project create(Long clientId, ProjectSaveRequest request) {
        Project project = new Project();
        project.clientId = clientId;
        project.title = request.getTitle();
        project.description = request.getDescription();
        project.budget = request.getBudget();
        project.deadline = request.getDeadline();
        project.status = ProjectStatus.REGISTERED;
        project.createdAt = LocalDateTime.now();
        project.updatedAt = LocalDateTime.now();
        return project;
    }

}
