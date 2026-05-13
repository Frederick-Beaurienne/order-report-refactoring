package com.fred.orderreport.infrastructure.export;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.fred.orderreport.domain.result.CustomerReportData;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps report data to JSON export format.
 */
@Service
public class JsonReportMapper {

    public List<Map<String, Object>> map(
            List<CustomerReportData> reportData) {

        List<Map<String, Object>> jsonData =
                new ArrayList<>();

        for (CustomerReportData data : reportData) {

            jsonData.add(
                    mapCustomerReport(
                            data.getCustomerId(),
                            data.getName(),
                            data.getTotal(),
                            data.getCurrency(),
                            data.getLoyaltyPoints()
                    )
            );
        }

        return jsonData;
    }

    public Map<String, Object> mapCustomerReport(String customerId,
                                                 String name,
                                                 double total,
                                                 String currency,
                                                 double loyaltyPoints) {

        Map<String, Object> jsonEntry = new HashMap<>();

        jsonEntry.put("customer_id", customerId);
        jsonEntry.put("name", name);
        jsonEntry.put("total", total);
        jsonEntry.put("currency", currency);
        jsonEntry.put("loyalty_points", (int) Math.floor(loyaltyPoints));

        return jsonEntry;
    }
}