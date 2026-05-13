package com.fred.orderreport.infrastructure.csv;

import com.fred.orderreport.domain.model.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
/**
 * Parses promotion CSV files into legacy-compatible promotion structures.
 */
public class PromotionCsvParser {

    private final CsvFileReader csvFileReader;

    public Map<String, Promotion> parse(Path promotionPath) throws IOException {

        Map<String, Promotion> promotions = new HashMap<>();

        try {

            List<String> lines  = csvFileReader.readDataLines(promotionPath);

            for (String line : lines) {

                try {

                    String[] parts = line.split(",");

                    Promotion promotion = new Promotion(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts.length > 3 ? parts[3] : "true"
                    );

                    promotions.put(parts[0], promotion);

                } catch (Exception e) {
                    // Legacy behavior: invalid promotion lines are ignored
                }
            }

        } catch (FileNotFoundException e) {
            // Legacy behavior: missing promotion file is ignored
        }

        return promotions;
    }
}