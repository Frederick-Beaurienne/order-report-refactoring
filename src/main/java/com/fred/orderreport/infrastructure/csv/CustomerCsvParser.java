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
 * Parses customer CSV files into legacy-compatible customer structures.
 */
public class CustomerCsvParser {

    private final CsvFileReader csvFileReader;

    public Map<String, Map<String, String>> parse(Path customerPath) throws IOException {

        Map<String, Map<String, String>> customers = new HashMap<>();

        List<String> lines  = csvFileReader.readDataLines(customerPath);

        for (String line : lines) {

            try {

                String[] parts = line.split(",");

                Map<String, String> customer = new HashMap<>();

                customer.put("id", parts[0]);
                customer.put("name", parts[1]);
                customer.put("level", parts.length > 2 ? parts[2] : "BASIC");
                customer.put("shipping_zone", parts.length > 3 ? parts[3] : "ZONE1");
                customer.put("currency", parts.length > 4 ? parts[4] : "EUR");

                customers.put(parts[0], customer);

            } catch (Exception e) {
                // Legacy behavior: invalid customer lines are ignored
            }
        }

        return customers;
    }
}