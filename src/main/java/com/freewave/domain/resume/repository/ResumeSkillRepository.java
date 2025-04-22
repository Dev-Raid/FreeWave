package com.freewave.domain.resume.repository;

import com.freewave.domain.resume.entity.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, Long> {
    List<ResumeSkill> findAllByResume_Id(Long resumeId);

    Optional<ResumeSkill> findByResume_IdAndSkill_Id(Long resumeId, Long skillId);
}
