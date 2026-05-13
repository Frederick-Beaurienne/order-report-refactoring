package com.fred.orderreport.infrastructure.csv;

import com.fred.orderreport.domain.model.Order;
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

    public List<Order> parse(Path orderPath) throws IOException {

        List<Order> orders = new ArrayList<>();

        List<String> lines  = csvFileReader.readDataLines(orderPath);

        for (String line : lines) {

            try {

                String[] parts = line.split(",");

                int qty = Integer.parseInt(parts[3]);
                double price = Double.parseDouble(parts[4]);

                if (qty <= 0 || price < 0) {
                    continue; // legacy validation behavior
                }

                Order order = new Order(
                        parts[0],
                        parts[1],
                        parts[2],
                        qty,
                        price,
                        parts.length > 5 ? parts[5] : "",
                        parts.length > 6 ? parts[6] : "",
                        parts.length > 7 ? parts[7] : "12:00"
                );

                orders.add(order);

            } catch (Exception e) {
                // Legacy behavior: invalid order lines are ignored
            }
        }

        return orders;
    }
}