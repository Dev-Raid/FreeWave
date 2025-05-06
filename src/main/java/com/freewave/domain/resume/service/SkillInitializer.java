package com.freewave.domain.resume.service;

import com.freewave.domain.resume.entity.Skill;
import com.freewave.domain.resume.enums.TechStack;
import com.freewave.domain.resume.repository.SkillRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillInitializer implements CommandLineRunner {

    private final SkillRepository skillRepository;

    @Override
    public void run(String... args) {
        // 모든 TechStack 값에 대해 Skill을 DB에 저장
        Arrays.stream(TechStack.values()).forEach(stack -> {
            // 이미 있는 스킬은 건너뜀
            skillRepository.findByTechStack(stack)
                    .orElseGet(() -> skillRepository.save(new Skill(stack)));
        });
    }

}
