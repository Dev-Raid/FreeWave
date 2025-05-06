package com.freewave.domain.project.entity;

import com.freewave.domain.common.Timestamped;
import com.freewave.domain.estimate.entity.Estimate;
import com.freewave.domain.project.dto.request.ProjectRequest;
import com.freewave.domain.project.enums.ProjectStatus;
import com.freewave.domain.resume.entity.Skill;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estimate> estimatesList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectSkill> projectSkillsList = new ArrayList<>();

    public static Project create(Long clientId, ProjectRequest request, List<Skill> skills) {
        Project project = new Project();
        project.clientId = clientId;
        project.title = request.getTitle();
        project.description = request.getDescription();
        project.budget = request.getBudget();
        project.deadline = request.getDeadline();
        project.status = ProjectStatus.REGISTERED;
        for (Skill skill : skills) {
            project.projectSkillsList.add(new ProjectSkill(project, skill));
        }
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
