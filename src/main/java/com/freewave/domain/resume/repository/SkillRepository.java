package com.freewave.domain.resume.repository;

import com.freewave.domain.resume.entity.Skill;
import com.freewave.domain.resume.enums.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByTechStack(TechStack techStack);
}
