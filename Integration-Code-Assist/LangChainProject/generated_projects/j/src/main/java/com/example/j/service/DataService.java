package com.example.j.service;

import com.example.j.model.DataModel;
import com.example.j.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    public List<DataModel> getAllData() {
        return dataRepository.findAll();
    }

    public DataModel createData(DataModel dataModel) {
        return dataRepository.save(dataModel);
    }
}