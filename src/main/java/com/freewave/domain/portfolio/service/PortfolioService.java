package com.freewave.domain.portfolio.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.portfolio.dto.response.PortfolioResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PortfolioService {

    PortfolioResponse createPortfolio(PrincipalDetails principalDetails, MultipartFile multipartFile, String title, String description);

    List<PortfolioResponse> getPortfolioList(PrincipalDetails principalDetails);
}
