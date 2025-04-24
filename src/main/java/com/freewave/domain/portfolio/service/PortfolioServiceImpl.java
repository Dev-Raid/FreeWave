package com.freewave.domain.portfolio.service;

import com.freewave.domain.common.exception.InvalidRequestException;
import com.freewave.domain.common.exception.UnsupportedFileTypeException;
import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.common.service.S3Service;
import com.freewave.domain.common.service.S3ServiceFactory;
import com.freewave.domain.portfolio.dto.response.PortfolioResponse;
import com.freewave.domain.portfolio.entity.Portfolio;
import com.freewave.domain.portfolio.repository.PortfolioRepository;
import com.freewave.domain.resume.entity.Resume;
import com.freewave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioServiceImpl implements PortfolioService {

    private final UserService userService;
    private final S3ServiceFactory s3ServiceFactory;
    private final PortfolioRepository portfolioRepository;

    @Override
    @Transactional
    public PortfolioResponse createPortfolio(PrincipalDetails principalDetails, MultipartFile file, String title, String description) {
        Resume resume = userService.isValidUser(principalDetails.getUser().getId()).getResume();

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("Portfolio file is required");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new UnsupportedFileTypeException("Only pdf files can be uploaded. Current file type: " + contentType);
        }

        S3Service s3Service = s3ServiceFactory.getServiceForContentType(file.getContentType());
        String pdfUrl = s3Service.uploadFile(file);

        Portfolio newPortfolio = Portfolio.of(title, description, pdfUrl, resume);
        Portfolio savedPortfolio = portfolioRepository.save(newPortfolio);

        return new PortfolioResponse(
                savedPortfolio.getId(),
                savedPortfolio.getTitle(),
                savedPortfolio.getDescription(),
                savedPortfolio.getPdfUrl()
        );
    }
}
