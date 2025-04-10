package com.example.newbuilder.service;

import com.example.newbuilder.DTO.*;
import com.example.newbuilder.entity.Dataset;
import com.example.newbuilder.exception.DatasetNotFoundException;
import com.example.newbuilder.exception.DuplicateDatasetException;
import com.example.newbuilder.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Optional;
import java.util.UUID;

@Service
public class DatasetService {
    @Autowired
    private DatasetRepository repository;

    public DataSetResponse2 createDataset(DataSetRequest datasetRequest) {
        // // System.out.println("--------------------------------------------");
        System.out.println(repository.findById(datasetRequest.getDatasetId()).isEmpty());
        // // System.out.println("--------------------------------------------");
        if(!repository.findById(datasetRequest.getDatasetId()).isEmpty()){
        if(datasetRequest.getDatasetId().compareTo(repository.findByDatasetId(datasetRequest.getDatasetId()).get().getDatasetId())==0){
            System.out.println("same same but different");
            throw new DuplicateDatasetException("Dataset with ID " + datasetRequest.getDatasetId() + " already exists");
            
        }
    }
        
        // if (datasetRequest.getDataset_id() == null || datasetRequest.getDataset_id().isEmpty()) {
        //     throw new DatasetNotFoundException("Dataset ID cannot be null or empty");
        // }
        Dataset dataset = new Dataset();
        dataset.setId(UUID.randomUUID().toString());
        dataset.setDatasetId(datasetRequest.getDatasetId());
        dataset.setType(datasetRequest.getType());
        dataset.setName(datasetRequest.getName());
        dataset.setStatus(datasetRequest.getStatus());
        dataset.setTags(datasetRequest.getTags());
        //dataset.setDataVersion(datasetRequest.getDataVersion());
        dataset.setDataVersion(1);
        dataset.setCreatedBy(datasetRequest.getCreatedBy());
        dataset.setUpdatedBy(datasetRequest.getUpdatedBy());
        dataset.setValidationConfig(datasetRequest.getValidationConfig());
        dataset.setExtractionConfig(datasetRequest.getExtractionConfig());
        dataset.setDatasetConfig(datasetRequest.getDedupConfig());
        dataset.setDataSchema(datasetRequest.getDataSchema());
        dataset.setDenormConfig(datasetRequest.getDenormConfig());
        dataset.setRouterConfig(datasetRequest.getRouterConfig());
        dataset.setDatasetConfig(datasetRequest.getDatasetConfig());

        dataset.setCreatedDate(LocalDateTime.now());
        dataset.setUpdatedDate(LocalDateTime.now().toLocalDate());
        dataset.setPublishedDate(LocalDateTime.now());

        try {
            dataset = repository.save(dataset) ;
        }
        catch(IllegalArgumentException e ){
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
        catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        // System.out.println("Dataset created with ID: " + dataset.getDataset_id());
       

        return mapToResponse2(dataset,"Dataset created successfully");
    }

    public Dataset getDatasetById(String id) {
        return repository.findByDatasetId(id)
                .orElseThrow(() -> new DatasetNotFoundException("Dataset with ID " + id + " not found"));
    }

    @Transactional
    public Dataset updateDataset(String datasetId, Dataset newData) {
        return repository.findByDatasetId(datasetId).map(dataset -> {
            if (newData.getType() != null) dataset.setType(newData.getType());
            if (newData.getName() != null) dataset.setName(newData.getName());
            if (newData.getStatus() != null) dataset.setStatus(newData.getStatus());
            if (newData.getTags() != null) dataset.setTags(newData.getTags());
            //if (newData.getDataVersion() != 0) dataset.setDataVersion(dataset.getDataVersion()+1);
            dataset.setDataVersion(dataset.getDataVersion()+1);
            if (newData.getCreatedBy() != null) dataset.setCreatedBy(newData.getCreatedBy());
            if (newData.getUpdatedBy() != null) dataset.setUpdatedBy(newData.getUpdatedBy());
            if (newData.getValidationConfig() != null) dataset.setValidationConfig(newData.getValidationConfig());
            if (newData.getExtractionConfig() != null) dataset.setExtractionConfig(newData.getExtractionConfig());
            if (newData.getDatasetConfig() != null) dataset.setDatasetConfig(newData.getDatasetConfig());
            if (newData.getDataSchema() != null) dataset.setDataSchema(newData.getDataSchema());
            if (newData.getDenormConfig() != null) dataset.setDenormConfig(newData.getDenormConfig());
            if (newData.getRouterConfig() != null) dataset.setRouterConfig(newData.getRouterConfig());
            if (newData.getDatasetConfig() != null) dataset.setDatasetConfig(newData.getDatasetConfig());
            dataset.setUpdatedDate(LocalDateTime.now().toLocalDate());
            return repository.save(dataset);
        }).orElseThrow(() -> new DatasetNotFoundException("Dataset with ID " + datasetId + " not found"));
    }

    

    public List<DataSetResponse> findAllDatasets() {
        List<Dataset> datasets = repository.findAll();
        return datasets.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public DataSetResponse2 deleteDataset(String datasetId) {
        if (!repository.existsById(datasetId)) {
            throw new DatasetNotFoundException("Dataset with ID " + datasetId + " not found for deletion");
        }
        Dataset datasetv= getDatasetById( datasetId);
        repository.deleteByDatasetId(datasetId);
        return mapToResponse2(datasetv,"Dataset deleted successfully");
        
        
    }

    private DataSetResponse mapToResponse(Dataset dataset) {
        DataSetResponse datasetResponse = new DataSetResponse();
        datasetResponse.setId(dataset.getId());
        datasetResponse.setDatasetId(dataset.getDatasetId());
        datasetResponse.setType(dataset.getType());
        datasetResponse.setName(dataset.getName());
        datasetResponse.setStatus(dataset.getStatus());
        datasetResponse.setTags(dataset.getTags());
        datasetResponse.setDataVersion(dataset.getDataVersion());
        datasetResponse.setCreatedBy(dataset.getCreatedBy());
        datasetResponse.setUpdatedBy(dataset.getUpdatedBy());
        datasetResponse.setCreatedDate(dataset.getCreatedDate());
        datasetResponse.setUpdatedDate(dataset.getUpdatedDate());
        datasetResponse.setPublishedDate(dataset.getPublishedDate());
        datasetResponse.setValidationConfig(dataset.getValidationConfig());
        datasetResponse.setExtractionConfig(dataset.getExtractionConfig());
        datasetResponse.setDedupConfig(dataset.getDatasetConfig());
        datasetResponse.setDataSchema(dataset.getDataSchema());
        datasetResponse.setDenormConfig(dataset.getDenormConfig());
        datasetResponse.setRouterConfig(dataset.getRouterConfig());
        datasetResponse.setDatasetConfig(dataset.getDatasetConfig());
        return datasetResponse;
    }
    private DataSetResponse2 mapToResponse2(Dataset dataset,String message) {
        DataSetResponse2 datasetResponse2 = new DataSetResponse2();
        datasetResponse2.setId(dataset.getId());
        datasetResponse2.setDatasetId(dataset.getDatasetId());
        datasetResponse2.setType(dataset.getType());
        datasetResponse2.setDataVersion(dataset.getDataVersion());
        datasetResponse2.setMessage(message);
        datasetResponse2.setVer("v1");

        
        return datasetResponse2;
    }
}
