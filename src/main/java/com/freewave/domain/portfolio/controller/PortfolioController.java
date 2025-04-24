package com.freewave.domain.portfolio.controller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.portfolio.dto.response.PortfolioResponse;
import com.freewave.domain.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/v1/portfolio")
    public ResponseEntity<PortfolioResponse> createPortfolio(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(portfolioService.createPortfolio(principalDetails, multipartFile, title, description));
    }

    @GetMapping("/v1/portfolio")
    public ResponseEntity<List<PortfolioResponse>> getPortfolioList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.getPortfolioList(principalDetails));
    }

    @DeleteMapping("/v1/portfolio/{id}")
    public ResponseEntity<?> getPortfolioList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long id
    ) {
        portfolioService.delete(principalDetails, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
