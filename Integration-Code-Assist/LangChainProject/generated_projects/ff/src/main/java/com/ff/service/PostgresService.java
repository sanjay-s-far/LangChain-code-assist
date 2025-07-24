package com.ff.service;

import com.ff.model.ExampleData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostgresService {

    public List<ExampleData> getData() {
        // Replace this with actual database interaction
        List<ExampleData> data = new ArrayList<>();
        data.add(new ExampleData(1, "Example Data 1"));
        data.add(new ExampleData(2, "Example Data 2"));
        return data;
    }
}