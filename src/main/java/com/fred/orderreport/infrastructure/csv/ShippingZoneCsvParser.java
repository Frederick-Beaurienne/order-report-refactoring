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
public class ShippingZoneCsvParser {

    private final CsvFileReader csvFileReader;

    public Map<String, Map<String, Double>> parse(Path shippingZonePath) throws IOException {

        Map<String, Map<String, Double>> shippingZones = new HashMap<>();

        List<String> lines  = csvFileReader.readDataLines(shippingZonePath);

        for (String line : lines) {

            try {

                String[] parts = line.split(",");

                Map<String, Double> zone = new HashMap<>();

                zone.put("base", Double.parseDouble(parts[1]));
                zone.put("per_kg", parts.length > 2 ? Double.parseDouble(parts[2]) : 0.5);

                shippingZones.put(parts[0], zone);

            } catch (Exception e) {
                // Legacy behavior: invalid shipping zone lines are ignored
            }
        }

        return shippingZones;
    }
}