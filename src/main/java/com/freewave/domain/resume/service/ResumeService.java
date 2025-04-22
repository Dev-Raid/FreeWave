package com.freewave.domain.resume.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.resume.dto.request.ResumeRequest;
import com.freewave.domain.resume.dto.response.ResumeResponse;

public interface ResumeService {
    ResumeResponse updateSkill(PrincipalDetails principalDetails, ResumeRequest responseRequest);

    ResumeResponse getUserSkillList(Long resumeId);
}
