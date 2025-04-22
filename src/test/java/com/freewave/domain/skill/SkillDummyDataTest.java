package com.freewave.domain.skill;

import com.freewave.domain.resume.entity.Skill;
import com.freewave.domain.resume.enums.TechStack;
import com.freewave.domain.resume.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SkillDummyDataTest {

    @Autowired
    private SkillRepository skillRepository;

    @Test
    @Transactional
    @Commit
    public void saveAllTechStacks() {
        List<Skill> skillList = Arrays.stream(TechStack.values())
                .map(Skill::new)
                .toList();

        skillRepository.saveAll(skillList);
    }
}
