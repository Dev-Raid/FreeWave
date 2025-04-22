package com.freewave.domain.resume.entity;

import com.freewave.domain.resume.enums.TechStack;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TechStack techStack;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeSkill> resumeSkillList = new ArrayList<>();

    public Skill(TechStack techStack) {
        this.techStack = techStack;
    }
}
