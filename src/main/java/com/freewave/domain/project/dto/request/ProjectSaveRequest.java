package com.freewave.domain.project.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSaveRequest {

    private String title;
    private String description;
    private Integer budget;
    private LocalDate deadline;
}
