package com.fred.orderreport.infrastructure.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

/**
 * Exports report data to JSON.
 */
@Service
public class JsonReportExporter {

    public void export(List<Map<String, Object>> jsonData,
                       String outputPath) throws Exception {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        FileWriter writer = new FileWriter(outputPath);

        gson.toJson(jsonData, writer);

        writer.close();
    }
}