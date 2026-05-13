package com.fred.orderreport.infrastructure.csv;

import com.fred.orderreport.domain.model.ShippingZone;
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

    public Map<String, ShippingZone> parse(Path shippingZonePath) throws IOException {

        Map<String, ShippingZone> shippingZones = new HashMap<>();

        List<String> lines  = csvFileReader.readDataLines(shippingZonePath);

        for (String line : lines) {

            try {

                String[] parts = line.split(",");

                ShippingZone zone = new ShippingZone(
                        Double.parseDouble(parts[1]),
                        parts.length > 2 ? Double.parseDouble(parts[2]) : 0.5
                );

                shippingZones.put(parts[0], zone);

            } catch (Exception e) {
                // Legacy behavior: invalid shipping zone lines are ignored
            }
        }

        return shippingZones;
    }
}