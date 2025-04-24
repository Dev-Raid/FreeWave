package com.freewave.domain.portfolio.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/v1/portfolio")
    public ResponseEntity<?> createPortfolio(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(portfolioService.createPortfolio(principalDetails, multipartFile, title, description));
    }
}
