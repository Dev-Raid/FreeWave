package com.freewave.domain.project.entity;

import com.freewave.domain.common.Timestamped;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.enums.ProjectStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "project")
public class Project extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;
    private String title;
    private String description;
    private Integer budget;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    public static Project create(Long clientId, ProjectRequest request) {
        Project project = new Project();
        project.clientId = clientId;
        project.title = request.getTitle();
        project.description = request.getDescription();
        project.budget = request.getBudget();
        project.deadline = request.getDeadline();
        project.status = ProjectStatus.REGISTERED;
        return project;
    }

    public void update(ProjectRequest request) {
        title = request.getTitle();
        description = request.getDescription();
        budget = request.getBudget();
        deadline = request.getDeadline();
    }

    public void updateStatus(ProjectStatus newStatus) {
        this.status = newStatus;
    }
}
