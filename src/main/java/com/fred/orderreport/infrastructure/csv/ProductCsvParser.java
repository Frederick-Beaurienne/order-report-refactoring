package com.fred.orderreport.infrastructure.csv;

import com.fred.orderreport.domain.model.Product;
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

    public Map<String, Product> parse(Path productPath) throws IOException {

        List<String> lines  = csvFileReader.readDataLines(productPath);

        Map<String, Product> products = new HashMap<>();

        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                Product product = new Product(
                        parts[0],
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        parts.length > 4 ? Double.parseDouble(parts[4]) : 1.0,
                        parts.length > 5 ? parts[5].equals("true") : true
                );

                products.put(parts[0], product);
            } catch (Exception e) {
                // Legacy behavior: invalid product lines are ignored
            }
        }

        return products;
    }
}