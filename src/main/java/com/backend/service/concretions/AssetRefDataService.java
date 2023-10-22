package com.backend.service.concretions;

import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.AssetRefData;
import com.backend.repository.AssetRefDataRepository;

@Service
public class AssetRefDataService implements com.backend.service.abstractions.IAssetRefDataService {
    private final AssetRefDataRepository repository;

    @Autowired
    public AssetRefDataService(AssetRefDataRepository repository){
        this.repository = repository;
    }

    @Override
    public List<AssetRefData> findAllByAssetId(long assetId){
        return repository.findAllByAssetId(assetId);
    }

    @Override
    public List<AssetRefData> findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(Long assetId, LocalDate startDate, LocalDate endDate){
        return repository.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
    }
}
