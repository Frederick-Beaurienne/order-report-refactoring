package com.fred.orderreport.goldenmaster;

import com.fred.orderreport.legacyintegration.ReportApplication;
import legacy.OrderReportLegacy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyGoldenMasterTest {

    @Test
    void shouldMatchLegacyReport() throws Exception {

        String expected = Files.readString(
                Path.of("legacy/expected/report.txt")
        );

        // TODO inject new version output
        String actual = ReportApplication.run();

        assertEquals(expected, actual);
    }
}