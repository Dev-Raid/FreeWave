package com.freewave.domain.portfolio.entity;

import com.freewave.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String pdfUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    private Portfolio(String title, String description, String pdfUrl, Resume resume) {
        this.title = title;
        this.description = description;
        this.pdfUrl = pdfUrl;
        this.resume = resume;
    }

    public static Portfolio of(String title, String description, String pdfUrl, Resume resume) {
        return new Portfolio(title, description, pdfUrl, resume);
    }
}
