package com.assignment.gerimedica.service;


import com.assignment.gerimedica.exception.ApiException;
import com.assignment.gerimedica.exception.RecordNotFoundException;
import com.assignment.gerimedica.model.FileData;
import com.assignment.gerimedica.repository.FileDataRepository;
import com.assignment.gerimedica.util.CSVHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CsvFileService {

    private final FileDataRepository fileDataRepository;

    public CsvFileService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    public void saveFile(MultipartFile file) {
        log.info("inside savefile service");
        try {
            InputStream targetStream = getInputStream(file);
            List<FileData> fileDataList = CSVHelper.csvToData(targetStream);
            fileDataRepository.saveAll(fileDataList);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream loadFile() {
        log.info("inside loadFile service");
        List<FileData> fileDataList = fileDataRepository.findAll();
        ByteArrayInputStream in = CSVHelper.dataToCSV(fileDataList);
        return in;
    }

public Optional<FileData> getRecord(String code) {
    log.info("inside getRecord service");
    Optional<FileData> record = null;
    try {
        record = fileDataRepository.findByCode(code);
        if (!record.isPresent()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND,"Record for given code could not found");
        }
        return record;
    } catch (RecordNotFoundException e) {
        log.error("Record for given code could not found : {}", code);
        throw new ApiException(e.getStatus(), e.getMessage());
    }catch (Exception e) {
        throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}

    public List<FileData> getAllRecords() {
        log.info("inside getAllRecords service");
        List<FileData> fileDataList = new ArrayList<>();
        try {
            fileDataRepository.findAll().forEach(fileDataList::add);
            return fileDataList;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void deleteAllRecords() {
        log.info("inside deleteAllRecords service");
        try{
            fileDataRepository.deleteAll();
        }catch (Exception e) {
            log.error("Error in deleteRecords: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private InputStream getInputStream(MultipartFile file) throws IOException {
        String contents = new String(file.getBytes());
        contents = contents.replace("\"", "");
        InputStream targetStream = new ByteArrayInputStream(contents.getBytes());
        return targetStream;
    }
}
