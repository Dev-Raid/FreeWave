package com.freewave.domain.resume.service;

import com.freewave.domain.common.exception.InvalidRequestException;
import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.resume.dto.request.ResumeRequest;
import com.freewave.domain.resume.dto.response.ResumeResponse;
import com.freewave.domain.resume.entity.ResumeSkill;
import com.freewave.domain.resume.entity.Skill;
import com.freewave.domain.resume.enums.TechStack;
import com.freewave.domain.resume.repository.ResumeSkillRepository;
import com.freewave.domain.resume.repository.SkillRepository;
import com.freewave.domain.user.entity.User;
import com.freewave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeServiceImpl implements ResumeService {

    private final ResumeSkillRepository resumeSkillRepository;
    private final SkillRepository skillRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ResumeResponse updateSkill(PrincipalDetails principalDetails, ResumeRequest resumeRequest) {
        User user = userService.isValidUser(principalDetails.getUser().getId());

        List<Skill> skillList = new ArrayList<>();
        for (String skill : resumeRequest.getSkills()) {
            skillList.add(isValidSkill(skill));
        }

        List<ResumeSkill> newResumeSkillList = new ArrayList<>();
        for (Skill skill : skillList) {
            if (resumeSkillRepository.findByResume_IdAndSkill_Id(user.getResume().getId(), skill.getId()).isPresent()) {
                continue;
            }
            newResumeSkillList.add(new ResumeSkill(user.getResume(), skill));
        }
        resumeSkillRepository.saveAll(newResumeSkillList);

        return getUserSkillList(user.getResume().getId());
    }

    @Override
    public ResumeResponse getUserSkillList(Long resumeId) {
        List<String> skillList = resumeSkillRepository.findAllByResume_Id(resumeId).
                stream().map(resumeSkill -> resumeSkill
                        .getSkill()
                        .getTechStack()
                        .name()).toList();

        return new ResumeResponse(skillList);
    }

    @Override
    @Transactional
    public void deleteSkill(PrincipalDetails principalDetails, String skill) {
        ResumeSkill resumeSkill = resumeSkillRepository.findByResume_IdAndSkill_Id(
                principalDetails.getUser().getId(), isValidSkill(skill).getId()).orElseThrow(
                () -> new InvalidRequestException("Invalid resume skill")
        );

        resumeSkillRepository.delete(resumeSkill);
    }

    public Skill isValidSkill(String techStack) {
        return skillRepository.findByTechStack(TechStack.of(techStack)).orElseThrow(
                () -> new InvalidRequestException("Invalid tech stack")
        );
    }
}
