package com.assignment.gerimedica.repository;

import com.assignment.gerimedica.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, String> {

    Optional<FileData> findByCode(String code);

}
