package com.freewave.domain.resume.entity;

import com.freewave.domain.portfolio.entity.Portfolio;
import com.freewave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Resume {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeSkill> resumeSkillList = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolioList = new ArrayList<>();

    public Resume(User user) {
        this.user = user;
    }
}
