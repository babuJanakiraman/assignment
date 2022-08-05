package com.assignment.gerimedica.controller;

import com.assignment.gerimedica.Response.FileResponse;
import com.assignment.gerimedica.model.FileData;
import com.assignment.gerimedica.service.CsvFileService;
import com.assignment.gerimedica.util.CSVHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class DataController {

    private final CsvFileService csvFileService;

    public DataController(CsvFileService csvFileService) {
        this.csvFileService = csvFileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                csvFileService.saveFile(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                log.info("File Upload completed");
                return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(message));

            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileResponse(message));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile() {
        String filename = "exercise.csv";
        InputStreamResource file = new InputStreamResource(csvFileService.loadFile());
        log.info("File download completed");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @GetMapping("/get/{code}")
    public ResponseEntity<FileData> getByCode(@PathVariable String code) {

        FileData record = csvFileService.getRecord(code).get();
        log.info("get by code operation for code: {} is performed", code);

        return new ResponseEntity<>(record, HttpStatus.OK);

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<FileData>> getAllRecords(){
        List<FileData> records = csvFileService.getAllRecords();
        log.info("getAll operation is performed");
        return new ResponseEntity<>(records, HttpStatus.OK);

    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAllRecords(){
        csvFileService.deleteAllRecords();
        log.info("deleteAll operation is performed");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
