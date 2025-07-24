package com.rl.service;

import com.rl.model.MyData;
import com.rl.persistence.MyDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MyDataService {

    @Autowired
    private MyDataRepository myDataRepository;

    public List<MyData> getAllData() {
        log.info("Fetching all data");
        return myDataRepository.findAll();
    }

    public MyData saveData(MyData data) {
        log.info("Saving data: {}", data);
        return myDataRepository.save(data);
    }
}