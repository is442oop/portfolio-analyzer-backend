package com.backend.repository;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.backend.model.AssetRefData;

@RepositoryRestResource(exported = false)
public interface AssetRefDataRepository extends JpaRepository<AssetRefData, Long>{
    List<AssetRefData> findAllByAssetId(long assetId);
    List<AssetRefData> findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(Long assetId, LocalDate startDate, LocalDate endDate);
}