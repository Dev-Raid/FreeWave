package com.freewave.domain.resume.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResumeResponse {

    private final List<String> skillList;
}
