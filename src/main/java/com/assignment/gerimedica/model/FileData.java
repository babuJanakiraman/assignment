package com.assignment.gerimedica.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Data
public class FileData {

    private String source;
    private String codeListCode;
    @Column(unique=true)
    @Id
    private String code;

    public FileData(String source, String codeListCode, String code, String displayValue, String longDescription, String fromDate, String toDate, String sortingPriority) {
        this.source = source;
        this.codeListCode = codeListCode;
        this.code = code;
        this.displayValue = displayValue;
        this.longDescription = longDescription;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sortingPriority = sortingPriority;
    }

    private String displayValue;
    private String longDescription;
    private String fromDate;
    private String toDate;
    private String sortingPriority;

    public FileData() {
        
    }




//    public FileData(String source, String codeListCode, String code, String displayValue, String longDescription, String fromDate, String toDate, String sortingPriority) {
//        this.source = source;
//        this.codeListCode = codeListCode;
//        this.code = code;
//        this.displayValue = displayValue;
//        this.longDescription = longDescription;
//        this.fromDate = fromDate;
//        this.toDate = toDate;
//        this.sortingPriority = sortingPriority;
//    }
//    public FileData() {
//    }
}
