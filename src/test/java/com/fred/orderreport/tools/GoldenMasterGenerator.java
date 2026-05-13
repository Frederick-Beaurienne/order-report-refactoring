package com.fred.orderreport.tools;

import legacy.OrderReportLegacy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class GoldenMasterGenerator {

    public static void main(String[] args) throws IOException {
        generateGoldenMaster();
    }

    public static void generateGoldenMaster() throws IOException {
        String result = OrderReportLegacy.run();

        Files.writeString(
                Path.of("legacy/expected/report.txt"),
                result
        );
    }
}