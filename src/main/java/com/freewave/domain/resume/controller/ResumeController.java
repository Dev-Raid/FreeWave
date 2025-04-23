package com.freewave.domain.resume.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.resume.dto.request.ResumeRequest;
import com.freewave.domain.resume.dto.response.ResumeResponse;
import com.freewave.domain.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResumeController {

    private final ResumeService resumeService;

    @PutMapping("/v1/resumes/skills")
    public ResponseEntity<ResumeResponse> updateSkill(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ResumeRequest resumeRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.updateSkill(principalDetails, resumeRequest));
    }

    @GetMapping("/v1/resumes/skills")
    public ResponseEntity<ResumeResponse> getSkillList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.getUserSkillList(principalDetails.getUser().getId()));
    }

    @DeleteMapping("/v1/resumes/skills/{skill}")
    public ResponseEntity<?> deleteSkill(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable String skill
    ) {
        resumeService.deleteSkill(principalDetails, skill);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
