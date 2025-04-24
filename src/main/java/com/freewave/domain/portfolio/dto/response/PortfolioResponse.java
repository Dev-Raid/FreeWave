package com.freewave.domain.portfolio.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PortfolioResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String pdfFile;
}
