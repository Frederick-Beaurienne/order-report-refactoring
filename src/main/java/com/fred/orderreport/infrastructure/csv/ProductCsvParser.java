package com.fred.orderreport.infrastructure.csv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
/**
 * Parses product CSV files into legacy-compatible product structures.
 */
public class ProductCsvParser {

    private final CsvFileReader csvFileReader;

    public Map<String, Map<String, Object>> parse(Path productPath) throws IOException {

        List<String> lines  = csvFileReader.readDataLines(productPath);

        Map<String, Map<String, Object>> products = new HashMap<>();

        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                Map<String, Object> prod = new HashMap<>();
                prod.put("id", parts[0]);
                prod.put("name", parts[1]);
                prod.put("category", parts[2]);
                prod.put("price", Double.parseDouble(parts[3]));
                prod.put("weight", parts.length > 4 ? Double.parseDouble(parts[4]) : 1.0);
                prod.put("taxable", parts.length > 5 ? parts[5].equals("true") : true);
                products.put(parts[0], prod);
            } catch (Exception e) {
                // Legacy behavior: invalid product lines are ignored
            }
        }

        return products;
    }
}