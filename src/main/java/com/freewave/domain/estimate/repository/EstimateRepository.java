package com.freewave.domain.estimate.repository;

import com.freewave.domain.estimate.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

}
