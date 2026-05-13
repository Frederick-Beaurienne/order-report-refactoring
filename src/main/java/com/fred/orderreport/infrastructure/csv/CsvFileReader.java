package com.fred.orderreport.infrastructure.csv;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility component responsible for reading CSV files.
 */
@Service
public class CsvFileReader {

    /**
     * Reads only data lines from a CSV file, excluding the header row.
     *
     * @param path CSV file path
     * @return CSV data lines without header
     * @throws IOException if the file cannot be read
     */
    public List<String> readDataLines(Path path) throws IOException {
        return readAllLines(path)
                .stream()
                .skip(1)
                .toList();
    }

    /**
     * Reads all lines from a CSV file, including the header row.
     *
     * @param path CSV file path
     * @return all file lines
     * @throws IOException if the file cannot be read
     */
    private List<String> readAllLines(Path path) throws IOException {
        return Files.readAllLines(path);
    }
}