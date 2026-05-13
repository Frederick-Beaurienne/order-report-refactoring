package com.fred.orderreport.infrastructure.csv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
/**
 * Parses order CSV files into legacy-compatible order structures.
 */
public class OrderCsvParser {

    private final CsvFileReader csvFileReader;

    public List<Map<String, Object>> parse(Path orderPath) throws IOException {

        List<Map<String, Object>> orders = new ArrayList<>();

        List<String> lines  = csvFileReader.readDataLines(orderPath);

        for (String line : lines) {

            try {

                String[] parts = line.split(",");

                int qty = Integer.parseInt(parts[3]);
                double price = Double.parseDouble(parts[4]);

                if (qty <= 0 || price < 0) {
                    continue; // legacy validation behavior
                }

                Map<String, Object> order = new HashMap<>();

                order.put("id", parts[0]);
                order.put("customer_id", parts[1]);
                order.put("product_id", parts[2]);
                order.put("qty", qty);
                order.put("unit_price", price);
                order.put("date", parts.length > 5 ? parts[5] : "");
                order.put("promo_code", parts.length > 6 ? parts[6] : "");
                order.put("time", parts.length > 7 ? parts[7] : "12:00");

                orders.add(order);

            } catch (Exception e) {
                // Legacy behavior: invalid order lines are ignored
            }
        }

        return orders;
    }
}