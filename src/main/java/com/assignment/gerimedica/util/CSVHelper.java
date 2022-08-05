package com.assignment.gerimedica.util;

import com.assignment.gerimedica.model.FileData;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CSVHelper {

    public static String TYPE = "text/csv";
    public static String[] HEADERS = { "source", "codeListCode", "code", "displayValue", "longDescription", "fromDate", "toDate", "sortingPriority" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static ByteArrayInputStream dataToCSV(List<FileData> fileDataList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withHeader(HEADERS);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (FileData fileData : fileDataList) {
                List<String> data = Arrays.asList(
                        fileData.getSource(),
                        fileData.getCodeListCode(),
                        fileData.getCode(),
                        fileData.getDisplayValue(),
                        fileData.getLongDescription(),
                        fileData.getFromDate(),
                        fileData.getToDate(),
                        fileData.getSortingPriority()
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static List<FileData> csvToData(InputStream is) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            log.info("reader", bufferedReader.lines());
                List<FileData> fileDataList = new ArrayList<>();
                Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withSkipHeaderRecord()
                        .withIgnoreEmptyLines()
                        .withTrim()
                        .parse(bufferedReader);

            for (CSVRecord csvRecord : csvRecords) {
                    FileData fileData = new FileData(
                            csvRecord.get("source"),
                            csvRecord.get("codeListCode"),
                            csvRecord.get("code"),
                            csvRecord.get("displayValue"),
                            csvRecord.get("longDescription"),
                            csvRecord.get("fromDate"),
                            csvRecord.get("toDate"),
                            csvRecord.get("sortingPriority"));
                    fileDataList.add(fileData);
                }
                return fileDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}

