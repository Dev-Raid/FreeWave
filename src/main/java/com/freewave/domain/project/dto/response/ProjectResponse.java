package com.freewave.domain.project.dto.response;

import com.freewave.domain.project.entity.Project;
import com.freewave.domain.project.enums.ProjectStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProjectResponse {

    private final Long id;
    private final Long clientId;
    private final String title;
    private final String description;
    private final Integer budget;
    private final LocalDate deadline;
    private final ProjectStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ProjectResponse(Project project) {
        id = project.getId();
        clientId = project.getClientId();
        title = project.getTitle();
        description = project.getDescription();
        budget = project.getBudget();
        deadline = project.getDeadline();
        status = project.getStatus();
        createdAt = project.getCreatedAt();
        modifiedAt = project.getModifiedAt();
    }

}
