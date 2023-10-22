package com.backend.service.abstractions;

import java.util.List;
import java.time.LocalDate;

import com.backend.model.AssetRefData;

public interface IAssetRefDataService {
    List<AssetRefData> findAllByAssetId(long assetId);
    List<AssetRefData> findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(Long assetId, LocalDate startDate, LocalDate endDate);
}
